package rs.hostel.rezervacijaservis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RacunOdgovorDTO {

	private Long id;

	private BigDecimal iznos;

	private String status;
}
