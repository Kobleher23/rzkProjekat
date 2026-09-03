package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipSobe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// npr. "Zenski dorm 6 kreveta", "Dvokrevetna soba", "Mesoviti dorm 8 kreveta"
	private String naziv;

	private Integer kapacitet;

	private BigDecimal cenaPoNocenju;

	// da li se soba deli sa drugim gostima (dorm) ili je privatna
	private boolean deljena;
}
