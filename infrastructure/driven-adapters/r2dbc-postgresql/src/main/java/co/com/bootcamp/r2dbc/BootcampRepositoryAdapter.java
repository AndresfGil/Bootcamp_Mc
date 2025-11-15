package co.com.bootcamp.r2dbc;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.r2dbc.entities.BootcampEntity;
import co.com.bootcamp.r2dbc.entities.BootcampCapacidadEntity;
import co.com.bootcamp.r2dbc.entities.BootcampTecnologiaEntity;
import co.com.bootcamp.r2dbc.helper.BootcampEntityMapper;
import co.com.bootcamp.r2dbc.helper.BootcampQueryHelper;
import co.com.bootcamp.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class BootcampRepositoryAdapter extends ReactiveAdapterOperations<
        Bootcamp,
        BootcampEntity,
        Long,
        BootcampReactiveRepository
> implements BootcampRepository {

    private final BootcampEntityMapper bootcampEntityMapper;
    private final DatabaseClient databaseClient;
    private final BootcampCapacidadReactiveRepository bootcampCapacidadRepository;
    private final BootcampTecnologiaReactiveRepository bootcampTecnologiaRepository;
    private final BootcampQueryHelper queryHelper;

    public BootcampRepositoryAdapter(BootcampReactiveRepository repository,
                                     ObjectMapper reactiveMapper,
                                     BootcampEntityMapper entityMapper,
                                     DatabaseClient databaseClient,
                                     BootcampCapacidadReactiveRepository bootcampCapacidadRepository,
                                     BootcampTecnologiaReactiveRepository bootcampTecnologiaRepository,
                                     BootcampQueryHelper queryHelper) {

        super(repository, reactiveMapper, entityMapper::toDomain);
        this.bootcampEntityMapper = entityMapper;
        this.databaseClient = databaseClient;
        this.bootcampCapacidadRepository = bootcampCapacidadRepository;
        this.bootcampTecnologiaRepository = bootcampTecnologiaRepository;
        this.queryHelper = queryHelper;
    }

    @Override
    protected BootcampEntity toData(Bootcamp entity) {
        return bootcampEntityMapper.toEntity(entity);
    }

    @Override
    public Mono<Bootcamp> guardarBootcamp(Bootcamp bootcamp) {
        return super.save(bootcamp);
    }

    @Override
    public Mono<CustomPage<Bootcamp>> listarBootcamps(Integer page, Integer size, String sortBy, String sortDirection) {
        String orderBy = queryHelper.buildOrderByClause(sortBy, sortDirection);
        int offset = page * size;

        Mono<Long> countMono = repository.countAll();
        
        Mono<List<BootcampEntity>> dataMono = databaseClient.sql(
                        "SELECT * FROM bootcamp ORDER BY " + orderBy + " LIMIT :limit OFFSET :offset"
                )
                .bind("limit", size)
                .bind("offset", offset)
                .map(queryHelper::mapRowToEntity)
                .all()
                .collectList();

        return Mono.zip(countMono, dataMono)
                .map(tuple -> queryHelper.buildCustomPage(tuple.getT1(), tuple.getT2(), page, size, sortBy, sortDirection, offset));
    }

    @Override
    public Mono<Bootcamp> obtenerBootcampPorId(Long id) {
        return repository.findById(id)
                .map(bootcampEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> eliminarBootcamp(Long id) {
        return bootcampCapacidadRepository.deleteByBootcampId(id)
                .then(bootcampTecnologiaRepository.deleteByBootcampId(id))
                .then(repository.deleteById(id))
                .then();
    }

    @Override
    public Mono<List<Long>> obtenerCapacidadesReferenciadasPorOtrosBootcamps(List<Long> capacidadIds, Long bootcampIdExcluir) {
        if (capacidadIds == null || capacidadIds.isEmpty()) {
            return Mono.just(List.of());
        }

        return bootcampCapacidadRepository
                .findCapacidadIdsReferenciadasPorOtrosBootcamps(bootcampIdExcluir, capacidadIds)
                .collectList();
    }

    @Override
    public Mono<List<Long>> obtenerTecnologiasReferenciadasPorOtrosBootcamps(List<Long> tecnologiaIds, Long bootcampIdExcluir) {
        if (tecnologiaIds == null || tecnologiaIds.isEmpty()) {
            return Mono.just(List.of());
        }

        return bootcampTecnologiaRepository
                .findTecnologiaIdsReferenciadasPorOtrosBootcamps(bootcampIdExcluir, tecnologiaIds)
                .collectList();
    }

    @Override
    public Mono<Void> guardarRelacionesCapacidades(Long bootcampId, List<Long> capacidadIds) {
        if (capacidadIds == null || capacidadIds.isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(capacidadIds)
                .map(capacidadId -> BootcampCapacidadEntity.builder()
                        .bootcampId(bootcampId)
                        .capacidadId(capacidadId)
                        .build())
                .flatMap(bootcampCapacidadRepository::save)
                .then();
    }

    @Override
    public Mono<Void> guardarRelacionesTecnologias(Long bootcampId, List<Long> tecnologiaIds) {
        if (tecnologiaIds == null || tecnologiaIds.isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(tecnologiaIds)
                .map(tecnologiaId -> BootcampTecnologiaEntity.builder()
                        .bootcampId(bootcampId)
                        .tecnologiaId(tecnologiaId)
                        .build())
                .flatMap(bootcampTecnologiaRepository::save)
                .then();
    }
}
