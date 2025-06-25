/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package optimizacion;

/**
 *
 * @author mathiasreimer
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private ArrayList<Remolque> remolques = new ArrayList<>();
    private ArrayList<Mercaderia> mercaderias = new ArrayList<>();
    private int remolqueId = 1;
    private int mercaderiaId = 1;

    public MainFrame() {
        setTitle("Optimizador de Carga de Remolques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // Panel de Remolques
        JPanel remolquesPanel = new JPanel(new GridLayout(10, 2));
        JTextField tfPeso = new JTextField();
        JTextField tfVolumen = new JTextField();
        JTextField tfUbicacion = new JTextField();
        JTextField tfDestino = new JTextField();
        JTextField tfDistancia = new JTextField();
        JTextField tfConsumo = new JTextField();
        JButton btnAgregar = new JButton("Agregar Remolque");

        remolquesPanel.add(new JLabel("Capacidad Peso (kg):"));
        remolquesPanel.add(tfPeso);
        remolquesPanel.add(new JLabel("Capacidad Volumen (m³):"));
        remolquesPanel.add(tfVolumen);
        remolquesPanel.add(new JLabel("Ubicación Actual:"));
        remolquesPanel.add(tfUbicacion);
        remolquesPanel.add(new JLabel("Destino:"));
        remolquesPanel.add(tfDestino);
        remolquesPanel.add(new JLabel("Distancia (km):"));
        remolquesPanel.add(tfDistancia);
        remolquesPanel.add(new JLabel("Consumo por kg/km:"));
        remolquesPanel.add(tfConsumo);
        remolquesPanel.add(btnAgregar);

        btnAgregar.addActionListener(e -> {
            try {
                double peso = Double.parseDouble(tfPeso.getText());
                double vol = Double.parseDouble(tfVolumen.getText());
                String ubi = tfUbicacion.getText();
                String des = tfDestino.getText();
                double dist = Double.parseDouble(tfDistancia.getText());
                double cons = Double.parseDouble(tfConsumo.getText());

                int id = remolqueId++;
                Remolque nuevo = new Remolque(id, "Remolque" + id, peso, vol, ubi, des, dist, cons);
                remolques.add(nuevo);
                JOptionPane.showMessageDialog(this, "Remolque agregado exitosamente");

                tfPeso.setText(""); tfVolumen.setText(""); tfUbicacion.setText(""); tfDestino.setText(""); tfDistancia.setText(""); tfConsumo.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al ingresar datos: " + ex.getMessage());
            }
        });

        // Panel de Mercaderías
        JPanel mercaderiasPanel = new JPanel(new GridLayout(7, 2));
        JTextField tfMPeso = new JTextField();
        JTextField tfMVolumen = new JTextField();
        JTextField tfMDestino = new JTextField();
        JButton btnMAgregar = new JButton("Agregar Mercadería");

        mercaderiasPanel.add(new JLabel("Peso (kg):"));
        mercaderiasPanel.add(tfMPeso);
        mercaderiasPanel.add(new JLabel("Volumen (m³):"));
        mercaderiasPanel.add(tfMVolumen);
        mercaderiasPanel.add(new JLabel("Destino:"));
        mercaderiasPanel.add(tfMDestino);
        mercaderiasPanel.add(btnMAgregar);

        btnMAgregar.addActionListener(e -> {
            try {
                double peso = Double.parseDouble(tfMPeso.getText());
                double volumen = Double.parseDouble(tfMVolumen.getText());
                String destino = tfMDestino.getText();

                Mercaderia nueva = new Mercaderia(mercaderiaId++, peso, volumen, destino);
                mercaderias.add(nueva);
                JOptionPane.showMessageDialog(this, "Mercadería agregada exitosamente");

                tfMPeso.setText("");
                tfMVolumen.setText("");
                tfMDestino.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al ingresar datos: " + ex.getMessage());
            }
        });

        // Panel de Optimización
        JPanel optimizacionPanel = new JPanel(new BorderLayout());
        JTextArea resultadoArea = new JTextArea();
        JButton btnOptimizar = new JButton("Optimizar Carga");

        optimizacionPanel.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);
        optimizacionPanel.add(btnOptimizar, BorderLayout.SOUTH);

        btnOptimizar.addActionListener(e -> {
            resultadoArea.setText("");
            ArrayList<Asignacion> asignaciones = OptimizadorCarga.optimizar(remolques, mercaderias);
            for (Asignacion a : asignaciones) {
                resultadoArea.append("Mercadería " + a.getMercaderia().getId() + " asignada al Remolque " + a.getRemolque().getId()
                        + " | Costo: " + String.format("%.2f", a.getCostoTransporte()) + "\n");
            }
        });

        tabs.add("Remolques", new JScrollPane(remolquesPanel));
        tabs.add("Mercaderías", new JScrollPane(mercaderiasPanel));
        tabs.add("Optimización", optimizacionPanel);

        add(tabs);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
