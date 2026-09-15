package rs.hostel.notifikacijaservis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.notifikacijaservis.enums.TipNotifikacije;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PosaljiNotifikacijuZahtev {

	@NotBlank(message = "Email primaoca je obavezan")
	@Email(message = "Email nije u ispravnom formatu")
	private String email;

	@NotNull(message = "Tip notifikacije je obavezan")
	private TipNotifikacije tip;

	private Map<String, String> parametri = new HashMap<>();
}
