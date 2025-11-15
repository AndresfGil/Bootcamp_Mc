package co.com.bootcamp.model.bootcamp;

import co.com.bootcamp.model.bootcamp.bootcamp.BootcampConCapacidades;
import co.com.bootcamp.model.bootcamp.gateways.CapacidadInfo;
import co.com.bootcamp.model.bootcamp.gateways.TecnologiaInfo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BootcampConCapacidadesTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "Java");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologia))
                .build();
        List<CapacidadInfo> capacidades = Arrays.asList(capacidad);
        LocalDate fecha = LocalDate.now();

        BootcampConCapacidades bootcamp = BootcampConCapacidades.builder()
                .id(1L)
                .nombre("Bootcamp Java")
                .descripcion("Bootcamp completo de Java")
                .fechaLanzamiento(fecha)
                .duracion(12)
                .capacidades(capacidades)
                .build();

        assertEquals(1L, bootcamp.getId());
        assertEquals("Bootcamp Java", bootcamp.getNombre());
        assertEquals("Bootcamp completo de Java", bootcamp.getDescripcion());
        assertEquals(fecha, bootcamp.getFechaLanzamiento());
        assertEquals(12, bootcamp.getDuracion());
        assertEquals(capacidades, bootcamp.getCapacidades());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        BootcampConCapacidades bootcamp = BootcampConCapacidades.builder().build();

        assertNull(bootcamp.getId());
        assertNull(bootcamp.getNombre());
        assertNull(bootcamp.getDescripcion());
        assertNull(bootcamp.getFechaLanzamiento());
        assertNull(bootcamp.getDuracion());
        assertNull(bootcamp.getCapacidades());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        BootcampConCapacidades bootcamp = new BootcampConCapacidades();

        assertNull(bootcamp.getId());
        assertNull(bootcamp.getNombre());
        assertNull(bootcamp.getDescripcion());
        assertNull(bootcamp.getFechaLanzamiento());
        assertNull(bootcamp.getDuracion());
        assertNull(bootcamp.getCapacidades());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        LocalDate fecha = LocalDate.now();
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "Python");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .nombre("Data Science")
                .tecnologias(Arrays.asList(tecnologia))
                .build();
        List<CapacidadInfo> capacidades = Arrays.asList(capacidad);

        BootcampConCapacidades bootcamp = new BootcampConCapacidades(
                1L, "Bootcamp Python", "Bootcamp de Python", fecha, 10, capacidades
        );

        assertEquals(1L, bootcamp.getId());
        assertEquals("Bootcamp Python", bootcamp.getNombre());
        assertEquals("Bootcamp de Python", bootcamp.getDescripcion());
        assertEquals(fecha, bootcamp.getFechaLanzamiento());
        assertEquals(10, bootcamp.getDuracion());
        assertEquals(capacidades, bootcamp.getCapacidades());
    }

    @Test
    void setters_DeberiaModificarValores() {
        BootcampConCapacidades bootcamp = new BootcampConCapacidades();
        LocalDate fecha = LocalDate.now();
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "React");
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .nombre("Frontend")
                .tecnologias(Arrays.asList(tecnologia))
                .build();
        List<CapacidadInfo> capacidades = Arrays.asList(capacidad);

        bootcamp.setId(2L);
        bootcamp.setNombre("Bootcamp React");
        bootcamp.setDescripcion("Bootcamp de React");
        bootcamp.setFechaLanzamiento(fecha);
        bootcamp.setDuracion(8);
        bootcamp.setCapacidades(capacidades);

        assertEquals(2L, bootcamp.getId());
        assertEquals("Bootcamp React", bootcamp.getNombre());
        assertEquals("Bootcamp de React", bootcamp.getDescripcion());
        assertEquals(fecha, bootcamp.getFechaLanzamiento());
        assertEquals(8, bootcamp.getDuracion());
        assertEquals(capacidades, bootcamp.getCapacidades());
    }

    @Test
    void builder_ConCapacidadesVacia_DeberiaFuncionarCorrectamente() {
        BootcampConCapacidades bootcamp = BootcampConCapacidades.builder()
                .id(1L)
                .nombre("Test")
                .capacidades(Collections.emptyList())
                .build();

        assertNotNull(bootcamp.getCapacidades());
        assertTrue(bootcamp.getCapacidades().isEmpty());
    }

    @Test
    void builder_ConMultiplesCapacidades_DeberiaFuncionarCorrectamente() {
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(1L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(2L, "Spring");
        CapacidadInfo capacidad1 = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(Arrays.asList(tecnologia1, tecnologia2))
                .build();
        CapacidadInfo capacidad2 = CapacidadInfo.builder()
                .id(2L)
                .nombre("Frontend")
                .tecnologias(Collections.emptyList())
                .build();
        List<CapacidadInfo> capacidades = Arrays.asList(capacidad1, capacidad2);

        BootcampConCapacidades bootcamp = BootcampConCapacidades.builder()
                .id(1L)
                .nombre("Bootcamp Full Stack")
                .capacidades(capacidades)
                .build();

        assertEquals(2, bootcamp.getCapacidades().size());
        assertEquals("Backend", bootcamp.getCapacidades().get(0).getNombre());
        assertEquals("Frontend", bootcamp.getCapacidades().get(1).getNombre());
    }
}




