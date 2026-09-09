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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.smestajservis.dto.TipSobeZahtev;
import rs.hostel.smestajservis.model.TipSobe;
import rs.hostel.smestajservis.service.TipSobeService;

import java.util.List;

/**
 * Ulaz je TipSobeZahtev, ali IZLAZ je goli entitet TipSobe - namerno.
 * TipSobe nema nijednu relaciju (naziv, kapacitet, cena, deljena su
 * prosta polja), pa nema sta da se ulancava i izlazni DTO ne bi doneo
 * nista osim jos jedne klase za odrzavanje.
 *
 * Nema try/catch: greske baca servis, a u HTTP odgovore ih pretvara
 * GlobalniObradjivacGresaka.
 */
@RestController
@RequestMapping("/api/tipovi-soba")
public class TipSobeController {

	private final TipSobeService tipSobeService;

	public TipSobeController(TipSobeService tipSobeService) {
		this.tipSobeService = tipSobeService;
	}

	@GetMapping
	public List<TipSobe> sviTipovi() {
		return tipSobeService.sviTipovi();
	}

	@GetMapping("/{id}")
	public TipSobe jedanTip(@PathVariable Long id) {
		return tipSobeService.nadjiPoId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TipSobe kreiraj(@Valid @RequestBody TipSobeZahtev zahtev) {
		return tipSobeService.kreiraj(zahtev);
	}

	@PutMapping("/{id}")
	public TipSobe izmeni(@PathVariable Long id,
						  @Valid @RequestBody TipSobeZahtev zahtev) {
		return tipSobeService.izmeni(id, zahtev);
	}

	/** Guarded delete: 409 ako tip jos koristi bar jedna soba. */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		tipSobeService.obrisi(id);
	}
}
