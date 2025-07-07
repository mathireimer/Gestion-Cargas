// Archivo: Aplicacion.java
// Interfaz con tablas embellecidas

package optimizacion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import optimizacion.OptimizacionPanel;
import java.util.HashMap;


public class Aplicacion extends JFrame {
    private ArrayList<Remolque> listaRemolques = new ArrayList<>();
    private int idRemolque = 1;
    private int idMercaderia = 1;

    private JTabbedPane tabs;
    private JTable tablaRemolques, tablaCargas;
    private JTextField nombreRemolque, pesoRemolque, volumenRemolque;
    private JTextField pesoCarga, volumenCarga, distanciaCarga;
    private JComboBox<String> destinoCombo;

    public Aplicacion() {
        actualizarUltimoIdMercaderia();
        inicializarIdRemolqueDesdeArchivo();
        setTitle("Sistema de Optimización de Cargas");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        UIManager.put("TabbedPane.selected", new Color(200, 220, 255));
        UIManager.put("TabbedPane.contentAreaColor", new Color(230, 240, 255));
        UIManager.put("TabbedPane.background", new Color(180, 200, 255));
        UIManager.put("TabbedPane.foreground", Color.BLACK);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        tabs.addTab("Agregar Remolque", crearPanelConTransicion(crearPanelAgregarRemolque()));
        tabs.addTab("Agregar Carga", crearPanelConTransicion(crearPanelAgregarCarga()));
        tabs.addTab("Lista de Remolques", crearPanelConTransicion(crearPanelListaRemolques()));
        tabs.addTab("Lista de Cargas", crearPanelConTransicion(crearPanelListaCargas()));
tabs.addTab("Optimización", new OptimizacionPanel());

        add(tabs, BorderLayout.CENTER);
        cargarMercaderiasEnTabla();
        cargarRemolquesEnTabla();
    }

