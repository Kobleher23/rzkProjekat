package rs.hostel.rezervacijaservis.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.rezervacijaservis.dto.KrevetDTO;
import rs.hostel.rezervacijaservis.exception.BadRequestException;
import rs.hostel.rezervacijaservis.feign.SmestajClient;
import rs.hostel.rezervacijaservis.model.StatusRezervacije;
import rs.hostel.rezervacijaservis.repository.RezervacijaRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Pretraga dostupnosti - prva operacija koja spaja DVA IZVORA:
 *
 *   1. KOJI KREVETI POSTOJE  -> zna samo Smestaj servis (druga baza) -> Feign
 *   2. KOJI SU ZAUZETI       -> znamo mi, iz svoje baze rezervacija
 *
 * Nijedan servis sam ne moze da odgovori na pitanje "sta je slobodno".
 *
 * Posto zavisimo od tudjeg servisa, poziv je zasticen prekidacem
 * (circuit breaker): ako Smestaj padne, ne rusimo se s njim.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DostupnostService {

	private final SmestajClient smestajClient;
	private final RezervacijaRepository rezervacijaRepository;

	/**
	 * name = "smestaj" mora da se poklapa sa imenom instance u
	 * application.properties (resilience4j.circuitbreaker.instances.smestaj...).
	 */
	@CircuitBreaker(name = "smestaj", fallbackMethod = "fallbackDostupne")
	@Transactional(readOnly = true)
	public List<KrevetDTO> pretraziDostupne(Long hostelId, LocalDate dolazak, LocalDate odlazak) {
		proveriDatume(dolazak, odlazak);

		// 1. Feign poziv -> svi kreveti tog hostela (podatak iz druge baze)
		List<KrevetDTO> sviKreveti = smestajClient.kreveti(hostelId);

		// 2. za svaki krevet pitamo SVOJU bazu da li se termin preklapa
		// 3. ostaju samo oni bez ijednog preklapanja
		return sviKreveti.stream()
				.filter(krevet -> jeSlobodan(krevet.getKrevetId(), dolazak, odlazak))
				.toList();
	}

	/**
	 * Poziva se umesto pretraziDostupne kada poziv ka Smestaju padne,
	 * ili kada je prekidac OPEN (tada se Smestaj uopste i ne zove).
	 *
	 * Potpis mora biti IDENTICAN originalnoj metodi + Throwable na kraju.
	 */
	public List<KrevetDTO> fallbackDostupne(Long hostelId, LocalDate dolazak,
											LocalDate odlazak, Throwable t) {
		// Losi datumi nisu kvar Smestaja - tu gresku vracamo korisniku kao 400,
		// umesto da je sakrijemo praznom listom.
		if (t instanceof BadRequestException greska) {
			throw greska;
		}

		log.warn("SMESTAJ SERVIS NEDOSTUPAN - vracam praznu listu. hostelId={}, termin={} do {}, uzrok: {}",
				hostelId, dolazak, odlazak, t.toString());

		return Collections.emptyList();
	}

	/**
	 * Krevet je slobodan ako nema nijedne preklapajuce rezervacije.
	 * Koristi isti upit iz Faze 2 - otkazane se ne racunaju.
	 */
	private boolean jeSlobodan(Long krevetId, LocalDate dolazak, LocalDate odlazak) {
		if (krevetId == null) {
			return false;
		}
		return rezervacijaRepository
				.nadjiPreklapajuce(krevetId, dolazak, odlazak, StatusRezervacije.OTKAZANA)
				.isEmpty();
	}

	private void proveriDatume(LocalDate dolazak, LocalDate odlazak) {
		if (dolazak == null || odlazak == null) {
			throw new BadRequestException("Datum dolaska i datum odlaska su obavezni");
		}
		if (!dolazak.isBefore(odlazak)) {
			throw new BadRequestException("Datum dolaska mora biti pre datuma odlaska");
		}
	}
}
