package rs.hostel.smestajservis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.smestajservis.dto.KrevetOdgovor;
import rs.hostel.smestajservis.dto.SobaOdgovor;
import rs.hostel.smestajservis.dto.SobaSaKrevetimaZahtev;
import rs.hostel.smestajservis.dto.SobaZahtev;
import rs.hostel.smestajservis.exception.NotFoundException;
import rs.hostel.smestajservis.model.Hostel;
import rs.hostel.smestajservis.model.Krevet;
import rs.hostel.smestajservis.model.Soba;
import rs.hostel.smestajservis.model.TipSobe;
import rs.hostel.smestajservis.repository.HostelRepository;
import rs.hostel.smestajservis.repository.SobaRepository;
import rs.hostel.smestajservis.repository.TipSobeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SobaService {

	private final SobaRepository sobaRepository;
	private final HostelRepository hostelRepository;
	private final TipSobeRepository tipSobeRepository;

	@Transactional(readOnly = true)
	public List<SobaOdgovor> sveSobe(Long hostelId) {
		List<Soba> sobe = (hostelId == null)
				? sobaRepository.nadjiSveSaDetaljima()
				: sobaRepository.nadjiPoHostelu(hostelId);
		return sobe.stream().map(this::uOdgovor).toList();
	}

	@Transactional(readOnly = true)
	public SobaOdgovor jednaSoba(Long id) {
		return uOdgovor(nadjiEntitet(id));
	}


	@Transactional
	public SobaOdgovor kreiraj(SobaZahtev zahtev) {
		Soba soba = new Soba();
		soba.setBrojSobe(zahtev.getBrojSobe());
		soba.setSprat(zahtev.getSprat());

		soba.setHostel(nadjiHostel(zahtev.getHostelId()));
		soba.setTipSobe(nadjiTip(zahtev.getTipSobeId()));

		return uOdgovor(sobaRepository.save(soba));
	}

	@Transactional
	public SobaOdgovor kreirajSaKrevetima(SobaSaKrevetimaZahtev zahtev) {
		Soba soba = new Soba();
		soba.setBrojSobe(zahtev.getBrojSobe());
		soba.setSprat(zahtev.getSprat());
		soba.setHostel(nadjiHostel(zahtev.getHostelId()));
		soba.setTipSobe(nadjiTip(zahtev.getTipSobeId()));

		for (String oznaka : oznakeZa(zahtev)) {
			Krevet krevet = new Krevet();
			krevet.setOznaka(oznaka);
			krevet.setSoba(soba);
			soba.getKreveti().add(krevet);
		}

		return uOdgovor(sobaRepository.save(soba));
	}

	@Transactional
	public SobaOdgovor izmeni(Long id, SobaZahtev zahtev) {
		Soba soba = nadjiEntitet(id);
		soba.setBrojSobe(zahtev.getBrojSobe());
		soba.setSprat(zahtev.getSprat());

		if (zahtev.getTipSobeId() != null
				&& !zahtev.getTipSobeId().equals(soba.getTipSobe().getId())) {
			soba.setTipSobe(nadjiTip(zahtev.getTipSobeId()));
		}

		return uOdgovor(sobaRepository.save(soba));
	}

	@Transactional
	public void obrisi(Long id) {
		Soba soba = nadjiEntitet(id);
		sobaRepository.delete(soba);
	}

	@Transactional(readOnly = true)
	public Soba nadjiEntitet(Long id) {
		return sobaRepository.nadjiSaDetaljima(id)
				.orElseThrow(() -> new NotFoundException("Soba sa id " + id + " ne postoji"));
	}

	private Hostel nadjiHostel(Long hostelId) {
		return hostelRepository.findById(hostelId)
				.orElseThrow(() -> new NotFoundException(
						"Hostel sa id " + hostelId + " ne postoji"));
	}

	private TipSobe nadjiTip(Long tipSobeId) {
		return tipSobeRepository.findById(tipSobeId)
				.orElseThrow(() -> new NotFoundException(
						"Tip sobe sa id " + tipSobeId + " ne postoji"));
	}

	private List<String> oznakeZa(SobaSaKrevetimaZahtev zahtev) {
		if (zahtev.getOznakeKreveta() != null && !zahtev.getOznakeKreveta().isEmpty()) {
			return zahtev.getOznakeKreveta();
		}
		List<String> oznake = new ArrayList<>();
		for (int i = 0; i < zahtev.getBrojKreveta(); i++) {
			oznake.add(oznakaZaRedniBroj(i));
		}
		return oznake;
	}

	private String oznakaZaRedniBroj(int redniBroj) {
		if (redniBroj < 26) {
			return String.valueOf((char) ('A' + redniBroj));
		}
		return "K" + (redniBroj + 1);
	}


	private SobaOdgovor uOdgovor(Soba soba) {
		Hostel hostel = soba.getHostel();
		TipSobe tip = soba.getTipSobe();

		List<KrevetOdgovor> kreveti = soba.getKreveti().stream()
				.map(k -> new KrevetOdgovor(k.getId(), k.getOznaka(), soba.getId()))
				.toList();

		return new SobaOdgovor(
				soba.getId(),
				soba.getBrojSobe(),
				soba.getSprat(),
				(hostel != null) ? hostel.getId() : null,
				(hostel != null) ? hostel.getNaziv() : null,
				(tip != null) ? tip.getId() : null,
				(tip != null) ? tip.getNaziv() : null,
				(tip != null) ? tip.getCenaPoNocenju() : null,
				new ArrayList<>(kreveti));
	}
}
