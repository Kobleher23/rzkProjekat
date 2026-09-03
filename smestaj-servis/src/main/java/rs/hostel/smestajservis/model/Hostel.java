package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
// Sprecava da Jackson pukne na Hibernate LAZY proxy-ju.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Hostel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Naziv hostela je obavezan")
	private String naziv;

	private String adresa;

	@NotBlank(message = "Grad je obavezan")
	private String grad;

	@Column(length = 1000)
	private String opis;

	private String kontakt;

	// Jedan hostel ima vise soba. Vlasnik veze je Soba (polje "hostel"),
	// zato ovde stoji mappedBy - ova strana je "inverzna" (samo ogledalo).
	@OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Soba> sobe = new ArrayList<>();
}
