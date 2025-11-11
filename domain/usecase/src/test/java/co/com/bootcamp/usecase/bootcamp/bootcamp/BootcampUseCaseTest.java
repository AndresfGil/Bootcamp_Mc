package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.exception.CapacidadesInvalidasException;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.usecase.bootcamp.enrichment.BootcampEnrichmentService;
import co.com.bootcamp.usecase.bootcamp.validator.CapacidadValidatorService;
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

    @Mock
    private CapacidadValidatorService capacidadValidatorService;

    @Mock
    private BootcampEnrichmentService bootcampEnrichmentService;

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
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(capacidadValidatorService).validarCapacidadesExisten(bootcamp.getCapacidadesIds());
        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }



    @Test
    void guardarBootcamp_CuandoExactamenteCuatroCapacidades_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L));
        bootcampGuardado.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L));

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
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

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoRepositorioRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error de base de datos");
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinId_DeberiaGuardarExitosamente() {
        bootcamp.setId(null);
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
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
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
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

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
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
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
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
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
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
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }



    @Test
    void guardarBootcamp_CuandoRepositorioRetornaMonoVacio_DeberiaCompletarSinValor() {
        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(bootcamp);
    }

    @Test
    void listarBootcamps_CuandoDatosValidos_DeberiaRetornarPaginaEnriquecida() {
        Bootcamp bootcamp1 = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp 1")
                .capacidadesIds(Arrays.asList(1L, 2L))
                .build();
        Bootcamp bootcamp2 = Bootcamp.builder()
                .id(2L)
                .nombre("Bootcamp 2")
                .capacidadesIds(Arrays.asList(2L, 3L))
                .build();

        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Arrays.asList(bootcamp1, bootcamp2))
                .totalRows(2L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        BootcampConCapacidades bootcampEnriquecido1 = BootcampConCapacidades.builder()
                .id(1L)
                .nombre("Bootcamp 1")
                .build();
        BootcampConCapacidades bootcampEnriquecido2 = BootcampConCapacidades.builder()
                .id(2L)
                .nombre("Bootcamp 2")
                .build();

        CustomPage<BootcampConCapacidades> pageEnriquecida = CustomPage.<BootcampConCapacidades>builder()
                .data(Arrays.asList(bootcampEnriquecido1, bootcampEnriquecido2))
                .totalRows(2L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        when(bootcampRepository.listarBootcamps(0, 10, "nombre", "ASC"))
                .thenReturn(Mono.just(pageBootcamps));
        when(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .thenReturn(Mono.just(pageEnriquecida));

        StepVerifier.create(bootcampUseCase.listarBootcamps(0, 10, "nombre", "ASC"))
                .expectNext(pageEnriquecida)
                .verifyComplete();

        verify(bootcampRepository).listarBootcamps(0, 10, "nombre", "ASC");
        verify(bootcampEnrichmentService).enriquecerBootcampsConCapacidades(pageBootcamps);
    }

    @Test
    void listarBootcamps_CuandoRepositorioRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error de base de datos");
        when(bootcampRepository.listarBootcamps(0, 10, "nombre", "ASC"))
                .thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.listarBootcamps(0, 10, "nombre", "ASC"))
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampRepository).listarBootcamps(0, 10, "nombre", "ASC");
        verify(bootcampEnrichmentService, never()).enriquecerBootcampsConCapacidades(any());
    }

    @Test
    void listarBootcamps_CuandoEnrichmentServiceRetornaError_DeberiaPropagarError() {
        Bootcamp bootcamp = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp 1")
                .build();

        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Arrays.asList(bootcamp))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        RuntimeException error = new RuntimeException("Error al enriquecer");
        when(bootcampRepository.listarBootcamps(0, 10, "nombre", "ASC"))
                .thenReturn(Mono.just(pageBootcamps));
        when(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.listarBootcamps(0, 10, "nombre", "ASC"))
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampRepository).listarBootcamps(0, 10, "nombre", "ASC");
        verify(bootcampEnrichmentService).enriquecerBootcampsConCapacidades(pageBootcamps);
    }
}

