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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rezervacija {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Vise rezervacija moze pripadati istom gostu. Gost je u OVOJ bazi,
	// pa je ovo prava JPA relacija (kolona gost_id kao strani kljuc).
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gost_id")
	private Gost gost;

	// PAZNJA: obican Long, NE relacija - hostel zivi u bazi smestaj-servisa.
	// Detaljno objasnjenje je u sazetku ispod.
	private Long hostelId;

	@NotNull(message = "Datum dolaska je obavezan")
	private LocalDate datumDolaska;

	@NotNull(message = "Datum odlaska je obavezan")
	private LocalDate datumOdlaska;

	// EnumType.STRING -> u bazi stoji tekst "POTVRDJENA", a ne broj 1.
	// Zasto: ako kasnije ubacimo novu vrednost u sredinu enum-a, brojevi bi se
	// pomerili i stari redovi bi odjednom znacili nesto drugo. Tekst je stabilan.
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StatusRezervacije status = StatusRezervacije.KREIRANA;

	@Column(precision = 10, scale = 2)
	private BigDecimal ukupnaCena;

	private LocalDateTime datumKreiranja;

	// Jedna rezervacija ima vise stavki (po jedan krevet po stavci).
	// Vlasnik veze je StavkaRezervacije (polje "rezervacija") -> zato mappedBy.
	@OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StavkaRezervacije> stavke = new ArrayList<>();

	// Poziva se automatski pre prvog upisa u bazu.
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
