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
import rs.hostel.smestajservis.model.Hostel;
import rs.hostel.smestajservis.service.HostelService;

import java.util.List;

/**
 * Kontroler zna samo za HTTP: putanje, statuse, telo zahteva.
 * Svu logiku prosledjuje servisu.
 */
@RestController
@RequestMapping("/api/hosteli")
public class HostelController {

	private final HostelService hostelService;

	public HostelController(HostelService hostelService) {
		this.hostelService = hostelService;
	}

	@GetMapping
	public List<Hostel> sviHosteli() {
		return hostelService.sviHosteli();
	}

	// Ako hostel ne postoji, servis baca NotFoundException -> Spring salje 404.
	@GetMapping("/{id}")
	public Hostel jedanHostel(@PathVariable Long id) {
		return hostelService.nadjiPoId(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Hostel kreiraj(@Valid @RequestBody Hostel hostel) {
		return hostelService.kreiraj(hostel);
	}

	@PutMapping("/{id}")
	public Hostel izmeni(@PathVariable Long id, @Valid @RequestBody Hostel izmenjeni) {
		return hostelService.izmeni(id, izmenjeni);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		hostelService.obrisi(id);
	}
}
