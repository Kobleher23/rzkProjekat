package rs.hostel.rezervacijaservis.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.hostel.rezervacijaservis.dto.NotifikacijaZahtev;

@FeignClient(name = "NOTIFIKACIJE-SERVIS",
		contextId = "notifikacije",
		fallbackFactory = NotifikacijaClientFallbackFactory.class)
public interface NotifikacijaClient {

	@PostMapping("/api/notifikacije")
	void posalji(@RequestBody NotifikacijaZahtev zahtev);
}
