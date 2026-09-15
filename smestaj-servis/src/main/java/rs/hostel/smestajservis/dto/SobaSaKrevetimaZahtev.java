package rs.hostel.smestajservis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SobaSaKrevetimaZahtev extends SobaZahtev {

	private List<String> oznakeKreveta;

	@Positive(message = "Broj kreveta mora biti veci od nule")
	private Integer brojKreveta;

	@JsonIgnore
	@AssertTrue(message = "Zadajte ILI oznakeKreveta ILI brojKreveta - tacno jedno od to dvoje")
	public boolean isKrevetiIspravnoZadati() {
		boolean imaOznake = oznakeKreveta != null && !oznakeKreveta.isEmpty();
		boolean imaBroj = brojKreveta != null;
		return imaOznake ^ imaBroj;
	}
}
