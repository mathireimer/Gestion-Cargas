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

public class OptimizadorCarga {
    public static ArrayList<Asignacion> optimizar(ArrayList<Remolque> remolques, ArrayList<Mercaderia> mercaderias) {
        ArrayList<Asignacion> asignaciones = new ArrayList<>();

        for (Mercaderia m : mercaderias) {
            Remolque mejorRemolque = null;
            double menorCosto = Double.MAX_VALUE;

            for (Remolque r : remolques) {
                if (r.puedeCargar(m)) {
                    double costo = m.getPeso() * r.getConsumoPorKgKm() * r.getDistanciaDestino();
                    if (costo < menorCosto) {
                        menorCosto = costo;
                        mejorRemolque = r;
                    }
                }
            }

            if (mejorRemolque != null) {
                mejorRemolque.asignarCarga(m);
                asignaciones.add(new Asignacion(m, mejorRemolque));
            }
        }

        return asignaciones;
    }
}

