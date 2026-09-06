package rs.hostel.placanjeservis.model;

/**
 * Zivotni ciklus racuna.
 * NEPLACEN   -> racun je izdat, uplate jos ne pokrivaju ceo iznos
 * PLACEN     -> zbir USPESNIH uplata je dostigao (ili premasio) iznos racuna
 * STORNIRAN  -> racun je ponisten; na njega se vise ne evidentiraju uplate
 */
public enum StatusRacuna {
	NEPLACEN,
	PLACEN,
	STORNIRAN
}
