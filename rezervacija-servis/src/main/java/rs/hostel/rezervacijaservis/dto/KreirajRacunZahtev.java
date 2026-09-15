package rs.hostel.rezervacijaservis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KreirajRacunZahtev {

	private Long rezervacijaId;

	private List<StavkaRacunaDTO> stavke = new ArrayList<>();
}
