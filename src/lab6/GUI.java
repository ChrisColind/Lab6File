package lab6;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class GUI extends JFrame {

    //Componentes
    private JTextPane         textPane;
    private JComboBox<String> cmbFuente;
    private JComboBox<String> Tamaño;
    private JLabel            lblColorActual;
    private JPanel            panelColoresRecientes;
    
    //botones
    JButton btnGuardar = new JButton("💾");
    JButton btnColor = new JButton("...");
    JToggleButton btnNegrita;
    JToggleButton btnCursiva;
    JToggleButton btnSubrayado;
    JButton btnTabla = new JButton("Tabla");
    
    // Tama;os disponibles 
    private static final String[] TAMANOS= {
        "8","9","10","11","12","14","16","18","20","24",
        "28","32","36","42","48","64","72","92","144","190","240","300"
    };

    //Ultimos colores
    private Color[] UltimosColores = { 
        Color.RED
    };
    
    private Color colorActual = Color.BLACK;
    
    public GUI() {
        
        setTitle("Editor de texto");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearBarraHerramientas(),BorderLayout.NORTH);
        add(crearPanelCentral(),BorderLayout.CENTER);
        add(crearPanelBotones(),BorderLayout.SOUTH);
        
        //contiene: negrita, cursiva, tama;o, color, fuente
        FormatoTexto formato = new FormatoTexto(textPane);
        //contiene el codigo logica de las tablas
        TableManager tablas  = new TableManager(textPane);
        
        btnNegrita.addActionListener(e -> {
            // logica negrita
            formato.AplicarNegrita(btnNegrita.isSelected());
        });

        btnCursiva.addActionListener(e -> {
            //logica cursiva
            formato.AplicarCursiva(btnCursiva.isSelected());
        });

        btnSubrayado.addActionListener(e -> {
            // logica subrayado
            formato.AplicarSubrayado(btnSubrayado.isSelected());
        });

        btnTabla.addActionListener(e -> {
            // logica insertar tabla
        });

        btnGuardar.addActionListener(e -> {
            // logica guardar archivo
        });

        btnColor.addActionListener(e -> {
            // logica abrir JColorChooser
            Color elegido = JColorChooser.showDialog(this, "Elegir color", colorActual);
            if (elegido != null) {
                colorActual = elegido;
                lblColorActual.setBackground(elegido); // actualiza el cuadrito visual
                formato.AplicarColorFuente(elegido);   // aplica el color al texto
                agregarColorReciente(elegido);          // agrega al panel de colores
            }
            textPane.requestFocus();
        });

        cmbFuente.addActionListener(e -> {
            // logica cambiar fuente
            String fuente = (String) cmbFuente.getSelectedItem();
            if (fuente != null) formato.AplicarFuente(fuente);
        });

        Tamaño.addActionListener(e -> {
            // logica cambiar tamaño
            try {   
                int tamano = Integer.parseInt((String) Tamaño.getSelectedItem());
                formato.AplicarTamano(tamano);
            } catch (NumberFormatException ignored) {}
        });
    }
    
    private void agregarColorReciente(Color color) {
        // Agregar el color al arreglo (desplazando los anteriores)
        Color[] nuevos = new Color[UltimosColores.length + 1];
        nuevos[0] = color;
        System.arraycopy(UltimosColores, 0, nuevos, 1, UltimosColores.length);
        UltimosColores = nuevos;

        // Redibujar el panel de colores
        panelColoresRecientes.removeAll();
        for (Color c : UltimosColores) {
            panelColoresRecientes.add(crearCubito(c));
        }
        panelColoresRecientes.revalidate();
        panelColoresRecientes.repaint();
    }
    
    // Guardado
    private JPanel crearBarraHerramientas() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 3));
        barra.setBackground(new Color(236, 233, 216));
        barra.setBorder(new MatteBorder(0, 0, 1, 0, new Color(180, 180, 180)));
        
        //guardar===================================================
        btnGuardar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnGuardar.setPreferredSize(new Dimension(50, 24));
        btnGuardar.setFocusPainted(false);
        barra.add(btnGuardar);

        //Subrayado===================================================
        btnSubrayado = new JToggleButton("U");
        btnSubrayado.setPreferredSize(new Dimension(50, 24));
        btnSubrayado.setFocusPainted(false);
        barra.add(btnSubrayado);
        
        //Cursiva===================================================
        btnCursiva = new JToggleButton("I");
        btnCursiva.setFont(new Font("SansSerif", Font.ITALIC, 13));
        btnCursiva.setPreferredSize(new Dimension(50, 24));
        btnCursiva.setFocusPainted(false);
        barra.add(btnCursiva);
        
        // Negrita===================================================
        btnNegrita = new JToggleButton("B");
        btnNegrita.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNegrita.setPreferredSize(new Dimension(50, 24));
        btnNegrita.setFocusPainted(false);
        barra.add(btnNegrita);
        
        //Tabla==================================================
        btnTabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnTabla.setPreferredSize(new Dimension(70, 24));
        btnTabla.setFocusPainted(false);
        barra.add(btnTabla);    
        
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

        Tamaño = new JComboBox<>(TAMANOS);
        Tamaño.setSelectedItem("12");
        Tamaño.setBounds(62, 38, 138, 26);
        Tamaño.setBackground(Color.WHITE);
        panel.add(Tamaño);

        // JLABEL Color
        JLabel lblColor = new JLabel("Color");
        lblColor.setBounds(8, 72, 40, 22);
        panel.add(lblColor);
        
        //muestra el color que se esta usando en el momento
        lblColorActual = new JLabel();
        lblColorActual.setOpaque(true);
        lblColorActual.setBackground(Color.BLACK);
        lblColorActual.setBorder(new LineBorder(Color.GRAY, 1));
        lblColorActual.setBounds(62, 72, 24, 22);
        panel.add(lblColorActual);

        btnColor.setBounds(90, 70, 40, 26);
        btnColor.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnColor.setFocusPainted(false);
        panel.add(btnColor);    

        return panel;
    }
    
    //cubito de los ultimos colores
    private JLabel crearCubito(Color color) {
        JLabel celda = new JLabel();
        celda.setOpaque(true);
        celda.setBackground(color);
        celda.setBorder(new LineBorder(new Color(160, 160, 160), 1));
        celda.setPreferredSize(new Dimension(14, 14));
        celda.setMinimumSize(new Dimension(14, 14));
        celda.setMaximumSize(new Dimension(14, 14));
        return celda;
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

        for (Color c : UltimosColores) {
            JLabel celda = new JLabel();
            celda.setOpaque(true);
            celda.setBackground(c);
            celda.setBorder(new LineBorder(new Color(160, 160, 160), 1));
            celda.setPreferredSize(new Dimension(14, 14));  
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

}