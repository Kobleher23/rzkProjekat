package rs.hostel.notifikacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.notifikacijaservis.enums.TipNotifikacije;
import rs.hostel.notifikacijaservis.model.Sablon;

import java.util.Optional;

public interface SablonRepository extends JpaRepository<Sablon, Long> {

	Optional<Sablon> findByTip(TipNotifikacije tip);

	boolean existsByTip(TipNotifikacije tip);
}
