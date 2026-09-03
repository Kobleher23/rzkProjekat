package rs.hostel.smestajservis.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.smestajservis.exception.NotFoundException;
import rs.hostel.smestajservis.model.Hostel;
import rs.hostel.smestajservis.repository.HostelRepository;

import java.util.List;

/**
 * Servisni sloj - ovde zivi poslovna logika.
 *
 * Kontroler treba samo da prima HTTP zahtev i vraca odgovor;
 * repozitorijum treba samo da prica sa bazom. Sve izmedju (pravila,
 * provere, transakcije, kasnije i Feign pozivi) ide ovde.
 */
@Service
public class HostelService {

	private final HostelRepository hostelRepository;

	public HostelService(HostelRepository hostelRepository) {
		this.hostelRepository = hostelRepository;
	}

	// readOnly = true -> Hibernate zna da nema izmena, pa preskace
	// proveru "prljavih" objekata na kraju transakcije (brze je).
	@Transactional(readOnly = true)
	public List<Hostel> sviHosteli() {
		return hostelRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Hostel nadjiPoId(Long id) {
		return hostelRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Hostel sa id " + id + " ne postoji"));
	}

	@Transactional
	public Hostel kreiraj(Hostel hostel) {
		// Novi hostel ne sme da nosi id sa sobom - inace bi save() uradio
		// izmenu postojeceg umesto da napravi novi zapis.
		hostel.setId(null);
		return hostelRepository.save(hostel);
	}

	@Transactional
	public Hostel izmeni(Long id, Hostel izmenjeni) {
		Hostel postojeci = nadjiPoId(id);
		postojeci.setNaziv(izmenjeni.getNaziv());
		postojeci.setAdresa(izmenjeni.getAdresa());
		postojeci.setGrad(izmenjeni.getGrad());
		postojeci.setOpis(izmenjeni.getOpis());
		postojeci.setKontakt(izmenjeni.getKontakt());
		return hostelRepository.save(postojeci);
	}

	@Transactional
	public void obrisi(Long id) {
		if (!hostelRepository.existsById(id)) {
			throw new NotFoundException("Hostel sa id " + id + " ne postoji");
		}
		hostelRepository.deleteById(id);
	}
}
