/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author diego
 */
public class FormatoTexto {

    private JTextPane textPane;

    public FormatoTexto(JTextPane textPane) {
        this.textPane = textPane;
    }

    //retorna el texto que el usario selecciono
    public String TextoSeleccionado() {
        try {
            String seleccion = textPane.getSelectedText();
            if (seleccion == null) {
                return "";
            }
            return seleccion;
        } catch (Exception e) {
            System.out.println("Error al obtener texto seleccionado: " + e.getMessage());
            return "";
        }

    }

    // metodo que retorna en donde se inicia la seleccion
    public int InicioSeleccion() {
        return textPane.getSelectionStart();
    }

    //Este otro retorna en donde termina la seleccion
    public int FinSeleccion() {
        return textPane.getSelectionEnd();
    }

    //este verifica si hay texto seleccionado
    public boolean HaySeleccion() {
        return textPane.getSelectionStart() != textPane.getSelectionEnd();
    }

    // Metodo que obtiene el estilo del texto(es el que maneja todo el formato de texto)
    public StyledDocument ObtenerDocumento() {
        StyledDocument doc = textPane.getStyledDocument();
        if(doc == null){
            JOptionPane.showMessageDialog(null, "Error: El documento es nulo");
        }
        return doc;
    }

    //crear atributos para la fuente
    public SimpleAttributeSet AtributosFuente(String NameFuente) {
        if (NameFuente == null || NameFuente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Nombre de fuente no valido");
            return new SimpleAttributeSet();
        }
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setFontFamily(atributos, NameFuente);
        return atributos;
    }

    //crear atributos para el tamanio del texto
    public SimpleAttributeSet AtributosTamano(int tamano) {
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setFontSize(atributos, tamano);
        return atributos;
    }

    //atributos para negrita
    public SimpleAttributeSet AtributosNegrita(boolean activar) {
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setBold(atributos, activar);
        return atributos;
    }

    //atributos para subrayado
    public SimpleAttributeSet AtributosSubrayado(boolean activar) {
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setUnderline(atributos, activar);
        return atributos;
    }
    //atributos para cursiva

    public SimpleAttributeSet AtributosCursiva(boolean activar) {
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setItalic(atributos, activar);
        return atributos;
    }
    //atributos para el color de la fuente

    public SimpleAttributeSet AtributosColorFuente(Color color) {
        if (color == null) {
            JOptionPane.showMessageDialog(null, "Error: El color no puede ser nulo");
            return new SimpleAttributeSet();
        }
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setForeground(atributos, color);
        return atributos;
    }
    //atributos para el color para el fondo del texto

    public SimpleAttributeSet AtributosColorFondo(Color color) {
        if (color == null) {
            JOptionPane.showMessageDialog(null, "Error: El color de fondo no puede ser nulo");
            return new SimpleAttributeSet();
        }
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setBackground(atributos, color);
        return atributos;
    }
    //metodo para la alineacion del texto

    public SimpleAttributeSet AtributosAlineacion(int alineacion) {
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setAlignment(atributos, alineacion);
        return atributos;
    }

    // metodo que aplica los atributos anteriores
    public void AplicarAtributos(SimpleAttributeSet atributos) {
        try {
            StyledDocument doc = ObtenerDocumento();
            int inicio = InicioSeleccion();
            int fin = FinSeleccion();

            if (HaySeleccion()) {
                doc.setCharacterAttributes(inicio, fin - inicio, atributos, false);
            } else {
                textPane.setCharacterAttributes(atributos, false);
            }
        } catch (Exception e) {
            System.out.println("Error al aplicar formato al texto" + e.getMessage());
        }

    }

    //aplicar atributos de alineacion
    public void AplicarAtributosAlineacion(SimpleAttributeSet atributos) {
        try {
            StyledDocument doc = ObtenerDocumento();
            int inicio = InicioSeleccion();
            int fin = FinSeleccion();

            if (HaySeleccion()) {
                doc.setParagraphAttributes(inicio, fin - inicio, atributos, false);
            } else {
                doc.setParagraphAttributes(inicio, 1, atributos, false);
            }
        } catch (Exception e) {
            System.out.println("Error al aplicar formato al parrafo" + e.getMessage());
        }

    }

    // cambia el tamano del texto seleccionado
    public void AplicarTamano(int tamano) {
        try {
            SimpleAttributeSet atributos = AtributosTamano(tamano);
            AplicarAtributos(atributos);
            textPane.requestFocus();
        } catch (NumberFormatException e) {
            System.out.println("El tamano ingresado no es un numero valido: " + e.getMessage());
        }

    }

    //cambia la fuente del texto
    public void AplicarFuente(String nameFuente) {
        SimpleAttributeSet atributos = AtributosFuente(nameFuente);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }
    // activar o desactivar negrita

    public void AplicarNegrita(boolean activar) {
        SimpleAttributeSet atributos = AtributosNegrita(activar);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }
    // activar o desactivar cursiva

    public void AplicarCursiva(boolean activar) {
        SimpleAttributeSet atributos = AtributosCursiva(activar);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }
    // activar o desactivar subrayado

    public void AplicarSubrayado(boolean activar) {
        SimpleAttributeSet atributos = AtributosSubrayado(activar);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }
    // cambiar color de la fuente

    public void AplicarColorFuente(Color color) {
        SimpleAttributeSet atributos = AtributosColorFuente(color);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }

    // cambiar color del fondo del texto
    public void AplicarColorFondo(Color color) {
        SimpleAttributeSet atributos = AtributosColorFondo(color);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }
    // cambiar la alineacion del texto

    public void AplicarAlineacion(int alineacion) {
        SimpleAttributeSet atributos = AtributosAlineacion(alineacion);
        AplicarAtributos(atributos);
        textPane.requestFocus();
    }
}
