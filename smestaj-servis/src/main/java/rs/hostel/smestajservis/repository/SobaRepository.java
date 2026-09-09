package rs.hostel.smestajservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.smestajservis.model.Soba;

import java.util.List;
import java.util.Optional;

public interface SobaRepository extends JpaRepository<Soba, Long> {

	/**
	 * Sve sobe jednog hostela, sa vec ucitanim tipom i krevetima.
	 *
	 * "left join fetch" povlaci relacije u ISTOM upitu. Bez toga bi
	 * mapiranje u DTO okinulo poseban SELECT za tip i za krevete svake
	 * sobe (N+1 problem).
	 *
	 * "distinct" je nuzan jer join sa krevetima umnozava red sobe
	 * onoliko puta koliko soba ima kreveta.
	 */
	@Query("""
			select distinct s
			from Soba s
			left join fetch s.tipSobe
			left join fetch s.kreveti
			where s.hostel.id = :hostelId
			order by s.id
			""")
	List<Soba> nadjiPoHostelu(@Param("hostelId") Long hostelId);

	@Query("""
			select distinct s
			from Soba s
			left join fetch s.tipSobe
			left join fetch s.kreveti
			order by s.id
			""")
	List<Soba> nadjiSveSaDetaljima();

	@Query("""
			select distinct s
			from Soba s
			left join fetch s.tipSobe
			left join fetch s.kreveti
			where s.id = :id
			""")
	Optional<Soba> nadjiSaDetaljima(@Param("id") Long id);

	// Osnova za guarded delete tipa sobe: koliko soba koristi dati tip.
	long countByTipSobeId(Long tipSobeId);
}
