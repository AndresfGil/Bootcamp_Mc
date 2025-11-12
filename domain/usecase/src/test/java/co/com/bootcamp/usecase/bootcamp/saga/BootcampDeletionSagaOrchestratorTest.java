package co.com.bootcamp.usecase.bootcamp.saga;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.exception.BootcampEliminacionFallidaException;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampDeletionSagaOrchestratorTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private CapacidadGateway capacidadGateway;

    @Mock
    private TecnologiaGateway tecnologiaGateway;

    @InjectMocks
    private BootcampDeletionSagaOrchestrator sagaOrchestrator;

    private Bootcamp bootcamp;

    @BeforeEach
    void setUp() {
        bootcamp = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Java")
                .capacidadesIds(Arrays.asList(1L, 2L))
                .tecnologiasIds(Arrays.asList(10L, 20L))
                .build();
    }

    @Test
    void deleteBootcampWithSaga_CuandoBootcampNoExiste_DeberiaLanzarExcepcion() {
        String bootcampId = "1";

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().contains("Bootcamp no encontrado"))
                .verify();

        verify(bootcampRepository).obtenerBootcampPorId(1L);
        verify(bootcampRepository, never()).eliminarBootcamp(anyLong());
    }

    @Test
    void deleteBootcampWithSaga_CuandoBootcampSinCapacidadesNiTecnologias_DeberiaEliminarDirectamente() {
        String bootcampId = "1";
        Bootcamp bootcampSinRelaciones = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Simple")
                .capacidadesIds(null)
                .tecnologiasIds(null)
                .build();

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcampSinRelaciones));
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .verifyComplete();

        verify(bootcampRepository).obtenerBootcampPorId(1L);
        verify(bootcampRepository).eliminarBootcamp(1L);
        verify(capacidadGateway, never()).desactivarCapacidades(anyList());
        verify(tecnologiaGateway, never()).desactivarTecnologias(anyList());
    }

    @Test
    void deleteBootcampWithSaga_CuandoBootcampConCapacidadesYTecnologias_DeberiaEjecutarSaga() {
        String bootcampId = "1";
        List<Long> capacidadesReferenciadas = Collections.singletonList(1L);
        List<Long> tecnologiasReferenciadas = Collections.singletonList(10L);

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(capacidadesReferenciadas));
        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(tecnologiasReferenciadas));
        when(capacidadGateway.desactivarCapacidades(anyList())).thenReturn(Mono.empty());
        when(tecnologiaGateway.desactivarTecnologias(anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .verifyComplete();

        verify(bootcampRepository).obtenerBootcampPorId(1L);
        verify(bootcampRepository).obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong());
        verify(bootcampRepository).obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong());
        verify(capacidadGateway).desactivarCapacidades(anyList());
        verify(tecnologiaGateway).desactivarTecnologias(anyList());
        verify(bootcampRepository).eliminarBootcamp(1L);
    }

    @Test
    void deleteBootcampWithSaga_CuandoTodasLasCapacidadesEstanReferenciadas_DeberiaNoDesactivarCapacidades() {
        String bootcampId = "1";
        List<Long> capacidadesReferenciadas = Arrays.asList(1L, 2L);
        List<Long> tecnologiasReferenciadas = Collections.emptyList();

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(capacidadesReferenciadas));
        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(tecnologiasReferenciadas));
        when(tecnologiaGateway.desactivarTecnologias(anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .verifyComplete();

        verify(capacidadGateway, never()).desactivarCapacidades(anyList());
        verify(tecnologiaGateway).desactivarTecnologias(anyList());
    }

    @Test
    void deleteBootcampWithSaga_CuandoTodasLasTecnologiasEstanReferenciadas_DeberiaNoDesactivarTecnologias() {
        String bootcampId = "1";
        List<Long> capacidadesReferenciadas = Collections.emptyList();
        List<Long> tecnologiasReferenciadas = Arrays.asList(10L, 20L);

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(capacidadesReferenciadas));
        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(tecnologiasReferenciadas));
        when(capacidadGateway.desactivarCapacidades(anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .verifyComplete();

        verify(capacidadGateway).desactivarCapacidades(anyList());
        verify(tecnologiaGateway, never()).desactivarTecnologias(anyList());
    }

    @Test
    void deleteBootcampWithSaga_CuandoEliminarBootcampFalla_DeberiaHacerRollback() {
        String bootcampId = "1";
        List<Long> capacidadesReferenciadas = Collections.emptyList();
        List<Long> tecnologiasReferenciadas = Collections.emptyList();
        RuntimeException error = new RuntimeException("Error al eliminar");

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(capacidadesReferenciadas));
        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(tecnologiasReferenciadas));
        when(capacidadGateway.desactivarCapacidades(anyList())).thenReturn(Mono.empty());
        when(tecnologiaGateway.desactivarTecnologias(anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.error(error));
        when(tecnologiaGateway.activarTecnologias(anyList())).thenReturn(Mono.empty());
        when(capacidadGateway.activarCapacidades(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .expectErrorMatches(throwable -> throwable instanceof BootcampEliminacionFallidaException
                        && throwable.getMessage().contains("No se pudo eliminar el bootcamp"))
                .verify();

        verify(bootcampRepository).eliminarBootcamp(1L);
        verify(tecnologiaGateway).activarTecnologias(anyList());
        verify(capacidadGateway).activarCapacidades(anyList());
    }

//    @Test
//    void deleteBootcampWithSaga_CuandoDesactivarCapacidadesFalla_DeberiaHacerRollback() {
//        String bootcampId = "1";
//        List<Long> capacidadesReferenciadas = Collections.emptyList();
//        List<Long> tecnologiasReferenciadas = Collections.emptyList();
//        RuntimeException error = new RuntimeException("Error al desactivar capacidades");
//
//        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
//        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
//                .thenReturn(Mono.just(capacidadesReferenciadas));
//        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
//                .thenReturn(Mono.just(tecnologiasReferenciadas));
//        when(capacidadGateway.desactivarCapacidades(anyList())).thenReturn(Mono.error(error));
//        when(tecnologiaGateway.activarTecnologias(anyList())).thenReturn(Mono.empty());
//        when(capacidadGateway.activarCapacidades(anyList())).thenReturn(Mono.empty());
//
//        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
//                .expectError(BootcampEliminacionFallidaException.class)
//                .verify();
//
//        verify(capacidadGateway).desactivarCapacidades(anyList());
//        verify(tecnologiaGateway, never()).desactivarTecnologias(anyList());
//        verify(bootcampRepository, never()).eliminarBootcamp(anyLong());
//        verify(tecnologiaGateway).activarTecnologias(anyList());
//        verify(capacidadGateway).activarCapacidades(anyList());
//    }
//
//    @Test
//    void deleteBootcampWithSaga_CuandoDesactivarTecnologiasFalla_DeberiaHacerRollback() {
//        String bootcampId = "1";
//        List<Long> capacidadesReferenciadas = Collections.emptyList();
//        List<Long> tecnologiasReferenciadas = Collections.emptyList();
//        RuntimeException error = new RuntimeException("Error al desactivar tecnologías");
//
//        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
//        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
//                .thenReturn(Mono.just(capacidadesReferenciadas));
//        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
//                .thenReturn(Mono.just(tecnologiasReferenciadas));
//        when(capacidadGateway.desactivarCapacidades(anyList())).thenReturn(Mono.empty());
//        when(tecnologiaGateway.desactivarTecnologias(anyList())).thenReturn(Mono.error(error));
//        when(tecnologiaGateway.activarTecnologias(anyList())).thenReturn(Mono.empty());
//        when(capacidadGateway.activarCapacidades(anyList())).thenReturn(Mono.empty());
//
//        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
//                .expectErrorMatches(throwable -> throwable instanceof BootcampEliminacionFallidaException
//                        && throwable.getMessage().contains("No se pudo eliminar el bootcamp"))
//                .verify();
//
//        verify(capacidadGateway).desactivarCapacidades(anyList());
//        verify(tecnologiaGateway).desactivarTecnologias(anyList());
//        verify(bootcampRepository, never()).eliminarBootcamp(anyLong());
//        verify(tecnologiaGateway).activarTecnologias(anyList());
//        verify(capacidadGateway).activarCapacidades(anyList());
//    }

    @Test
    void deleteBootcampWithSaga_CuandoCapacidadesIdsVacia_DeberiaEliminarDirectamente() {
        String bootcampId = "1";
        Bootcamp bootcampSinCapacidades = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Sin Capacidades")
                .capacidadesIds(Collections.emptyList())
                .tecnologiasIds(null)
                .build();

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcampSinCapacidades));
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .verifyComplete();

        verify(bootcampRepository).eliminarBootcamp(1L);
        verify(capacidadGateway, never()).desactivarCapacidades(anyList());
    }

    @Test
    void deleteBootcampWithSaga_CuandoTecnologiasIdsVacia_DeberiaEliminarDirectamente() {
        String bootcampId = "1";
        Bootcamp bootcampSinTecnologias = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Sin Tecnologías")
                .capacidadesIds(null)
                .tecnologiasIds(Collections.emptyList())
                .build();

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcampSinTecnologias));
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .verifyComplete();

        verify(bootcampRepository).eliminarBootcamp(1L);
        verify(tecnologiaGateway, never()).desactivarTecnologias(anyList());
    }

    @Test
    void deleteBootcampWithSaga_CuandoRollbackFalla_DeberiaCompletarSinError() {
        String bootcampId = "1";
        List<Long> capacidadesReferenciadas = Collections.emptyList();
        List<Long> tecnologiasReferenciadas = Collections.emptyList();
        RuntimeException error = new RuntimeException("Error al eliminar");
        RuntimeException rollbackError = new RuntimeException("Error en rollback");

        when(bootcampRepository.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampRepository.obtenerCapacidadesReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(capacidadesReferenciadas));
        when(bootcampRepository.obtenerTecnologiasReferenciadasPorOtrosBootcamps(anyList(), anyLong()))
                .thenReturn(Mono.just(tecnologiasReferenciadas));
        when(capacidadGateway.desactivarCapacidades(anyList())).thenReturn(Mono.empty());
        when(tecnologiaGateway.desactivarTecnologias(anyList())).thenReturn(Mono.empty());
        when(bootcampRepository.eliminarBootcamp(1L)).thenReturn(Mono.error(error));
        when(tecnologiaGateway.activarTecnologias(anyList())).thenReturn(Mono.error(rollbackError));
        when(capacidadGateway.activarCapacidades(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(sagaOrchestrator.deleteBootcampWithSaga(bootcampId))
                .expectError(BootcampEliminacionFallidaException.class)
                .verify();

        verify(tecnologiaGateway).activarTecnologias(anyList());
        verify(capacidadGateway).activarCapacidades(anyList());
    }
}

