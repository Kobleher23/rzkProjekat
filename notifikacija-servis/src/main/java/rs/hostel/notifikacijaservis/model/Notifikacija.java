package rs.hostel.notifikacijaservis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.notifikacijaservis.enums.StatusNotifikacije;
import rs.hostel.notifikacijaservis.enums.TipNotifikacije;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notifikacija")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notifikacija {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "datum")
	private LocalDateTime datum;

	@Column(name = "primalac_email")
	private String primalacEmail;

	@Lob
	@Column(name = "sadrzaj", columnDefinition = "longtext")
	private String sadrzaj;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private StatusNotifikacije status;

	@Enumerated(EnumType.STRING)
	@Column(name = "tip")
	private TipNotifikacije tip;

	@PrePersist
	public void preUpisa() {
		if (datum == null) {
			datum = LocalDateTime.now();
		}
	}
}
