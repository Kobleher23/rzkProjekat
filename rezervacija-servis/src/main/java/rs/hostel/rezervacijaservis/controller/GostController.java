package rs.hostel.rezervacijaservis.controller;

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
import rs.hostel.rezervacijaservis.model.Gost;
import rs.hostel.rezervacijaservis.service.GostService;

import java.util.List;

@RestController
@RequestMapping("/api/gosti")
public class GostController {

	private final GostService gostService;

	public GostController(GostService gostService) {
		this.gostService = gostService;
	}

	@GetMapping
	public List<Gost> sviGosti() {
		return gostService.sviGosti();
	}

	@GetMapping("/{id}")
	public Gost jedanGost(@PathVariable Long id) {
		return gostService.nadjiPoId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Gost kreiraj(@Valid @RequestBody Gost gost) {
		return gostService.kreiraj(gost);
	}

	@PutMapping("/{id}")
	public Gost izmeni(@PathVariable Long id, @Valid @RequestBody Gost izmenjeni) {
		return gostService.izmeni(id, izmenjeni);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		gostService.obrisi(id);
	}
}
