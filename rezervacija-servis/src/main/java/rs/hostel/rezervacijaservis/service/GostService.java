package rs.hostel.rezervacijaservis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.rezervacijaservis.exception.NotFoundException;
import rs.hostel.rezervacijaservis.model.Gost;
import rs.hostel.rezervacijaservis.repository.GostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GostService {

	private final GostRepository gostRepository;

	@Transactional(readOnly = true)
	public List<Gost> sviGosti() {
		return gostRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Gost nadjiPoId(Long id) {
		return gostRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Gost sa id " + id + " ne postoji"));
	}

	@Transactional
	public Gost kreiraj(Gost gost) {
		gost.setId(null);
		return gostRepository.save(gost);
	}

	@Transactional
	public Gost izmeni(Long id, Gost izmenjeni) {
		Gost postojeci = nadjiPoId(id);
		postojeci.setIme(izmenjeni.getIme());
		postojeci.setPrezime(izmenjeni.getPrezime());
		postojeci.setEmail(izmenjeni.getEmail());
		postojeci.setTelefon(izmenjeni.getTelefon());
		postojeci.setBrojPasosa(izmenjeni.getBrojPasosa());
		postojeci.setDatumRodjenja(izmenjeni.getDatumRodjenja());
		return gostRepository.save(postojeci);
	}

	@Transactional
	public void obrisi(Long id) {
		if (!gostRepository.existsById(id)) {
			throw new NotFoundException("Gost sa id " + id + " ne postoji");
		}
		gostRepository.deleteById(id);
	}
}
