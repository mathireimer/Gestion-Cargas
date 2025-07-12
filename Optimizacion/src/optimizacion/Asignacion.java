package optimizacion;

import java.util.ArrayList;

public class Asignacion {

    private Remolque remolque;
    private ArrayList<Mercaderia> mercaderias = new ArrayList<>();
    private double costoTransporte;

    public Asignacion(Remolque remolque) {
        this.remolque = remolque;
        this.mercaderias = new ArrayList<>();
    }

    public void agregarMercaderia(Mercaderia mercaderia) {
        mercaderias.add(mercaderia);
    }

    public ArrayList<Mercaderia> getMercaderias() {
        return mercaderias;
    }

    public Remolque getRemolque() {
        return remolque;
    }

    public double getCostoTransporte() {
        return costoTransporte;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Remolque: ").append(remolque.getNombre()).append("\n");
        for (Mercaderia m : mercaderias) {
            sb.append("→ Mercadería ID: ").append(m.getId())
                    .append(" | Destino: ").append(m.getDestino())
                    .append(" | Peso: ").append(m.getPeso()).append(" kg\n");
        }
        return sb.toString();
    }
}
