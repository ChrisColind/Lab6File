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
import javax.swing.text.*;
import java.awt.Color;
import java.io.*;



public class FileManager {

    private JTextPane textPane;
    private TableManager tableManager;

    public FileManager(JTextPane textPane, TableManager tableManager) {
        this.textPane      = textPane;
        this.tableManager  = tableManager;
    }

    public void guardarArchivo(String rutaArchivo) {
        try {
            XWPFDocument doc      = new XWPFDocument();
            StyledDocument styledDoc = textPane.getStyledDocument();
            String textoCompleto  = textPane.getText();

            XWPFParagraph paragraph = doc.createParagraph();

            int i = 0;
            while (i < textoCompleto.length()) {

                Element elem  = styledDoc.getCharacterElement(i);
                AttributeSet attrs = elem.getAttributes();

                // Leer formato
                String  fuente = StyleConstants.getFontFamily(attrs);
                int     tamano = StyleConstants.getFontSize(attrs);
                boolean bold   = StyleConstants.isBold(attrs);
                boolean italic = StyleConstants.isItalic(attrs);
                boolean under  = StyleConstants.isUnderline(attrs);
                Color   color  = StyleConstants.getForeground(attrs);

                int fin = elem.getEndOffset();
                if (fin > textoCompleto.length()) fin = textoCompleto.length();

                String fragmento = textoCompleto.substring(i, fin);

                // Salto de línea = nuevo parrafo
                if (fragmento.contains("\n")) {
                    String[] lineas = fragmento.split("\n", -1);
                    for (int j = 0; j < lineas.length; j++) {
                        if (j > 0) paragraph = doc.createParagraph();
                        XWPFRun run = paragraph.createRun();
                        aplicarFormato(run, lineas[j], fuente, tamano, bold, italic, under, color);
                    }
                } else {
                    XWPFRun run = paragraph.createRun();
                    aplicarFormato(run, fragmento, fuente, tamano, bold, italic, under, color);
                }

                i = fin;
            }

            // Guardar tablas tambien
            tableManager.guardarTablasEnDocx(doc);

            // Escribir archivo
            FileOutputStream out = new FileOutputStream(rutaArchivo);
            doc.write(out);
            out.close();
            doc.close();

            JOptionPane.showMessageDialog(null, "Archivo guardado correctamente ✅");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Aplica formato a un XWPFRun
    private void aplicarFormato(XWPFRun run, String texto,
                                 String fuente, int tamano,
                                 boolean bold, boolean italic,
                                 boolean under, Color color) {
        run.setText(texto);
        run.setFontFamily(fuente);
        run.setFontSize(tamano);
        run.setBold(bold);
        run.setItalic(italic);
        run.setUnderline(under
            ? UnderlinePatterns.SINGLE
            : UnderlinePatterns.NONE);

        String hex = String.format("%02X%02X%02X",
                color.getRed(), color.getGreen(), color.getBlue());
        run.setColor(hex);
    }

    public void abrirArchivo(String rutaArchivo) {
        try {
            FileInputStream fis  = new FileInputStream(rutaArchivo);
            XWPFDocument doc     = new XWPFDocument(fis);
            StyledDocument styledDoc = textPane.getStyledDocument();
            FormatoTexto formato = new FormatoTexto(textPane);

            //Limpiar editor
            textPane.setText("");

            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {

                    String texto = run.getText(0);
                    if (texto == null) continue;

                    String  fuente = run.getFontFamily() != null ? run.getFontFamily() : "Arial";
                    int     tamano = run.getFontSize()   != -1   ? run.getFontSize()   : 12;
                    boolean bold   = run.isBold();
                    boolean italic = run.isItalic();
                    boolean under  = run.getUnderline() == UnderlinePatterns.SINGLE;

                    String colorHex = run.getColor();
                    Color color = Color.BLACK;
                    if (colorHex != null && !colorHex.isEmpty()) {
                        color = Color.decode("#" + colorHex);
                    }

                    // Usar FormatoTexto pra construir atributos
                    SimpleAttributeSet attrs = new SimpleAttributeSet();
                    attrs.addAttributes(formato.AtributosFuente(fuente));
                    attrs.addAttributes(formato.AtributosTamano(tamano));
                    attrs.addAttributes(formato.AtributosNegrita(bold));
                    attrs.addAttributes(formato.AtributosCursiva(italic));
                    attrs.addAttributes(formato.AtributosSubrayado(under));
                    attrs.addAttributes(formato.AtributosColorFuente(color));

                    int pos = styledDoc.getLength();
                    styledDoc.insertString(pos, texto, attrs);
                }

                // Salto de linea por parrafo
                styledDoc.insertString(styledDoc.getLength(), "\n", null);
            }

            // Cargar tablas tambin
            tableManager.cargarTablasDesdeDocx(doc);

            doc.close();
            fis.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String elegirRutaGuardar() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar como...");
        chooser.setSelectedFile(new File("documento.docx"));
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            if (!ruta.endsWith(".docx")) ruta += ".docx";
            return ruta;
        }
        return null;
    }

    public String elegirRutaAbrir() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir archivo...");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}