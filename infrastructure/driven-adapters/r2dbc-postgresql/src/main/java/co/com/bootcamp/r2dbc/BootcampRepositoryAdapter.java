package co.com.bootcamp.r2dbc;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.r2dbc.entities.BootcampEntity;
import co.com.bootcamp.r2dbc.helper.BootcampEntityMapper;
import co.com.bootcamp.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BootcampRepositoryAdapter extends ReactiveAdapterOperations<
        Bootcamp,
        BootcampEntity,
        Long,
        BootcampReactiveRepository
> implements BootcampRepository {

    private final BootcampEntityMapper bootcampEntityMapper;

    public BootcampRepositoryAdapter(BootcampReactiveRepository repository,
                                     ObjectMapper reactiveMapper,
                                     BootcampEntityMapper entityMapper,
                                     DatabaseClient databaseClient) {

        super(repository, reactiveMapper, entityMapper::toDomain);
        this.bootcampEntityMapper = entityMapper;
    }

    @Override
    protected BootcampEntity toData(Bootcamp entity) {
        return bootcampEntityMapper.toEntity(entity);
    }

    @Override
    public Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp) {
        return super.save(bootcamp);
    }
}
