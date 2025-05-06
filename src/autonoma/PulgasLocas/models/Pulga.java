/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.Graphics;
import java.awt.Image;

/*
 * 
 * @author Juan Esteban Hernández Martínez
 * @since 20250505
 * @version 1.0.0 
 */
public abstract class Pulga {
    protected int x, y;
    protected Image imagen;

    public Pulga(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void dibujar(Graphics g);
    public abstract void recibirImpacto();
    public abstract boolean estaViva();

    public int getX() { return x; }
    public int getY() { return y; }
    
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public boolean contienePunto(int px, int py) {
        return estaViva() && px >= x && px <= x + 25 && py >= y && py <= y + 25;
    }
    public void moverAleatoriamente(int ancho, int alto) {
        this.x = (int) (Math.random() * ancho);
        this.y = (int) (Math.random() * alto);
    }
}
