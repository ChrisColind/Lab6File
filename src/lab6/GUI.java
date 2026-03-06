package lab6;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.StyleConstants;

public class GUI extends JFrame {

    //Componentes
    private JTextPane textPane;
    private JComboBox<String> cmbFuente;
    private JComboBox<String> Tamaño;
    private JLabel lblColorActual;
    private JPanel panelColoresRecientes;

    //botones
    JButton btnAbrir = new JButton("📂"); // NUEVO
    JButton btnGuardar = new JButton("💾");
    JButton btnColor = new JButton("...");
    JToggleButton btnNegrita;
    JToggleButton btnCursiva;
    JToggleButton btnSubrayado;
    JButton btnTabla = new JButton("Tabla");

    //GESTORES (NUEVO)
    private FormatoTexto formato;
    private TableManager tablas;
    private FileManager fileManager;

    private static final String[] TAMANOS = {
        "8","9","10","11","12","14","16","18","20","24",
        "28","32","36","42","48","64","72","92","144","190","240","300"
    };

    private boolean yaGuardo = true;

    private Color[] UltimosColores = {};
    private Color colorActual = Color.BLACK;

    public GUI() {

        setTitle("Editor de texto");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraHerramientas(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);

        //INICIALIZAR LOGICA (NUEVO)
        formato = new FormatoTexto(textPane);
        tablas = new TableManager(textPane);
        fileManager = new FileManager(textPane, tablas);

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { yaGuardo = false; }
            public void removeUpdate(DocumentEvent e) { yaGuardo = false; }
            public void changedUpdate(DocumentEvent e) { yaGuardo = false; }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (yaGuardo) {
                    dispose();
                } else {
                    int respuesta = JOptionPane.showConfirmDialog(
                            GUI.this,
                            "Tienes cambios sin guardar. ¿Deseas salir de todas formas?",
                            "Advertencia",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (respuesta == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                }
            }
        });

        //EVENTOS ==========================

        btnAbrir.addActionListener(e -> {
            String ruta = fileManager.elegirRutaAbrir();
            if (ruta != null) {
                fileManager.abrirArchivo(ruta);
                yaGuardo = true;
            }
        });

        btnGuardar.addActionListener(e -> {
            String ruta = fileManager.elegirRutaGuardar();
            if (ruta != null) {
                fileManager.guardarArchivo(ruta);
                yaGuardo = true;
            }
        });

        btnNegrita.addActionListener(e ->
                formato.AplicarNegrita(btnNegrita.isSelected()));

        btnCursiva.addActionListener(e ->
                formato.AplicarCursiva(btnCursiva.isSelected()));

        btnSubrayado.addActionListener(e ->
                formato.AplicarSubrayado(btnSubrayado.isSelected()));

        btnTabla.addActionListener(e ->
                tablas.mostrarDialogoCrearTabla(this));

        btnColor.addActionListener(e -> {

            Color elegido = JColorChooser.showDialog(this, "Elegir color", colorActual);

            if (elegido != null) {
                colorActual = elegido;
                lblColorActual.setBackground(elegido);
                formato.AplicarColorFuente(elegido);
                agregarColorReciente(elegido);
            }

            textPane.requestFocus();
        });

        cmbFuente.addActionListener(e -> {

            String fuente = (String) cmbFuente.getSelectedItem();

            if (fuente != null)
                formato.AplicarFuente(fuente);

        });

