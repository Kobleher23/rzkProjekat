package rs.hostel.placanjeservis.model;

/**
 * Kojim kanalom je novac stigao. Cuva se po uplati, ne po racunu -
 * jedan racun se moze platiti u vise rata i razlicitim kanalima.
 */
public enum NacinPlacanja {
	KARTICA,
	GOTOVINA,
	PRENOS
}
