package co.com.bootcamp.model.bootcamp.gateways;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacidadInfoTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(1L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(2L, "Spring");
        List<TecnologiaInfo> tecnologias = Arrays.asList(tecnologia1, tecnologia2);

        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend")
                .tecnologias(tecnologias)
                .build();

        assertEquals(1L, capacidad.getId());
        assertEquals("Backend", capacidad.getNombre());
        assertEquals(tecnologias, capacidad.getTecnologias());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        CapacidadInfo capacidad = CapacidadInfo.builder().build();

        assertNull(capacidad.getId());
        assertNull(capacidad.getNombre());
        assertNull(capacidad.getTecnologias());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        CapacidadInfo capacidad = new CapacidadInfo();

        assertNull(capacidad.getId());
        assertNull(capacidad.getNombre());
        assertNull(capacidad.getTecnologias());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "Python");
        List<TecnologiaInfo> tecnologias = Arrays.asList(tecnologia);

        CapacidadInfo capacidad = new CapacidadInfo(1L, "Data Science", tecnologias);

        assertEquals(1L, capacidad.getId());
        assertEquals("Data Science", capacidad.getNombre());
        assertEquals(tecnologias, capacidad.getTecnologias());
    }

    @Test
    void setters_DeberiaModificarValores() {
        CapacidadInfo capacidad = new CapacidadInfo();
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "React");
        List<TecnologiaInfo> tecnologias = Arrays.asList(tecnologia);

        capacidad.setId(2L);
        capacidad.setNombre("Frontend");
        capacidad.setTecnologias(tecnologias);

        assertEquals(2L, capacidad.getId());
        assertEquals("Frontend", capacidad.getNombre());
        assertEquals(tecnologias, capacidad.getTecnologias());
    }

    @Test
    void builder_ConTecnologiasVacia_DeberiaFuncionarCorrectamente() {
        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .nombre("Test")
                .tecnologias(Collections.emptyList())
                .build();

        assertNotNull(capacidad.getTecnologias());
        assertTrue(capacidad.getTecnologias().isEmpty());
    }

    @Test
    void builder_ConMultiplesTecnologias_DeberiaFuncionarCorrectamente() {
        TecnologiaInfo tecnologia1 = new TecnologiaInfo(1L, "Java");
        TecnologiaInfo tecnologia2 = new TecnologiaInfo(2L, "Spring");
        TecnologiaInfo tecnologia3 = new TecnologiaInfo(3L, "Hibernate");
        List<TecnologiaInfo> tecnologias = Arrays.asList(tecnologia1, tecnologia2, tecnologia3);

        CapacidadInfo capacidad = CapacidadInfo.builder()
                .id(1L)
                .nombre("Backend Avanzado")
                .tecnologias(tecnologias)
                .build();

        assertEquals(3, capacidad.getTecnologias().size());
        assertEquals("Java", capacidad.getTecnologias().get(0).getNombre());
        assertEquals("Spring", capacidad.getTecnologias().get(1).getNombre());
        assertEquals("Hibernate", capacidad.getTecnologias().get(2).getNombre());
    }
}




