package co.com.bootcamp.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("bootcamp")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class BootcampEntity {

    @Id
    @Column("id")
    private Long id;

    private String nombre;
    private String descripcion;

    @Column("fecha_lanzamiento")
    private LocalDate fechaLanzamiento;

    private Integer duracion;

    @Column("capacidades_ids")
    private String capacidadesIds;

}
