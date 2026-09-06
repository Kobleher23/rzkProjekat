package rs.hostel.notifikacijaservis.model;

/**
 * Ishod pokusaja slanja.
 * POSLATA   -> obavestenje je otislo primaocu
 * NEUSPESNA -> nije poslato (npr. primalac je iskljucio email obavestenja);
 *              red i dalje ostaje u bazi kao trag da je pokusaj postojao
 */
public enum StatusNotifikacije {
	POSLATA,
	NEUSPESNA
}
