package rs.hostel.rezervacijaservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
// Kada se Gost ucitava LAZY, Hibernate umesto pravog objekta podmetne "proxy"
// koji ima interna polja (hibernateLazyInitializer, handler). Jackson ne ume
// da ih serijalizuje i puca -> ovde mu kazemo da ih preskoci.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Ime je obavezno")
	private String ime;

	@NotBlank(message = "Prezime je obavezno")
	private String prezime;

	@Email(message = "Email nije u ispravnom formatu")
	private String email;

	private String telefon;

	private String brojPasosa;

	private LocalDate datumRodjenja;
}
