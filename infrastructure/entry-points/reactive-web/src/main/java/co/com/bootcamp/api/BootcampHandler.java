package co.com.bootcamp.api;

import co.com.bootcamp.api.dto.BootcampRequestDto;
import co.com.bootcamp.api.helpers.BootcampMapper;
import co.com.bootcamp.api.helpers.DtoValidator;
import co.com.bootcamp.usecase.bootcamp.bootcamp.BootcampUseCase;
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
    private final DtoValidator dtoValidator;
    private final BootcampMapper bootcampMapper;

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
}
