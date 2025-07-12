package optimizacion;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.List;

public class OptimizacionPanel extends JPanel {

    private JTextArea resultadoArea;

    public OptimizacionPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 255));

        JLabel titulo = new JLabel("Optimización de Asignación", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JButton calcularButton = new JButton("Iniciar Optimización");
        calcularButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        calcularButton.setBackground(new Color(100, 149, 237));
        calcularButton.setForeground(Color.WHITE);
        calcularButton.setFocusPainted(false);
        calcularButton.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        calcularButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calcularButton.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true));

        calcularButton.addActionListener((ActionEvent e) -> realizarOptimizacion());

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        resultadoArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titulo, BorderLayout.NORTH);
        topPanel.add(calcularButton, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void realizarOptimizacion() {
    ArrayList<Mercaderia> mercaderias = OptimizadorCarga.cargarMercaderias("mercaderias.txt");
    ArrayList<Remolque> remolques = OptimizadorCarga.cargarRemolques("remolques.txt");

    List<Asignacion> asignaciones = OptimizadorCarga.optimizar(mercaderias, remolques);
    mostrarAsignaciones(asignaciones);
    }

    public void mostrarAsignaciones(List<Asignacion> asignaciones) {
        StringBuilder resultado = new StringBuilder();
        int opcion = 1;
        double costoTotalGeneral = 0;

        resultado.append("--- ASIGNACIÓN ÓPTIMA ---\n");
        for (Asignacion asignacion : asignaciones) {
            Remolque remolque = asignacion.getRemolque();
            List<Mercaderia> mercaderias = asignacion.getMercaderias();

            double pesoTotal = mercaderias.stream().mapToDouble(Mercaderia::getPeso).sum();
            double volumenTotal = mercaderias.stream().mapToDouble(Mercaderia::getVolumen).sum();
            List<String> destinos = mercaderias.stream()
                    .map(Mercaderia::getDestino)
                    .distinct()
                    .collect(Collectors.toList());

            double[] distanciaCosto = OptimizadorCarga.calcularCosto(pesoTotal, destinos);
            double distancia = distanciaCosto[0];
            double costo = distanciaCosto[1];

            resultado.append(String.format("Remolque #%d\n", remolque.getId()));
            resultado.append(String.format("Mercaderías transportadas: %s\n",
                    mercaderias.stream().map(m -> String.valueOf(m.getId())).collect(Collectors.toList())));
            resultado.append(String.format("Destinos: %s\n", destinos));
            resultado.append(String.format("Peso Total: %.2f kg\n", pesoTotal));
            resultado.append(String.format("Volumen Total: %.2f m3\n", volumenTotal));
            resultado.append(String.format("Distancia Total (ida y vuelta): %.2f km\n", distancia));
            resultado.append(String.format("Costo Total: $%.2f USD\n", costo));
            resultado.append("\n");

            costoTotalGeneral += costo;
        }
        resultado.append(String.format("COSTO TOTAL COMBINADO: $%.2f USD\n", costoTotalGeneral));

resultadoArea.setText(resultado.toString());
    }

}
