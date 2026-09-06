package rs.hostel.placanjeservis.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.placanjeservis.model.NacinPlacanja;

import java.math.BigDecimal;

/**
 * Telo zahteva za POST /api/racuni/{id}/uplate.
 * Id racuna se NE salje u telu - stize iz putanje.
 */
@Getter
@Setter
@NoArgsConstructor
public class UplataZahtev {

	@NotNull(message = "Iznos uplate je obavezan")
	@Positive(message = "Iznos uplate mora biti veci od nule")
	private BigDecimal iznos;

	@NotNull(message = "Nacin placanja je obavezan (KARTICA, GOTOVINA ili PRENOS)")
	private NacinPlacanja nacinPlacanja;
}
