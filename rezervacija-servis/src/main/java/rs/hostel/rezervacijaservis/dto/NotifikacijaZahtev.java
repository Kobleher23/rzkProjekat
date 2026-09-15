package rs.hostel.rezervacijaservis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class NotifikacijaZahtev {

	@JsonProperty("email")
	private String primalacEmail;

	private String tip;

	private Map<String, String> parametri = new HashMap<>();
}
