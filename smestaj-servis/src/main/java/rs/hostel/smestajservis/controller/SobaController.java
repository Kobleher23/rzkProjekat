package rs.hostel.smestajservis.controller;

import jakarta.validation.Valid;
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
import rs.hostel.smestajservis.dto.SobaOdgovor;
import rs.hostel.smestajservis.dto.SobaSaKrevetimaZahtev;
import rs.hostel.smestajservis.dto.SobaZahtev;
import rs.hostel.smestajservis.service.SobaService;

import java.util.List;

@RestController
@RequestMapping("/api/sobe")
public class SobaController {

	private final SobaService sobaService;

	public SobaController(SobaService sobaService) {
		this.sobaService = sobaService;
	}

	/** Sa ?hostelId= vraca sobe tog hostela, bez njega sve sobe. */
	@GetMapping
	public List<SobaOdgovor> sobe(@RequestParam(required = false) Long hostelId) {
		return sobaService.sveSobe(hostelId);
	}

	@GetMapping("/{id}")
	public SobaOdgovor jednaSoba(@PathVariable Long id) {
		return sobaService.jednaSoba(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SobaOdgovor kreiraj(@Valid @RequestBody SobaZahtev zahtev) {
		return sobaService.kreiraj(zahtev);
	}

	/**
	 * KONVENIJENCIJA: soba + kreveti u jednom pozivu.
	 * Kreveti se zadaju ili listom oznaka ili brojem.
	 */
	@PostMapping("/sa-krevetima")
	@ResponseStatus(HttpStatus.CREATED)
	public SobaOdgovor kreirajSaKrevetima(@Valid @RequestBody SobaSaKrevetimaZahtev zahtev) {
		return sobaService.kreirajSaKrevetima(zahtev);
	}

	@PutMapping("/{id}")
	public SobaOdgovor izmeni(@PathVariable Long id,
							  @Valid @RequestBody SobaZahtev zahtev) {
		return sobaService.izmeni(id, zahtev);
	}

	/** Brise sobu i sve njene krevete (cascade). */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		sobaService.obrisi(id);
	}
}
