package rs.hostel.rezervacijaservis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.hostel.rezervacijaservis.enums.StatusRezervacije;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RezervacijaOdgovorDTO {

	private Long id;

	private StatusRezervacije status;

	private Long gostId;

	private Long hostelId;

	private LocalDate datumDolaska;

	private LocalDate datumOdlaska;

	private long brojNocenja;

	private BigDecimal ukupnaCena;

	private Long racunId;

	private List<StavkaRezervacijeDTO> stavke = new ArrayList<>();
}
