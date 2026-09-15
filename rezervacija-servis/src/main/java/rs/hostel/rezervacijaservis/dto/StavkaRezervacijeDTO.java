package rs.hostel.rezervacijaservis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StavkaRezervacijeDTO {

	private Long krevetId;

	private String oznaka;

	private Long tipSobeId;

	private BigDecimal cena;
}
