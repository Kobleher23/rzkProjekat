package rs.hostel.rezervacijaservis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class KrevetDTO {

	private Long krevetId;

	private String oznaka;

	private Long sobaId;

	private Long tipSobeId;

	private String tipSobeNaziv;

	@JsonAlias("cenaPoNocenju")
	private BigDecimal cena;
}
