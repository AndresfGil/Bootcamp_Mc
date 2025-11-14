package co.com.bootcamp.r2dbc.entities;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_tecnologia")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BootcampTecnologiaEntity {

    @Column("bootcamp_id")
    private Long bootcampId;

    @Column("tecnologia_id")
    private Long tecnologiaId;
}

