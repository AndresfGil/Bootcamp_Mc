package co.com.bootcamp.usecase.bootcamp.validator;

import co.com.bootcamp.model.bootcamp.exception.CapacidadNoEncontradaException;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacidadValidatorServiceTest {

    @Mock
    private CapacidadGateway capacidadGateway;

    @InjectMocks
    private CapacidadValidatorService capacidadValidatorService;

    private CapacidadInfo capacidad1;
    private CapacidadInfo capacidad2;
    private TecnologiaInfo tecnologia1;

    @BeforeEach
    void setUp() {
        tecnologia1 = new TecnologiaInfo(1L, "Java");

        capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologia1))
                .build();

        capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .nombre("Frontend")
                .tecnologias(Collections.emptyList())
                .build();
    }

    @Test
    void validarCapacidadesExisten_CuandoListaNull_DeberiaRetornarMonoVacio() {
        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(null))
                .verifyComplete();

        verify(capacidadGateway, never()).obtenerCapacidadesPorIds(any());
    }

    @Test
    void validarCapacidadesExisten_CuandoListaVacia_DeberiaRetornarMonoVacio() {
        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(Collections.emptyList()))
                .verifyComplete();

        verify(capacidadGateway, never()).obtenerCapacidadesPorIds(any());
    }

    @Test
    void validarCapacidadesExisten_CuandoTodasLasCapacidadesExisten_DeberiaRetornarMonoVacio() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.just(capacidad1, capacidad2));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoUnaCapacidadNoExiste_DeberiaLanzarExcepcion() {
        List<Long> capacidadesIds = Arrays.asList(1L, 999L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.just(capacidad1));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadNoEncontradaException
                        && throwable.getMessage().contains("no fueron encontradas"))
                .verify();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoMultiplesCapacidadesNoExisten_DeberiaLanzarExcepcionConTodosLosIds() {
        List<Long> capacidadesIds = Arrays.asList(1L, 999L, 888L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.just(capacidad1));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .expectErrorMatches(throwable -> {
                    if (throwable instanceof CapacidadNoEncontradaException) {
                        CapacidadNoEncontradaException ex = (CapacidadNoEncontradaException) throwable;
                        return ex.getErrors().get(0).contains("999") && ex.getErrors().get(0).contains("888");
                    }
                    return false;
                })
                .verify();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoNingunaCapacidadExiste_DeberiaLanzarExcepcion() {
        List<Long> capacidadesIds = Arrays.asList(999L, 888L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.empty());

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .expectError(CapacidadNoEncontradaException.class)
                .verify();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoUnaSolaCapacidadExiste_DeberiaRetornarMonoVacio() {
        List<Long> capacidadesIds = Collections.singletonList(1L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.just(capacidad1));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoCapacidadesDuplicadas_DeberiaValidarCorrectamente() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 1L, 2L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.just(capacidad1, capacidad2));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoGatewayRetornaError_DeberiaPropagarError() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L);

        RuntimeException error = new RuntimeException("Error al obtener capacidades");
        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.error(error));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .expectError(RuntimeException.class)
                .verify();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }

    @Test
    void validarCapacidadesExisten_CuandoCuatroCapacidadesExisten_DeberiaRetornarMonoVacio() {
        CapacidadInfo capacidad3 = CapacidadInfo.builder()
                .id(3L)
                .nombre("DevOps")
                .build();
        CapacidadInfo capacidad4 = CapacidadInfo.builder()
                .id(4L)
                .nombre("QA")
                .build();

        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L, 4L);

        when(capacidadGateway.obtenerCapacidadesPorIds(capacidadesIds))
                .thenReturn(Flux.just(capacidad1, capacidad2, capacidad3, capacidad4));

        StepVerifier.create(capacidadValidatorService.validarCapacidadesExisten(capacidadesIds))
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(capacidadesIds);
    }
}

