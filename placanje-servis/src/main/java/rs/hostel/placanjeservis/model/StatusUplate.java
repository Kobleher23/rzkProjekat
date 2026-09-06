package rs.hostel.placanjeservis.model;

/**
 * Ishod pojedinacne uplate.
 * USPESNA   -> novac je prosao; SAMO ove se sabiraju kad se racuna dug
 * NEUSPESNA -> pokusaj je odbijen (npr. kartica odbijena); ostaje kao trag
 */
public enum StatusUplate {
	USPESNA,
	NEUSPESNA
}
