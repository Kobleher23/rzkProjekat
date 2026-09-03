package rs.hostel.smestajservis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Baca se kada trazeni zapis ne postoji.
 *
 * @ResponseStatus govori Spring-u da ovaj izuzetak automatski pretvori
 * u HTTP 404, pa kontroler ne mora nista da hvata.
 * Kasnije cemo ovo zameniti sa @RestControllerAdvice (globalni error handler).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	public NotFoundException(String poruka) {
		super(poruka);
	}
}
