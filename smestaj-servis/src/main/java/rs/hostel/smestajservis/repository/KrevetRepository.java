package rs.hostel.smestajservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.smestajservis.model.Krevet;

import java.util.List;

public interface KrevetRepository extends JpaRepository<Krevet, Long> {

	@Query("""
			select k
			from Krevet k
			left join fetch k.soba s
			left join fetch s.tipSobe t
			where s.hostel.id = :hostelId
			order by k.id
			""")
	List<Krevet> nadjiPoHostelu(@Param("hostelId") Long hostelId);

	List<Krevet> findBySobaIdOrderById(Long sobaId);
}
