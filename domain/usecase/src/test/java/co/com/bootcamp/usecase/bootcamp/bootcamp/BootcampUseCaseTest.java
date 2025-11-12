package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaInfo;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import co.com.bootcamp.usecase.bootcamp.enrichment.BootcampEnrichmentService;
import co.com.bootcamp.usecase.bootcamp.saga.BootcampDeletionSagaOrchestrator;
import co.com.bootcamp.usecase.bootcamp.validator.CapacidadValidatorService;
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

    @Mock
    private BootcampDeletionSagaOrchestrator sagaOrchestrator;

    @Mock
    private CapacidadGateway capacidadGateway;

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
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(10L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(20L, "Spring");
        CapacidadInfo capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologia1, tecnologia2))
                .build();
        CapacidadInfo capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .nombre("Frontend")
                .tecnologias(Collections.singletonList(tecnologia1))
                .build();

        bootcampGuardado.setTecnologiasIds(Arrays.asList(10L, 20L));

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds()))
                .thenReturn(Flux.just(capacidad1, capacidad2));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(capacidadValidatorService).validarCapacidadesExisten(bootcamp.getCapacidadesIds());
        verify(capacidadGateway).obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds());
        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }



    @Test
    void guardarBootcamp_CuandoExactamenteCuatroCapacidades_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L));
        bootcampGuardado.setCapacidadesIds(Arrays.asList(1L, 2L, 3L, 4L));
        bootcampGuardado.setTecnologiasIds(Arrays.asList(10L, 20L));

        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoUnaSolaCapacidad_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Collections.singletonList(1L));
        bootcampGuardado.setCapacidadesIds(Collections.singletonList(1L));
        bootcampGuardado.setTecnologiasIds(Collections.singletonList(10L));

        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoRepositorioRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error de base de datos");
        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinId_DeberiaGuardarExitosamente() {
        bootcamp.setId(null);
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(10L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(20L, "Spring");
        CapacidadInfo capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia1))
                .build();
        CapacidadInfo capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .tecnologias(Collections.singletonList(tecnologia2))
                .build();

        bootcampGuardado.setTecnologiasIds(Arrays.asList(10L, 20L));

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds()))
                .thenReturn(Flux.just(capacidad1, capacidad2));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds());
        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinNombre_DeberiaGuardarExitosamente() {
        bootcamp.setNombre(null);
        bootcampGuardado.setNombre(null);
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(10L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(20L, "Spring");
        CapacidadInfo capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia1))
                .build();
        CapacidadInfo capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .tecnologias(Collections.singletonList(tecnologia2))
                .build();

        bootcampGuardado.setTecnologiasIds(Arrays.asList(10L, 20L));

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds()))
                .thenReturn(Flux.just(capacidad1, capacidad2));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds());
        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoTresCapacidades_DeberiaGuardarExitosamente() {
        bootcamp.setCapacidadesIds(Arrays.asList(1L, 2L, 3L));
        bootcampGuardado.setCapacidadesIds(Arrays.asList(1L, 2L, 3L));
        bootcampGuardado.setTecnologiasIds(Arrays.asList(10L, 20L));

        TecnologiaInfo tecnologia1 = new TecnologiaInfo(10L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(20L, "Spring");
        CapacidadInfo capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia1))
                .build();
        CapacidadInfo capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .tecnologias(Collections.singletonList(tecnologia2))
                .build();
        CapacidadInfo capacidad3 = CapacidadInfo.builder()
                .id(3L)
                .tecnologias(Collections.singletonList(tecnologia1))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad1, capacidad2, capacidad3));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinDescripcion_DeberiaGuardarExitosamente() {
        bootcamp.setDescripcion(null);
        bootcampGuardado.setDescripcion(null);
        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinFechaLanzamiento_DeberiaGuardarExitosamente() {
        bootcamp.setFechaLanzamiento(null);
        bootcampGuardado.setFechaLanzamiento(null);
        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoBootcampSinDuracion_DeberiaGuardarExitosamente() {
        bootcamp.setDuracion(null);
        bootcampGuardado.setDuracion(null);
        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }



    @Test
    void guardarBootcamp_CuandoRepositorioRetornaMonoVacio_DeberiaCompletarSinValor() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Collections.singletonList(tecnologia))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }
//
//    @Test
//    void guardarBootcamp_CuandoCapacidadesIdsEsNull_DeberiaEstablecerTecnologiasIdsVacia() {
//        bootcamp.setCapacidadesIds(null);
//        bootcampGuardado.setCapacidadesIds(null);
//        bootcampGuardado.setTecnologiasIds(Collections.emptyList());
//
//        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
//        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));
//
//        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
//                .expectNext(bootcampGuardado)
//                .verifyComplete();
//
//        verify(capacidadGateway, never()).obtenerCapacidadesPorIds(any());
//        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
//    }


    @Test
    void guardarBootcamp_CuandoTecnologiasDuplicadas_DeberiaEliminarDuplicados() {
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(10L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(20L, "Spring");
        CapacidadInfo capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .tecnologias(Arrays.asList(tecnologia1, tecnologia2))
                .build();
        CapacidadInfo capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .tecnologias(Arrays.asList(tecnologia1, tecnologia2))
                .build();

        bootcampGuardado.setTecnologiasIds(Arrays.asList(10L, 20L));

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.just(capacidad1, capacidad2));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNext(bootcampGuardado)
                .verifyComplete();

        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
    }

    @Test
    void guardarBootcamp_CuandoCapacidadGatewayRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error al obtener capacidades");

        when(capacidadValidatorService.validarCapacidadesExisten(any())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(any())).thenReturn(Flux.error(error));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectError(RuntimeException.class)
                .verify();

        verify(bootcampRepository, never()).guardarBootcamp(any());
    }

    @Test
    void eliminarBootcamp_CuandoIdValido_DeberiaEliminarExitosamente() {
        String bootcampId = "1";

        when(sagaOrchestrator.deleteBootcampWithSaga(bootcampId)).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.eliminarBootcamp(bootcampId))
                .verifyComplete();

        verify(sagaOrchestrator).deleteBootcampWithSaga(bootcampId);
    }

    @Test
    void eliminarBootcamp_CuandoSagaOrchestratorRetornaError_DeberiaPropagarError() {
        String bootcampId = "1";
        RuntimeException error = new RuntimeException("Error al eliminar bootcamp");

        when(sagaOrchestrator.deleteBootcampWithSaga(bootcampId)).thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.eliminarBootcamp(bootcampId))
                .expectError(RuntimeException.class)
                .verify();

        verify(sagaOrchestrator).deleteBootcampWithSaga(bootcampId);
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

