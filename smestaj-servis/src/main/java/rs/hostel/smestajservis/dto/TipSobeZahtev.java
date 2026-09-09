package rs.hostel.smestajservis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * ULAZ za POST/PUT /api/tipovi-soba.
 *
 * Nema polje id - njega dodeljuje baza pri kreiranju, a pri izmeni stize
 * iz putanje. Da je id ovde, klijent bi mogao da ga posalje i "premesti"
 * izmenu na tudji zapis.
 *
 * Izlazni DTO za TipSobe NE postoji namerno: ovaj entitet nema nijednu
 * relaciju, sva polja su prosta, pa nema sta da se ulancava u JSON-u -
 * odgovor je goli entitet.
 */
@Getter
@Setter
@NoArgsConstructor
public class TipSobeZahtev {

	@NotBlank(message = "Naziv tipa sobe je obavezan")
	private String naziv;

	@NotNull(message = "Kapacitet je obavezan")
	@Positive(message = "Kapacitet mora biti veci od nule")
	private Integer kapacitet;

	@NotNull(message = "Cena po nocenju je obavezna")
	@Positive(message = "Cena po nocenju mora biti veca od nule")
	private BigDecimal cenaPoNocenju;

	private boolean deljena;
}
