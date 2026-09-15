package rs.hostel.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/api/hosteli/**", "/api/sobe/**", "/api/kreveti/**", "/api/tipovi-soba/**")
						.uri("lb://SMESTAJ-SERVIS"))
				.route(p -> p.path("/api/rezervacije/**", "/api/gosti/**", "/api/dostupnost/**")
						.uri("lb://REZERVACIJA-SERVIS"))
				.route(p -> p.path("/api/racuni/**")
						.uri("lb://PLACANJE-SERVIS"))
				.route(p -> p.path("/api/notifikacije/**", "/api/sabloni/**", "/api/podesavanja/**")
						.uri("lb://NOTIFIKACIJE-SERVIS"))
				.build();
	}
}
