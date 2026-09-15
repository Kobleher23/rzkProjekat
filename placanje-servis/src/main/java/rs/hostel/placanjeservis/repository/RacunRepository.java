package rs.hostel.placanjeservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.placanjeservis.model.Racun;

import java.util.List;

public interface RacunRepository extends JpaRepository<Racun, Long> {

	List<Racun> findByRezervacijaId(Long rezervacijaId);
}
