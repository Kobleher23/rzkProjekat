package rs.hostel.notifikacijaservis.controller;

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
import rs.hostel.notifikacijaservis.model.Sablon;
import rs.hostel.notifikacijaservis.model.TipNotifikacije;
import rs.hostel.notifikacijaservis.service.SablonService;

import java.util.List;

@RestController
@RequestMapping("/api/sabloni")
public class SablonController {

	private final SablonService sablonService;

	public SablonController(SablonService sablonService) {
		this.sablonService = sablonService;
	}

	@GetMapping
	public List<Sablon> sviSabloni() {
		return sablonService.sviSabloni();
	}

	@GetMapping("/{id}")
	public Sablon jedan(@PathVariable Long id) {
		return sablonService.nadjiPoId(id);
	}

	@GetMapping("/tip/{tip}")
	public Sablon poTipu(@PathVariable TipNotifikacije tip) {
		return sablonService.nadjiPoTipu(tip);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Sablon kreiraj(@Valid @RequestBody Sablon sablon) {
		return sablonService.kreiraj(sablon);
	}

	@PutMapping("/{id}")
	public Sablon izmeni(@PathVariable Long id, @Valid @RequestBody Sablon izmenjeni) {
		return sablonService.izmeni(id, izmenjeni);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		sablonService.obrisi(id);
	}
}
