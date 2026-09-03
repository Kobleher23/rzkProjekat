package rs.hostel.rezervacijaservis.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Slika kreveta onako kako je vidi Rezervacija servis.
 *
 * Ovo NIJE klasa Krevet iz Smestaj servisa - to je zasebna klasa u nasem
 * paketu, koju popunjava Jackson iz JSON-a koji Smestaj posalje.
 */
@Getter
@Setter
@NoArgsConstructor
public class KrevetDTO {

	private Long krevetId;

	private String oznaka;

	private Long sobaId;

	private Long tipSobeId;

	private String tipSobeNaziv;

	// Smestaj servis ovo polje salje pod imenom "cenaPoNocenju",
	// a kod nas se zove "cena".
	//
	// @JsonAlias menja SAMO citanje: prihvata i "cenaPoNocenju" i "cena"
	// kada popunjava objekat. Pri slanju ostaje nase ime - "cena".
	// (@JsonProperty bi preimenovao polje u oba smera, pa bi i nas
	//  odgovor izlazio kao "cenaPoNocenju".)
	@JsonAlias("cenaPoNocenju")
	private BigDecimal cena;
}
