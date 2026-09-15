package rs.hostel.placanjeservis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KreirajRacunZahtev {

	@NotNull(message = "Id rezervacije je obavezan")
	private Long rezervacijaId;

	@NotEmpty(message = "Racun mora imati bar jednu stavku")
	@Valid
	private List<StavkaZahtev> stavke = new ArrayList<>();
}
