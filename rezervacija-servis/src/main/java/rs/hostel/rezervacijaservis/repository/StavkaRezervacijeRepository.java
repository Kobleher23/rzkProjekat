package rs.hostel.rezervacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.rezervacijaservis.model.StavkaRezervacije;

public interface StavkaRezervacijeRepository extends JpaRepository<StavkaRezervacije, Long> {
}
