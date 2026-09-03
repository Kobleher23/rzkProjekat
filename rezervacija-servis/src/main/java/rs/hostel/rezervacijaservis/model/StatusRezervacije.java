package rs.hostel.rezervacijaservis.model;

/**
 * Zivotni ciklus rezervacije.
 * KREIRANA   -> tek napravljena, jos nije potvrdjena
 * POTVRDJENA -> potvrdjena (npr. placena / garantovana)
 * PRIJAVLJEN -> gost se check-in-ovao
 * ODJAVLJEN  -> gost se check-out-ovao
 * OTKAZANA   -> otkazana; ovakve NE zauzimaju krevet
 */
public enum StatusRezervacije {
	KREIRANA,
	POTVRDJENA,
	PRIJAVLJEN,
	ODJAVLJEN,
	OTKAZANA
}
