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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Istorija - jedan red po pokusaju slanja.
 *
 * Sadrzaj se cuva POPUNJEN (posle zamene placeholdera), a ne kao referenca
 * na sablon. Razlog: sablon se vremenom menja, a mi moramo da znamo sta je
 * tacno pisalo u poruci koja je stvarno otisla gostu.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notifikacija {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String primalacEmail;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private TipNotifikacije tip;

	// @Lob -> u MySQL-u postaje TEXT umesto VARCHAR(255),
	// jer popunjena poruka lako prelazi 255 karaktera.
	@Lob
	private String sadrzaj;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private StatusNotifikacije status;

	private LocalDateTime datum;

	@PrePersist
	public void preUpisa() {
		if (datum == null) {
			datum = LocalDateTime.now();
		}
	}
}
