package rs.hostel.rezervacijaservis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServisNedostupanException extends RuntimeException {

	private String message;
}
