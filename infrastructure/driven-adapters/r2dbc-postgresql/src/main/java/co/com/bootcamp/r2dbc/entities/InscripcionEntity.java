package co.com.bootcamp.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("inscripcion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class InscripcionEntity {

    @Id
    private Long id;

    @Column("persona_id")
    private Long personaId;

    @Column("bootcamp_id")
    private Long bootcampId;

    @Column("fecha_inscripcion")
    private LocalDateTime fechaInscripcion;
}

