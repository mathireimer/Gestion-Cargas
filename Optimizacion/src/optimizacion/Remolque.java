package optimizacion;

import java.util.ArrayList;

public class Remolque {

    private int id;
    private String nombre;
    private double capacidadVolumen;
    private double capacidadPeso;
    private String destino;
    private double distancia;
    private double consumoPorKgKm;
    private double distanciaDestino;
    private double pesoDisponible;
    private double volumenDisponible;

    public Remolque(int id, String nombre, double capacidadVolumen, double capacidadPeso, String destino, String destino1, double distancia, double par1) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadVolumen = capacidadVolumen;
        this.capacidadPeso = capacidadPeso;
        this.destino = destino;
        this.distancia = distancia;
        this.pesoDisponible = capacidadPeso;
this.volumenDisponible = capacidadVolumen;

    }

    public double getConsumoPorKgKm() {
        return consumoPorKgKm;
    }

    public double getDistanciaDestino() {
        return distanciaDestino;
    }

    public boolean puedeCargar(Mercaderia m) {
        return (this.pesoDisponible >= m.getPeso() && this.volumenDisponible >= m.getVolumen());
    }

    public void asignarCarga(Mercaderia m) {
        this.pesoDisponible -= m.getPeso();
        this.volumenDisponible -= m.getVolumen();
    }

    public Remolque(int id, String nombre, double capacidadVolumen, double capacidadPeso, String destino, double distancia, double consumoPorKgKm, double distanciaDestino, double pesoDisponible, double volumenDisponible) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadVolumen = capacidadVolumen;
        this.capacidadPeso = capacidadPeso;
        this.destino = destino;
        this.distancia = distancia;
        this.consumoPorKgKm = consumoPorKgKm;
        this.distanciaDestino = distanciaDestino;
        this.pesoDisponible = pesoDisponible;
        this.volumenDisponible = volumenDisponible;
    }

    public double getPesoMaximo() {
    return capacidadPeso;
}

public double getVolumenMaximo() {
    return capacidadVolumen;
}

    
    public int getId() {
        return id;
    }

    public double getPesoDisponible() {
        return pesoDisponible;
    }

    public void setPesoDisponible(double pesoDisponible) {
        this.pesoDisponible = pesoDisponible;
    }

    public double getVolumenDisponible() {
        return volumenDisponible;
    }

    public void setVolumenDisponible(double volumenDisponible) {
        this.volumenDisponible = volumenDisponible;
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
