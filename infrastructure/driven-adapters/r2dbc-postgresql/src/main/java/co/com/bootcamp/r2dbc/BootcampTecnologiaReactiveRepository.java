package co.com.bootcamp.r2dbc;

import co.com.bootcamp.r2dbc.entities.BootcampTecnologiaEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampTecnologiaReactiveRepository extends ReactiveCrudRepository<BootcampTecnologiaEntity, Long> {
    

    Mono<Void> deleteByBootcampId(Long bootcampId);
    
    @Query("SELECT DISTINCT tecnologia_id FROM bootcamp_tecnologia WHERE bootcamp_id != :bootcampIdExcluir AND tecnologia_id IN (:tecnologiaIds)")
    Flux<Long> findTecnologiaIdsReferenciadasPorOtrosBootcamps(Long bootcampIdExcluir, List<Long> tecnologiaIds);
}

