package co.com.bootcamp.model.bootcamp;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Bootcamp {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaLanzamiento;
    private Integer duracion;
    private List<Long> capacidadesIds;

}
