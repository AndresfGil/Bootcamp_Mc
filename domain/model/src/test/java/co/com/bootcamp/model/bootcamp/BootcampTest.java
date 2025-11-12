package co.com.bootcamp.model.bootcamp;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BootcampTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        List<Long> capacidadesIds = Arrays.asList(1L, 2L);
        List<Long> tecnologiasIds = Arrays.asList(10L, 20L);
        LocalDate fecha = LocalDate.now();
        Bootcamp bootcamp = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(fecha)
                .duracion(12)
                .capacidadesIds(capacidadesIds)
                .tecnologiasIds(tecnologiasIds)
                .build();

        assertEquals(1L, bootcamp.getId());
        assertEquals("Bootcamp Java", bootcamp.getNombre());
        assertEquals("Bootcamp completo de Java", bootcamp.getDescripcion());
        assertEquals(fecha, bootcamp.getFechaLanzamiento());
        assertEquals(12, bootcamp.getDuracion());
        assertEquals(capacidadesIds, bootcamp.getCapacidadesIds());
        assertEquals(tecnologiasIds, bootcamp.getTecnologiasIds());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        Bootcamp bootcamp = Bootcamp.builder().build();

        assertNull(bootcamp.getId());
        assertNull(bootcamp.getNombre());
        assertNull(bootcamp.getDescripcion());
        assertNull(bootcamp.getFechaLanzamiento());
        assertNull(bootcamp.getDuracion());
        assertNull(bootcamp.getCapacidadesIds());
        assertNull(bootcamp.getTecnologiasIds());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        Bootcamp bootcamp = new Bootcamp();

        assertNull(bootcamp.getId());
        assertNull(bootcamp.getNombre());
        assertNull(bootcamp.getDescripcion());
        assertNull(bootcamp.getFechaLanzamiento());
        assertNull(bootcamp.getDuracion());
        assertNull(bootcamp.getCapacidadesIds());
        assertNull(bootcamp.getTecnologiasIds());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        LocalDate fecha = LocalDate.now();
        List<Long> capacidadesIds = Arrays.asList(1L, 2L, 3L);
        List<Long> tecnologiasIds = Arrays.asList(10L, 20L);
        Bootcamp bootcamp = new Bootcamp(1L, "Bootcamp Python", "Bootcamp de Python", fecha, 10, capacidadesIds, tecnologiasIds);

        assertEquals(1L, bootcamp.getId());
        assertEquals("Bootcamp Python", bootcamp.getNombre());
        assertEquals("Bootcamp de Python", bootcamp.getDescripcion());
        assertEquals(fecha, bootcamp.getFechaLanzamiento());
        assertEquals(10, bootcamp.getDuracion());
        assertEquals(capacidadesIds, bootcamp.getCapacidadesIds());
        assertEquals(tecnologiasIds, bootcamp.getTecnologiasIds());
    }

    @Test
    void toBuilder_DeberiaCrearNuevaInstanciaConValoresModificados() {
        LocalDate fechaOriginal = LocalDate.now();
        Bootcamp original = Bootcamp.builder()
                .id(1L)
                .nombre("Bootcamp Original")
                .descripcion("Original")
                .fechaLanzamiento(fechaOriginal)
                .duracion(12)
                .capacidadesIds(Arrays.asList(1L, 2L))
                .build();

        LocalDate fechaModificada = LocalDate.now().plusDays(30);
        Bootcamp modificada = original.toBuilder()
                .descripcion("Modificada")
                .fechaLanzamiento(fechaModificada)
                .build();

        assertEquals(1L, modificada.getId());
        assertEquals("Bootcamp Original", modificada.getNombre());
        assertEquals("Modificada", modificada.getDescripcion());
        assertEquals(fechaModificada, modificada.getFechaLanzamiento());
        assertNotSame(original, modificada);
    }

    @Test
    void setters_DeberiaModificarValores() {
        Bootcamp bootcamp = new Bootcamp();
        LocalDate fecha = LocalDate.now();
        List<Long> capacidadesIds = Arrays.asList(1L, 2L);
        List<Long> tecnologiasIds = Arrays.asList(10L, 20L);

        bootcamp.setId(2L);
        bootcamp.setNombre("Bootcamp React");
        bootcamp.setDescripcion("Bootcamp de React");
        bootcamp.setFechaLanzamiento(fecha);
        bootcamp.setDuracion(8);
        bootcamp.setCapacidadesIds(capacidadesIds);
        bootcamp.setTecnologiasIds(tecnologiasIds);

        assertEquals(2L, bootcamp.getId());
        assertEquals("Bootcamp React", bootcamp.getNombre());
        assertEquals("Bootcamp de React", bootcamp.getDescripcion());
        assertEquals(fecha, bootcamp.getFechaLanzamiento());
        assertEquals(8, bootcamp.getDuracion());
        assertEquals(capacidadesIds, bootcamp.getCapacidadesIds());
        assertEquals(tecnologiasIds, bootcamp.getTecnologiasIds());
    }

    @Test
    void builder_ConCapacidadesIdsVacia_DeberiaFuncionarCorrectamente() {
        Bootcamp bootcamp = Bootcamp.builder()
                .id(1L)
                .nombre("Test")
                .capacidadesIds(Collections.emptyList())
                .tecnologiasIds(Collections.emptyList())
                .build();

        assertNotNull(bootcamp.getCapacidadesIds());
        assertTrue(bootcamp.getCapacidadesIds().isEmpty());
        assertNotNull(bootcamp.getTecnologiasIds());
        assertTrue(bootcamp.getTecnologiasIds().isEmpty());
    }
}

