/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package optimizacion;

/**
 *
 * @author mathiasreimer
 */
import java.util.ArrayList;

public class Remolque {
    private int id;
    private String nombre;
    private double capacidadPeso;
    private double capacidadVolumen;
    private double pesoDisponible;
    private double volumenDisponible;
    private String ubicacionActual;
    private String destino;
    private double distanciaDestino;
    private double consumoPorKgKm;
    private ArrayList<Mercaderia> cargasAsignadas = new ArrayList<>();

    public Remolque(int id, String nombre, double capacidadPeso, double capacidadVolumen, String ubicacionActual,
                    String destino, double distanciaDestino, double consumoPorKgKm) {
        this.id = id;
        this.nombre = nombre;
        this.capacidadPeso = capacidadPeso;
        this.capacidadVolumen = capacidadVolumen;
        this.pesoDisponible = capacidadPeso;
        this.volumenDisponible = capacidadVolumen;
        this.ubicacionActual = ubicacionActual;
        this.destino = destino;
        this.distanciaDestino = distanciaDestino;
        this.consumoPorKgKm = consumoPorKgKm;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPesoDisponible() { return pesoDisponible; }
    public double getVolumenDisponible() { return volumenDisponible; }
    public double getDistanciaDestino() { return distanciaDestino; }
    public double getConsumoPorKgKm() { return consumoPorKgKm; }
    public String getDestino() { return destino; }
    public ArrayList<Mercaderia> getCargasAsignadas() { return cargasAsignadas; }

    public boolean puedeCargar(Mercaderia m) {
        return m.getPeso() <= pesoDisponible && m.getVolumen() <= volumenDisponible;
    }

    public void asignarCarga(Mercaderia m) {
        if (puedeCargar(m)) {
            cargasAsignadas.add(m);
            pesoDisponible -= m.getPeso();
            volumenDisponible -= m.getVolumen();
        }
    }
}
