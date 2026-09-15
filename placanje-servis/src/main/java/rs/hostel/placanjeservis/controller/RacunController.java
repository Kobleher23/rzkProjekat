package rs.hostel.placanjeservis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rs.hostel.placanjeservis.dto.KreirajRacunZahtev;
import rs.hostel.placanjeservis.dto.UplataZahtev;
import rs.hostel.placanjeservis.model.Racun;
import rs.hostel.placanjeservis.service.PlacanjeService;

import java.util.List;

@RestController
@RequestMapping("/api/racuni")
@RequiredArgsConstructor
public class RacunController {

	private final PlacanjeService placanjeService;

	@GetMapping
	public List<Racun> sviRacuni() {
		return placanjeService.sviRacuni();
	}

	@GetMapping("/{id}")
	public Racun jedanRacun(@PathVariable Long id) {
		return placanjeService.nadjiPoId(id);
	}

	@GetMapping("/po-rezervaciji")
	public List<Racun> poRezervaciji(@RequestParam Long rezervacijaId) {
		return placanjeService.racuniZaRezervaciju(rezervacijaId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Racun kreiraj(@Valid @RequestBody KreirajRacunZahtev zahtev) {
		return placanjeService.kreirajRacun(zahtev);
	}

	@PostMapping("/{id}/uplate")
	@ResponseStatus(HttpStatus.CREATED)
	public Racun evidentirajUplatu(@PathVariable Long id,
								   @Valid @RequestBody UplataZahtev zahtev) {
		return placanjeService.evidentirajUplatu(
				id, zahtev.getIznos(), zahtev.getNacinPlacanja());
	}

	@PutMapping("/{id}/storniraj")
	public Racun storniraj(@PathVariable Long id) {
		return placanjeService.storniraj(id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void obrisi(@PathVariable Long id) {
		placanjeService.obrisi(id);
	}
}
