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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Racun {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// PAZNJA: obican Long, NE relacija - rezervacija zivi u bazi
	// rezervacija-servisa (hostel_rezervacija). Ovde cuvamo samo referencu.
	private Long rezervacijaId;

	// Ukupan iznos racuna. NE postavlja ga klijent - servis ga racuna
	// kao zbir stavki, da klijent ne bi mogao da posalje pogresan total.
	@Column(precision = 10, scale = 2)
	private BigDecimal iznos;

	// EnumType.STRING -> u bazi stoji tekst "NEPLACEN", a ne redni broj.
	// Da je ORDINAL, ubacivanje nove vrednosti u sredinu enum-a bi
	// promenilo znacenje vec upisanih redova.
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StatusRacuna status = StatusRacuna.NEPLACEN;

	private LocalDateTime datumKreiranja;

	// Jedan racun ima vise stavki. Vlasnik veze je StavkaRacuna (polje "racun"),
	// zato mappedBy. CascadeType.ALL -> cuvanje racuna cuva i njegove stavke.
	@OneToMany(mappedBy = "racun", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StavkaRacuna> stavke = new ArrayList<>();

	// Jedan racun ima vise uplata (placanje u ratama, ili neuspeli pokusaji).
	@OneToMany(mappedBy = "racun", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Uplata> uplate = new ArrayList<>();

	// Poziva se automatski pre prvog upisa u bazu.
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
