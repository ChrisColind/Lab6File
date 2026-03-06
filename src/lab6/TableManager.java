/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6;

import org.apache.poi.xwpf.usermodel.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TableManager {

    private JTextPane textPane;

    public TableManager(JTextPane textPane) {
        this.textPane = textPane;
    }



    
    //Muestra un diálogo que pide filas y columnas,
     
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
            "¿Cuántas columnas?",
            "Crear tabla",
            JOptionPane.QUESTION_MESSAGE
        );
        if (inputCols == null) return; // cancel

        try {
            int filas = Integer.parseInt(inputFilas.trim());
            int cols  = Integer.parseInt(inputCols.trim());

            if (filas <= 0 || cols <= 0) {
                JOptionPane.showMessageDialog(parent, "Ingresa números mayores a 0.");
                return;
            }

            insertarTablaEnEditor(parent, filas, cols);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Por favor ingresa solo números.");
        }
    }
   
     
    private void insertarTablaEnEditor(JFrame parent, int filas, int cols) {
        // Crear modelo de tabla vacioo
        DefaultTableModel modelo = new DefaultTableModel(filas, cols);

        for (int i = 0; i < cols; i++) {
            modelo.setColumnIdentifiers(generarEncabezados(cols));
        }

        JTable tabla = new JTable(modelo);
        tabla.setGridColor(Color.GRAY);
        tabla.setShowGrid(true);
        tabla.setRowHeight(24);
        tabla.setBackground(Color.WHITE);
        tabla.getTableHeader().setBackground(new Color(220, 220, 220));
        tabla.setPreferredScrollableViewportSize(
            new Dimension(400, filas * 24 + 20)
        );

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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
     // Busca todas las JTable dentro del JTextPane
     //y las guarda en el documento .docx con Apache POI
     
    public void guardarTablasEnDocx(XWPFDocument doc) {
        // Recorrer los componentes insertados en el JTextPane
        Component[] componentes = textPane.getComponents();

        for (Component comp : componentes) {
            // Buscar JScrollPane que contenga JTable
            if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                Component vista = scroll.getViewport().getView();

                if (vista instanceof JTable) {
                    JTable tabla = (JTable) vista;
                    guardarJTableEnDocx(doc, tabla);
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
    
    private String[] generarEncabezados(int cols) {
        String[] headers = new String[cols];
        for (int i = 0; i < cols; i++) {
            headers[i] = String.valueOf((char) ('A' + i));
        }
        return headers;
    }
}
