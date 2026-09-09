package rs.hostel.smestajservis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.smestajservis.dto.KrevetInfoDTO;
import rs.hostel.smestajservis.dto.KrevetOdgovor;
import rs.hostel.smestajservis.dto.KrevetZahtev;
import rs.hostel.smestajservis.exception.NotFoundException;
import rs.hostel.smestajservis.model.Krevet;
import rs.hostel.smestajservis.model.Soba;
import rs.hostel.smestajservis.model.TipSobe;
import rs.hostel.smestajservis.repository.KrevetRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KrevetService {

	private final KrevetRepository krevetRepository;
	// Ponovo koristimo SobaService da bi se "soba ne postoji -> 404"
	// pisalo na jednom mestu.
	private final SobaService sobaService;

	// ---------- postojece: hrani /api/interni/kreveti (Feign iz Rezervacije) ----------

	@Transactional(readOnly = true)
	public List<KrevetInfoDTO> kreveiPoHostelu(Long hostelId) {
		return krevetRepository.nadjiPoHostelu(hostelId).stream()
				.map(this::uDto)
				.toList();
	}

	// ---------- CRUD (Faza 5A.5) ----------

	@Transactional(readOnly = true)
	public List<KrevetOdgovor> kreveti(Long sobaId) {
		// sobaId je opcion: bez njega vracamo sve krevete.
		List<Krevet> kreveti = (sobaId == null)
				? krevetRepository.findAll()
				: krevetRepository.findBySobaIdOrderById(sobaId);
		return kreveti.stream().map(this::uOdgovor).toList();
	}

	@Transactional(readOnly = true)
	public KrevetOdgovor jedanKrevet(Long id) {
		return uOdgovor(nadjiEntitet(id));
	}

	@Transactional
	public KrevetOdgovor kreiraj(KrevetZahtev zahtev) {
		// Id sobe se pretvara u pravi entitet; ako soba ne postoji,
		// SobaService baca NotFoundException -> 404.
		Soba soba = sobaService.nadjiEntitet(zahtev.getSobaId());

		Krevet krevet = new Krevet();
		krevet.setOznaka(zahtev.getOznaka());
		krevet.setSoba(soba);

		return uOdgovor(krevetRepository.save(krevet));
	}

	/**
	 * Menja se SAMO oznaka.
	 *
	 * Premestanje kreveta u drugu sobu bi promenilo koliko je koja soba
	 * popunjena, a krevet je vec mozda rezervisan preko svog id-ja u
	 * Rezervacija servisu - zato to namerno nije podrzano.
	 */
	@Transactional
	public KrevetOdgovor izmeni(Long id, KrevetZahtev zahtev) {
		Krevet krevet = nadjiEntitet(id);
		krevet.setOznaka(zahtev.getOznaka());
		return uOdgovor(krevetRepository.save(krevet));
	}

	@Transactional
	public void obrisi(Long id) {
		Krevet krevet = nadjiEntitet(id);
		krevetRepository.delete(krevet);
	}

	// ---------- pomocne ----------

	private Krevet nadjiEntitet(Long id) {
		return krevetRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Krevet sa id " + id + " ne postoji"));
	}

	private KrevetOdgovor uOdgovor(Krevet krevet) {
		Soba soba = krevet.getSoba();
		return new KrevetOdgovor(
				krevet.getId(),
				krevet.getOznaka(),
				(soba != null) ? soba.getId() : null);
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
