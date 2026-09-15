package rs.hostel.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ApiKeyGlobalFilter implements GlobalFilter, Ordered {

	private static final String HEADER_API_KEY = "x-api-key";

	@Value("${api.security.key}")
	private String ocekivaniKljuc;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest zahtev = exchange.getRequest();
		String putanja = zahtev.getURI().getPath();
		String poslatiKljuc = zahtev.getHeaders().getFirst(HEADER_API_KEY);

		if (poslatiKljuc == null || !poslatiKljuc.equals(ocekivaniKljuc)) {
			log.warn("ODBIJENO 403 - {} {} (x-api-key {})",
					zahtev.getMethod(), putanja, poslatiKljuc == null ? "nedostaje" : "neispravan");
			exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
			return exchange.getResponse().setComplete();
		}

		log.info("PROPUSTENO - {} {}", zahtev.getMethod(), putanja);
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
