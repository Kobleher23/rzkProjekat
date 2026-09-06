package rs.hostel.notifikacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.notifikacijaservis.model.Notifikacija;
import rs.hostel.notifikacijaservis.model.TipNotifikacije;

import java.util.List;

public interface NotifikacijaRepository extends JpaRepository<Notifikacija, Long> {

	// Istorija za jednog primaoca, najnovije prvo.
	List<Notifikacija> findByPrimalacEmailOrderByDatumDesc(String primalacEmail);

	List<Notifikacija> findByTipOrderByDatumDesc(TipNotifikacije tip);
}
