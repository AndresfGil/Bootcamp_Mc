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

import java.util.Collections;
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
                    entity.setTecnologiasIds(row.get("tecnologias_ids", String.class));
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

    @Override
    public Mono<Bootcamp> obtenerBootcampPorId(Long id) {
        return repository.findById(id)
                .map(bootcampEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> eliminarBootcamp(Long id) {
        return repository.deleteById(id)
                .then();
    }

    @Override
    public Mono<List<Long>> obtenerCapacidadesReferenciadasPorOtrosBootcamps(List<Long> capacidadIds, Long bootcampIdExcluir) {
        if (capacidadIds == null || capacidadIds.isEmpty()) {
            return Mono.just(java.util.Collections.emptyList());
        }

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT capacidades_ids FROM bootcamp WHERE id != :bootcampIdExcluir AND ("
        );

        for (int i = 0; i < capacidadIds.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(" OR ");
            }
            sqlBuilder.append("JSON_CONTAINS(capacidades_ids, :capacidadId").append(i).append(")");
        }
        sqlBuilder.append(")");

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sqlBuilder.toString())
                .bind("bootcampIdExcluir", bootcampIdExcluir);

        for (int i = 0; i < capacidadIds.size(); i++) {
            spec = spec.bind("capacidadId" + i, "[" + capacidadIds.get(i) + "]");
        }

        return spec.map((row, metadata) -> {
                    String capacidadesJson = row.get("capacidades_ids", String.class);
                    return parseJsonToList(capacidadesJson);
                })
                .all()
                .collectList()
                .map(listaCapacidades -> listaCapacidades.stream()
                        .flatMap(List::stream)
                        .distinct()
                        .filter(capacidadIds::contains)
                        .toList());
    }

    @Override
    public Mono<List<Long>> obtenerTecnologiasReferenciadasPorOtrosBootcamps(List<Long> tecnologiaIds, Long bootcampIdExcluir) {
        if (tecnologiaIds == null || tecnologiaIds.isEmpty()) {
            return Mono.just(java.util.Collections.emptyList());
        }

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT tecnologias_ids FROM bootcamp WHERE id != :bootcampIdExcluir AND tecnologias_ids IS NOT NULL AND ("
        );

        for (int i = 0; i < tecnologiaIds.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(" OR ");
            }
            sqlBuilder.append("JSON_CONTAINS(tecnologias_ids, :tecnologiaId").append(i).append(")");
        }
        sqlBuilder.append(")");

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sqlBuilder.toString())
                .bind("bootcampIdExcluir", bootcampIdExcluir);

        for (int i = 0; i < tecnologiaIds.size(); i++) {
            spec = spec.bind("tecnologiaId" + i, "[" + tecnologiaIds.get(i) + "]");
        }

        return spec.map((row, metadata) -> {
                    String tecnologiasJson = row.get("tecnologias_ids", String.class);
                    return parseJsonToList(tecnologiasJson);
                })
                .all()
                .collectList()
                .map(listaTecnologias -> listaTecnologias.stream()
                        .flatMap(List::stream)
                        .distinct()
                        .filter(tecnologiaIds::contains)
                        .toList());
    }

    private List<Long> parseJsonToList(String json) {
        if (json == null || json.isEmpty() || json.equals("[]")) {
            return Collections.emptyList();
        }
        try {
            return bootcampEntityMapper.getJsonMapper().readValue(
                    json,
                    new com.fasterxml.jackson.core.type.TypeReference<List<Long>>() {}
            );
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
