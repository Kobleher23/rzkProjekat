package rs.hostel.rezervacijaservis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rs.hostel.rezervacijaservis.dto.KrevetDTO;

import java.util.List;

/**
 * Deklarativni HTTP klijent ka Smestaj servisu.
 *
 * Mi pisemo SAMO interfejs - Feign u toku rada sam napravi implementaciju
 * koja salje pravi HTTP zahtev. Nema RestTemplate-a, nema rucnog sastavljanja
 * URL-a, nema parsiranja JSON-a.
 *
 * name = "SMESTAJ-SERVIS" je ime pod kojim je Smestaj servis registrovan
 * na Eureki (dolazi iz njegovog spring.application.name). Feign to ime
 * odnese Eureki, dobije stvarnu adresu (localhost:8081) i tek onda salje zahtev.
 * Zato nigde ne pisemo host ni port.
 */
@FeignClient(name = "SMESTAJ-SERVIS")
public interface SmestajClient {

	/**
	 * Gadja GET http://SMESTAJ-SERVIS/api/interni/kreveti?hostelId={hostelId}
	 */
	@GetMapping("/api/interni/kreveti")
	List<KrevetDTO> kreveti(@RequestParam("hostelId") Long hostelId);
}
