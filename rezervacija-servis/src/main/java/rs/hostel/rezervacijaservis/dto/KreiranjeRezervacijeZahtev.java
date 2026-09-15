package rs.hostel.rezervacijaservis.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KreiranjeRezervacijeZahtev {

	@NotNull(message = "Id gosta je obavezan")
	private Long gostId;

	@NotNull(message = "Id hostela je obavezan")
	private Long hostelId;

	@NotNull(message = "Datum dolaska je obavezan")
	private LocalDate dolazak;

	@NotNull(message = "Datum odlaska je obavezan")
	private LocalDate odlazak;

	@NotEmpty(message = "Izaberite bar jedan krevet")
	private List<@NotNull(message = "Id kreveta ne sme biti prazan") Long> krevetIds = new ArrayList<>();
}
