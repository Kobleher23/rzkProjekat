package rs.hostel.notifikacijaservis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.notifikacijaservis.dto.PosaljiNotifikacijuZahtev;
import rs.hostel.notifikacijaservis.model.Notifikacija;
import rs.hostel.notifikacijaservis.service.NotifikacijaService;

import java.util.List;

@RestController
@RequestMapping("/api/notifikacije")
@RequiredArgsConstructor
public class NotifikacijaController {

	private final NotifikacijaService notifikacijaService;

	/**
	 * SLOZENA OPERACIJA: sastavi poruku iz sablona, proveri preferencu
	 * primaoca, "posalji" je i zabelezi ishod.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Notifikacija posalji(@Valid @RequestBody PosaljiNotifikacijuZahtev zahtev) {
		return notifikacijaService.posaljiNotifikaciju(
				zahtev.getEmail(), zahtev.getTip(), zahtev.getParametri());
	}

	// Istorija svih poslatih (i odbijenih) obavestenja.
	@GetMapping
	public List<Notifikacija> istorija() {
		return notifikacijaService.sveNotifikacije();
	}

	@GetMapping("/{id}")
	public Notifikacija jedna(@PathVariable Long id) {
		return notifikacijaService.nadjiPoId(id);
	}

	@GetMapping("/po-emailu")
	public List<Notifikacija> poEmailu(@RequestParam String email) {
		return notifikacijaService.istorijaZaEmail(email);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		notifikacijaService.obrisi(id);
	}
}
