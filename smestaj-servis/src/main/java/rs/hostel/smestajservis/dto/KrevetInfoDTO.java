package rs.hostel.smestajservis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Podaci o jednom krevetu koje Smestaj servis salje drugim servisima.
 *
 * Ovo je "ravan" prikaz - spojeni podaci iz krevet -> soba -> tip_sobe,
 * bez JPA relacija i bez ulancanih objekata. Tako pozivalac dobija tacno
 * ono sto mu treba, a mi ostajemo slobodni da menjamo svoje entitete.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KrevetInfoDTO {

	private Long krevetId;

	private String oznaka;

	private Long sobaId;

	private Long tipSobeId;

	private String tipSobeNaziv;

	private BigDecimal cenaPoNocenju;
}
