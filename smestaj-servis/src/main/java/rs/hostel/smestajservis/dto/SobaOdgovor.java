package rs.hostel.smestajservis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * IZLAZ za sobu.
 *
 * Entitet Soba ima tri relacije (hostel, tipSobe, kreveti). Ovde su
 * spljostene: hostel i tip idu kao id + naziv, a kreveti kao lista
 * ravnih KrevetOdgovor objekata. Pozivalac dobija sve sto mu treba
 * u jednom odgovoru, a mi ostajemo slobodni da menjamo entitete.
 */
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
