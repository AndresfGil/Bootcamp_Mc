package co.com.bootcamp.consumer.dto;

import co.com.bootcamp.model.bootcamp.gateways.TecnologiaInfo;

import java.util.List;

public record CapacidadBatchResponseDto(
        Long id,
        String nombre,
        String descripcion,
        List<TecnologiaInfo> tecnologias
) {
}
