package rs.hostel.rezervacijaservis.model;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StavkaRezervacije {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Vise stavki pripada jednoj rezervaciji. Rezervacija je u OVOJ bazi,
	// pa je ovo prava JPA relacija (kolona rezervacija_id kao strani kljuc).
	//
	// @JsonIgnore prekida beskonacnu petlju pri slanju JSON-a:
	// Rezervacija -> stavke -> rezervacija -> stavke -> ...
	// Stavka se ionako uvek prikazuje UNUTAR svoje rezervacije, pa je
	// ta povratna referenca u JSON-u suvisna.
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rezervacija_id")
	private Rezervacija rezervacija;

	// PAZNJA: obicni Long-ovi, NE relacije - krevet i tip sobe zive
	// u bazi smestaj-servisa. Ovde cuvamo samo referencu (ID).
	private Long krevetId;

	private Long tipSobeId;

	@Column(precision = 10, scale = 2)
	private BigDecimal cena;
}
