package rs.hostel.rezervacijaservis.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.rezervacijaservis.dto.KrevetDTO;
import rs.hostel.rezervacijaservis.service.DostupnostService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dostupnost")
public class DostupnostController {

	private final DostupnostService dostupnostService;

	public DostupnostController(DostupnostService dostupnostService) {
		this.dostupnostService = dostupnostService;
	}

	/**
	 * GET /api/dostupnost?hostelId=1&dolazak=2026-09-01&odlazak=2026-09-05
	 * Vraca krevete koji su slobodni u celom trazenom terminu.
	 */
	@GetMapping
	public List<KrevetDTO> dostupni(
			@RequestParam Long hostelId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dolazak,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate odlazak) {
		return dostupnostService.pretraziDostupne(hostelId, dolazak, odlazak);
	}
}
