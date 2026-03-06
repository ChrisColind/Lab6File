/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6;
/**
 *
 * @author gpopo
 */
import org.apache.poi.xwpf.usermodel.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

public class TableManager {

    private JTextPane textPane;

    public TableManager(JTextPane textPane) {
        this.textPane = textPane;
    }



    
    //Muestra un dialogo que pide filas y columnas,
     
    public void mostrarDialogoCrearTabla(JFrame parent) {
        // Pedir numero de filas
        String inputFilas = JOptionPane.showInputDialog(
            parent,
            "¿Cuántas filas?",
            "Crear tabla",
            JOptionPane.QUESTION_MESSAGE
        );
        if (inputFilas == null) return; // canceló

        // Pedir numero de columnas
        String inputCols = JOptionPane.showInputDialog(
            parent,
            "¿Cuantas columnas?",
            "Crear tabla",
            JOptionPane.QUESTION_MESSAGE
        );
        if (inputCols == null) return; // cancel

        try {
            int filas = Integer.parseInt(inputFilas.trim());
            int cols  = Integer.parseInt(inputCols.trim());

            if (filas <= 0 || cols <= 0) {
                JOptionPane.showMessageDialog(parent, "Ingresa numeros mayores a 0.");
                return;
            }

            insertarTablaEnEditor(parent, filas, cols);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Por favor ingresa solo numeros.");
        }
    }
   
     
    private void insertarTablaEnEditor(JFrame parent, int filas, int cols) {

    // Crear modelo de tabla
    DefaultTableModel modelo = new DefaultTableModel(filas, cols);
    modelo.setColumnIdentifiers(generarEncabezados(cols));

    JTable tabla = new JTable(modelo);

    // Estética de la tabla
    tabla.setGridColor(Color.GRAY);
    tabla.setShowGrid(true);
    tabla.setBackground(Color.WHITE);
    tabla.getTableHeader().setBackground(new Color(220, 220, 220));

    // Editor personalizado que toma el formato actual del JTextPane
    tabla.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            JTextField editor = (JTextField) super.getTableCellEditorComponent(
                    table, value, isSelected, row, column);

            // Obtener atributos actuales del editor
            javax.swing.text.AttributeSet attrs = textPane.getCharacterAttributes();

            String fuente = javax.swing.text.StyleConstants.getFontFamily(attrs);
            int tamano = javax.swing.text.StyleConstants.getFontSize(attrs);
            boolean negrita = javax.swing.text.StyleConstants.isBold(attrs);
            boolean cursiva = javax.swing.text.StyleConstants.isItalic(attrs);

            int estilo = Font.PLAIN;
            if (negrita) estilo |= Font.BOLD;
            if (cursiva) estilo |= Font.ITALIC;

            Font fuenteActual = new Font(fuente, estilo, tamano);
            editor.setFont(fuenteActual);

            // Aplicar color del texto
            Color color = javax.swing.text.StyleConstants.getForeground(attrs);
            editor.setForeground(color);

            return editor;
        }
    });

    tabla.setRowHeight(24);

    JScrollPane scrollTabla = new JScrollPane(tabla);
    scrollTabla.setPreferredSize(
        new Dimension(cols * 100, filas * 24 + 30)
    );

    // Insertar tabla en el JTextPane
    textPane.setCaretPosition(textPane.getDocument().getLength());
    textPane.insertComponent(scrollTabla);

    try {
        textPane.getDocument().insertString(
            textPane.getDocument().getLength(), "\n", null
        );
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    
     // Busca todas las JTable dentro del JTextPane
     //y las guarda en el documento .docx con Apache POI
     

    public void guardarTablasEnDocx(XWPFDocument doc) {
    // Iterar el documento del JTextPane buscando componentes
        javax.swing.text.StyledDocument styledDoc = textPane.getStyledDocument();
        int largo = styledDoc.getLength();

        for (int i = 0; i < largo; i++) {
        javax.swing.text.Element elem = styledDoc.getCharacterElement(i);
        AttributeSet attrs = elem.getAttributes();

         // Los componentes insertados tienen este atributo
            Object comp = StyleConstants.getComponent(attrs);

            if (comp instanceof JScrollPane) {
             JScrollPane scroll = (JScrollPane) comp;
             Component vista = scroll.getViewport().getView();
             if (vista instanceof JTable) {
                guardarJTableEnDocx(doc, (JTable) vista);
                }
            }
        }
    }
    
     //Convierte una JTable a XWPFTable y la agrega al documento
     
    private void guardarJTableEnDocx(XWPFDocument doc, JTable tabla) {
        int filas = tabla.getRowCount();
        int cols  = tabla.getColumnCount();

        // Crear tabla en el .docx
        XWPFTable xwpfTable = doc.createTable(filas + 1, cols); // +1 para encabezado

        // Fila de encabezados
        XWPFTableRow headerRow = xwpfTable.getRow(0);
        for (int c = 0; c < cols; c++) {
            String header = tabla.getColumnName(c);
            headerRow.getCell(c).setText(header != null ? header : "");
        }

        // Filas de datos
        for (int f = 0; f < filas; f++) {
            XWPFTableRow row = xwpfTable.getRow(f + 1);
            for (int c = 0; c < cols; c++) {
                Object valor = tabla.getValueAt(f, c);
                String texto = valor != null ? valor.toString() : "";
                row.getCell(c).setText(texto);
            }
        }
    }


    public void cargarTablasDesdeDocx(XWPFDocument doc) {
        for (XWPFTable xwpfTable : doc.getTables()) {
            int filas = xwpfTable.getRows().size();
            if (filas == 0) continue;

            int cols = xwpfTable.getRow(0).getTableCells().size();

            String[] encabezados = new String[cols];
            XWPFTableRow headerRow = xwpfTable.getRow(0);
            for (int c = 0; c < cols; c++) {
                encabezados[c] = headerRow.getCell(c).getText();
            }

            DefaultTableModel modelo = new DefaultTableModel(encabezados, 0);
            for (int f = 1; f < filas; f++) {
                XWPFTableRow row = xwpfTable.getRow(f);
                String[] rowData = new String[cols];
                for (int c = 0; c < cols; c++) {
                    rowData[c] = row.getCell(c).getText();
                }
                modelo.addRow(rowData);
            }

            // Insertar en el editor
            JTable tabla = new JTable(modelo);
            
            Font fuenteActual = obtenerFuenteActual();

            tabla.setFont(fuenteActual);
            tabla.setRowHeight(fuenteActual.getSize() + 12);

            DefaultCellEditor editor = new DefaultCellEditor(new JTextField());
            editor.getComponent().setFont(fuenteActual);
            tabla.setDefaultEditor(Object.class, editor);

            tabla.setGridColor(Color.GRAY);
            tabla.setShowGrid(true);
            tabla.setRowHeight(24);
            tabla.setBackground(Color.WHITE);

            JScrollPane scrollTabla = new JScrollPane(tabla);
            scrollTabla.setPreferredSize(
                new Dimension(cols * 100, filas * 24 + 30)
            );

            textPane.setCaretPosition(textPane.getDocument().getLength());
            textPane.insertComponent(scrollTabla);

            try {
                textPane.getDocument().insertString(
                    textPane.getDocument().getLength(), "\n", null
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private Font obtenerFuenteActual() {

        javax.swing.text.AttributeSet attrs =
            textPane.getCharacterAttributes();

        String fuente = javax.swing.text.StyleConstants.getFontFamily(attrs);
        int tamano = javax.swing.text.StyleConstants.getFontSize(attrs);

        boolean negrita = javax.swing.text.StyleConstants.isBold(attrs);
        boolean cursiva = javax.swing.text.StyleConstants.isItalic(attrs);

        int estilo = Font.PLAIN;

        if(negrita) estilo |= Font.BOLD;
        if(cursiva) estilo |= Font.ITALIC;

        return new Font(fuente, estilo, tamano);
    }
    
    private String[] generarEncabezados(int cols) {
        String[] headers = new String[cols];
        for (int i = 0; i < cols; i++) {
            headers[i] = String.valueOf((char) ('A' + i));
        }
        return headers;
    }
}