    private JPanel crearPanelConTransicion(JPanel panelOriginal) {
        JPanel wrapper = new JPanel(new BorderLayout());
        panelOriginal.setOpaque(false);
        wrapper.setOpaque(false);
        Timer timer = new Timer(5, null);
        final float[] alpha = {0};

        timer.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                alpha[0] += 0.05f;
                if (alpha[0] >= 1f) {
                    alpha[0] = 1f;
                    timer.stop();
                }
                panelOriginal.repaint();
            }
        });
        timer.start();

        wrapper.add(panelOriginal);
        return wrapper;
    }

    private JTable crearTabla(Object[][] data, String[] columns) {
        JTable tabla = new JTable(new DefaultTableModel(data, columns));
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(25);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(180, 200, 255));
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return tabla;
    }

    private JPanel crearPanelListaRemolques() {
        JPanel panel = new JPanel(new BorderLayout());
        tablaRemolques = crearTabla(new Object[][]{}, new String[]{"ID", "Nombre", "Capacidad Peso", "Capacidad Volumen"});
        panel.add(new JScrollPane(tablaRemolques), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelListaCargas() {
        JPanel panel = new JPanel(new BorderLayout());
        tablaCargas = crearTabla(new Object[][]{}, new String[]{"ID", "Peso", "Volumen", "Destino"});
        panel.add(new JScrollPane(tablaCargas), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelAgregarRemolque() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(180, 200, 255));

        JLabel titulo = new JLabel("Agregar Remolque");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setBounds(300, 20, 300, 30);
        panel.add(titulo);

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblPeso = new JLabel("Capacidad Peso:");
        JLabel lblVolumen = new JLabel("Capacidad Volumen:");

        nombreRemolque = new JTextField();
        pesoRemolque = new JTextField();
        volumenRemolque = new JTextField();

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarRemolqueYGuardarArchivo());

        lblNombre.setBounds(60, 100, 150, 25);
        nombreRemolque.setBounds(200, 100, 200, 25);

        lblPeso.setBounds(60, 140, 150, 25);
        pesoRemolque.setBounds(200, 140, 200, 25);

        lblVolumen.setBounds(60, 180, 150, 25);
        volumenRemolque.setBounds(200, 180, 200, 25);

        btnAgregar.setBounds(200, 230, 120, 30);

        panel.add(lblNombre); panel.add(nombreRemolque);
        panel.add(lblPeso); panel.add(pesoRemolque);
        panel.add(lblVolumen); panel.add(volumenRemolque);
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel crearPanelAgregarCarga() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 240, 255));

        JLabel titulo = new JLabel("Agregar Mercadería");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setBounds(290, 20, 300, 30);
        panel.add(titulo);

        JLabel lblPeso = new JLabel("Peso:");
        JLabel lblVolumen = new JLabel("Volumen:");
        JLabel lblDestino = new JLabel("Destino:");
        JLabel lblDistancia = new JLabel("Distancia:");

        pesoCarga = new JTextField();
        volumenCarga = new JTextField();
        distanciaCarga = new JTextField();
        destinoCombo = new JComboBox<>(new String[]{"Lambaré", "Ciudad del Este", "Encarnación", "Canindeyu"});
        destinoCombo.addActionListener(e -> actualizarDistancia());

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarCargaYGuardarArchivo());

        lblPeso.setBounds(60, 100, 150, 25);
        pesoCarga.setBounds(200, 100, 200, 25);

        lblVolumen.setBounds(60, 140, 150, 25);
        volumenCarga.setBounds(200, 140, 200, 25);

        lblDestino.setBounds(60, 180, 150, 25);
        destinoCombo.setBounds(200, 180, 200, 25);

        lblDistancia.setBounds(60, 220, 150, 25);
        distanciaCarga.setBounds(200, 220, 200, 25);

        btnAgregar.setBounds(200, 270, 120, 30);

        panel.add(lblPeso); panel.add(pesoCarga);
        panel.add(lblVolumen); panel.add(volumenCarga);
        panel.add(lblDestino); panel.add(destinoCombo);
        panel.add(lblDistancia); panel.add(distanciaCarga);
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel crearPanelOptimizar() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton btnOptimizar = new JButton("Optimizar Asignación");
        btnOptimizar.addActionListener(e -> {
            ArrayList<Mercaderia> mercaderias = OptimizadorCarga.cargarMercaderias("mercaderias.txt");
            ArrayList<Remolque> remolques = OptimizadorCarga.cargarRemolques("remolques.txt");
            ArrayList<Asignacion> asignaciones = OptimizadorCarga.optimizarAsignacion(remolques, mercaderias);
        });
        panel.add(btnOptimizar, BorderLayout.NORTH);
        return panel;
    }

    private void actualizarDistancia() {
        String destino = destinoCombo.getSelectedItem().toString();
        int distancia = switch (destino) {
            case "Lambaré" -> 12;
            case "Ciudad del Este" -> 327;
            case "Encarnación" -> 375;
            case "Canindeyu" -> 425;
            default -> 0;
        };
        distanciaCarga.setText(String.valueOf(distancia));
    }

    private void cargarMercaderiasEnTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tablaCargas.getModel();
            modelo.setRowCount(0);
            BufferedReader reader = new BufferedReader(new FileReader("mercaderias.txt"));
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                String idStr = partes[0].split(":")[1].trim();
                String peso = partes[1].split(":")[1].trim();
                String volumen = partes[2].split(":")[1].trim();
                String destino = partes[3].split(":")[1].trim();
                int distancia = Integer.parseInt(partes[4].split(":")[1].trim());
                modelo.addRow(new Object[]{idStr, peso, volumen, destino});
            }
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar mercaderías: " + e.getMessage());
        }
    }

    

    
    private void cargarRemolquesEnTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tablaRemolques.getModel();
            modelo.setRowCount(0);
            BufferedReader reader = new BufferedReader(new FileReader("remolques.txt"));
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                String id = partes[0].split(":")[1].trim();
                String nombre = partes[1].split(":")[1].trim();
                String volumen = partes[2].split(":")[1].trim();
                String peso = partes[3].split(":")[1].trim();
                modelo.addRow(new Object[]{id, nombre, peso, volumen});
            }
            reader.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar remolques: " + e.getMessage());
        }
    }

    private void agregarRemolqueYGuardarArchivo() {
        try {
            String nombre = nombreRemolque.getText();
            double capacidadPeso = Double.parseDouble(pesoRemolque.getText());
            double capacidadVolumen = Double.parseDouble(volumenRemolque.getText());
            Remolque nuevo = new Remolque(idRemolque++, nombre, capacidadPeso, capacidadVolumen, "Almacén", "", 0, 0);
            listaRemolques.add(nuevo);
            BufferedWriter writer = new BufferedWriter(new FileWriter("remolques.txt", true));
            writer.write("ID: " + nuevo.getId() + ", Nombre: " + nuevo.getNombre() + ", Volumen: " + nuevo.getCapacidadVolumen() + ", Peso: " + nuevo.getCapacidadPeso());
            writer.newLine();
            writer.close();
            cargarRemolquesEnTabla();
            JOptionPane.showMessageDialog(this, "Remolque agregado exitosamente.");
            nombreRemolque.setText(""); pesoRemolque.setText(""); volumenRemolque.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar remolque: " + e.getMessage());
        }
    }

    private void agregarCargaYGuardarArchivo() {
        try {
            double peso = Double.parseDouble(pesoCarga.getText());
            double volumen = Double.parseDouble(volumenCarga.getText());
            String destino = destinoCombo.getSelectedItem().toString();
            int distancia = Integer.parseInt(distanciaCarga.getText());

            BufferedWriter writer = new BufferedWriter(new FileWriter("mercaderias.txt", true));
            writer.write("ID: " + idMercaderia++ + ", Peso: " + peso + ", Volumen: " + volumen + ", Destino: " + destino + ", Distancia: " + distancia);
            writer.newLine();
            writer.close();

            JOptionPane.showMessageDialog(this, "Mercadería agregada exitosamente.");
            pesoCarga.setText(""); volumenCarga.setText(""); distanciaCarga.setText("");
            cargarMercaderiasEnTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar mercadería: " + e.getMessage());
        }
    }

    private void actualizarUltimoIdMercaderia() {
        try (BufferedReader reader = new BufferedReader(new FileReader("mercaderias.txt"))) {
            String linea;
            int maxId = 0;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("ID:")) {
                    String[] partes = linea.split(",");
                    String idStr = partes[0].split(":")[1].trim();
                    int id = Integer.parseInt(idStr);
                    if (id > maxId) maxId = id;
                }
            }
            idMercaderia = maxId + 1;
        } catch (IOException e) {
            idMercaderia = 1;
        }
    }

    private void inicializarIdRemolqueDesdeArchivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader("remolques.txt"))) {
            String linea;
            int ultimoId = 0;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("ID:")) {
                    String[] partes = linea.split(",");
                    String idStr = partes[0].split(":")[1].trim();
                    int id = Integer.parseInt(idStr);
                    if (id > ultimoId) ultimoId = id;
                }
            }
            idRemolque = ultimoId + 1;
        } catch (IOException e) {
            idRemolque = 1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Aplicacion().setVisible(true));
    }
}
