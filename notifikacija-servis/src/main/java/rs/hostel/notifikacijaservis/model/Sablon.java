package rs.hostel.notifikacijaservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Predlozak poruke za jedan tip notifikacije.
 *
 * Telo sadrzi placeholdere u viticastim zagradama, npr.
 * "Postovani, vasa rezervacija {id} je potvrdjena."
 * Servis ih pri slanju zamenjuje stvarnim vrednostima.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Sablon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// unique = true -> po jedan sablon za svaki tip. Baza ovo cuva i onda
	// kada bi neko zaobisao servis i upisao red direktno u tabelu.
	@NotNull(message = "Tip sablona je obavezan")
	@Enumerated(EnumType.STRING)
	@Column(length = 30, unique = true)
	private TipNotifikacije tip;

	@NotBlank(message = "Naslov je obavezan")
	private String naslov;

	@NotBlank(message = "Telo sablona je obavezno")
	@Lob
	private String telo;
}
