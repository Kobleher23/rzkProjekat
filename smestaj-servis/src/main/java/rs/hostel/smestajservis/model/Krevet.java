package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Krevet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// npr. "A", "B", "gornji lezaj 1"
	private String oznaka;

	// Vise kreveta pripada jednoj sobi. Kolona soba_id je strani kljuc.
	//
	// @JsonIgnore prekida petlju Soba -> kreveti -> soba -> kreveti -> ...
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soba_id")
	private Soba soba;
}
