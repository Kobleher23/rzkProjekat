package rs.hostel.notifikacijaservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "podesavanje")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Podesavanje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotBlank(message = "Email je obavezan")
	@Email(message = "Email nije u ispravnom formatu")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "email_ukljuceno", nullable = false)
	private boolean emailUkljuceno = true;
}
