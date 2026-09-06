package rs.hostel.notifikacijaservis.model;

/**
 * Povod za slanje. Tip je istovremeno i kljuc po kom se bira Sablon -
 * zato je u tabeli sablon ova kolona jedinstvena.
 */
public enum TipNotifikacije {
	POTVRDA_REZERVACIJE,
	PODSETNIK,
	OTKAZIVANJE,
	PRIZNANICA
}
