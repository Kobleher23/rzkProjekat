package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Soba {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String brojSobe;

	private Integer sprat;

	// Vise soba pripada jednom hostelu. Ovo je vlasnik veze -
	// ovde se u tabeli "soba" pravi kolona hostel_id (strani kljuc).
	//
	// @JsonIgnore prekida petlju Hostel -> sobe -> hostel -> sobe -> ...
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostel_id")
	private Hostel hostel;

	// Vise soba moze imati isti tip. Kolona tip_sobe_id je strani kljuc.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tip_sobe_id")
	private TipSobe tipSobe;

	// Jedna soba ima vise kreveta. Vlasnik veze je Krevet (polje "soba").
	@OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Krevet> kreveti = new ArrayList<>();
}
