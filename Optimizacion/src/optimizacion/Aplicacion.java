package optimizacion;

import com.formdev.flatlaf.FlatIntelliJLaf; // Look & Feel

// Swing y componentes gráficos
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.Set;
import java.util.HashSet;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Component;

// AWT: Layouts, fuentes, colores, márgenes, etc.
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Graphics;
import java.util.List;

// Eventos
import java.awt.event.ActionEvent;

// Utilidades generales
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Archivos
import java.io.*;

// Constantes de ventana
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Aplicacion extends JFrame {

    private ArrayList<Remolque> listaRemolques = new ArrayList<>();
    private List<Mercaderia> listaMercaderias;  // << Atributo de la clase

    private int idRemolque = 1;
    private int idMercaderia = 1;

    private JTabbedPane tabs;
    private JTable tablaRemolques, tablaCargas;
    private JTextField nombreRemolque, pesoRemolque, volumenRemolque;
    private JTextField pesoCarga, volumenCarga, distanciaCarga;
    private JComboBox<String> destinoCombo;

    public Aplicacion() {
        try {
    setIconImage(new ImageIcon(getClass().getResource("/optimizacion/favicon.ico")).getImage());
} catch (Exception e) {
    System.err.println("No se pudo cargar el icono: " + e.getMessage());
}

        actualizarUltimoIdMercaderia();
        inicializarIdRemolqueDesdeArchivo();
        setTitle("Sistema de Optimización de Cargas");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        tabs.addTab("Agregar Remolque", crearPanelAgregarRemolque());
        tabs.addTab("Agregar Carga", crearPanelAgregarCarga());
        tabs.addTab("Lista de Remolques", crearPanelListaRemolques());
        tabs.addTab("Lista de Cargas", crearPanelListaCargas());
        tabs.addTab("Optimización", crearPanelOptimizar());

        add(tabs, BorderLayout.CENTER);
        cargarMercaderiasEnTabla();
        cargarRemolquesEnTabla();
    }

    private JTable crearTabla(Object[][] data, String[] columns) {
        JTable tabla = new JTable(new DefaultTableModel(data, columns));
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(new Color(200, 220, 255));
        tabla.getTableHeader().setForeground(new Color(30, 30, 30));
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setShowGrid(true);
        tabla.setSelectionBackground(new Color(180, 200, 240));
        tabla.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return tabla;
    }

    private JPanel crearPanelListaRemolques() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));

        JLabel titulo = new JLabel("Lista de Remolques");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titulo.setForeground(new Color(33, 37, 41));

        tablaRemolques = crearTabla(new Object[][]{}, new String[]{"ID", "Nombre", "Capacidad Peso", "Capacidad Volumen"});

        JScrollPane scrollPane = new JScrollPane(tablaRemolques);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelListaCargas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 248, 255));

        JLabel titulo = new JLabel("Lista de Cargas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titulo.setForeground(new Color(33, 37, 41));

        tablaCargas = crearTabla(new Object[][]{}, new String[]{"ID", "Peso", "Volumen", "Destino"});

        JScrollPane scrollPane = new JScrollPane(tablaCargas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelAgregarRemolque() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Agregar Remolque");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(33, 37, 41));

        JLabel lblNombre = new JLabel("Nombre del Remolque:");
        JLabel lblPeso = new JLabel("Capacidad Máxima (kg):");
        JLabel lblVolumen = new JLabel("Capacidad Volumen (m³):");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        lblNombre.setFont(labelFont);
        lblPeso.setFont(labelFont);
        lblVolumen.setFont(labelFont);

        nombreRemolque = new JTextField(20);
        pesoRemolque = new JTextField(20);
        volumenRemolque = new JTextField(20);

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        nombreRemolque.setFont(fieldFont);
        pesoRemolque.setFont(fieldFont);
        volumenRemolque.setFont(fieldFont);

        JButton btnAgregar = new JButton("Guardar Remolque") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 149, 237)); // Fondo del botón
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(70, 130, 180)); // Borde del botón
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };

        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setOpaque(false);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setPreferredSize(new Dimension(260, 45));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregar.addActionListener(e -> agregarRemolqueYGuardarArchivo());

        // Posicionar todo centrado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        panel.add(lblNombre, gbc);
        gbc.gridx = 1;
        panel.add(nombreRemolque, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblPeso, gbc);
        gbc.gridx = 1;
        panel.add(pesoRemolque, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblVolumen, gbc);
        gbc.gridx = 1;
        panel.add(volumenRemolque, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private JPanel crearPanelAgregarCarga() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Agregar Mercadería");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(33, 37, 41));

        JLabel lblPeso = new JLabel("Peso (kg):");
        JLabel lblVolumen = new JLabel("Volumen (m³):");
        JLabel lblDestino = new JLabel("Destino:");
        JLabel lblDistancia = new JLabel("Distancia (km):");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        lblPeso.setFont(labelFont);
        lblVolumen.setFont(labelFont);
        lblDestino.setFont(labelFont);
        lblDistancia.setFont(labelFont);

        pesoCarga = new JTextField(20);
        volumenCarga = new JTextField(20);
        distanciaCarga = new JTextField(20);
        distanciaCarga.setEditable(false); // para que no se modifique manualmente

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        pesoCarga.setFont(fieldFont);
        volumenCarga.setFont(fieldFont);
        distanciaCarga.setFont(fieldFont);

        destinoCombo = new JComboBox<>(new String[]{"Lambaré", "Ciudad del Este", "Encarnación", "Canindeyu"});
        destinoCombo.setFont(fieldFont);
        destinoCombo.addActionListener(e -> actualizarDistancia());

        JButton btnAgregar = new JButton("Guardar Mercadería") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 149, 237));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(70, 130, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };

        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setContentAreaFilled(false);
        btnAgregar.setOpaque(false);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setPreferredSize(new Dimension(260, 45));
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAgregar.addActionListener(e -> agregarCargaYGuardarArchivo());

        // Posicionar elementos centrados
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy++;
        panel.add(lblPeso, gbc);
        gbc.gridx = 1;
        panel.add(pesoCarga, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblVolumen, gbc);
        gbc.gridx = 1;
        panel.add(volumenCarga, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblDestino, gbc);
        gbc.gridx = 1;
        panel.add(destinoCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblDistancia, gbc);
        gbc.gridx = 1;
        panel.add(distanciaCarga, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAgregar, gbc);

        return panel;
    }

    private JPanel crearPanelOptimizar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;


        JLabel titulo = new JLabel("Optimización de Asignación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(33, 37, 41));

        JPanel resultadoPanel = new JPanel();
        resultadoPanel.setLayout(new BoxLayout(resultadoPanel, BoxLayout.Y_AXIS));
        resultadoPanel.setBackground(new Color(245, 248, 255));

        JScrollPane scroll = new JScrollPane(resultadoPanel);
        scroll.setPreferredSize(new Dimension(700, 300));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 240)));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JButton btnOptimizar = new JButton("Iniciar Optimización") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(100, 149, 237));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(70, 130, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };

        btnOptimizar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnOptimizar.setForeground(Color.WHITE);
        btnOptimizar.setContentAreaFilled(false);
        btnOptimizar.setOpaque(false);
        btnOptimizar.setFocusPainted(false);
        btnOptimizar.setPreferredSize(new Dimension(260, 45));
        btnOptimizar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Acción del botón
        btnOptimizar.addActionListener(e -> {
            resultadoPanel.removeAll();

            List<Mercaderia> mercaderias = OptimizadorCarga.cargarMercaderias("mercaderias.txt");
            List<Remolque> remolques = OptimizadorCarga.cargarRemolques("remolques.txt");
            List<Asignacion> mejoresAsignaciones = OptimizadorCarga.optimizar(mercaderias, remolques);

            Map<Integer, ArrayList<Mercaderia>> asignacionesPorRemolque = new HashMap<>();
            for (Asignacion asignacion : mejoresAsignaciones) {
                int remolqueId = asignacion.getRemolque().getId();
                asignacionesPorRemolque.putIfAbsent(remolqueId, new ArrayList<>());

                // AQUÍ PEGÁS ESTO:
                for (Mercaderia m : asignacion.getMercaderias()) {
                    asignacionesPorRemolque.get(remolqueId).add(m);
                }
            }

            double costoFinalTotal = 0;
            for (Map.Entry<Integer, ArrayList<Mercaderia>> entry : asignacionesPorRemolque.entrySet()) {
                ArrayList<Mercaderia> mercaderiasRemolque = entry.getValue();
                ArrayList<String> destinos = new ArrayList<>();
                ArrayList<Integer> idsCargas = new ArrayList<>();
                double pesoTotal = 0;
                double volumenTotal = 0;

                for (Mercaderia m : mercaderiasRemolque) {
                    pesoTotal += m.getPeso();
                    volumenTotal += m.getVolumen();
                    idsCargas.add(m.getId());
                    if (!destinos.contains(m.getDestino())) {
                        destinos.add(m.getDestino());
                    }
                }

                int distanciaTotal = OptimizadorCarga.calcularDistanciaRuta(destinos);
                double consumo = distanciaTotal * (0.3 + pesoTotal * 0.0001);
                double costoTotal = consumo * 1.25;
                costoFinalTotal += costoTotal;

                // BUSCAR nombre del remolque
                String nombreRemolque = "";
                for (Remolque r : remolques) {
                    if (r.getId() == entry.getKey()) {
                        nombreRemolque = r.getNombre();
                        break;
                    }
                }

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 220, 250), 1, true),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel remolqueLabel = new JLabel("Remolque #" + entry.getKey() + " - " + nombreRemolque);
                JLabel destinosLabel = new JLabel("Destinos: " + destinos);
                JLabel cargasLabel = new JLabel("Cargas IDs: " + idsCargas);
                JLabel pesoLabel = new JLabel("Peso Total: " + pesoTotal + " kg");
                JLabel volumenLabel = new JLabel("Volumen Utilizado: " + volumenTotal + " m³");
                JLabel distanciaLabel = new JLabel("Distancia Total: " + distanciaTotal + " km");
                JLabel costoLabel = new JLabel("Costo Total: $" + String.format("%.2f", costoTotal) + " USD");

                Font font = new Font("Segoe UI", Font.PLAIN, 16);
                remolqueLabel.setFont(font);
                destinosLabel.setFont(font);
                cargasLabel.setFont(font);
                pesoLabel.setFont(font);
                volumenLabel.setFont(font);
                distanciaLabel.setFont(font);
                costoLabel.setFont(font);

                card.add(remolqueLabel);
                card.add(destinosLabel);
                card.add(cargasLabel);
                card.add(pesoLabel);
                card.add(volumenLabel);
                card.add(distanciaLabel);
                card.add(costoLabel);

                resultadoPanel.add(Box.createVerticalStrut(10));
                resultadoPanel.add(card);
            }

            JLabel totalLabel = new JLabel("Costo Final Total: $" + String.format("%.2f", costoFinalTotal) + " USD");
            totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            resultadoPanel.add(Box.createVerticalStrut(20));
            resultadoPanel.add(totalLabel);

            resultadoPanel.revalidate();
            resultadoPanel.repaint();

        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(btnOptimizar, gbc);

        gbc.gridy++;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(scroll, gbc);

        return panel;
    }

    private void actualizarDistancia() {
        String destino = destinoCombo.getSelectedItem().toString();
        int distancia = switch (destino) {
            case "Lambaré" ->
                12;
            case "Ciudad del Este" ->
                327;
            case "Encarnación" ->
                375;
            case "Canindeyu" ->
                425;
            default ->
                0;
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
            nombreRemolque.setText("");
            pesoRemolque.setText("");
            volumenRemolque.setText("");
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
            pesoCarga.setText("");
            volumenCarga.setText("");
            distanciaCarga.setText("");
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
                    if (id > maxId) {
                        maxId = id;
                    }
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
                    if (id > ultimoId) {
                        ultimoId = id;
                    }
                }
            }
            idRemolque = ultimoId + 1;
        } catch (IOException e) {
            idRemolque = 1;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLaf: " + e);
        }
        SwingUtilities.invokeLater(() -> new Aplicacion().setVisible(true));


    }
}
