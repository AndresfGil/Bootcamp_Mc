package co.com.bootcamp.r2dbc;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.r2dbc.entities.BootcampEntity;
import co.com.bootcamp.r2dbc.helper.BootcampEntityMapper;
import co.com.bootcamp.r2dbc.helper.ReactiveAdapterOperations;
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

    public BootcampRepositoryAdapter(BootcampReactiveRepository repository,
                                     ObjectMapper reactiveMapper,
                                     BootcampEntityMapper entityMapper,
                                     DatabaseClient databaseClient) {

        super(repository, reactiveMapper, entityMapper::toDomain);
        this.bootcampEntityMapper = entityMapper;
        this.databaseClient = databaseClient;
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
        String orderBy = buildOrderByClause(sortBy, sortDirection);
        int offset = page * size;

        Mono<Long> countMono = databaseClient.sql("SELECT COUNT(*) FROM bootcamp")
                .map(row -> row.get(0, Long.class))
                .one();

        Mono<List<BootcampEntity>> dataMono = databaseClient.sql(
                        "SELECT * FROM bootcamp ORDER BY " + orderBy + " LIMIT :limit OFFSET :offset"
                )
                .bind("limit", size)
                .bind("offset", offset)
                .map((row, metadata) -> {
                    BootcampEntity entity = new BootcampEntity();
                    entity.setId(row.get("id", Long.class));
                    entity.setNombre(row.get("nombre", String.class));
                    entity.setDescripcion(row.get("descripcion", String.class));
                    entity.setFechaLanzamiento(row.get("fecha_lanzamiento", java.time.LocalDate.class));
                    entity.setDuracion(row.get("duracion", Integer.class));
                    entity.setCapacidadesIds(row.get("capacidades_ids", String.class));
                    return entity;
                })
                .all()
                .collectList();

        return Mono.zip(countMono, dataMono)
                .map(tuple -> {
                    Long totalRows = tuple.getT1();
                    List<BootcampEntity> entities = tuple.getT2();

                    List<Bootcamp> bootcamps = entities.stream()
                            .map(bootcampEntityMapper::toDomain)
                            .toList();

                    boolean hasNext = (offset + size) < totalRows;
                    String sort = sortBy + " " + sortDirection;

                    return CustomPage.<Bootcamp>builder()
                            .data(bootcamps)
                            .totalRows(totalRows)
                            .pageSize(size)
                            .pageNum(page)
                            .hasNext(hasNext)
                            .sort(sort)
                            .build();
                });
    }

    private String buildOrderByClause(String sortBy, String sortDirection) {
        String direction = "ASC".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";

        return switch (sortBy.toLowerCase()) {
            case "fechalanzamiento", "fecha_lanzamiento" ->
                "fecha_lanzamiento " + direction;
            case "duracion" ->
                "duracion " + direction;
            case "cantidadcapacidades", "cantidad_capacidades" ->
                "JSON_LENGTH(capacidades_ids) " + direction;
            case "nombre" ->
                "nombre " + direction;
            default ->
                "nombre " + direction;
        };
    }
}
