package rs.hostel.smestajservis.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import rs.hostel.smestajservis.dto.GreskaOdgovor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JEDNO mesto na kom se izuzeci pretvaraju u HTTP odgovore.
 *
 * @RestControllerAdvice je presretac oko SVIH kontrolera: kada metoda
 * kontrolera baci izuzetak, Spring prvo trazi ovde metodu oznacenu sa
 * @ExceptionHandler za taj tip. Zato u kontrolerima i servisima nema
 * nijednog try/catch-a - oni samo bacaju izuzetak koji opisuje sta je
 * poslo naopako, a prevod u status kod i telo odgovora je ovde.
 */
@Slf4j
@RestControllerAdvice
public class GlobalniObradjivacGresaka {

	/**
	 * 400 - palo je @Valid nad ulaznim DTO-om.
	 *
	 * Spring skuplja SVE prekrsaje odjednom, pa klijent u jednom odgovoru
	 * dobija spisak svih losih polja umesto da ih otkriva jedno po jedno.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GreskaOdgovor> validacija(MethodArgumentNotValidException ex,
													HttpServletRequest zahtev) {
		// LinkedHashMap cuva redosled kojim su greske prijavljene.
		Map<String, String> greske = new LinkedHashMap<>();
		for (FieldError polje : ex.getBindingResult().getFieldErrors()) {
			greske.put(polje.getField(), polje.getDefaultMessage());
		}
		// Pravila napisana preko @AssertTrue nisu vezana za jedno polje,
		// pa stizu kao "globalne" greske - i njih ubacujemo u istu mapu.
		ex.getBindingResult().getGlobalErrors().forEach(g ->
				greske.put(g.getObjectName(), g.getDefaultMessage()));

		GreskaOdgovor telo = new GreskaOdgovor(
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				"Validacija nije prosla - proverite polja u odgovoru",
				zahtev.getRequestURI());
		telo.setGreske(greske);

		log.warn("Validacija pala na {} -> {}", zahtev.getRequestURI(), greske);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(telo);
	}

	/** 404 - trazeni zapis ne postoji. */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<GreskaOdgovor> nijeNadjeno(NotFoundException ex,
													 HttpServletRequest zahtev) {
		log.warn("404 na {} -> {}", zahtev.getRequestURI(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GreskaOdgovor(
				HttpStatus.NOT_FOUND.value(),
				"Not Found",
				ex.getMessage(),
				zahtev.getRequestURI()));
	}

	/** 409 - zahtev je ispravan, ali se kosi sa stanjem podataka (guarded delete). */
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<GreskaOdgovor> konflikt(ConflictException ex,
												  HttpServletRequest zahtev) {
		log.warn("409 na {} -> {}", zahtev.getRequestURI(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new GreskaOdgovor(
				HttpStatus.CONFLICT.value(),
				"Conflict",
				ex.getMessage(),
				zahtev.getRequestURI()));
	}

	/**
	 * 400 - telo zahteva uopste nije moglo da se procita: pokvaren JSON,
	 * slovo umesto broja, ili vrednost koja nije u enum-u. Bez ovoga bi
	 * Spring vratio podrazumevanu stranicu sa stack trace-om.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<GreskaOdgovor> neispravanJson(HttpMessageNotReadableException ex,
														HttpServletRequest zahtev) {
		log.warn("Neispravno telo na {} -> {}", zahtev.getRequestURI(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GreskaOdgovor(
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				"Telo zahteva nije ispravan JSON ili neko polje ima pogresan tip",
				zahtev.getRequestURI()));
	}

	/** 400 - parametar iz putanje/upita nije mogao da se pretvori u ocekivani tip. */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<GreskaOdgovor> losTipParametra(MethodArgumentTypeMismatchException ex,
														 HttpServletRequest zahtev) {
		String poruka = "Parametar '" + ex.getName() + "' ima neispravnu vrednost: " + ex.getValue();
		log.warn("Los parametar na {} -> {}", zahtev.getRequestURI(), poruka);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GreskaOdgovor(
				HttpStatus.BAD_REQUEST.value(),
				"Bad Request",
				poruka,
				zahtev.getRequestURI()));
	}

	/**
	 * 500 - sve sto nismo predvideli.
	 *
	 * Klijentu ide uredna poruka BEZ stack trace-a (stack trace u odgovoru
	 * otkriva unutrasnjost aplikacije), ali se ceo izuzetak ispisuje u log
	 * da nijedna greska ne bi bila progutana.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<GreskaOdgovor> neocekivano(Exception ex,
													 HttpServletRequest zahtev) {
		log.error("Neocekivana greska na {}", zahtev.getRequestURI(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GreskaOdgovor(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				"Doslo je do neocekivane greske - detalji su u logu servisa",
				zahtev.getRequestURI()));
	}
}
