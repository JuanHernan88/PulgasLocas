/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.Graphics;

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
    }

    @Override
    public void dibujar(Graphics g) {
        // dibujar imagen normal
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
