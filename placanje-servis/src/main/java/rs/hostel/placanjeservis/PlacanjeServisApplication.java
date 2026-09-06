package rs.hostel.placanjeservis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Placanje servis (Faza 5A).
 *
 * Za sada je samostalan: ne zove druge servise, samo se registruje na Eureku
 * da bi ga drugi mogli naci u Fazi 5B.
 */
@SpringBootApplication
public class PlacanjeServisApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlacanjeServisApplication.class, args);
	}

}
