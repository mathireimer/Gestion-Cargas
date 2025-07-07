package optimizacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;

public class OptimizacionPanel extends JPanel {

    private JTextArea resultadoArea;
// En tu clase OptimizadorCarga

    public static ArrayList<Mercaderia> cargarMercaderias(String archivo) {
        ArrayList<Mercaderia> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                int id = Integer.parseInt(partes[0].split(":")[1].trim());
                double peso = Double.parseDouble(partes[1].split(":")[1].trim());
                double volumen = Double.parseDouble(partes[2].split(":")[1].trim());
                String destino = partes[3].split(":")[1].trim();
                int distancia = Integer.parseInt(partes[4].split(":")[1].trim());
                lista.add(new Mercaderia(id, peso, volumen, destino, distancia));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static ArrayList<ArrayList<Asignacion>> generarAsignaciones(ArrayList<Mercaderia> mercaderias, ArrayList<Remolque> remolques) {
    ArrayList<ArrayList<Asignacion>> soluciones = new ArrayList<>();
    ArrayList<Asignacion> asignaciones = new ArrayList<>();
    for (int i = 0; i < Math.min(mercaderias.size(), remolques.size()); i++) {
        asignaciones.add(new Asignacion(mercaderias.get(i), remolques.get(i)));
    }
    soluciones.add(asignaciones);
    return soluciones;
}
    
    public static ArrayList<Remolque> cargarRemolques(String archivo) {
        ArrayList<Remolque> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                int id = Integer.parseInt(partes[0].split(":")[1].trim());
                String nombre = partes[1].split(":")[1].trim();
                double volumen = Double.parseDouble(partes[2].split(":")[1].trim());
                double peso = Double.parseDouble(partes[3].split(":")[1].trim());
                lista.add(new Remolque(id, nombre, peso, volumen, "Almacen", "", 0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

  

    public OptimizacionPanel() {
        setLayout(new BorderLayout());

        JButton calcularButton = new JButton("Calcular Optimización");
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);

        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarOptimizacion();
            }
        });

        add(calcularButton, BorderLayout.NORTH);
        add(new JScrollPane(resultadoArea), BorderLayout.CENTER);
    }

   private void realizarOptimizacion() {
    ArrayList<Mercaderia> mercaderias = OptimizadorCarga.cargarMercaderias("mercaderias.txt");
    ArrayList<Remolque> remolques = OptimizadorCarga.cargarRemolques("remolques.txt");
    
    ArrayList<Asignacion> asignaciones = OptimizadorCarga.optimizarAsignacion(remolques, mercaderias);
    
    resultadoArea.setText("");  // Limpiar área de resultados

    // Agrupar mercaderías por remolque
    Map<Integer, ArrayList<Mercaderia>> asignacionesPorRemolque = new HashMap<>();
    for (Asignacion asignacion : asignaciones) {
        int remolqueId = asignacion.getRemolque().getId();
        asignacionesPorRemolque.putIfAbsent(remolqueId, new ArrayList<>());
        asignacionesPorRemolque.get(remolqueId).add(asignacion.getMercaderia());
    }

    double costoTotalCombinado = 0;
    for (Map.Entry<Integer, ArrayList<Mercaderia>> entry : asignacionesPorRemolque.entrySet()) {
        ArrayList<Mercaderia> mercaderiasRemolque = entry.getValue();

        ArrayList<String> destinos = new ArrayList<>();
        double pesoTotal = 0;
        for (Mercaderia m : mercaderiasRemolque) {
            pesoTotal += m.getPeso();
            if (!destinos.contains(m.getDestino())) {
                destinos.add(m.getDestino());
            }
        }

        int distanciaTotal = OptimizadorCarga.calcularDistanciaRuta(destinos);
        double consumoTotal = distanciaTotal * (0.3 + pesoTotal * 0.0001);
        double costoTotal = consumoTotal * 1.25;
        costoTotalCombinado += costoTotal;

        resultadoArea.append("Remolque #" + entry.getKey() + "\n");
        resultadoArea.append("Mercaderías transportadas:\n");
        for (Mercaderia m : mercaderiasRemolque) {
            resultadoArea.append("  - ID: " + m.getId() + ", Destino: " + m.getDestino() + ", Peso: " + m.getPeso() + "\n");
        }
        resultadoArea.append("Destinos visitados: " + destinos + "\n");
        resultadoArea.append("Peso Total: " + pesoTotal + " kg\n");
        resultadoArea.append("Distancia Total (ida/vuelta): " + distanciaTotal + " km\n");
        resultadoArea.append("Costo Total: $" + String.format("%.2f", costoTotal) + " USD\n\n");
    }

    resultadoArea.append("Costo Total Combinado: $" + String.format("%.2f", costoTotalCombinado) + " USD\n");
}



}
