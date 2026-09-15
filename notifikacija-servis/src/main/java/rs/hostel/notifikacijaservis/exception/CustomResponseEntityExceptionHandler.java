package rs.hostel.notifikacijaservis.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ErrorEntity> handleNotFoundException(NotFoundException ex) {
		ErrorEntity errorEntity = new ErrorEntity(ex.getMessage(), LocalDate.now());
		return new ResponseEntity<>(errorEntity, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	public final ResponseEntity<ErrorEntity> handleBadRequestException(BadRequestException ex) {
		ErrorEntity errorEntity = new ErrorEntity(ex.getMessage(), LocalDate.now());
		return new ResponseEntity<>(errorEntity, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String, String> greske = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(greska -> greske.put(greska.getField(), greska.getDefaultMessage()));
		ex.getBindingResult().getGlobalErrors()
				.forEach(greska -> greske.put(greska.getObjectName(), greska.getDefaultMessage()));
		return new ResponseEntity<>(greske, HttpStatus.BAD_REQUEST);
	}
}
