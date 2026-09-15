package rs.hostel.rezervacijaservis.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rs.hostel.rezervacijaservis.dto.KrevetDTO;

import java.util.List;

@FeignClient(name = "SMESTAJ-SERVIS")
public interface SmestajClient {

	@GetMapping("/api/interni/kreveti")
	List<KrevetDTO> kreveti(@RequestParam("hostelId") Long hostelId);
}
