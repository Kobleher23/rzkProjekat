package rs.hostel.smestajservis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KrevetInfoDTO {

	private Long krevetId;

	private String oznaka;

	private Long sobaId;

	private Long tipSobeId;

	private String tipSobeNaziv;

	private BigDecimal cenaPoNocenju;
}
