package rs.hostel.notifikacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.notifikacijaservis.model.Podesavanje;

import java.util.Optional;

public interface PodesavanjeRepository extends JpaRepository<Podesavanje, Long> {

	Optional<Podesavanje> findByEmail(String email);
}
