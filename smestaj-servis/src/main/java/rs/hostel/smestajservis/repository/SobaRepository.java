package rs.hostel.smestajservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.smestajservis.model.Soba;

import java.util.List;
import java.util.Optional;

public interface SobaRepository extends JpaRepository<Soba, Long> {

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

	long countByTipSobeId(Long tipSobeId);
}
