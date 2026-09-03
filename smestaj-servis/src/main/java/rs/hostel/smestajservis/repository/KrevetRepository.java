package rs.hostel.smestajservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.smestajservis.model.Krevet;

import java.util.List;

public interface KrevetRepository extends JpaRepository<Krevet, Long> {

	/**
	 * Svi kreveti u jednom hostelu, zajedno sa sobom i tipom sobe.
	 *
	 * "left join fetch" znaci: povuci sobu i tip sobe ODMAH, u istom upitu.
	 * Bez toga bi Hibernate za svaki krevet slao dodatne upite (N+1 problem):
	 * 1 upit za krevete + 1 za sobu svakog kreveta + 1 za tip svake sobe.
	 *
	 * "left" (a ne obican join) da se ne izgube kreveti u sobi
	 * kojoj tip sobe nije postavljen.
	 */
	@Query("""
			select k
			from Krevet k
			left join fetch k.soba s
			left join fetch s.tipSobe t
			where s.hostel.id = :hostelId
			order by k.id
			""")
	List<Krevet> nadjiPoHostelu(@Param("hostelId") Long hostelId);
}
