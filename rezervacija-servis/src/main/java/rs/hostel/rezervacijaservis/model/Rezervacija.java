package rs.hostel.rezervacijaservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.rezervacijaservis.enums.StatusRezervacije;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rezervacija")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rezervacija {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@NotNull(message = "Datum dolaska je obavezan")
	@Column(name = "datum_dolaska", nullable = false)
	private LocalDate datumDolaska;

	@Column(name = "datum_kreiranja")
	private LocalDateTime datumKreiranja;

	@NotNull(message = "Datum odlaska je obavezan")
	@Column(name = "datum_odlaska", nullable = false)
	private LocalDate datumOdlaska;

	@Column(name = "hostel_id")
	private Long hostelId;


	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private StatusRezervacije status = StatusRezervacije.KREIRANA;

	@Column(name = "ukupna_cena", precision = 10, scale = 2)
	private BigDecimal ukupnaCena;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gost_id")
	private Gost gost;


	@OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StavkaRezervacije> stavke = new ArrayList<>();

	@PrePersist
	public void preUpisa() {
		if (datumKreiranja == null) {
			datumKreiranja = LocalDateTime.now();
		}
		if (status == null) {
			status = StatusRezervacije.KREIRANA;
		}
	}
}
