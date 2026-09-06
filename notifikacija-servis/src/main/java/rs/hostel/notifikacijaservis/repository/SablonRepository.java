package rs.hostel.notifikacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.notifikacijaservis.model.Sablon;
import rs.hostel.notifikacijaservis.model.TipNotifikacije;

import java.util.Optional;

public interface SablonRepository extends JpaRepository<Sablon, Long> {

	// Optional, a ne Sablon: tip mozda jos nema definisan sablon,
	// pa pozivalac mora eksplicitno da obradi taj slucaj.
	Optional<Sablon> findByTip(TipNotifikacije tip);

	boolean existsByTip(TipNotifikacije tip);
}
