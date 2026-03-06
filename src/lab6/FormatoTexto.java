/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab6;

import java.awt.Color;
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
    
    //crear atributos para la fuente
    public SimpleAttributeSet AtributosFuente(String NameFuente){
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setFontFamily(atributos, NameFuente);
        return atributos;
    }
    //crear atributos para el tamanio del texto
    public SimpleAttributeSet AtributosTamano(int tamano){
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setFontSize(atributos, tamano);
        return atributos;
    }
    //atributos para negrita
    public SimpleAttributeSet AtributosNegrita(boolean activar){
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setBold(atributos, activar);
        return atributos;
    }
    //atributos para subrayado
     public SimpleAttributeSet AtributosSubrayado(boolean activar){
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setUnderline(atributos, activar);
        return atributos;
    }
     //atributos para cursiva
      public SimpleAttributeSet AtributosCursiva(boolean activar){
        SimpleAttributeSet atributos = new SimpleAttributeSet();
        StyleConstants.setItalic(atributos, activar);
        return atributos;
    }
      //atributos para el color de la fuente
       public SimpleAttributeSet AtributosColorFuente(Color color){
           SimpleAttributeSet atributos = new SimpleAttributeSet();
           StyleConstants.setForeground(atributos, color);
           return atributos;
       }
           public SimpleAttributeSet AtributosColorFondo(Color color){
           SimpleAttributeSet atributos = new SimpleAttributeSet();
           StyleConstants.setBackground(atributos, color);
           return atributos;
       }
}
