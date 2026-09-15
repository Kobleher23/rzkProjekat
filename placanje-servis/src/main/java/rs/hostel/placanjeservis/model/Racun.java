package rs.hostel.placanjeservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.placanjeservis.enums.StatusRacuna;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "racun")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Racun {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "datum_kreiranja")
	private LocalDateTime datumKreiranja;


	@Column(name = "iznos", precision = 10, scale = 2)
	private BigDecimal iznos;


	@Column(name = "rezervacija_id")
	private Long rezervacijaId;


	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private StatusRacuna status = StatusRacuna.NEPLACEN;

	@OneToMany(mappedBy = "racun", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StavkaRacuna> stavke = new ArrayList<>();

	@OneToMany(mappedBy = "racun", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Uplata> uplate = new ArrayList<>();

	@PrePersist
	public void preUpisa() {
		if (datumKreiranja == null) {
			datumKreiranja = LocalDateTime.now();
		}
		if (status == null) {
			status = StatusRacuna.NEPLACEN;
		}
	}
}
