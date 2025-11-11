package co.com.bootcamp.model.bootcamp.gateways;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TecnologiaInfoTest {

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        TecnologiaInfo tecnologia = new TecnologiaInfo();

        assertNull(tecnologia.getId());
        assertNull(tecnologia.getNombre());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "Java");

        assertEquals(1L, tecnologia.getId());
        assertEquals("Java", tecnologia.getNombre());
    }

    @Test
    void setters_DeberiaModificarValores() {
        TecnologiaInfo tecnologia = new TecnologiaInfo();

        tecnologia.setId(2L);
        tecnologia.setNombre("Spring");

        assertEquals(2L, tecnologia.getId());
        assertEquals("Spring", tecnologia.getNombre());
    }

    @Test
    void getters_DeberiaRetornarValoresCorrectos() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "Python");

        assertEquals(1L, tecnologia.getId());
        assertEquals("Python", tecnologia.getNombre());
    }

    @Test
    void constructor_ConValoresNulos_DeberiaPermitirNull() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(null, null);

        assertNull(tecnologia.getId());
        assertNull(tecnologia.getNombre());
    }

    @Test
    void setters_ConValoresNulos_DeberiaPermitirNull() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "Java");

        tecnologia.setId(null);
        tecnologia.setNombre(null);

        assertNull(tecnologia.getId());
        assertNull(tecnologia.getNombre());
    }

    @Test
    void constructor_ConIdCero_DeberiaFuncionarCorrectamente() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(0L, "Test");

        assertEquals(0L, tecnologia.getId());
        assertEquals("Test", tecnologia.getNombre());
    }

    @Test
    void constructor_ConNombreVacio_DeberiaFuncionarCorrectamente() {
        TecnologiaInfo tecnologia = new TecnologiaInfo(1L, "");

        assertEquals(1L, tecnologia.getId());
        assertEquals("", tecnologia.getNombre());
    }
}

