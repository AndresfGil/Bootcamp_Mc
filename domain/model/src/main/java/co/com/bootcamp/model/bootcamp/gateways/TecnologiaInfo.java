package co.com.bootcamp.model.bootcamp.gateways;

public class TecnologiaInfo {

    private Long id;
    private String nombre;

    public TecnologiaInfo() {
    }

    public TecnologiaInfo(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
