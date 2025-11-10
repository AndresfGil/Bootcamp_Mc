package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.exception.CapacidadesInvalidasException;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @InjectMocks
    private BootcampUseCase bootcampUseCase;

    private Bootcamp bootcamp;
    private Bootcamp bootcampGuardado;

    @BeforeEach
    void setUp() {
        bootcamp = Bootcamp.builder()
                .nombre("Bootcamp Java Full Stack")
                .descripcion("Bootcamp completo de desarrollo Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .capacidadesIds(Arrays.asList(1L, 2L))
                .build();

        bootcampGuardado = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Java Full Stack")
                .descripcion("Bootcamp completo de desarrollo Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .capacidadesIds(Arrays.asList(1L, 2L))
                .build();
    }

    @Test
    void guardarBootcamp_CuandoDatosValidos_DeberiaGuardarExitosamente() {
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoCapacidadesIdsEsNull_DeberiaLanzarCapacidadesInvalidasException() {
        bootcamp.setCapacidadesIds(null);

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadesInvalidasException
                        && throwable.getMessage().contains("debe tener al menos"))
                .verify();

        verify(bootcampRepository, never()).guardarBootcamp(any());
    }

    @Test
    void guardarBootcamp_CuandoCapacidadesIdsEstaVacia_DeberiaLanzarCapacidadesInvalidasException() {
        bootcamp.setCapacidadesIds(Collections.emptyList());

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadesInvalidasException
                        && throwable.getMessage().contains("debe tener al menos"))
                .verify();

        verify(bootcampRepository, never()).guardarBootcamp(any());
    }

    @Test
    void guardarBootcamp_CuandoMasDeCuatroCapacidades_DeberiaLanzarCapacidadesInvalidasException() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L, 5L));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectErrorMatches(throwable -> throwable instanceof CapacidadesInvalidasException
                        && throwable.getMessage().contains("no puede tener más de"))
                .verify();

        verify(bootcampRepository, never()).guardarBootcamp(any());
    }

    @Test
    void guardarBootcamp_CuandoExactamenteCuatroCapacidades_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L));
        bootcampGuardado.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L));

        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoUnaSolaCapacidad_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Collections.singletonList(1L));
        bootcampGuardado.setCapacidadesIds(Collections.singletonList(1L));

        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoRepositorioRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error de base de datos");
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinId_DeberiaGuardarExitosamente() {
        bootcamp.setId(null);
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinNombre_DeberiaGuardarExitosamente() {
        bootcamp.setNombre(null);
        bootcampGuardado.setNombre(null);
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoTresCapacidades_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L));
        bootcampGuardado.setCapacidadesIds(Arrays.asList(1L, 2L, 3L));

        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinDescripcion_DeberiaGuardarExitosamente() {
        bootcamp.setDescripcion(null);
        bootcampGuardado.setDescripcion(null);
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinFechaLanzamiento_DeberiaGuardarExitosamente() {
        bootcamp.setFechaLanzamiento(null);
        bootcampGuardado.setFechaLanzamiento(null);
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinDuracion_DeberiaGuardarExitosamente() {
        bootcamp.setDuracion(null);
        bootcampGuardado.setDuracion(null);
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoSeisCapacidades_DeberiaLanzarExcepcion() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectError(CapacidadesInvalidasException.class)
                .verify();

        verify(bootcampRepository, never()).guardarBootcamp(any());
    }

    @Test
    void guardarBootcamp_CuandoRepositorioRetornaMonoVacio_DeberiaCompletarSinValor() {
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }
}

