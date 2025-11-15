package co.com.bootcamp.r2dbc.helper;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.r2dbc.entities.BootcampEntity;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootcampQueryHelper {

    private final BootcampEntityMapper bootcampEntityMapper;

    public BootcampQueryHelper(BootcampEntityMapper bootcampEntityMapper) {
        this.bootcampEntityMapper = bootcampEntityMapper;
    }

    public BootcampEntity mapRowToEntity(Row row, RowMetadata metadata) {
        BootcampEntity entity = new BootcampEntity();
        entity.setId(row.get("id", Long.class));
        entity.setNombre(row.get("nombre", String.class));
        entity.setDescripcion(row.get("descripcion", String.class));
        entity.setFechaLanzamiento(row.get("fecha_lanzamiento", java.time.LocalDate.class));
        entity.setDuracion(row.get("duracion", Integer.class));
        entity.setCapacidadesIds(row.get("capacidades_ids", String.class));
        entity.setTecnologiasIds(row.get("tecnologias_ids", String.class));
        return entity;
    }

    public CustomPage<Bootcamp> buildCustomPage(Long totalRows, List<BootcampEntity> entities,
                                                 Integer page, Integer size, String sortBy,
                                                 String sortDirection, int offset) {
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
    }

    public String buildOrderByClause(String sortBy, String sortDirection) {
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


