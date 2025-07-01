package optimizacion;

import java.util.ArrayList;

public class Remolque {
    private int id;
    private String nombre;
    private double capacidadVolumen;
    private double capacidadPeso;
    private String destino;
    private double distancia;

    public Remolque(int id, String nombre, double capacidadVolumen, double capacidadPeso, String destino, String destino1, double distancia, double par1) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadVolumen = capacidadVolumen;
        this.capacidadPeso = capacidadPeso;
        this.destino = destino;
        this.distancia = distancia;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCapacidadVolumen() {
        return capacidadVolumen;
    }

    public double getCapacidadPeso() {
        return capacidadPeso;
    }

    public String getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }
}

