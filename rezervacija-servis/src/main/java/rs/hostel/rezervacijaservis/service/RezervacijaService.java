package rs.hostel.rezervacijaservis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import rs.hostel.rezervacijaservis.client.NotifikacijaClient;
import rs.hostel.rezervacijaservis.client.PlacanjeClient;
import rs.hostel.rezervacijaservis.client.SmestajClient;
import rs.hostel.rezervacijaservis.dto.KreirajRacunZahtev;
import rs.hostel.rezervacijaservis.dto.KreiranjeRezervacijeZahtev;
import rs.hostel.rezervacijaservis.dto.KrevetDTO;
import rs.hostel.rezervacijaservis.dto.NotifikacijaZahtev;
import rs.hostel.rezervacijaservis.dto.RacunOdgovorDTO;
import rs.hostel.rezervacijaservis.dto.RezervacijaOdgovorDTO;
import rs.hostel.rezervacijaservis.dto.StavkaRacunaDTO;
import rs.hostel.rezervacijaservis.dto.StavkaRezervacijeDTO;
import rs.hostel.rezervacijaservis.enums.StatusRezervacije;
import rs.hostel.rezervacijaservis.exception.BadRequestException;
import rs.hostel.rezervacijaservis.exception.ConflictException;
import rs.hostel.rezervacijaservis.exception.NotFoundException;
import rs.hostel.rezervacijaservis.exception.ServisNedostupanException;
import rs.hostel.rezervacijaservis.model.Gost;
import rs.hostel.rezervacijaservis.model.Rezervacija;
import rs.hostel.rezervacijaservis.model.StavkaRezervacije;
import rs.hostel.rezervacijaservis.repository.GostRepository;
import rs.hostel.rezervacijaservis.repository.RezervacijaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RezervacijaService {

	private static final String TIP_POTVRDA = "POTVRDA_REZERVACIJE";

	private final RezervacijaRepository rezervacijaRepository;
	private final GostRepository gostRepository;

	private final SmestajClient smestajClient;
	private final PlacanjeClient placanjeClient;
	private final NotifikacijaClient notifikacijaClient;

	private final TransactionTemplate transactionTemplate;

	@Transactional(readOnly = true)
	public List<Rezervacija> sveRezervacije() {
		return rezervacijaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Rezervacija nadjiPoId(Long id) {
		return rezervacijaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Rezervacija sa id " + id + " ne postoji"));
	}

	public RezervacijaOdgovorDTO kreirajRezervaciju(KreiranjeRezervacijeZahtev zahtev) {
		LocalDate dolazak = zahtev.getDolazak();
		LocalDate odlazak = zahtev.getOdlazak();
		proveriDatume(dolazak, odlazak);
		proveriDuplikate(zahtev.getKrevetIds());

		Gost gost = gostRepository.findById(zahtev.getGostId())
				.orElseThrow(() -> new NotFoundException(
						"Gost sa id " + zahtev.getGostId() + " ne postoji"));

		Map<Long, KrevetDTO> kreveti = povuciTrazeneKrevete(zahtev.getHostelId(), zahtev.getKrevetIds());
		long brojNocenja = izracunajBrojNocenja(dolazak, odlazak);

		Rezervacija rezervacija = transactionTemplate.execute(status -> {
			proveriDaSuSlobodni(kreveti, dolazak, odlazak);
			return rezervacijaRepository.save(
					napraviRezervaciju(gost, zahtev.getHostelId(), dolazak, odlazak, kreveti, brojNocenja));
		});
		log.info("Rezervacija id={} sacuvana u statusu KREIRANA, ukupno {}",
				rezervacija.getId(), rezervacija.getUkupnaCena());

		RacunOdgovorDTO racun = kreirajRacun(rezervacija, kreveti, brojNocenja);

		potvrdi(rezervacija);

		posaljiPotvrdu(gost, rezervacija);

		RezervacijaOdgovorDTO odgovor = new RezervacijaOdgovorDTO();
		odgovor.setId(rezervacija.getId());
		odgovor.setStatus(rezervacija.getStatus());
		odgovor.setGostId(gost.getId());
		odgovor.setHostelId(rezervacija.getHostelId());
		odgovor.setDatumDolaska(dolazak);
		odgovor.setDatumOdlaska(odlazak);
		odgovor.setBrojNocenja(brojNocenja);
		odgovor.setUkupnaCena(rezervacija.getUkupnaCena());
		odgovor.setRacunId(racun.getId());
		for (StavkaRezervacije stavka : rezervacija.getStavke()) {
			KrevetDTO krevet = kreveti.get(stavka.getKrevetId());
			odgovor.getStavke().add(new StavkaRezervacijeDTO(
					stavka.getKrevetId(), krevet.getOznaka(), stavka.getTipSobeId(), stavka.getCena()));
		}
		return odgovor;
	}

	private Map<Long, KrevetDTO> povuciTrazeneKrevete(Long hostelId, List<Long> krevetIds) {
		List<KrevetDTO> sviKreveti;
		try {
			sviKreveti = smestajClient.kreveti(hostelId);
		} catch (RuntimeException e) {
			String uzrok = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
			throw new ServisNedostupanException(
					"Cene kreveta nisu dostupne jer Smestaj servis nije odgovorio ("
							+ uzrok + "). Rezervacija nije kreirana.");
		}

		Map<Long, KrevetDTO> poId = sviKreveti.stream()
				.collect(Collectors.toMap(KrevetDTO::getKrevetId, Function.identity()));

		Map<Long, KrevetDTO> trazeni = new LinkedHashMap<>();
		for (Long krevetId : krevetIds) {
			KrevetDTO krevet = poId.get(krevetId);
			if (krevet == null) {
				throw new NotFoundException(
						"Krevet sa id " + krevetId + " ne postoji u hostelu " + hostelId);
			}
			if (krevet.getCena() == null) {
				throw new ConflictException(
						"Krevet " + krevet.getOznaka() + " (id " + krevetId + ") nema definisanu cenu");
			}
			trazeni.put(krevetId, krevet);
		}
		return trazeni;
	}

	private void proveriDaSuSlobodni(Map<Long, KrevetDTO> kreveti, LocalDate dolazak, LocalDate odlazak) {
		for (KrevetDTO krevet : kreveti.values()) {
			List<Rezervacija> preklapajuce = rezervacijaRepository.nadjiPreklapajuce(
					krevet.getKrevetId(), dolazak, odlazak, StatusRezervacije.OTKAZANA);
			if (!preklapajuce.isEmpty()) {
				Rezervacija postojeca = preklapajuce.get(0);
				throw new ConflictException(
						"Krevet " + krevet.getOznaka() + " (id " + krevet.getKrevetId()
								+ ") je zauzet od " + postojeca.getDatumDolaska()
								+ " do " + postojeca.getDatumOdlaska()
								+ " (rezervacija " + postojeca.getId() + ", status " + postojeca.getStatus() + ")");
			}
		}
	}

	private Rezervacija napraviRezervaciju(Gost gost, Long hostelId, LocalDate dolazak, LocalDate odlazak,
										   Map<Long, KrevetDTO> kreveti, long brojNocenja) {
		Rezervacija rezervacija = new Rezervacija();
		rezervacija.setGost(gost);
		rezervacija.setHostelId(hostelId);
		rezervacija.setDatumDolaska(dolazak);
		rezervacija.setDatumOdlaska(odlazak);
		rezervacija.setStatus(StatusRezervacije.KREIRANA);
		rezervacija.setDatumKreiranja(LocalDateTime.now());

		BigDecimal ukupno = BigDecimal.ZERO;
		for (KrevetDTO krevet : kreveti.values()) {
			BigDecimal cena = krevet.getCena().multiply(BigDecimal.valueOf(brojNocenja));

			StavkaRezervacije stavka = new StavkaRezervacije();
			stavka.setKrevetId(krevet.getKrevetId());
			stavka.setTipSobeId(krevet.getTipSobeId());
			stavka.setCena(cena);
			stavka.setRezervacija(rezervacija);
			rezervacija.getStavke().add(stavka);

			ukupno = ukupno.add(cena);
		}
		rezervacija.setUkupnaCena(ukupno);
		return rezervacija;
	}

	private RacunOdgovorDTO kreirajRacun(Rezervacija rezervacija, Map<Long, KrevetDTO> kreveti, long brojNocenja) {
		KreirajRacunZahtev zahtev = new KreirajRacunZahtev();
		zahtev.setRezervacijaId(rezervacija.getId());
		for (StavkaRezervacije stavka : rezervacija.getStavke()) {
			KrevetDTO krevet = kreveti.get(stavka.getKrevetId());
			zahtev.getStavke().add(new StavkaRacunaDTO(
					"Krevet " + krevet.getOznaka() + ", " + brojNocenja + " nocenja",
					stavka.getCena()));
		}

		try {
			RacunOdgovorDTO racun = placanjeClient.kreirajRacun(zahtev);
			log.info("Racun id={} kreiran u Placanju za rezervaciju {} (iznos {}, status {})",
					racun.getId(), rezervacija.getId(), racun.getIznos(), racun.getStatus());
			return racun;
		} catch (RuntimeException e) {
			String uzrok = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
			log.error("Placanje nije kreiralo racun za rezervaciju {} - ostaje KREIRANA. Uzrok: {}",
					rezervacija.getId(), uzrok);
			throw new ServisNedostupanException(
					"Racun nije kreiran jer Placanje servis nije odgovorio (" + uzrok + "). "
							+ "Rezervacija " + rezervacija.getId()
							+ " je sacuvana u statusu KREIRANA i NIJE potvrdjena.");
		}
	}

	private void potvrdi(Rezervacija rezervacija) {
		transactionTemplate.executeWithoutResult(status ->
				rezervacijaRepository.findById(rezervacija.getId())
						.orElseThrow(() -> new NotFoundException(
								"Rezervacija sa id " + rezervacija.getId() + " ne postoji"))
						.setStatus(StatusRezervacije.POTVRDJENA));
		rezervacija.setStatus(StatusRezervacije.POTVRDJENA);
	}

	private void posaljiPotvrdu(Gost gost, Rezervacija rezervacija) {
		if (gost.getEmail() == null || gost.getEmail().isBlank()) {
			log.warn("Gost {} nema email - potvrda za rezervaciju {} se ne salje",
					gost.getId(), rezervacija.getId());
			return;
		}

		Map<String, String> parametri = new LinkedHashMap<>();
		parametri.put("id", String.valueOf(rezervacija.getId()));
		parametri.put("ime", gost.getIme());
		parametri.put("dolazak", String.valueOf(rezervacija.getDatumDolaska()));
		parametri.put("odlazak", String.valueOf(rezervacija.getDatumOdlaska()));
		parametri.put("ukupnaCena", String.valueOf(rezervacija.getUkupnaCena()));

		NotifikacijaZahtev zahtev = new NotifikacijaZahtev();
		zahtev.setPrimalacEmail(gost.getEmail());
		zahtev.setTip(TIP_POTVRDA);
		zahtev.setParametri(parametri);

		try {
			notifikacijaClient.posalji(zahtev);
		} catch (RuntimeException e) {
			log.warn("Potvrda za rezervaciju {} nije poslata - tok se nastavlja. Uzrok: {}",
					rezervacija.getId(), NestedExceptionUtils.getMostSpecificCause(e).getMessage());
		}
	}

	private long izracunajBrojNocenja(LocalDate dolazak, LocalDate odlazak) {
		return ChronoUnit.DAYS.between(dolazak, odlazak);
	}

	private void proveriDuplikate(List<Long> krevetIds) {
		Set<Long> vidjeni = new HashSet<>();
		for (Long krevetId : krevetIds) {
			if (!vidjeni.add(krevetId)) {
				throw new BadRequestException("Krevet sa id " + krevetId + " je naveden vise puta");
			}
		}
	}

	@Transactional
	public Rezervacija izmeni(Long id, Rezervacija izmenjena) {
		Rezervacija postojeca = nadjiPoId(id);
		proveriDatume(izmenjena.getDatumDolaska(), izmenjena.getDatumOdlaska());
		postojeca.setGost(izmenjena.getGost());
		postojeca.setHostelId(izmenjena.getHostelId());
		postojeca.setDatumDolaska(izmenjena.getDatumDolaska());
		postojeca.setDatumOdlaska(izmenjena.getDatumOdlaska());
		postojeca.setStatus(izmenjena.getStatus());
		postojeca.setUkupnaCena(izmenjena.getUkupnaCena());
		return rezervacijaRepository.save(postojeca);
	}

	@Transactional
	public void obrisi(Long id) {
		if (!rezervacijaRepository.existsById(id)) {
			throw new NotFoundException("Rezervacija sa id " + id + " ne postoji");
		}
		rezervacijaRepository.deleteById(id);
	}

	@Transactional
	public Rezervacija otkazi(Long id) {
		Rezervacija rezervacija = nadjiPoId(id);
		rezervacija.setStatus(StatusRezervacije.OTKAZANA);
		return rezervacijaRepository.save(rezervacija);
	}

	@Transactional(readOnly = true)
	public List<Rezervacija> nadjiPreklapajuce(Long krevetId, LocalDate dolazak, LocalDate odlazak) {
		proveriDatume(dolazak, odlazak);
		return rezervacijaRepository.nadjiPreklapajuce(
				krevetId, dolazak, odlazak, StatusRezervacije.OTKAZANA);
	}

	@Transactional(readOnly = true)
	public boolean jeSlobodan(Long krevetId, LocalDate dolazak, LocalDate odlazak) {
		return nadjiPreklapajuce(krevetId, dolazak, odlazak).isEmpty();
	}

	private void proveriDatume(LocalDate dolazak, LocalDate odlazak) {
		if (dolazak != null && odlazak != null && !dolazak.isBefore(odlazak)) {
			throw new BadRequestException(
					"Datum dolaska mora biti pre datuma odlaska");
		}
	}
}
