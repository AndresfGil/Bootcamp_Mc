package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import reactor.core.publisher.Mono;

public interface BootcampRepository {

    Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp);

    Mono<CustomPage<Bootcamp>> listarBootcamps(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    );
}
