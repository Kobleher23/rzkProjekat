package rs.hostel.placanjeservis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Jedna stavka onako kako je klijent salje pri kreiranju racuna.
 * Namerno NIJE entitet StavkaRacuna - klijent ne sme da postavlja
 * id ni referencu na racun, to radi servis.
 */
@Getter
@Setter
@NoArgsConstructor
public class StavkaZahtev {

	@NotBlank(message = "Opis stavke je obavezan")
	private String opis;

	@NotNull(message = "Iznos stavke je obavezan")
	@Positive(message = "Iznos stavke mora biti veci od nule")
	private BigDecimal iznos;
}
