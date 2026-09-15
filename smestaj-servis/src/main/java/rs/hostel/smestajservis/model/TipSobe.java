package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tip_sobe")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipSobe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "cena_po_nocenju", precision = 38, scale = 2)
	private BigDecimal cenaPoNocenju;


	@Column(name = "deljena", nullable = false)
	private boolean deljena;

	@Column(name = "kapacitet")
	private Integer kapacitet;

	@Column(name = "naziv")
	private String naziv;
}
