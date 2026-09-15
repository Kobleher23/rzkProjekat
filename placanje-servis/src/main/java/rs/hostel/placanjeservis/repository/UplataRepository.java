package rs.hostel.placanjeservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.placanjeservis.enums.StatusUplate;
import rs.hostel.placanjeservis.model.Uplata;

import java.math.BigDecimal;
import java.util.List;

public interface UplataRepository extends JpaRepository<Uplata, Long> {

	List<Uplata> findByRacunId(Long racunId);

	@Query("""
			select coalesce(sum(u.iznos), 0)
			from Uplata u
			where u.racun.id = :racunId
			  and u.status = :status
			""")
	BigDecimal zbirUplata(@Param("racunId") Long racunId,
						  @Param("status") StatusUplate status);
}
