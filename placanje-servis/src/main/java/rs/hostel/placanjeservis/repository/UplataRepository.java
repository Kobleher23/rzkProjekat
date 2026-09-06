package rs.hostel.placanjeservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.placanjeservis.model.StatusUplate;
import rs.hostel.placanjeservis.model.Uplata;

import java.math.BigDecimal;
import java.util.List;

public interface UplataRepository extends JpaRepository<Uplata, Long> {

	List<Uplata> findByRacunId(Long racunId);

	/**
	 * Zbir uplata za jedan racun, ali SAMO onih sa zadatim statusom
	 * (u praksi USPESNA - neuspeli pokusaji ne smeju da smanjuju dug).
	 *
	 * coalesce(..., 0) je bitan: kada racun jos nema nijednu uplatu,
	 * sum() bi vratio null i pozivalac bi pukao na NullPointerException.
	 */
	@Query("""
			select coalesce(sum(u.iznos), 0)
			from Uplata u
			where u.racun.id = :racunId
			  and u.status = :status
			""")
	BigDecimal zbirUplata(@Param("racunId") Long racunId,
						  @Param("status") StatusUplate status);
}
