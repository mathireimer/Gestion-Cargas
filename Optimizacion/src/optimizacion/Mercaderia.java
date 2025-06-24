/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package optimizacion;

/**
 *
 * @author mathiasreimer
 */
public class Mercaderia {
    private int id;
    private double peso;
    private double volumen;
    private String destino;

    public Mercaderia(int id, double peso, double volumen, String destino) {
        this.id = id;
        this.peso = peso;
        this.volumen = volumen;
        this.destino = destino;
    }

    public int getId() { return id; }
    public double getPeso() { return peso; }
    public double getVolumen() { return volumen; }
    public String getDestino() { return destino; }
}
