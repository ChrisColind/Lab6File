/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6;

import javax.swing.JTextPane;
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
    public String TextoSeleccionado(){
        return textPane.getSelectedText();
    }
    // metodo que retorna en donde se inicia la seleccion
    public int InicioSeleccion(){
        return textPane.getSelectionStart();
    }
    //Este otro retorna en donde termina la seleccion
    public int FinSeleccion(){
        return textPane.getSelectionEnd();
    }
    //este verifica si hay texto seleccionado
    public boolean HaySeleccion(){
        return textPane.getSelectionStart() != textPane.getSelectionEnd();
    }
    // Metodo que obtiene el estilo del texto(es el que maneja todo el formato de texto)
    public StyledDocument ObtenerDocumento(){
        return textPane.getStyledDocument();
    }
}
