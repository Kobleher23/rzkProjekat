package rs.hostel.smestajservis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.smestajservis.dto.KrevetInfoDTO;
import rs.hostel.smestajservis.service.KrevetService;

import java.util.List;

@RestController
@RequestMapping("/api/interni")
@RequiredArgsConstructor
public class InterniController {

	private final KrevetService krevetService;

	@GetMapping("/kreveti")
	public List<KrevetInfoDTO> kreveti(@RequestParam Long hostelId) {
		return krevetService.kreveiPoHostelu(hostelId);
	}
}
