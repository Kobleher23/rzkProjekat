package rs.hostel.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka naming server - "telefonski imenik" sistema.
 *
 * Svaki servis se ovde prijavi svojim imenom (spring.application.name)
 * i kaze na kojoj adresi i portu se nalazi. Kasnije Feign trazi servis
 * PO IMENU, a Eureka mu vrati konkretnu adresu.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
