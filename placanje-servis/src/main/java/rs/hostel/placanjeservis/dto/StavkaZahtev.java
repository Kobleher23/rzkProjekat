package rs.hostel.placanjeservis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
