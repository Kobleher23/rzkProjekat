package rs.hostel.notifikacijaservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Preferenca jednog primaoca - da li uopste zeli email obavestenja.
 *
 * Ovo je "opt-out" tabela: ako za neki email NEMA reda, podrazumeva se
 * da je slanje dozvoljeno. Red se pravi tek kada neko iskljuci obavestenja.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Podesavanje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Email je obavezan")
	@Email(message = "Email nije u ispravnom formatu")
	@Column(unique = true)
	private String email;

	private boolean emailUkljuceno = true;
}
