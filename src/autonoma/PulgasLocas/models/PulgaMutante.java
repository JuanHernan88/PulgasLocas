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
public class PulgaMutante extends Pulga {
    private int impactos;

    public PulgaMutante(int x, int y) {
        super(x, y);
        this.impactos = 0;
        this.imagen = new ImageIcon(getClass().getResource("/autonoma/PulgasLocas/resources/Pulga_M.png")).getImage();
    
    }

    @Override
    public void dibujar(Graphics g) {
        if (estaViva()) {
            g.drawImage(imagen, x, y, 30, 30, null); // Tamaño más grande
        }
    }

    @Override
    public void recibirImpacto() {
        impactos++;
    }

    @Override
    public boolean estaViva() {
        return impactos < 2;
    }
    public boolean debeConvertirse() {
        return impactos == 1;
    }

    @Override
    public boolean contienePunto(int px, int py) {
        return estaViva() && px >= x && px <= x + 30 && py >= y && py <= y + 30;
    }
}
