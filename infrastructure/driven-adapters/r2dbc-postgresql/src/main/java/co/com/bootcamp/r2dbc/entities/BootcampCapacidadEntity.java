package co.com.bootcamp.r2dbc.entities;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_capacidad")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BootcampCapacidadEntity {

    @Column("bootcamp_id")
    private Long bootcampId;

    @Column("capacidad_id")
    private Long capacidadId;
}

