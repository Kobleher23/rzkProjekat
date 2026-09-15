package rs.hostel.notifikacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.notifikacijaservis.enums.TipNotifikacije;
import rs.hostel.notifikacijaservis.model.Notifikacija;

import java.util.List;

public interface NotifikacijaRepository extends JpaRepository<Notifikacija, Long> {

	List<Notifikacija> findByPrimalacEmailOrderByDatumDesc(String primalacEmail);

	List<Notifikacija> findByTipOrderByDatumDesc(TipNotifikacije tip);
}
