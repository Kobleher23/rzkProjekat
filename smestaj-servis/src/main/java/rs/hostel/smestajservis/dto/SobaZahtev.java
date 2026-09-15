package rs.hostel.smestajservis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SobaZahtev {

	@NotBlank(message = "Broj sobe je obavezan")
	private String brojSobe;

	private Integer sprat;

	@NotNull(message = "Id hostela je obavezan")
	private Long hostelId;

	@NotNull(message = "Id tipa sobe je obavezan")
	private Long tipSobeId;
}
