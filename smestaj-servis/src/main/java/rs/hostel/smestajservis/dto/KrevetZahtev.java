package rs.hostel.smestajservis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ULAZ za POST/PUT /api/kreveti.
 * Soba stize kao id - servis je ucitava i povezuje.
 */
@Getter
@Setter
@NoArgsConstructor
public class KrevetZahtev {

	@NotBlank(message = "Oznaka kreveta je obavezna")
	private String oznaka;

	@NotNull(message = "Id sobe je obavezan")
	private Long sobaId;
}
