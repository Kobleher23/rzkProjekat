package rs.hostel.smestajservis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * ULAZ za POST /api/sobe/sa-krevetima - konvenijencija koja u jednom
 * pozivu pravi sobu i njene krevete.
 *
 * Nasledjuje sva polja sobe i dodaje DVA medjusobno iskljuciva nacina da
 * se zadaju kreveti:
 *   - oznakeKreveta: ["A","B","C"]  -> tacno te oznake
 *   - brojKreveta:   3              -> automatski A, B, C
 */
@Getter
@Setter
@NoArgsConstructor
public class SobaSaKrevetimaZahtev extends SobaZahtev {

	private List<String> oznakeKreveta;

	@Positive(message = "Broj kreveta mora biti veci od nule")
	private Integer brojKreveta;

	/**
	 * Bean Validation poziva svaki "is..." metod oznacen sa @AssertTrue i
	 * ocekuje true. Ovako pravilo "tacno jedan od dva nacina" ulazi u
	 * standardnu validaciju i zavrsava kao 400 kroz globalni handler -
	 * bez rucne provere u servisu.
	 */
	@JsonIgnore
	@AssertTrue(message = "Zadajte ILI oznakeKreveta ILI brojKreveta - tacno jedno od to dvoje")
	public boolean isKrevetiIspravnoZadati() {
		boolean imaOznake = oznakeKreveta != null && !oznakeKreveta.isEmpty();
		boolean imaBroj = brojKreveta != null;
		return imaOznake ^ imaBroj;
	}
}
