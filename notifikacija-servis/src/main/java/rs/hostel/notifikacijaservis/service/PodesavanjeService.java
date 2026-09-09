package rs.hostel.notifikacijaservis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.notifikacijaservis.exception.BadRequestException;
import rs.hostel.notifikacijaservis.exception.NotFoundException;
import rs.hostel.notifikacijaservis.model.Podesavanje;
import rs.hostel.notifikacijaservis.repository.PodesavanjeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PodesavanjeService {

	private final PodesavanjeRepository podesavanjeRepository;

	@Transactional(readOnly = true)
	public List<Podesavanje> svaPodesavanja() {
		return podesavanjeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Podesavanje nadjiPoId(Long id) {
		return podesavanjeRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Podesavanje sa id " + id + " ne postoji"));
	}

	@Transactional(readOnly = true)
	public Podesavanje nadjiPoEmailu(String email) {
		return podesavanjeRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(
						"Ne postoji podesavanje za email " + email));
	}

	@Transactional
	public Podesavanje kreiraj(Podesavanje podesavanje) {
		podesavanje.setId(null);
		if (podesavanjeRepository.findByEmail(podesavanje.getEmail()).isPresent()) {
			throw new BadRequestException(
					"Podesavanje za email " + podesavanje.getEmail() + " vec postoji");
		}
		return podesavanjeRepository.save(podesavanje);
	}

	@Transactional
	public Podesavanje izmeni(Long id, Podesavanje izmenjeno) {
		Podesavanje postojece = nadjiPoId(id);
		postojece.setEmail(izmenjeno.getEmail());
		postojece.setEmailUkljuceno(izmenjeno.isEmailUkljuceno());
		return podesavanjeRepository.save(postojece);
	}

	@Transactional
	public void obrisi(Long id) {
		if (!podesavanjeRepository.existsById(id)) {
			throw new NotFoundException("Podesavanje sa id " + id + " ne postoji");
		}
		podesavanjeRepository.deleteById(id);
	}
}
