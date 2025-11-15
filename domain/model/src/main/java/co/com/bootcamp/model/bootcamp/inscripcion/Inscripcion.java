package co.com.bootcamp.model.bootcamp.inscripcion;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Inscripcion {

    private Long id;
    private Long personaId;
    private Long bootcampId;
    private LocalDateTime fechaInscripcion;

}
