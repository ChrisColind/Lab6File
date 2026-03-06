/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab6;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author croge
 */
public class Lab6 {

    /**
     * @param args the command line arguments
     */
   
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new GUI().setVisible(true);
        });
    
    }
    
}
