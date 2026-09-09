package rs.hostel.smestajservis.service;

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
public class TipSobeService {

	private final TipSobeRepository tipSobeRepository;
	// Potreban je da bi se pre brisanja proverilo koliko soba koristi tip.
	private final SobaRepository sobaRepository;

	public TipSobeService(TipSobeRepository tipSobeRepository,
						  SobaRepository sobaRepository) {
		this.tipSobeRepository = tipSobeRepository;
		this.sobaRepository = sobaRepository;
	}

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

	/**
	 * GUARDED DELETE.
	 *
	 * Tip sobe je referenca koju sobe dele. Obicno brisanje bi ili puklo
	 * na stranom kljucu (ruzna SQL greska klijentu), ili - da je veza
	 * postavljena sa cascade - povuklo za sobom i sobe. Zato prvo brojimo
	 * ko ga koristi, pa odbijamo sa 409 i konkretnim brojem u poruci.
	 */
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

	// Kopiranje polje-po-polje iz DTO-a u entitet. Namerno rucno:
	// tako je ocigledno koja polja klijent sme da menja, a koja ne (id).
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
