package rs.hostel.rezervacijaservis.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.hostel.rezervacijaservis.dto.KreirajRacunZahtev;
import rs.hostel.rezervacijaservis.dto.RacunOdgovorDTO;

@FeignClient(name = "PLACANJE-SERVIS")
public interface PlacanjeClient {

	@PostMapping("/api/racuni")
	RacunOdgovorDTO kreirajRacun(@RequestBody KreirajRacunZahtev zahtev);
}
