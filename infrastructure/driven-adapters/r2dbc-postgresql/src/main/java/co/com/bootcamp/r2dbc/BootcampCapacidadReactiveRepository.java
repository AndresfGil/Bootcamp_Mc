package co.com.bootcamp.r2dbc;

import co.com.bootcamp.r2dbc.entities.BootcampCapacidadEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampCapacidadReactiveRepository extends ReactiveCrudRepository<BootcampCapacidadEntity, Long> {
    

    Mono<Void> deleteByBootcampId(Long bootcampId);
    
    @Query("SELECT DISTINCT capacidad_id FROM bootcamp_capacidad WHERE bootcamp_id != :bootcampIdExcluir AND capacidad_id IN (:capacidadIds)")
    Flux<Long> findCapacidadIdsReferenciadasPorOtrosBootcamps(Long bootcampIdExcluir, List<Long> capacidadIds);
}

