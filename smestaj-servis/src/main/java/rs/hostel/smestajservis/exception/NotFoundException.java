package rs.hostel.smestajservis.exception;

/**
 * Baca se kada trazeni zapis ne postoji.
 *
 * RANIJE je ovde stajalo @ResponseStatus(HttpStatus.NOT_FOUND). Ta anotacija
 * ume da postavi samo STATUS - telo odgovora je i dalje pravio Spring, sa
 * stack trace-om i bez kontrole nad poljima.
 *
 * Sada izuzetak hvata GlobalniObradjivacGresaka i vraca isti oblik JSON-a
 * kao i sve ostale greske. Anotacija je uklonjena da bi bilo jasno gde se
 * odluka donosi - na jednom mestu, a ne raspodeljena po klasama izuzetaka.
 */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String poruka) {
		super(poruka);
	}
}
