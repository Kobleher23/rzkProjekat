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
public class StavkaRacunaDTO {

	private String opis;

	private BigDecimal iznos;
}
