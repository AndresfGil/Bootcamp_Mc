package co.com.bootcamp.api;

import co.com.bootcamp.api.dto.BootcampListRequestDto;
import co.com.bootcamp.api.dto.BootcampRequestDto;
import co.com.bootcamp.api.dto.InscripcionRequestDto;
import co.com.bootcamp.api.dto.InscripcionResponseDto;
import co.com.bootcamp.api.helpers.BootcampMapper;
import co.com.bootcamp.api.helpers.DtoValidator;
import co.com.bootcamp.api.helpers.InscripcionMapper;
import co.com.bootcamp.usecase.bootcamp.bootcamp.BootcampUseCase;
import co.com.bootcamp.usecase.bootcamp.inscripcion.InscripcionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampHandler {
    
    private final BootcampUseCase bootcampUseCase;
    private final InscripcionUseCase inscripcionUseCase;
    private final DtoValidator dtoValidator;
    private final BootcampMapper bootcampMapper;
    private final InscripcionMapper inscripcionMapper;

    public Mono<ServerResponse> listenGuardarBootcamp(ServerRequest req) {
        return req.bodyToMono(BootcampRequestDto.class)
                .flatMap(dto -> dtoValidator.validate(dto)
                        .map(bootcampMapper::toDomain)
                        .flatMap(bootcampUseCase::guardarBootcamp)
                        .map(bootcampMapper::toResponseDto)
                        .flatMap(bootcampResponseDto -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(bootcampResponseDto)));
    }

    public Mono<ServerResponse> listenListarBootcamps(ServerRequest req) {
        return Mono.fromCallable(() -> {
                    String pageParam = req.queryParam("page").orElse("0");
                    String sizeParam = req.queryParam("size").orElse("10");
                    String sortByParam = req.queryParam("sortBy").orElse("nombre");
                    String sortDirectionParam = req.queryParam("sortDirection").orElse("ASC");

                    return new BootcampListRequestDto(
                            Integer.parseInt(pageParam),
                            Integer.parseInt(sizeParam),
                            sortByParam,
                            sortDirectionParam
                    );
                })
                .flatMap(dto -> dtoValidator.validate(dto)
                        .flatMap(dtoValidado -> bootcampUseCase.listarBootcamps(
                                dtoValidado.page(),
                                dtoValidado.size(),
                                dtoValidado.sortBy(),
                                dtoValidado.sortDirection()
                        ))
                        .map(bootcampMapper::toPageResponseDto)
                        .flatMap(response -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response)));
    }

    public Mono<ServerResponse> listenEliminarBootcamp(ServerRequest req) {
        String bootcampId = req.pathVariable("id");
        return bootcampUseCase.eliminarBootcamp(bootcampId)
                .then(ServerResponse
                        .status(HttpStatus.NO_CONTENT)
                        .build());
    }


    public Mono<ServerResponse> listenRegistrarInscripcion(ServerRequest req) {
        return req.bodyToMono(InscripcionRequestDto.class)
                .flatMap(dto -> dtoValidator.validate(dto)
                        .map(inscripcionMapper::toDomain)
                        .flatMap(inscripcionUseCase::registrarInscripcion)
                        .map(inscripcionMapper::toResponseDto)
                        .flatMap(inscripcionResponseDto -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(inscripcionResponseDto)));
    }

}
