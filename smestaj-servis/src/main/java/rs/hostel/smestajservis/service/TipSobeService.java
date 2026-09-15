package rs.hostel.smestajservis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.smestajservis.dto.TipSobeZahtev;
import rs.hostel.smestajservis.exception.ConflictException;
import rs.hostel.smestajservis.exception.NotFoundException;
import rs.hostel.smestajservis.model.TipSobe;
import rs.hostel.smestajservis.repository.SobaRepository;
import rs.hostel.smestajservis.repository.TipSobeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipSobeService {

	private final TipSobeRepository tipSobeRepository;
	private final SobaRepository sobaRepository;

	@Transactional(readOnly = true)
	public List<TipSobe> sviTipovi() {
		return tipSobeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public TipSobe nadjiPoId(Long id) {
		return tipSobeRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Tip sobe sa id " + id + " ne postoji"));
	}

	@Transactional
	public TipSobe kreiraj(TipSobeZahtev zahtev) {
		TipSobe tip = new TipSobe();
		primeni(tip, zahtev);
		return tipSobeRepository.save(tip);
	}

	@Transactional
	public TipSobe izmeni(Long id, TipSobeZahtev zahtev) {
		TipSobe postojeci = nadjiPoId(id);
		primeni(postojeci, zahtev);
		return tipSobeRepository.save(postojeci);
	}

	@Transactional
	public void obrisi(Long id) {
		TipSobe tip = nadjiPoId(id);

		long brojSoba = sobaRepository.countByTipSobeId(id);
		if (brojSoba > 0) {
			throw new ConflictException(
					"Tip sobe '" + tip.getNaziv() + "' (id " + id + ") koristi "
							+ brojSoba + " " + rec(brojSoba)
							+ " - prvo im promenite tip ili obrisite te sobe");
		}

		tipSobeRepository.deleteById(id);
	}

	private void primeni(TipSobe tip, TipSobeZahtev zahtev) {
		tip.setNaziv(zahtev.getNaziv());
		tip.setKapacitet(zahtev.getKapacitet());
		tip.setCenaPoNocenju(zahtev.getCenaPoNocenju());
		tip.setDeljena(zahtev.isDeljena());
	}

	private String rec(long broj) {
		return broj == 1 ? "soba" : "soba/sobe";
	}
}
