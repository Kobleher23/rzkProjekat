package rs.hostel.notifikacijaservis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Baca se kada je zahtev besmislen po poslovnim pravilima
 * (npr. sablon za taj tip vec postoji).
 * @ResponseStatus automatski pretvara ovaj izuzetak u HTTP 400.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	public BadRequestException(String poruka) {
		super(poruka);
	}
}
