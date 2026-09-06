package rs.hostel.placanjeservis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StavkaRacuna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Vise stavki pripada jednom racunu. Racun je u OVOJ bazi,
	// pa je ovo prava JPA relacija (kolona racun_id kao strani kljuc).
	//
	// @JsonIgnore prekida beskonacnu petlju pri slanju JSON-a:
	// Racun -> stavke -> racun -> stavke -> ...
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "racun_id")
	private Racun racun;

	@NotBlank(message = "Opis stavke je obavezan")
	private String opis;

	@NotNull(message = "Iznos stavke je obavezan")
	@Positive(message = "Iznos stavke mora biti veci od nule")
	@Column(precision = 10, scale = 2)
	private BigDecimal iznos;
}
