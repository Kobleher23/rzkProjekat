package rs.hostel.rezervacijaservis.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.rezervacijaservis.dto.KrevetDTO;
import rs.hostel.rezervacijaservis.enums.StatusRezervacije;
import rs.hostel.rezervacijaservis.exception.BadRequestException;
import rs.hostel.rezervacijaservis.client.SmestajClient;
import rs.hostel.rezervacijaservis.repository.RezervacijaRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DostupnostService {

	private final SmestajClient smestajClient;
	private final RezervacijaRepository rezervacijaRepository;

	@CircuitBreaker(name = "smestaj", fallbackMethod = "fallbackDostupne")
	@Transactional(readOnly = true)
	public List<KrevetDTO> pretraziDostupne(Long hostelId, LocalDate dolazak, LocalDate odlazak) {
		proveriDatume(dolazak, odlazak);

		List<KrevetDTO> sviKreveti = smestajClient.kreveti(hostelId);

		return sviKreveti.stream()
				.filter(krevet -> jeSlobodan(krevet.getKrevetId(), dolazak, odlazak))
				.toList();
	}

	public List<KrevetDTO> fallbackDostupne(Long hostelId, LocalDate dolazak,
											LocalDate odlazak, Throwable t) {
		if (t instanceof BadRequestException greska) {
			throw greska;
		}

		log.warn("SMESTAJ SERVIS NEDOSTUPAN - vracam praznu listu. hostelId={}, termin={} do {}, uzrok: {}",
				hostelId, dolazak, odlazak, t.toString());

		return Collections.emptyList();
	}

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
