package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampRepository {

    Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp);
}
