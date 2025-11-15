package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampRepository {

    Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp);

    Mono<CustomPage<Bootcamp>> listarBootcamps(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    );
    
    Mono<Bootcamp> obtenerBootcampPorId(Long id);
    
    Mono<Void> eliminarBootcamp(Long id);
    
    Mono<List<Long>> obtenerCapacidadesReferenciadasPorOtrosBootcamps(List<Long> capacidadIds, Long bootcampIdExcluir);
    
    Mono<List<Long>> obtenerTecnologiasReferenciadasPorOtrosBootcamps(List<Long> tecnologiaIds, Long bootcampIdExcluir);
    
    Mono<Void> guardarRelacionesCapacidades(Long bootcampId, List<Long> capacidadIds);
    
    Mono<Void> guardarRelacionesTecnologias(Long bootcampId, List<Long> tecnologiaIds);
}
