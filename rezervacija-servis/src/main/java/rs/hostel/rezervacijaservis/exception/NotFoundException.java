package rs.hostel.rezervacijaservis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Baca se kada trazeni zapis ne postoji.
 * @ResponseStatus automatski pretvara ovaj izuzetak u HTTP 404.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	public NotFoundException(String poruka) {
		super(poruka);
	}
}
