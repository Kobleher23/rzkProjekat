package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hostel")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Hostel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "adresa")
	private String adresa;

	@NotBlank(message = "Grad je obavezan")
	@Column(name = "grad", nullable = false)
	private String grad;

	@Column(name = "kontakt")
	private String kontakt;

	@NotBlank(message = "Naziv hostela je obavezan")
	@Column(name = "naziv", nullable = false)
	private String naziv;

	@Column(name = "opis", length = 1000)
	private String opis;

	@OneToMany(mappedBy = "hostel", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Soba> sobe = new ArrayList<>();
}
