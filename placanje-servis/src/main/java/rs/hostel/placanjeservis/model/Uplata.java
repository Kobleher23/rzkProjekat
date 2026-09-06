package rs.hostel.placanjeservis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Uplata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Vise uplata pripada jednom racunu (placanje u ratama).
	// @JsonIgnore prekida petlju Racun -> uplate -> racun -> ...
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "racun_id")
	private Racun racun;

	@Column(precision = 10, scale = 2)
	private BigDecimal iznos;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private NacinPlacanja nacinPlacanja;

	private LocalDateTime datum;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StatusUplate status = StatusUplate.USPESNA;

	@PrePersist
	public void preUpisa() {
		if (datum == null) {
			datum = LocalDateTime.now();
		}
		if (status == null) {
			status = StatusUplate.USPESNA;
		}
	}
}
