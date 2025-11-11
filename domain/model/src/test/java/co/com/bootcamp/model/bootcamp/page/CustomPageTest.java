package co.com.bootcamp.model.bootcamp.page;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomPageTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        List<String> data = Arrays.asList("item1", "item2", "item3");
        CustomPage<String> page = CustomPage.<String>builder()
                .data(data)
                .totalRows(100L)
                .pageSize(10)
                .pageNum(1)
                .sort("nombre")
                .hasNext(true)
                .build();

        assertEquals(data, page.getData());
        assertEquals(100L, page.getTotalRows());
        assertEquals(10, page.getPageSize());
        assertEquals(1, page.getPageNum());
        assertEquals("nombre", page.getSort());
        assertTrue(page.getHasNext());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        CustomPage<String> page = CustomPage.<String>builder().build();

        assertNull(page.getData());
        assertNull(page.getTotalRows());
        assertNull(page.getPageSize());
        assertNull(page.getPageNum());
        assertNull(page.getSort());
        assertNull(page.getHasNext());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaConListaVacia() {
        CustomPage<String> page = new CustomPage<>();

        assertNotNull(page.getData());
        assertTrue(page.getData().isEmpty());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        List<String> data = Arrays.asList("item1", "item2");
        CustomPage<String> page = new CustomPage<>(data, 50L, 5, 2, "id", false);

        assertEquals(data, page.getData());
        assertEquals(50L, page.getTotalRows());
        assertEquals(5, page.getPageSize());
        assertEquals(2, page.getPageNum());
        assertEquals("id", page.getSort());
        assertFalse(page.getHasNext());
    }

    @Test
    void add_DeberiaAgregarItemALaLista() {
        CustomPage<String> page = new CustomPage<>();
        page.add("item1");
        page.add("item2");

        assertEquals(2, page.getData().size());
        assertTrue(page.getData().contains("item1"));
        assertTrue(page.getData().contains("item2"));
    }

    @Test
    void add_ConListaInicializada_DeberiaAgregarItem() {
        List<String> initialData = new ArrayList<>(Arrays.asList("item1"));
        CustomPage<String> page = CustomPage.<String>builder()
                .data(initialData)
                .build();

        page.add("item2");

        assertEquals(2, page.getData().size());
        assertTrue(page.getData().contains("item1"));
        assertTrue(page.getData().contains("item2"));
    }

    @Test
    void iterator_DeberiaRetornarIteratorDeLaLista() {
        List<String> data = Arrays.asList("item1", "item2", "item3");
        CustomPage<String> page = CustomPage.<String>builder()
                .data(data)
                .build();

        Iterator<String> iterator = page.iterator();
        List<String> iteratedItems = new ArrayList<>();
        iterator.forEachRemaining(iteratedItems::add);

        assertEquals(data, iteratedItems);
    }

    @Test
    void iterator_ConListaVacia_DeberiaRetornarIteratorVacio() {
        CustomPage<String> page = new CustomPage<>();

        Iterator<String> iterator = page.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    void setters_DeberiaModificarValores() {
        CustomPage<String> page = new CustomPage<>();
        List<String> data = Arrays.asList("new1", "new2");

        page.setData(data);
        page.setTotalRows(200L);
        page.setPageSize(20);
        page.setPageNum(3);
        page.setSort("fecha");
        page.setHasNext(false);

        assertEquals(data, page.getData());
        assertEquals(200L, page.getTotalRows());
        assertEquals(20, page.getPageSize());
        assertEquals(3, page.getPageNum());
        assertEquals("fecha", page.getSort());
        assertFalse(page.getHasNext());
    }

    @Test
    void equals_DeberiaCompararCorrectamente() {
        List<String> data1 = Arrays.asList("item1", "item2");
        List<String> data2 = Arrays.asList("item1", "item2");
        CustomPage<String> page1 = CustomPage.<String>builder()
                .data(data1)
                .totalRows(100L)
                .pageSize(10)
                .pageNum(1)
                .build();
        CustomPage<String> page2 = CustomPage.<String>builder()
                .data(data2)
                .totalRows(100L)
                .pageSize(10)
                .pageNum(1)
                .build();

        assertEquals(page1, page2);
    }

    @Test
    void hashCode_DeberiaGenerarHashCodeConsistente() {
        List<String> data = Arrays.asList("item1", "item2");
        CustomPage<String> page1 = CustomPage.<String>builder()
                .data(data)
                .totalRows(100L)
                .build();
        CustomPage<String> page2 = CustomPage.<String>builder()
                .data(data)
                .totalRows(100L)
                .build();

        assertEquals(page1.hashCode(), page2.hashCode());
    }
}

