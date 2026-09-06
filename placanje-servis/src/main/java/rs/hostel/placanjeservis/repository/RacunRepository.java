package rs.hostel.placanjeservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.hostel.placanjeservis.model.Racun;

import java.util.List;

public interface RacunRepository extends JpaRepository<Racun, Long> {

	// Spring Data izvodi upit iz imena metode - nije potreban @Query.
	// Koristi se kada nas zanima sta je sve naplaceno za jednu rezervaciju.
	List<Racun> findByRezervacijaId(Long rezervacijaId);
}
