package rs.hostel.smestajservis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import rs.hostel.smestajservis.dto.KrevetOdgovor;
import rs.hostel.smestajservis.dto.KrevetZahtev;
import rs.hostel.smestajservis.service.KrevetService;

import java.util.List;

@RestController
@RequestMapping("/api/kreveti")
@RequiredArgsConstructor
public class KrevetController {

	private final KrevetService krevetService;

	@GetMapping
	public List<KrevetOdgovor> kreveti(@RequestParam(required = false) Long sobaId) {
		return krevetService.kreveti(sobaId);
	}

	@GetMapping("/{id}")
	public KrevetOdgovor jedanKrevet(@PathVariable Long id) {
		return krevetService.jedanKrevet(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public KrevetOdgovor kreiraj(@Valid @RequestBody KrevetZahtev zahtev) {
		return krevetService.kreiraj(zahtev);
	}

	@PutMapping("/{id}")
	public KrevetOdgovor izmeni(@PathVariable Long id,
								@Valid @RequestBody KrevetZahtev zahtev) {
		return krevetService.izmeni(id, zahtev);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		krevetService.obrisi(id);
	}
}
