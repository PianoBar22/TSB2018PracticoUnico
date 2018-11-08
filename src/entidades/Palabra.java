package entidades;

import java.util.Map;

public class Palabra implements Map.Entry<String, Integer> {
    private String descripcion;
    private int frecuencia;

    public Palabra(String descripcion) {
        this.descripcion = descripcion;
        this.frecuencia = 1;
    }

    @Override
    public String getKey() {
        return descripcion;
    }

    @Override
    public Integer getValue() {
        return frecuencia;
    }

    @Override
    public Integer setValue(Integer value) {
        frecuencia = value;
        return frecuencia;
    }

    @Override
    public boolean equals(Object o) {
        return descripcion.equals(o);
    }

    @Override
    public int hashCode() {
        return descripcion.hashCode();
    }

    public Integer incrementarFrecuencia(){
        return ++frecuencia;
    }

    public Integer incrementarFrecuencia(Integer cantidad){
        frecuencia += cantidad;
        return frecuencia;
    }
}
