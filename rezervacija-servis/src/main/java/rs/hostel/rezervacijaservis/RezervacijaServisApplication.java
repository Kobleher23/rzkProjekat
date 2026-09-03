package rs.hostel.rezervacijaservis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @EnableFeignClients kaze Spring-u da prodje kroz pakete, pronadje
 * interfejse oznacene sa @FeignClient i za svaki napravi implementaciju
 * u toku rada. Bez ove anotacije SmestajClient ostaje obican interfejs
 * i aplikacija puca jer nema sta da se ubaci (injektuje).
 */
@SpringBootApplication
@EnableFeignClients
public class RezervacijaServisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RezervacijaServisApplication.class, args);
	}

}