        Tamaño.addActionListener(e -> {

            try {

                int tamano = Integer.parseInt((String) Tamaño.getSelectedItem());
                formato.AplicarTamano(tamano);

            } catch (Exception ignored) {}

        });
    }

    private JPanel crearBarraHerramientas() {

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 3));
        barra.setBackground(new Color(236,233,216));
        barra.setBorder(new MatteBorder(0,0,1,0,new Color(180,180,180)));

        btnAbrir.setPreferredSize(new Dimension(50,24));
        barra.add(btnAbrir);

        btnGuardar.setPreferredSize(new Dimension(50,24));
        barra.add(btnGuardar);

        btnSubrayado = new JToggleButton("U");
        btnSubrayado.setPreferredSize(new Dimension(50,24));
        barra.add(btnSubrayado);

        btnCursiva = new JToggleButton("I");
        btnCursiva.setFont(new Font("SansSerif", Font.ITALIC, 13));
        btnCursiva.setPreferredSize(new Dimension(50,24));
        barra.add(btnCursiva);

        btnNegrita = new JToggleButton("B");
        btnNegrita.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNegrita.setPreferredSize(new Dimension(50,24));
        barra.add(btnNegrita);

        btnTabla.setPreferredSize(new Dimension(70,24));
        barra.add(btnTabla);

        return barra;
    }

    private JPanel crearPanelCentral() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(crearPanelOpciones(), BorderLayout.WEST);
        panel.add(crearEditor(), BorderLayout.CENTER);
        panel.add(crearPanelColores(), BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelOpciones() {

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(210,0));
        panel.setBackground(new Color(236,233,216));

        JLabel lblFuente = new JLabel("Fuente");
        lblFuente.setBounds(8,8,50,22);
        panel.add(lblFuente);

        String[] fuentes = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        cmbFuente = new JComboBox<>(fuentes);
        cmbFuente.setSelectedItem("Arial");
        cmbFuente.setBounds(62,6,138,26);
        panel.add(cmbFuente);

        JLabel lblTamano = new JLabel("Tamaño");
        lblTamano.setBounds(8,40,52,22);
        panel.add(lblTamano);

        Tamaño = new JComboBox<>(TAMANOS);
        Tamaño.setSelectedItem("12");
        Tamaño.setBounds(62,38,138,26);
        panel.add(Tamaño);

        JLabel lblColor = new JLabel("Color");
        lblColor.setBounds(8,72,40,22);
        panel.add(lblColor);

        lblColorActual = new JLabel();
        lblColorActual.setOpaque(true);
        lblColorActual.setBackground(Color.BLACK);
        lblColorActual.setBorder(new LineBorder(Color.GRAY,1));
        lblColorActual.setBounds(62,72,24,22);
        panel.add(lblColorActual);

        btnColor.setBounds(90,70,40,26);
        panel.add(btnColor);

        return panel;
    }

    private JScrollPane crearEditor() {

        textPane = new JTextPane();
        textPane.setFont(new Font("Arial",Font.PLAIN,12));
        textPane.setBorder(new EmptyBorder(10,10,10,10));

        return new JScrollPane(textPane);
    }

    private JPanel crearPanelColores() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(100,0));

        JLabel lbl = new JLabel("Colores utilizados:");
        lbl.setBorder(new EmptyBorder(4,4,2,4));
        panel.add(lbl, BorderLayout.NORTH);

        panelColoresRecientes = new JPanel(new FlowLayout());

        panel.add(panelColoresRecientes, BorderLayout.CENTER);

        return panel;
    }

    private void agregarColorReciente(Color color) {

    int max = 10;

    Color[] nuevos = new Color[Math.min(UltimosColores.length + 1, max)];
    nuevos[0] = color;

    for(int i=0;i<nuevos.length-1 && i<UltimosColores.length;i++){
        nuevos[i+1] = UltimosColores[i];
    }

    UltimosColores = nuevos;

    panelColoresRecientes.removeAll();

    for(Color c : UltimosColores)
        panelColoresRecientes.add(crearCubito(c));

    panelColoresRecientes.revalidate();
    panelColoresRecientes.repaint();
}

    private JLabel crearCubito(Color color) {

    JLabel celda = new JLabel();
    celda.setOpaque(true);
    celda.setBackground(color);
    celda.setBorder(new LineBorder(Color.GRAY));
    celda.setPreferredSize(new Dimension(14,14));

    celda.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            colorActual = color;
            lblColorActual.setBackground(color);
            formato.AplicarColorFuente(color);
            textPane.requestFocus();
        }
    });

    return celda;
}
}