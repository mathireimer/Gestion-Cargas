/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package optimizacion;

/**
 *
 * @author mathiasreimer
 */
public class Asignacion {
    private Mercaderia mercaderia;
    private Remolque remolque;
    private double costoTransporte;

    public Asignacion(Mercaderia mercaderia, Remolque remolque) {
        this.mercaderia = mercaderia;
        this.remolque = remolque;
        this.costoTransporte = mercaderia.getPeso() * remolque.getConsumoPorKgKm() * remolque.getDistanciaDestino();
    }

    public Mercaderia getMercaderia() { return mercaderia; }
    public Remolque getRemolque() { return remolque; }
    public double getCostoTransporte() { return costoTransporte; }
}
