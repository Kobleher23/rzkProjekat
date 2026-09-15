package rs.hostel.rezervacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.rezervacijaservis.enums.StatusRezervacije;
import rs.hostel.rezervacijaservis.model.Rezervacija;

import java.time.LocalDate;
import java.util.List;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {

	@Query("""
			select distinct r
			from Rezervacija r
			join r.stavke s
			where s.krevetId = :krevetId
			  and r.status <> :izuzetiStatus
			  and r.datumDolaska < :datumOdlaska
			  and :datumDolaska < r.datumOdlaska
			""")
	List<Rezervacija> nadjiPreklapajuce(@Param("krevetId") Long krevetId,
										@Param("datumDolaska") LocalDate datumDolaska,
										@Param("datumOdlaska") LocalDate datumOdlaska,
										@Param("izuzetiStatus") StatusRezervacije izuzetiStatus);
}
