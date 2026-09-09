package rs.hostel.notifikacijaservis.controller;

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
import rs.hostel.notifikacijaservis.model.Podesavanje;
import rs.hostel.notifikacijaservis.service.PodesavanjeService;

import java.util.List;

@RestController
@RequestMapping("/api/podesavanja")
@RequiredArgsConstructor
public class PodesavanjeController {

	private final PodesavanjeService podesavanjeService;

	@GetMapping
	public List<Podesavanje> svaPodesavanja() {
		return podesavanjeService.svaPodesavanja();
	}

	@GetMapping("/{id}")
	public Podesavanje jedno(@PathVariable Long id) {
		return podesavanjeService.nadjiPoId(id);
	}

	@GetMapping("/po-emailu")
	public Podesavanje poEmailu(@RequestParam String email) {
		return podesavanjeService.nadjiPoEmailu(email);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Podesavanje kreiraj(@Valid @RequestBody Podesavanje podesavanje) {
		return podesavanjeService.kreiraj(podesavanje);
	}

	@PutMapping("/{id}")
	public Podesavanje izmeni(@PathVariable Long id,
							  @Valid @RequestBody Podesavanje izmenjeno) {
		return podesavanjeService.izmeni(id, izmenjeno);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		podesavanjeService.obrisi(id);
	}
}
