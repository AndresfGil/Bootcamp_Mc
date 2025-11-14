package co.com.bootcamp.r2dbc;

import co.com.bootcamp.r2dbc.entities.BootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BootcampReactiveRepository extends ReactiveCrudRepository<BootcampEntity, Long>, ReactiveQueryByExampleExecutor<BootcampEntity> {

    @Query("SELECT COUNT(*) FROM bootcamp")
    Mono<Long> countAll();
}
