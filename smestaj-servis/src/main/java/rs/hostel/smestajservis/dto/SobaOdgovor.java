package rs.hostel.smestajservis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SobaOdgovor {

	private Long id;

	private String brojSobe;

	private Integer sprat;

	private Long hostelId;

	private String hostelNaziv;

	private Long tipSobeId;

	private String tipSobeNaziv;

	private BigDecimal cenaPoNocenju;

	private List<KrevetOdgovor> kreveti = new ArrayList<>();
}
