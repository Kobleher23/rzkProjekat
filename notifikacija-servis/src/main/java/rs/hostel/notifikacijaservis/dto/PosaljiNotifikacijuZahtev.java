package rs.hostel.notifikacijaservis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.notifikacijaservis.model.TipNotifikacije;

import java.util.HashMap;
import java.util.Map;

/**
 * Telo zahteva za POST /api/notifikacije.
 *
 * Klijent NE salje gotov tekst poruke - salje tip (koji bira sablon)
 * i mapu parametara kojima se popunjavaju placeholderi u sablonu.
 */
@Getter
@Setter
@NoArgsConstructor
public class PosaljiNotifikacijuZahtev {

	@NotBlank(message = "Email primaoca je obavezan")
	@Email(message = "Email nije u ispravnom formatu")
	private String email;

	@NotNull(message = "Tip notifikacije je obavezan")
	private TipNotifikacije tip;

	// Kljucevi odgovaraju placeholderima u sablonu bez zagrada:
	// telo "rezervacija {id}" + parametri {"id": "42"} -> "rezervacija 42".
	private Map<String, String> parametri = new HashMap<>();
}
