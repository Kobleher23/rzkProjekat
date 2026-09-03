package rs.hostel.rezervacijaservis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.rezervacijaservis.exception.BadRequestException;
import rs.hostel.rezervacijaservis.exception.NotFoundException;
import rs.hostel.rezervacijaservis.model.Rezervacija;
import rs.hostel.rezervacijaservis.model.StatusRezervacije;
import rs.hostel.rezervacijaservis.model.StavkaRezervacije;
import rs.hostel.rezervacijaservis.repository.RezervacijaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class RezervacijaService {

	private final RezervacijaRepository rezervacijaRepository;

	public RezervacijaService(RezervacijaRepository rezervacijaRepository) {
		this.rezervacijaRepository = rezervacijaRepository;
	}

	@Transactional(readOnly = true)
	public List<Rezervacija> sveRezervacije() {
		return rezervacijaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Rezervacija nadjiPoId(Long id) {
		return rezervacijaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Rezervacija sa id " + id + " ne postoji"));
	}

	@Transactional
	public Rezervacija kreiraj(Rezervacija rezervacija) {
		rezervacija.setId(null);
		proveriDatume(rezervacija.getDatumDolaska(), rezervacija.getDatumOdlaska());
		povezikStavke(rezervacija);
		return rezervacijaRepository.save(rezervacija);
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

	/**
	 * Otkazivanje NE brise zapis - samo menja status.
	 * Time rezervacija prestaje da zauzima krevet, a istorija ostaje sacuvana.
	 */
	@Transactional
	public Rezervacija otkazi(Long id) {
		Rezervacija rezervacija = nadjiPoId(id);
		rezervacija.setStatus(StatusRezervacije.OTKAZANA);
		return rezervacijaRepository.save(rezervacija);
	}

	/**
	 * Rezervacije koje se preklapaju sa trazenim terminom za dati krevet.
	 * Pravilo "otkazane se ne racunaju" je poslovno pravilo, pa stoji ovde,
	 * a ne u repozitorijumu (on samo izvrsava upit koji mu se zada).
	 */
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

	/**
	 * Kada stavke stignu kroz JSON, njihova "rezervacija" strana je prazna.
	 * Posto je bas ona vlasnik veze, moramo je rucno postaviti -
	 * inace bi kolona rezervacija_id ostala NULL.
	 */
	private void povezikStavke(Rezervacija rezervacija) {
		if (rezervacija.getStavke() != null) {
			for (StavkaRezervacije stavka : rezervacija.getStavke()) {
				stavka.setRezervacija(rezervacija);
			}
		}
	}
}
