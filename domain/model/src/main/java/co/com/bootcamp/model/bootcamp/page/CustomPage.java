package co.com.bootcamp.model.bootcamp.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomPage<T> implements Iterable<T> {

    private List<T> data = new ArrayList<>();
    private Long totalRows;
    private Integer pageSize;
    private Integer pageNum;
    private String sort;
    private Boolean hasNext;

    public void add(T item) {
        data.add(item);
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }
}