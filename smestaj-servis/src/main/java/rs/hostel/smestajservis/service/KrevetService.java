package rs.hostel.smestajservis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.smestajservis.dto.KrevetInfoDTO;
import rs.hostel.smestajservis.model.Krevet;
import rs.hostel.smestajservis.model.Soba;
import rs.hostel.smestajservis.model.TipSobe;
import rs.hostel.smestajservis.repository.KrevetRepository;

import java.util.List;

@Service
public class KrevetService {

	private final KrevetRepository krevetRepository;

	public KrevetService(KrevetRepository krevetRepository) {
		this.krevetRepository = krevetRepository;
	}

	/**
	 * Svi kreveti datog hostela, pretvoreni u ravan DTO oblik
	 * pogodan za slanje drugom servisu.
	 */
	@Transactional(readOnly = true)
	public List<KrevetInfoDTO> kreveiPoHostelu(Long hostelId) {
		return krevetRepository.nadjiPoHostelu(hostelId).stream()
				.map(this::uDto)
				.toList();
	}

	private KrevetInfoDTO uDto(Krevet krevet) {
		Soba soba = krevet.getSoba();
		TipSobe tip = (soba != null) ? soba.getTipSobe() : null;

		return new KrevetInfoDTO(
				krevet.getId(),
				krevet.getOznaka(),
				(soba != null) ? soba.getId() : null,
				(tip != null) ? tip.getId() : null,
				(tip != null) ? tip.getNaziv() : null,
				(tip != null) ? tip.getCenaPoNocenju() : null
		);
	}
}
