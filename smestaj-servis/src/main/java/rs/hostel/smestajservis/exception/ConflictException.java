package rs.hostel.smestajservis.exception;

/**
 * Baca se kada je zahtev sam po sebi ispravan, ali se kosi sa trenutnim
 * stanjem podataka - npr. brisanje tipa sobe koji jos neko koristi.
 *
 * Nema @ResponseStatus: u HTTP 409 ga pretvara GlobalniObradjivacGresaka,
 * koji uz status vraca i uredno telo odgovora.
 */
public class ConflictException extends RuntimeException {

	public ConflictException(String poruka) {
		super(poruka);
	}
}
