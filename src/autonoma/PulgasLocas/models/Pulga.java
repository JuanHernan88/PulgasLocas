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
    protected int x;
    protected int y;
    protected int velocidadX;
    protected int velocidadY;
    protected Sprite sprite;
    protected boolean activa;

    public Pulga(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.activa = true;
    }

    public abstract void dibujar(Graphics g);
    public abstract boolean serImpactada(); // Método abstracto, se implementa en subclases

    public boolean colisiona(Pulga otra) {
        // Lógica de colisión (placeholder)
        return false;
    }

    public void saltar(int mx, int maxY) {
        // Lógica de salto (placeholder)
    }

    public void actualizar() {
        // Lógica de actualización (placeholder)
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean estaActiva() { return activa; }

    void setX(int nextInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setY(int nextInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    boolean estaViva() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    boolean contienePunto(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void recibirImpacto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}