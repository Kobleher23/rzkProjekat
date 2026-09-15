package rs.hostel.smestajservis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "soba")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Soba {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "broj_sobe")
	private String brojSobe;

	@Column(name = "sprat")
	private Integer sprat;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hostel_id")
	private Hostel hostel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tip_sobe_id")
	private TipSobe tipSobe;

	@OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Krevet> kreveti = new ArrayList<>();
}
