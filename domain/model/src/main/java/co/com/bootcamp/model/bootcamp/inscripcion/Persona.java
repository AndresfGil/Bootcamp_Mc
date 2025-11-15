package co.com.bootcamp.model.bootcamp.inscripcion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Persona {

    private Long id;
    private String nombre;
    private String correo;

}
