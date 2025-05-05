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
public abstract class Pulga {
    protected int x, y;

    public Pulga(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void dibujar(Graphics g);
    public abstract void recibirImpacto();
    public abstract boolean estaViva();

    public int getX() { return x; }
    public int getY() { return y; }
}
