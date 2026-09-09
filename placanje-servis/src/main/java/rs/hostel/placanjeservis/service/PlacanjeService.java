package rs.hostel.placanjeservis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.placanjeservis.dto.KreirajRacunZahtev;
import rs.hostel.placanjeservis.dto.StavkaZahtev;
import rs.hostel.placanjeservis.exception.BadRequestException;
import rs.hostel.placanjeservis.exception.NotFoundException;
import rs.hostel.placanjeservis.model.NacinPlacanja;
import rs.hostel.placanjeservis.model.Racun;
import rs.hostel.placanjeservis.model.StatusRacuna;
import rs.hostel.placanjeservis.model.StatusUplate;
import rs.hostel.placanjeservis.model.StavkaRacuna;
import rs.hostel.placanjeservis.model.Uplata;
import rs.hostel.placanjeservis.repository.RacunRepository;
import rs.hostel.placanjeservis.repository.UplataRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlacanjeService {

	private final RacunRepository racunRepository;
	private final UplataRepository uplataRepository;

	// ---------- citanje (obican CRUD) ----------

	@Transactional(readOnly = true)
	public List<Racun> sviRacuni() {
		return racunRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Racun nadjiPoId(Long id) {
		return racunRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Racun sa id " + id + " ne postoji"));
	}

	@Transactional(readOnly = true)
	public List<Racun> racuniZaRezervaciju(Long rezervacijaId) {
		return racunRepository.findByRezervacijaId(rezervacijaId);
	}

	// ---------- SLOZENA OPERACIJA 1: kreiranje racuna ----------

	/**
	 * Pravi nov racun iz liste stavki.
	 *
	 * Ukupan iznos NE stize od klijenta - servis ga izvodi kao zbir stavki.
	 * Tako racun i njegove stavke ne mogu da se raziđu: total je uvek
	 * tacno ono sto stavke kazu.
	 */
	@Transactional
	public Racun kreirajRacun(KreirajRacunZahtev zahtev) {
		if (zahtev.getStavke() == null || zahtev.getStavke().isEmpty()) {
			throw new BadRequestException("Racun mora imati bar jednu stavku");
		}

		Racun racun = new Racun();
		racun.setRezervacijaId(zahtev.getRezervacijaId());
		racun.setStatus(StatusRacuna.NEPLACEN);

		BigDecimal ukupno = BigDecimal.ZERO;

		for (StavkaZahtev ulaz : zahtev.getStavke()) {
			StavkaRacuna stavka = new StavkaRacuna();
			stavka.setOpis(ulaz.getOpis());
			stavka.setIznos(ulaz.getIznos());

			// Obe strane veze moraju da se poveze: stavka pokazuje na racun
			// (to je kolona racun_id u bazi), a racun drzi stavku u listi
			// da bi je CascadeType.ALL sacuvao zajedno sa racunom.
			stavka.setRacun(racun);
			racun.getStavke().add(stavka);

			ukupno = ukupno.add(ulaz.getIznos());
		}

		racun.setIznos(ukupno);

		Racun sacuvan = racunRepository.save(racun);
		log.info("Kreiran racun id={} za rezervaciju={} na iznos={} ({} stavki)",
				sacuvan.getId(), sacuvan.getRezervacijaId(), sacuvan.getIznos(),
				sacuvan.getStavke().size());
		return sacuvan;
	}

	// ---------- SLOZENA OPERACIJA 2: evidentiranje uplate ----------

	/**
	 * Dodaje uplatu na racun i, ako je racun time pokriven, prebacuje ga u PLACEN.
	 *
	 * Odluka o statusu se NE donosi po pojedinacnoj uplati, nego po zbiru svih
	 * USPESNIH uplata na tom racunu - zato racun moze da se plati u vise rata.
	 */
	@Transactional
	public Racun evidentirajUplatu(Long racunId, BigDecimal iznos, NacinPlacanja nacin) {
		Racun racun = nadjiPoId(racunId);

		if (racun.getStatus() == StatusRacuna.STORNIRAN) {
			throw new BadRequestException(
					"Racun " + racunId + " je storniran - uplata nije moguca");
		}
		if (iznos == null || iznos.signum() <= 0) {
			throw new BadRequestException("Iznos uplate mora biti veci od nule");
		}

		Uplata uplata = new Uplata();
		uplata.setRacun(racun);
		uplata.setIznos(iznos);
		uplata.setNacinPlacanja(nacin);
		uplata.setStatus(StatusUplate.USPESNA);

		// saveAndFlush gura INSERT u bazu ODMAH. Bez toga bi upit ispod
		// mogao da sabere stanje bez ove uplate i racun ne bi presao u PLACEN.
		uplataRepository.saveAndFlush(uplata);
		racun.getUplate().add(uplata);

		// Sabiraju se samo USPESNE uplate - neuspeli pokusaji ostaju
		// zabelezeni, ali ne umanjuju dug.
		BigDecimal placeno = uplataRepository.zbirUplata(racunId, StatusUplate.USPESNA);

		// compareTo, NE equals: BigDecimal("100.00").equals(new BigDecimal("100"))
		// je false jer equals poredi i broj decimala. compareTo poredi vrednost.
		if (placeno.compareTo(racun.getIznos()) >= 0) {
			racun.setStatus(StatusRacuna.PLACEN);
			log.info("Racun id={} je PLACEN (uplaceno {} od {})",
					racunId, placeno, racun.getIznos());
		} else {
			log.info("Racun id={} i dalje NEPLACEN (uplaceno {} od {}, ostaje {})",
					racunId, placeno, racun.getIznos(), racun.getIznos().subtract(placeno));
		}

		return racunRepository.save(racun);
	}

	// ---------- ostatak CRUD-a ----------

	@Transactional
	public Racun storniraj(Long id) {
		Racun racun = nadjiPoId(id);
		if (racun.getStatus() == StatusRacuna.PLACEN) {
			throw new BadRequestException(
					"Racun " + id + " je vec placen - storniranje nije moguce");
		}
		racun.setStatus(StatusRacuna.STORNIRAN);
		return racunRepository.save(racun);
	}

	@Transactional
	public void obrisi(Long id) {
		if (!racunRepository.existsById(id)) {
			throw new NotFoundException("Racun sa id " + id + " ne postoji");
		}
		racunRepository.deleteById(id);
	}
}
