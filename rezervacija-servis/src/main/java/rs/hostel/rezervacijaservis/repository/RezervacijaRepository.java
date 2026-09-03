package rs.hostel.rezervacijaservis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.hostel.rezervacijaservis.model.Rezervacija;
import rs.hostel.rezervacijaservis.model.StatusRezervacije;

import java.time.LocalDate;
import java.util.List;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {

	/**
	 * Nalazi sve rezervacije koje za dati krevet PREKLAPAJU trazeni termin.
	 *
	 * Uslov preklapanja (klasicni "interval overlap"):
	 *     postojeci.dolazak < trazeni.odlazak  I  trazeni.dolazak < postojeci.odlazak
	 *
	 * Strogo "<" (a ne "<=") znaci da je dan odlaska slobodan za novog gosta -
	 * jedan se ujutru odjavljuje, drugi se popodne prijavljuje u isti krevet.
	 *
	 * Koji se status izuzima prosledjuje servisni sloj - repozitorijum
	 * samo izvrsava upit i ne odlucuje o poslovnim pravilima.
	 */
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
