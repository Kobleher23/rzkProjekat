package rs.hostel.smestajservis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.smestajservis.dto.KrevetInfoDTO;
import rs.hostel.smestajservis.service.KrevetService;

import java.util.List;

/**
 * API namenjen DRUGIM SERVISIMA, ne krajnjem korisniku.
 *
 * Prefiks "/api/interni" je dogovor da se na prvi pogled vidi razlika:
 * /api/hosteli je javni API, /api/interni/... zove Rezervacija preko Feign-a.
 * Kasnije, kad dodamo Gateway (Faza 7), interne rute se lako zatvore spolja.
 */
@RestController
@RequestMapping("/api/interni")
@RequiredArgsConstructor
public class InterniController {

	private final KrevetService krevetService;

	/**
	 * GET /api/interni/kreveti?hostelId=1
	 * Vraca sve krevete hostela, sa nazivom tipa sobe i cenom po nocenju.
	 */
	@GetMapping("/kreveti")
	public List<KrevetInfoDTO> kreveti(@RequestParam Long hostelId) {
		return krevetService.kreveiPoHostelu(hostelId);
	}
}
