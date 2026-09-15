package rs.hostel.rezervacijaservis.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifikacijaClientFallbackFactory implements FallbackFactory<NotifikacijaClient> {

	@Override
	public NotifikacijaClient create(Throwable uzrok) {
		return zahtev -> log.warn(
				"FALLBACK Notifikacije - obavestenje {} za {} NIJE poslato, rezervacija se nastavlja. Uzrok: {}",
				zahtev.getTip(), zahtev.getPrimalacEmail(), uzrok.toString());
	}
}
