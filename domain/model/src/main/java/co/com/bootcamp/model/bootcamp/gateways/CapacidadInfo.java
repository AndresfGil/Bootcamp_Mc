package co.com.bootcamp.model.bootcamp.gateways;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacidadInfo {
    private Long id;
    private String nombre;
    private List<TecnologiaInfo> tecnologias;

}

