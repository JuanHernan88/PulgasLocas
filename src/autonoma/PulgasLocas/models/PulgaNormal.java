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
    public PulgaNormal(int x, int y) {
        super(x, y, GestorDeSprite.obtenerSprite("pulga_normal"));
    }

    @Override
    public boolean serImpactada() {
        activa = false;
        return true;
    }

    @Override
    public void dibujar(Graphics g) {
        sprite.dibujar(g, x, y);
    }
}
