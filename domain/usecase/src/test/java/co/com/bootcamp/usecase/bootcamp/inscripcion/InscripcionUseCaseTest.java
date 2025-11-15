package co.com.bootcamp.usecase.bootcamp.inscripcion;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.exception.BootcampNoEncontrado;
import co.com.bootcamp.model.bootcamp.exception.InscripcionInvalidaException;
import co.com.bootcamp.model.bootcamp.exception.UsuarioNoEncontradoException;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.InscripcionRepository;
import co.com.bootcamp.model.bootcamp.gateways.PersonaGateway;
import co.com.bootcamp.model.bootcamp.inscripcion.Inscripcion;
import co.com.bootcamp.model.bootcamp.inscripcion.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InscripcionUseCaseTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private PersonaGateway personaGateway;

    @Mock
    private BootcampRepository bootcampRepository;

    @InjectMocks
    private InscripcionUseCase inscripcionUseCase;

    private Inscripcion inscripcion;
    private Persona persona;
    private Bootcamp bootcamp;

    @BeforeEach
    void setUp() {
        inscripcion = Inscripcion.builder()
                .id(1L)
                .personaId(100L)
                .bootcampId(200L)
                .fechaInscripcion(LocalDateTime.now())
                .build();

        persona = Persona.builder()
                .id(100L)
                .nombre("Juan Pérez")
                .correo("juan.perez@example.com")
                .build();

        bootcamp = Bootcamp.builder()
                .id(200L)
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .build();
    }

    @Test
    void registrarInscripcion_CuandoDatosValidos_DeberiaRegistrarExitosamente() {
        when(personaGateway.obtenerPersonaPorId(100L)).thenReturn(Mono.just(persona));
        when(bootcampRepository.obtenerBootcampPorId(200L)).thenReturn(Mono.just(bootcamp));
        when(inscripcionRepository.contarInscripcionesActivasPorPersona(100L)).thenReturn(Mono.just(2L));
        when(inscripcionRepository.obtenerBootcampIdsDeInscripcionesActivas(100L))
                .thenReturn(Flux.empty());
        when(inscripcionRepository.registrarInscripcion(inscripcion)).thenReturn(Mono.just(inscripcion));

        StepVerifier.create(inscripcionUseCase.registrarInscripcion(inscripcion))
                .expectNext(inscripcion)
                .verifyComplete();

        verify(personaGateway).obtenerPersonaPorId(100L);
        verify(bootcampRepository).obtenerBootcampPorId(200L);
        verify(inscripcionRepository).contarInscripcionesActivasPorPersona(100L);
        verify(inscripcionRepository).obtenerBootcampIdsDeInscripcionesActivas(100L);
        verify(inscripcionRepository).registrarInscripcion(inscripcion);
    }
    
    @Test
    void registrarInscripcion_CuandoBootcampNoExiste_DeberiaLanzarBootcampNoEncontrado() {
        when(personaGateway.obtenerPersonaPorId(100L)).thenReturn(Mono.just(persona));
        when(bootcampRepository.obtenerBootcampPorId(200L)).thenReturn(Mono.empty());

        StepVerifier.create(inscripcionUseCase.registrarInscripcion(inscripcion))
                .expectError(BootcampNoEncontrado.class)
                .verify();

        verify(personaGateway).obtenerPersonaPorId(100L);
        verify(bootcampRepository).obtenerBootcampPorId(200L);
        verify(inscripcionRepository, never()).registrarInscripcion(any());
    }

    @Test
    void registrarInscripcion_CuandoNoHayInscripcionesActivas_DeberiaRegistrarExitosamente() {
        when(personaGateway.obtenerPersonaPorId(100L)).thenReturn(Mono.just(persona));
        when(bootcampRepository.obtenerBootcampPorId(200L)).thenReturn(Mono.just(bootcamp));
        when(inscripcionRepository.contarInscripcionesActivasPorPersona(100L)).thenReturn(Mono.just(0L));
        when(inscripcionRepository.obtenerBootcampIdsDeInscripcionesActivas(100L))
                .thenReturn(Flux.empty());
        when(inscripcionRepository.registrarInscripcion(inscripcion)).thenReturn(Mono.just(inscripcion));

        StepVerifier.create(inscripcionUseCase.registrarInscripcion(inscripcion))
                .expectNext(inscripcion)
                .verifyComplete();

        verify(personaGateway).obtenerPersonaPorId(100L);
        verify(bootcampRepository).obtenerBootcampPorId(200L);
        verify(inscripcionRepository).contarInscripcionesActivasPorPersona(100L);
        verify(inscripcionRepository).obtenerBootcampIdsDeInscripcionesActivas(100L);
        verify(inscripcionRepository).registrarInscripcion(inscripcion);
    }

    @Test
    void registrarInscripcion_CuandoNoHaySolapamientoDeFechas_DeberiaRegistrarExitosamente() {
        LocalDate fechaInicioActivo = LocalDate.now().minusDays(20);
        Bootcamp bootcampActivo = Bootcamp.builder()
                .id(300L)
                .nombre("Bootcamp Activo")
                .fechaLanzamiento(fechaInicioActivo)
                .duracion(4)
                .build();

        when(personaGateway.obtenerPersonaPorId(100L)).thenReturn(Mono.just(persona));
        when(bootcampRepository.obtenerBootcampPorId(200L)).thenReturn(Mono.just(bootcamp));
        when(inscripcionRepository.contarInscripcionesActivasPorPersona(100L)).thenReturn(Mono.just(1L));
        when(inscripcionRepository.obtenerBootcampIdsDeInscripcionesActivas(100L))
                .thenReturn(Flux.just(300L));
        when(bootcampRepository.obtenerBootcampPorId(300L)).thenReturn(Mono.just(bootcampActivo));
        when(inscripcionRepository.registrarInscripcion(inscripcion)).thenReturn(Mono.just(inscripcion));

        StepVerifier.create(inscripcionUseCase.registrarInscripcion(inscripcion))
                .expectNext(inscripcion)
                .verifyComplete();

        verify(personaGateway).obtenerPersonaPorId(100L);
        verify(bootcampRepository).obtenerBootcampPorId(200L);
        verify(inscripcionRepository).contarInscripcionesActivasPorPersona(100L);
        verify(inscripcionRepository).obtenerBootcampIdsDeInscripcionesActivas(100L);
        verify(bootcampRepository).obtenerBootcampPorId(300L);
        verify(inscripcionRepository).registrarInscripcion(inscripcion);
    }
}

