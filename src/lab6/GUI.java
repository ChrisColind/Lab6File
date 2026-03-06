package lab6;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GUI extends JFrame {

    // ── Componentes ───────────────────────────────────────────────────────
    private JTextPane         textPane;
    private JComboBox<String> cmbFuente;
    private JComboBox<String> cmbTamano;
    private JLabel            lblColorActual;
    private JPanel            panelColoresRecientes;

    // Tama;os disponibles 
    private static final String[] TAMANOS= {
        "8","9","10","11","12","14","16","18","20","24",
        "28","32","36","42","48","64","72","92","144","190","240","300"
    };

    //Ultimos colores
    private static final Color[] COLORES = {
        //guarda los ultimos colores guardados
    };

    public GUI() {
        setTitle("Editor de texto");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraGuardado(),BorderLayout.NORTH);
        add(crearPanelCentral(),BorderLayout.CENTER);
        add(crearPanelBotones(),BorderLayout.SOUTH);
    }

    // Guardado
    private JPanel crearBarraGuardado() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 3));
        barra.setBackground(new Color(236, 233, 216));
        barra.setBorder(new MatteBorder(0, 0, 1, 0, new Color(180, 180, 180)));
        JButton btnGuardar = new JButton("💾");
        barra.add(btnGuardar);  

        return barra;
    }

    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(crearPanelOpciones(), BorderLayout.WEST);
        panel.add(crearEditor(),        BorderLayout.CENTER);
        panel.add(crearPanelColores(),  BorderLayout.EAST);

        return panel;
    }

    //Opciones: seleccion de tama;o, fuente, color
    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(210, 0));
        panel.setBackground(new Color(236, 233, 216));
        panel.setBorder(new MatteBorder(0, 0, 0, 1, new Color(180, 180, 180)));

        // JLABEL Fuente
        JLabel lblFuente = new JLabel("Fuente");
        lblFuente.setBounds(8, 8, 50, 22);
        panel.add(lblFuente);

        String[] fuentes = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getAvailableFontFamilyNames();
        cmbFuente = new JComboBox<>(fuentes);
        cmbFuente.setSelectedItem("Arial");
        cmbFuente.setBounds(62, 6, 138, 26);
        cmbFuente.setBackground(Color.WHITE);
        panel.add(cmbFuente);

        // JLABEL Tamaño
        JLabel lblTamano = new JLabel("Tamaño");
        lblTamano.setBounds(8, 40, 52, 22);
        panel.add(lblTamano);

        cmbTamano = new JComboBox<>(TAMANOS);
        cmbTamano.setSelectedItem("12");
        cmbTamano.setBounds(62, 38, 138, 26);
        cmbTamano.setBackground(Color.WHITE);
        panel.add(cmbTamano);

        // JLABEL Color
        JLabel lblColor = new JLabel("Color");
        lblColor.setBounds(8, 72, 40, 22);
        panel.add(lblColor);

        lblColorActual = new JLabel();
        lblColorActual.setOpaque(true);
        lblColorActual.setBackground(Color.BLACK);
        lblColorActual.setBorder(new LineBorder(Color.GRAY, 1));
        lblColorActual.setBounds(62, 72, 24, 22);
        panel.add(lblColorActual);

        JButton btnColor = new JButton("...");
        btnColor.setBounds(90, 70, 40, 26);
        btnColor.setFont(new Font("SansSerif", Font.PLAIN, 11));
        panel.add(btnColor);

        return panel;
    }

    // JText, centro
    private JScrollPane crearEditor() {
        textPane = new JTextPane();
        textPane.setBackground(Color.WHITE);
        textPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        textPane.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(new LineBorder(new Color(180, 180, 180), 1));
        return scroll;
    }

    // Muestra ultimos colores: Panel derecho  
    private JPanel crearPanelColores() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(100, 0));  // panel más angosto
        panel.setBackground(new Color(236, 233, 216));
        panel.setBorder(new MatteBorder(0, 1, 0, 0, new Color(180, 180, 180)));

        JLabel lbl = new JLabel("Colores utilizados:");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setBorder(new EmptyBorder(4, 4, 2, 4));
        panel.add(lbl, BorderLayout.NORTH);

        panelColoresRecientes = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        panelColoresRecientes.setBackground(new Color(236, 233, 216));
        panelColoresRecientes.setBorder(new EmptyBorder(2, 4, 2, 4));

        for (Color c : COLORES) {
            JLabel celda = new JLabel();
            celda.setOpaque(true);
            celda.setBackground(c);
            celda.setBorder(new LineBorder(new Color(160, 160, 160), 1));
            celda.setPreferredSize(new Dimension(14, 14));  // cuadrado exacto
            celda.setMinimumSize(new Dimension(14, 14));
            celda.setMaximumSize(new Dimension(14, 14));
            panelColoresRecientes.add(celda);
        }

        panel.add(panelColoresRecientes, BorderLayout.CENTER);
        return panel;
    }

    // PANEL INFERIOR: Aceptar / Cancelar
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        panel.setBackground(new Color(236, 233, 216));
        panel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(180, 180, 180)));

        JButton btnAceptar  = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.setPreferredSize(new Dimension(90, 26));
        btnCancelar.setPreferredSize(new Dimension(90, 26));

        panel.add(btnAceptar);
        panel.add(btnCancelar);
        return panel;
    }

    private JButton crearBoton(String icono) {
        JButton btn = new JButton(icono);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(28, 24));
        btn.setMargin(new Insets(1, 2, 1, 2));
        btn.setFocusPainted(false);
        return btn;
    }

    //main por mientras
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new GUI().setVisible(true);
        });
    }
}