package co.com.bootcamp.usecase.bootcamp.bootcamp;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.exception.CapacidadesInvalidasException;
import co.com.bootcamp.model.bootcamp.exception.CapacidadNoEncontradaException;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private CapacidadInfo capacidadInfo1;
    private CapacidadInfo capacidadInfo2;
    private TecnologiaInfo tecnologiaInfo1;
    private TecnologiaInfo tecnologiaInfo2;

    @BeforeEach
    void setUp() {
        bootcamp = Bootcamp.builder()
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .capacidadesIds(Arrays.asList(1L, 2L))
                .build();

        bootcampGuardado = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .capacidadesIds(Arrays.asList(1L, 2L))
                .tecnologiasIds(Arrays.asList(10L, 20L))
                .build();

        tecnologiaInfo1 = new TecnologiaInfo(10L, "Java");
        tecnologiaInfo2 = new TecnologiaInfo(20L, "Spring");

        capacidadInfo1 = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologiaInfo1, tecnologiaInfo2))
                .build();

        capacidadInfo2 = CapacidadInfo.builder()
                .id(2L)
                .nombre("Frontend")
                .tecnologias(Collections.singletonList(tecnologiaInfo1))
                .build();
    }

    @Test
    void guardarBootcamp_CuandoDatosValidos_DeberiaGuardarExitosamente() {
        when(capacidadValidatorService.validarCapacidadesExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds()))
                .thenReturn(Flux.just(capacidadInfo1, capacidadInfo2));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampGuardado));
        when(bootcampRepository.guardarRelacionesCapacidades(anyLong(), anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarRelacionesTecnologias(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNextMatches(b -> b.getId().equals(1L) && b.getTecnologiasIds().size() == 2)
                .verifyComplete();

        verify(capacidadValidatorService).validarCapacidadesExisten(bootcamp.getCapacidadesIds());
        verify(capacidadGateway).obtenerCapacidadesPorIds(bootcamp.getCapacidadesIds());
        verify(bootcampRepository).guardarBootcamp(any(Bootcamp.class));
        verify(bootcampRepository).guardarRelacionesCapacidades(1L, bootcamp.getCapacidadesIds());
        verify(bootcampRepository).guardarRelacionesTecnologias(1L, Arrays.asList(10L, 20L));
    }



    @Test
    void guardarBootcamp_CuandoCapacidadesNoExisten_DeberiaLanzarExcepcion() {
        Bootcamp bootcampTest = Bootcamp.builder()
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .capacidadesIds(Arrays.asList(99L))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(anyList()))
                .thenReturn(Mono.error(new CapacidadNoEncontradaException(Arrays.asList(99L))));
        when(capacidadGateway.obtenerCapacidadesPorIds(anyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcampTest))
                .expectError(CapacidadNoEncontradaException.class)
                .verify();

        verify(capacidadValidatorService).validarCapacidadesExisten(bootcampTest.getCapacidadesIds());
        verify(bootcampRepository, never()).guardarBootcamp(any(Bootcamp.class));
    }

    
    @Test
    void guardarBootcamp_CuandoTecnologiasDuplicadas_DeberiaEliminarDuplicados() {
        TecnologiaInfo tecnologiaDuplicada = new TecnologiaInfo(10L, "Java");
        CapacidadInfo capacidadConDuplicados = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologiaInfo1, tecnologiaDuplicada))
                .build();

        Bootcamp bootcampConTecnologias = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(12)
                .capacidadesIds(Collections.singletonList(1L))
                .tecnologiasIds(Collections.singletonList(10L))
                .build();

        when(capacidadValidatorService.validarCapacidadesExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadGateway.obtenerCapacidadesPorIds(Collections.singletonList(1L)))
                .thenReturn(Flux.just(capacidadConDuplicados));
        when(bootcampRepository.guardarBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcampConTecnologias));
        when(bootcampRepository.guardarRelacionesCapacidades(anyLong(), anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.guardarRelacionesTecnologias(anyLong(), anyList())).thenReturn(Mono.empty());

        bootcamp.setCapacidadesIds(Collections.singletonList(1L));

        StepVerifier.create(bootcampUseCase.guardarBootcamp(bootcamp))
                .expectNextMatches(b -> b.getId().equals(1L) && b.getTecnologiasIds().size() == 1)
                .verifyComplete();

        verify(capacidadValidatorService).validarCapacidadesExisten(Collections.singletonList(1L));
        verify(capacidadGateway).obtenerCapacidadesPorIds(Collections.singletonList(1L));
    }

    @Test
    void listarBootcamps_CuandoDatosValidos_DeberiaRetornarLista() {
        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Collections.emptyList())
                .totalRows(0L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("id")
                .build();

        CustomPage<BootcampConCapacidades> pageEnriquecida = CustomPage.<BootcampConCapacidades>builder()
                .data(Collections.emptyList())
                .totalRows(0L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("id")
                .build();

        when(bootcampRepository.listarBootcamps(0, 10, "id", "asc"))
                .thenReturn(Mono.just(pageBootcamps));
        when(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(any(CustomPage.class)))
                .thenReturn(Mono.just(pageEnriquecida));

        StepVerifier.create(bootcampUseCase.listarBootcamps(0, 10, "id", "asc"))
                .expectNext(pageEnriquecida)
                .verifyComplete();

        verify(bootcampRepository).listarBootcamps(0, 10, "id", "asc");
        verify(bootcampEnrichmentService).enriquecerBootcampsConCapacidades(pageBootcamps);
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
    void eliminarBootcamp_CuandoIdInvalido_DeberiaPropagarError() {
        String bootcampId = "999";
        RuntimeException error = new RuntimeException("Error al eliminar bootcamp");

        when(sagaOrchestrator.deleteBootcampWithSaga(bootcampId)).thenReturn(Mono.error(error));

        StepVerifier.create(bootcampUseCase.eliminarBootcamp(bootcampId))
                .expectError(RuntimeException.class)
                .verify();

        verify(sagaOrchestrator).deleteBootcampWithSaga(bootcampId);
    }
}

