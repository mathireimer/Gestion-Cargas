/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package optimizacion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

        double consumoBasePorKm = 0.3;
        double incrementoPorKg = 0.0001;
        double costoCombustiblePorLitro = 1.25;

        double distancia = mercaderia.getDistancia() * 2;  // Ida y vuelta
        double pesoTotal = mercaderia.getPeso();

        double consumoTotal = distancia * (consumoBasePorKm + pesoTotal * incrementoPorKg);
        this.costoTransporte = consumoTotal * costoCombustiblePorLitro;
    }

    public Mercaderia getMercaderia() { return mercaderia; }
    public Remolque getRemolque() { return remolque; }
    public double getCostoTransporte() { return costoTransporte; }
}
