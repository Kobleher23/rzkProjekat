package rs.hostel.placanjeservis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Telo zahteva za POST /api/racuni.
 *
 * Nema polja "iznos" - ukupan iznos NIJE ulaz nego rezultat: servis ga
 * racuna kao zbir stavki. Da je klijent slao total, mogao bi da posalje
 * broj koji se ne poklapa sa stavkama.
 */
@Getter
@Setter
@NoArgsConstructor
public class KreirajRacunZahtev {

	@NotNull(message = "Id rezervacije je obavezan")
	private Long rezervacijaId;

	// @Valid na listi propagira validaciju na svaku stavku ponaosob -
	// bez njega bi @NotBlank/@Positive unutar StavkaZahtev bili ignorisani.
	@NotEmpty(message = "Racun mora imati bar jednu stavku")
	@Valid
	private List<StavkaZahtev> stavke = new ArrayList<>();
}
