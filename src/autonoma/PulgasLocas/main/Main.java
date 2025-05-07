/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.main;
import autonoma.PulgasLocas.views.VentanaPrincipal;
import javax.swing.SwingUtilities;

/**
 *
 * @author marib
 */


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal();
        });
    }
}
