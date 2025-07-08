package optimizacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        ArrayList<Asignacion> asignaciones = OptimizadorCarga.optimizarAsignacion(remolques, mercaderias);
        String resumen = OptimizadorCarga.generarResumenAsignaciones(asignaciones);
        resultadoArea.setText(resumen);
    }
}
