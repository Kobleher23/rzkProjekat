package rs.hostel.smestajservis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * IZLAZ za krevet.
 *
 * Entitet Krevet ima @ManyToOne ka Sobi. Kad bismo vracali goli entitet,
 * Jackson bi krenuo Krevet -> soba -> kreveti -> soba ... Ovde umesto
 * celog objekta stoji samo sobaId - ravno i bez ulancavanja.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KrevetOdgovor {

	private Long id;

	private String oznaka;

	private Long sobaId;
}
