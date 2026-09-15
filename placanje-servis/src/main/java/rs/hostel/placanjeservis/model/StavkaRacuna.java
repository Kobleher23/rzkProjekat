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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stavka_racuna")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StavkaRacuna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull(message = "Iznos stavke je obavezan")
	@Positive(message = "Iznos stavke mora biti veci od nule")
	@Column(name = "iznos", nullable = false, precision = 10, scale = 2)
	private BigDecimal iznos;

	@NotBlank(message = "Opis stavke je obavezan")
	@Column(name = "opis", nullable = false)
	private String opis;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "racun_id")
	private Racun racun;
}
