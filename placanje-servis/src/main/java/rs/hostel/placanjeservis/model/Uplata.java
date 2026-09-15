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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.placanjeservis.enums.NacinPlacanja;
import rs.hostel.placanjeservis.enums.StatusUplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "uplata")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Uplata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "datum")
	private LocalDateTime datum;

	@Column(name = "iznos", precision = 10, scale = 2)
	private BigDecimal iznos;

	@Enumerated(EnumType.STRING)
	@Column(name = "nacin_placanja")
	private NacinPlacanja nacinPlacanja;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private StatusUplate status = StatusUplate.USPESNA;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "racun_id")
	private Racun racun;

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
