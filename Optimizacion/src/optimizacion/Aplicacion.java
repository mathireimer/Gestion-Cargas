package optimizacion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aplicación con pestañas que permite registrar remolques y cargas,
 * visualizar las listas y ejecutar la optimización.
 */
public class Aplicacion extends JFrame {
    private final ArrayList<Remolque> remolques = new ArrayList<>();
    private final ArrayList<Mercaderia> mercaderias = new ArrayList<>();
    private final Map<String, Double> distancias = new LinkedHashMap<>();
    private int remolqueId = 1;
    private int mercaderiaId = 1;

    private JTextArea areaRemolques;
    private JTextArea areaMercaderias;

    public Aplicacion() {
        setTitle("Optimizador de Carga de Remolques");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cargarDistancias();

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Agregar Remolque", crearPanelRemolques());
        tabs.add("Agregar Carga", crearPanelMercaderias());
        tabs.add("Lista de Remolques", crearPanelListaRemolques());
        tabs.add("Lista de Cargas", crearPanelListaMercaderias());
        tabs.add("Optimización", crearPanelOptimizacion());

        add(tabs);
        setVisible(true);
    }

    private JPanel crearPanelRemolques() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField tfNombre = new JTextField();
        JTextField tfPeso = new JTextField();
        JTextField tfVolumen = new JTextField();
        JComboBox<String> cbDestino = new JComboBox<>(distancias.keySet().toArray(new String[0]));
        JLabel lblDistancia = new JLabel("0.0");
        JButton btnAgregar = new JButton("Agregar Remolque");

        panel.add(new JLabel("Nombre:"));
        panel.add(tfNombre);
        panel.add(new JLabel("Capacidad Peso (kg):"));
        panel.add(tfPeso);
        panel.add(new JLabel("Capacidad Volumen (m³):"));
        panel.add(tfVolumen);
        panel.add(new JLabel("Destino:"));
        panel.add(cbDestino);
        panel.add(new JLabel("Distancia (km):"));
        panel.add(lblDistancia);
        panel.add(new JLabel());
        panel.add(btnAgregar);

        cbDestino.addActionListener(e -> {
            String destino = (String) cbDestino.getSelectedItem();
            double d = distancias.getOrDefault(destino, 0.0);
            lblDistancia.setText(String.valueOf(d));
        });

        btnAgregar.addActionListener(e -> {
            try {
                double peso = Double.parseDouble(tfPeso.getText());
                double vol = Double.parseDouble(tfVolumen.getText());
                String nombre = tfNombre.getText();
                String destino = (String) cbDestino.getSelectedItem();
                double dist = distancias.getOrDefault(destino, 0.0);

                Remolque r = new Remolque(remolqueId++, nombre, peso, vol, "", destino, dist, 1.0);
                remolques.add(r);
                JOptionPane.showMessageDialog(this, "Remolque agregado exitosamente");

                tfNombre.setText("");
                tfPeso.setText("");
                tfVolumen.setText("");
                cbDestino.setSelectedIndex(0);
                lblDistancia.setText("0.0");
                actualizarListas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al ingresar datos: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel crearPanelMercaderias() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JTextField tfPeso = new JTextField();
        JTextField tfVolumen = new JTextField();
        JTextField tfDestino = new JTextField();
        JButton btnAgregar = new JButton("Agregar Mercadería");

        panel.add(new JLabel("Peso (kg):"));
        panel.add(tfPeso);
        panel.add(new JLabel("Volumen (m³):"));
        panel.add(tfVolumen);
        panel.add(new JLabel("Destino:"));
        panel.add(tfDestino);
        panel.add(new JLabel());
        panel.add(btnAgregar);

        btnAgregar.addActionListener(e -> {
            try {
                double peso = Double.parseDouble(tfPeso.getText());
                double volumen = Double.parseDouble(tfVolumen.getText());
                String destino = tfDestino.getText();

                Mercaderia m = new Mercaderia(mercaderiaId++, peso, volumen, destino);
                mercaderias.add(m);
                JOptionPane.showMessageDialog(this, "Mercadería agregada exitosamente");

                tfPeso.setText("");
                tfVolumen.setText("");
                tfDestino.setText("");
                actualizarListas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al ingresar datos: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel crearPanelListaRemolques() {
        areaRemolques = new JTextArea();
        areaRemolques.setEditable(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(areaRemolques), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelListaMercaderias() {
        areaMercaderias = new JTextArea();
        areaMercaderias.setEditable(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(areaMercaderias), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelOptimizacion() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        JButton btnOptimizar = new JButton("Optimizar Carga");
        panel.add(new JScrollPane(resultadoArea), BorderLayout.CENTER);
        panel.add(btnOptimizar, BorderLayout.SOUTH);

        btnOptimizar.addActionListener(e -> {
            resultadoArea.setText("");
            ArrayList<Asignacion> asignaciones = OptimizadorCarga.optimizar(remolques, mercaderias);
            for (Asignacion a : asignaciones) {
                resultadoArea.append("Mercadería " + a.getMercaderia().getId() +
                        " asignada al Remolque " + a.getRemolque().getId() +
                        " | Costo: " + String.format("%.2f", a.getCostoTransporte()) + "\n");
            }
        });
        return panel;
    }

    private void cargarDistancias() {
        distancias.put("Montevideo", 0.0);
        distancias.put("Colonia", 177.0);
        distancias.put("Punta del Este", 140.0);
        distancias.put("Salto", 496.0);
        distancias.put("Paysandu", 378.0);
    }

    private void actualizarListas() {
        if (areaRemolques != null) {
            areaRemolques.setText("");
            for (Remolque r : remolques) {
                areaRemolques.append(
                        "Remolque " + r.getId() + " - " + r.getNombre() +
                        " destino: " + r.getDestino() +
                        " dist: " + r.getDistanciaDestino() + "km" +
                        " peso disp: " + r.getPesoDisponible() +
                        " vol disp: " + r.getVolumenDisponible() + "\n");
            }
        }
        if (areaMercaderias != null) {
            areaMercaderias.setText("");
            for (Mercaderia m : mercaderias) {
                areaMercaderias.append("Mercadería " + m.getId() +
                        " destino: " + m.getDestino() +
                        " peso: " + m.getPeso() +
                        " vol: " + m.getVolumen() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Aplicacion::new);
    }
}

