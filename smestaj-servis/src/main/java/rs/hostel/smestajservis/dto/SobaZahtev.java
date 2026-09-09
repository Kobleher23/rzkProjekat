package rs.hostel.smestajservis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ULAZ za POST/PUT /api/sobe.
 *
 * Hostel i TipSobe stizu kao ID-jevi, a ne kao ugnezdeni objekti. Klijent
 * time NE moze da izmeni hostel ili tip sobe usput - servis ih ucitava iz
 * baze po id-ju i povezuje. Da smo primali cele objekte, Hibernate bi mogao
 * da prepise postojeci hostel poljima koja klijent posalje.
 */
@Getter
@Setter
@NoArgsConstructor
public class SobaZahtev {

	@NotBlank(message = "Broj sobe je obavezan")
	private String brojSobe;

	private Integer sprat;

	@NotNull(message = "Id hostela je obavezan")
	private Long hostelId;

	@NotNull(message = "Id tipa sobe je obavezan")
	private Long tipSobeId;
}
