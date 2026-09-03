package rs.hostel.smestajservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.smestajservis.model.Hostel;

public interface HostelRepository extends JpaRepository<Hostel, Long> {
}
