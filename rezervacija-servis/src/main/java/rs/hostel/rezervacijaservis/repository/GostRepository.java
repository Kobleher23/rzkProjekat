package rs.hostel.rezervacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.rezervacijaservis.model.Gost;

public interface GostRepository extends JpaRepository<Gost, Long> {
}
