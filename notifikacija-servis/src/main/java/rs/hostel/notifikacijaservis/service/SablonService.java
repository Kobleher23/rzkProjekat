package rs.hostel.notifikacijaservis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.notifikacijaservis.exception.BadRequestException;
import rs.hostel.notifikacijaservis.exception.NotFoundException;
import rs.hostel.notifikacijaservis.model.Sablon;
import rs.hostel.notifikacijaservis.model.TipNotifikacije;
import rs.hostel.notifikacijaservis.repository.SablonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SablonService {

	private final SablonRepository sablonRepository;

	@Transactional(readOnly = true)
	public List<Sablon> sviSabloni() {
		return sablonRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Sablon nadjiPoId(Long id) {
		return sablonRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Sablon sa id " + id + " ne postoji"));
	}

	@Transactional(readOnly = true)
	public Sablon nadjiPoTipu(TipNotifikacije tip) {
		return sablonRepository.findByTip(tip)
				.orElseThrow(() -> new NotFoundException("Ne postoji sablon za tip " + tip));
	}

	@Transactional
	public Sablon kreiraj(Sablon sablon) {
		sablon.setId(null);
		// Tip je jedinstven - lepsa poruka nego da pukne constraint iz baze.
		if (sablonRepository.existsByTip(sablon.getTip())) {
			throw new BadRequestException(
					"Sablon za tip " + sablon.getTip() + " vec postoji - izmenite postojeci");
		}
		return sablonRepository.save(sablon);
	}

	@Transactional
	public Sablon izmeni(Long id, Sablon izmenjeni) {
		Sablon postojeci = nadjiPoId(id);
		// Menjanje tipa bi moglo da napravi duplikat, pa proveravamo
		// samo kada se tip zaista menja.
		if (postojeci.getTip() != izmenjeni.getTip()
				&& sablonRepository.existsByTip(izmenjeni.getTip())) {
			throw new BadRequestException(
					"Sablon za tip " + izmenjeni.getTip() + " vec postoji");
		}
		postojeci.setTip(izmenjeni.getTip());
		postojeci.setNaslov(izmenjeni.getNaslov());
		postojeci.setTelo(izmenjeni.getTelo());
		return sablonRepository.save(postojeci);
	}

	@Transactional
	public void obrisi(Long id) {
		if (!sablonRepository.existsById(id)) {
			throw new NotFoundException("Sablon sa id " + id + " ne postoji");
		}
		sablonRepository.deleteById(id);
	}
}
