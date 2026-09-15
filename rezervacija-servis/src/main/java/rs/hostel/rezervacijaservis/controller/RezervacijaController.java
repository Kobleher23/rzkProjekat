package rs.hostel.rezervacijaservis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.rezervacijaservis.dto.KreiranjeRezervacijeZahtev;
import rs.hostel.rezervacijaservis.dto.RezervacijaOdgovorDTO;
import rs.hostel.rezervacijaservis.model.Rezervacija;
import rs.hostel.rezervacijaservis.service.RezervacijaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rezervacije")
@RequiredArgsConstructor
public class RezervacijaController {

	private final RezervacijaService rezervacijaService;

	@GetMapping
	public List<Rezervacija> sveRezervacije() {
		return rezervacijaService.sveRezervacije();
	}

	@GetMapping("/{id}")
	public Rezervacija jednaRezervacija(@PathVariable Long id) {
		return rezervacijaService.nadjiPoId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RezervacijaOdgovorDTO kreiraj(@Valid @RequestBody KreiranjeRezervacijeZahtev zahtev) {
		return rezervacijaService.kreirajRezervaciju(zahtev);
	}

	@PutMapping("/{id}")
	public Rezervacija izmeni(@PathVariable Long id,
							  @Valid @RequestBody Rezervacija izmenjena) {
		return rezervacijaService.izmeni(id, izmenjena);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		rezervacijaService.obrisi(id);
	}

	@PutMapping("/{id}/otkazi")
	public Rezervacija otkazi(@PathVariable Long id) {
		return rezervacijaService.otkazi(id);
	}

	@GetMapping("/preklapanja")
	public List<Rezervacija> preklapanja(
			@RequestParam Long krevetId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dolazak,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate odlazak) {
		return rezervacijaService.nadjiPreklapajuce(krevetId, dolazak, odlazak);
	}

	@GetMapping("/slobodan")
	public boolean slobodan(
			@RequestParam Long krevetId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dolazak,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate odlazak) {
		return rezervacijaService.jeSlobodan(krevetId, dolazak, odlazak);
	}
}
