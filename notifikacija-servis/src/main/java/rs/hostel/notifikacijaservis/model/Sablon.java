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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.notifikacijaservis.enums.TipNotifikacije;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sablon")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Sablon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotBlank(message = "Naslov je obavezan")
	@Column(name = "naslov", nullable = false)
	private String naslov;

	@NotBlank(message = "Telo sablona je obavezno")
	@Lob
	@Column(name = "telo", nullable = false, columnDefinition = "longtext")
	private String telo;

	@NotNull(message = "Tip sablona je obavezan")
	@Enumerated(EnumType.STRING)
	@Column(name = "tip", nullable = false, unique = true)
	private TipNotifikacije tip;
}
