package optimizacion;

import com.formdev.flatlaf.FlatIntelliJLaf; // NUEVO IMPORT
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

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

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        tabs.addTab("Agregar Remolque", crearPanelAgregarRemolque());
        tabs.addTab("Agregar Carga", crearPanelAgregarCarga());
        tabs.addTab("Lista de Remolques", crearPanelListaRemolques());
        tabs.addTab("Lista de Cargas", crearPanelListaCargas());

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
        tablaCargas = crearTabla(new Object[][]{}, new String[]{"ID", "Peso", "Volumen", "Destino"});
        panel.add(new JScrollPane(tablaCargas), BorderLayout.CENTER);
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
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar FlatLaf: " + e);
        }
        SwingUtilities.invokeLater(() -> new Aplicacion().setVisible(true));
    }
}
