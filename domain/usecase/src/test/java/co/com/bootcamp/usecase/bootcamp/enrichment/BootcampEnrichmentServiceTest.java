package co.com.bootcamp.usecase.bootcamp.enrichment;

import co.com.bootcamp.model.bootcamp.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadGateway;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaInfo;
import co.com.bootcamp.model.bootcamp.page.CustomPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampEnrichmentServiceTest {

    @Mock
    private CapacidadGateway capacidadGateway;

    @InjectMocks
    private BootcampEnrichmentService bootcampEnrichmentService;

    private Bootcamp bootcamp1;
    private Bootcamp bootcamp2;
    private CapacidadInfo capacidad1;
    private CapacidadInfo capacidad2;
    private TecnologiaInfo tecnologia1;
    private TecnologiaInfo tecnologia2;

    @BeforeEach
    void setUp() {
        tecnologia1 = new TecnologiaInfo(1L, "Java");
        tecnologia2 = new TecnologiaInfo(2L, "Spring");

        capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologia1, tecnologia2))
                .build();

        capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .nombre("Frontend")
                .tecnologias(Collections.emptyList())
                .build();

        bootcamp1 = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(LocalDate.now())
                .duracion(12)
                .capacidadesIds(Arrays.asList(1L, 2L))
                .build();

        bootcamp2 = Bootcamp.builder()
                .id(2L)
                .nombre("Bootcamp Python")
                .descripcion("Bootcamp de Python")
                .fechaLanzamiento(LocalDate.now().plusDays(30))
                .duracion(10)
                .capacidadesIds(Collections.singletonList(1L))
                .build();
    }

    @Test
    void enriquecerBootcampsConCapacidades_CuandoListaVacia_DeberiaRetornarPaginaVacia() {
        CustomPage<Bootcamp> pageVacia = CustomPage.<Bootcamp>builder()
                .data(Collections.emptyList())
                .totalRows(0L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageVacia))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertTrue(page.getData().isEmpty());
                    assertEquals(0L, page.getTotalRows());
                    assertEquals(10, page.getPageSize());
                    assertEquals(0, page.getPageNum());
                    assertFalse(page.getHasNext());
                })
                .verifyComplete();

        verify(capacidadGateway, never()).obtenerCapacidadesPorIds(any());
    }

    @Test
    void enriquecerBootcampsConCapacidades_CuandoUnBootcamp_DeberiaEnriquecerCorrectamente() {
        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Collections.singletonList(bootcamp1))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre")
                .build();

        when(capacidadGateway.obtenerCapacidadesPorIds(Arrays.asList(1L, 2L)))
                .thenReturn(Flux.just(capacidad1, capacidad2));

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertEquals(1, page.getData().size());
                    assertEquals(1L, page.getTotalRows());
                    assertEquals(10, page.getPageSize());
                    assertEquals(0, page.getPageNum());
                    assertFalse(page.getHasNext());
                    assertEquals("nombre", page.getSort());

                    BootcampConCapacidades bootcampEnriquecido = page.getData().get(0);
                    assertEquals(1L, bootcampEnriquecido.getId());
                    assertEquals("Bootcamp Java", bootcampEnriquecido.getNombre());
                    assertEquals("Bootcamp completo de Java", bootcampEnriquecido.getDescripcion());
                    assertEquals(2, bootcampEnriquecido.getCapacidades().size());
                    assertEquals("Backend", bootcampEnriquecido.getCapacidades().get(0).getNombre());
                    assertEquals("Frontend", bootcampEnriquecido.getCapacidades().get(1).getNombre());
                })
                .verifyComplete();

        verify(capacidadGateway).obtenerCapacidadesPorIds(Arrays.asList(1L, 2L));
    }

    @Test
    void enriquecerBootcampsConCapacidades_CuandoMultiplesBootcamps_DeberiaEnriquecerTodos() {
        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Arrays.asList(bootcamp1, bootcamp2))
                .totalRows(2L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        when(capacidadGateway.obtenerCapacidadesPorIds(Arrays.asList(1L, 2L)))
                .thenReturn(Flux.just(capacidad1, capacidad2));

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .assertNext(page -> {
                    assertEquals(2, page.getData().size());
                    assertEquals(2L, page.getTotalRows());

                    BootcampConCapacidades bootcampEnriquecido1 = page.getData().get(0);
                    assertEquals(1L, bootcampEnriquecido1.getId());
                    assertEquals("Bootcamp Java", bootcampEnriquecido1.getNombre());
                    assertEquals(2, bootcampEnriquecido1.getCapacidades().size());

                    BootcampConCapacidades bootcampEnriquecido2 = page.getData().get(1);
                    assertEquals(2L, bootcampEnriquecido2.getId());
                    assertEquals("Bootcamp Python", bootcampEnriquecido2.getNombre());
                    assertEquals(1, bootcampEnriquecido2.getCapacidades().size());
                })
                .verifyComplete();
    }

    @Test
    void enriquecerBootcampsConCapacidades_CuandoBootcampSinCapacidadesIds_DeberiaRetornarListaVacia() {
        Bootcamp bootcampSinCapacidades = Bootcamp.builder()
                .id(3L)
                .nombre("Bootcamp Sin Capacidades")
                .capacidadesIds(null)
                .build();

        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Collections.singletonList(bootcampSinCapacidades))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        when(capacidadGateway.obtenerCapacidadesPorIds(Collections.emptyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .assertNext(page -> {
                    assertEquals(1, page.getData().size());
                    BootcampConCapacidades bootcampEnriquecido = page.getData().get(0);
                    assertEquals(3L, bootcampEnriquecido.getId());
                    assertTrue(bootcampEnriquecido.getCapacidades().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void enriquecerBootcampsConCapacidades_CuandoCapacidadesIdsVacia_DeberiaRetornarListaVacia() {
        Bootcamp bootcampConListaVacia = Bootcamp.builder()
                .id(4L)
                .nombre("Bootcamp Lista Vacia")
                .capacidadesIds(Collections.emptyList())
                .build();

        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Collections.singletonList(bootcampConListaVacia))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        when(capacidadGateway.obtenerCapacidadesPorIds(Collections.emptyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .assertNext(page -> {
                    assertEquals(1, page.getData().size());
                    BootcampConCapacidades bootcampEnriquecido = page.getData().get(0);
                    assertEquals(4L, bootcampEnriquecido.getId());
                    assertTrue(bootcampEnriquecido.getCapacidades().isEmpty());
                })
                .verifyComplete();
    }


    @Test
    void enriquecerBootcampsConCapacidades_CuandoGatewayRetornaError_DeberiaPropagarError() {
        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Collections.singletonList(bootcamp1))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        RuntimeException error = new RuntimeException("Error al obtener capacidades");
        when(capacidadGateway.obtenerCapacidadesPorIds(any()))
                .thenReturn(Flux.error(error));

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void enriquecerBootcampsConCapacidades_CuandoCapacidadNoExiste_DeberiaFiltrarla() {
        Bootcamp bootcampConCapacidadInexistente = Bootcamp.builder()
                .id(5L)
                .nombre("Bootcamp Con Capacidad Inexistente")
                .capacidadesIds(Arrays.asList(1L, 999L))
                .build();

        CustomPage<Bootcamp> pageBootcamps = CustomPage.<Bootcamp>builder()
                .data(Collections.singletonList(bootcampConCapacidadInexistente))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        when(capacidadGateway.obtenerCapacidadesPorIds(Arrays.asList(1L, 999L)))
                .thenReturn(Flux.just(capacidad1));

        StepVerifier.create(bootcampEnrichmentService.enriquecerBootcampsConCapacidades(pageBootcamps))
                .assertNext(page -> {
                    BootcampConCapacidades bootcampEnriquecido = page.getData().get(0);
                    assertEquals(1, bootcampEnriquecido.getCapacidades().size());
                    assertEquals(1L, bootcampEnriquecido.getCapacidades().get(0).getId());
                })
                .verifyComplete();
    }
}

