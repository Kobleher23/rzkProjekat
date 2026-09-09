package rs.hostel.smestajservis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Jedinstven oblik tela odgovora za SVE greske ovog servisa.
 *
 * Zahvaljujuci ovome klijent uvek dobija ista polja, bez obzira da li je
 * pukla validacija, nesto nije nadjeno ili je brisanje odbijeno.
 *
 * NON_NULL -> polje "greske" se pojavljuje samo kod validacionih gresaka;
 * kod 404 i 409 ga uopste nema u JSON-u umesto da stoji kao null.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GreskaOdgovor {

	private LocalDateTime timestamp;

	private int status;

	private String error;

	private String message;

	private String path;

	// Mapa "ime polja" -> "poruka", popunjena samo kod validacije.
	private Map<String, String> greske;

	public GreskaOdgovor(int status, String error, String message, String path) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}
}
