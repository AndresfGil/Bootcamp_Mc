package co.com.bootcamp.r2dbc;

import co.com.bootcamp.r2dbc.entities.InscripcionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InscripcionReactiveRepository extends ReactiveCrudRepository<InscripcionEntity, Long>, ReactiveQueryByExampleExecutor<InscripcionEntity> {
    
    @Query("SELECT COUNT(*) FROM inscripcion WHERE persona_id = :personaId")
    Mono<Long> countByPersonaId(Long personaId);
    
    @Query("SELECT bootcamp_id FROM inscripcion WHERE persona_id = :personaId")
    Flux<Long> findBootcampIdsByPersonaId(Long personaId);
}

