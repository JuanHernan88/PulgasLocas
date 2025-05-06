/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.Graphics;
import javax.swing.ImageIcon;

/*
 * 
 * @author Juan Esteban Hernández Martínez
 * @since 20250505
 * @version 1.0.0 
 */
public class PulgaNormal extends Pulga {
    private boolean viva;

    public PulgaNormal(int x, int y) {
        super(x, y);
        this.viva = true;
        this.imagen = new ImageIcon(getClass().getResource("/autonoma/PulgasLocas/resources/Pulga_N.png")).getImage();
    }

    @Override
    public void dibujar(Graphics g) {
        if (viva) {
            g.drawImage(imagen, x, y, 25, 25, null); // Tamaño normal
        }
    }

    @Override
    public void recibirImpacto() {
        viva = false;
    }

    @Override
    public boolean estaViva() {
        return viva;
    }
}
