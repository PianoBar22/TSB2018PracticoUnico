package entidades;

import java.io.Serializable;

public class Contador implements Serializable {
    private int frecuencia;

    public Contador() {
        frecuencia = 1;
    }

    public Contador(int cantidad) {
        frecuencia = cantidad;
    }

    public int incrementar(){
        return ++frecuencia;
    }

    public int incrementar(int cantidad){
        frecuencia += cantidad;

        return frecuencia;
    }

    @Override
    public boolean equals(Object obj) {
        return frecuencia == ((Contador)obj).frecuencia;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    @Override
    public String toString() {
        return "frecuencia=" + frecuencia;
    }
}
