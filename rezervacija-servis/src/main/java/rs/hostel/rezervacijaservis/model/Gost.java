package rs.hostel.rezervacijaservis.model;

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

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gost")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "broj_pasosa")
	private String brojPasosa;

	@Column(name = "datum_rodjenja")
	private LocalDate datumRodjenja;

	@Email(message = "Email nije u ispravnom formatu")
	@Column(name = "email")
	private String email;

	@NotBlank(message = "Ime je obavezno")
	@Column(name = "ime", nullable = false)
	private String ime;

	@NotBlank(message = "Prezime je obavezno")
	@Column(name = "prezime", nullable = false)
	private String prezime;

	@Column(name = "telefon")
	private String telefon;
}
