package rs.hostel.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config server - centralno mesto sa koga svi servisi povlace konfiguraciju.
 *
 * @EnableConfigServer podize REST endpointe oblika
 *     /{ime-servisa}/{profil}
 * koji citaju .properties fajlove iz git repoa zadatog u
 * spring.cloud.config.server.git.uri i vracaju ih kao JSON.
 *
 * Namerno se NE registruje na Eureku: servisi mu pristupaju preko fiksne
 * adrese jos pre nego sto uopste imaju svoju konfiguraciju - dakle i pre
 * nego sto znaju gde je Eureka.
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
