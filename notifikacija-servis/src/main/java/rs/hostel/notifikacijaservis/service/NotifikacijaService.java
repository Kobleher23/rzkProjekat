package rs.hostel.notifikacijaservis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.hostel.notifikacijaservis.enums.StatusNotifikacije;
import rs.hostel.notifikacijaservis.enums.TipNotifikacije;
import rs.hostel.notifikacijaservis.exception.NotFoundException;
import rs.hostel.notifikacijaservis.model.Notifikacija;
import rs.hostel.notifikacijaservis.model.Podesavanje;
import rs.hostel.notifikacijaservis.model.Sablon;
import rs.hostel.notifikacijaservis.repository.NotifikacijaRepository;
import rs.hostel.notifikacijaservis.repository.PodesavanjeRepository;
import rs.hostel.notifikacijaservis.repository.SablonRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifikacijaService {

	private static final Pattern PLACEHOLDER = Pattern.compile("\\{([^}]+)\\}");

	private final NotifikacijaRepository notifikacijaRepository;
	private final SablonRepository sablonRepository;
	private final PodesavanjeRepository podesavanjeRepository;

	@Transactional(readOnly = true)
	public List<Notifikacija> sveNotifikacije() {
		return notifikacijaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Notifikacija nadjiPoId(Long id) {
		return notifikacijaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Notifikacija sa id " + id + " ne postoji"));
	}

	@Transactional(readOnly = true)
	public List<Notifikacija> istorijaZaEmail(String email) {
		return notifikacijaRepository.findByPrimalacEmailOrderByDatumDesc(email);
	}

	@Transactional
	public Notifikacija posaljiNotifikaciju(String email, TipNotifikacije tip,
											Map<String, String> parametri) {

		Sablon sablon = sablonRepository.findByTip(tip)
				.orElseThrow(() -> new NotFoundException(
						"Ne postoji sablon za tip " + tip + " - prvo ga napravite preko /api/sabloni"));

		String naslov = popuni(sablon.getNaslov(), parametri);
		String sadrzaj = popuni(sablon.getTelo(), parametri);

		upozoriNaNepopunjene(sadrzaj, tip);

		boolean dozvoljeno = podesavanjeRepository.findByEmail(email)
				.map(Podesavanje::isEmailUkljuceno)
				.orElse(true);

		Notifikacija notifikacija = new Notifikacija();
		notifikacija.setPrimalacEmail(email);
		notifikacija.setTip(tip);
		notifikacija.setSadrzaj(sadrzaj);

		if (!dozvoljeno) {
			notifikacija.setStatus(StatusNotifikacije.NEUSPESNA);
			log.warn("NIJE POSLATO - primalac {} je iskljucio email obavestenja (tip={})",
					email, tip);
			return notifikacijaRepository.save(notifikacija);
		}

		log.info("SALJEM EMAIL -> {} | naslov: {} | telo: {}", email, naslov, sadrzaj);

		notifikacija.setStatus(StatusNotifikacije.POSLATA);
		Notifikacija sacuvana = notifikacijaRepository.save(notifikacija);
		log.info("Notifikacija id={} zabelezena kao POSLATA (tip={}, primalac={})",
				sacuvana.getId(), tip, email);
		return sacuvana;
	}

	@Transactional
	public void obrisi(Long id) {
		if (!notifikacijaRepository.existsById(id)) {
			throw new NotFoundException("Notifikacija sa id " + id + " ne postoji");
		}
		notifikacijaRepository.deleteById(id);
	}

	private String popuni(String tekst, Map<String, String> parametri) {
		if (tekst == null || parametri == null || parametri.isEmpty()) {
			return tekst;
		}
		String rezultat = tekst;
		for (Map.Entry<String, String> par : parametri.entrySet()) {
			if (par.getKey() == null || par.getValue() == null) {
				continue;
			}
			rezultat = rezultat.replace("{" + par.getKey() + "}", par.getValue());
		}
		return rezultat;
	}

	private void upozoriNaNepopunjene(String tekst, TipNotifikacije tip) {
		if (tekst == null) {
			return;
		}
		Matcher m = PLACEHOLDER.matcher(tekst);
		while (m.find()) {
			log.warn("Placeholder {} u sablonu za tip {} nije popunjen - salje se kao tekst",
					m.group(), tip);
		}
	}

	@Transactional(readOnly = true)
	public Optional<Sablon> sablonZaTip(TipNotifikacije tip) {
		return sablonRepository.findByTip(tip);
	}
}
