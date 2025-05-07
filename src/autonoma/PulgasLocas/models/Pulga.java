/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.interfaces.Actualizable;
import autonoma.PulgasLocas.interfaces.Dibujable;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public abstract class Pulga implements Dibujable, Actualizable {
    protected int x;
    protected int y;
    protected Sprite sprite;
    protected boolean activa;
    protected static Random random = new Random();

    public Pulga(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.activa = true;
        if (this.sprite == null) {
            System.err.println("Error crítico: Se intentó crear una Pulga con un Sprite nulo.");
        }
    }

    public abstract void recibirImpacto();

    @Override
    public void dibujar(Graphics g) {
        if (activa && sprite != null) {
            sprite.dibujar(g, this.x, this.y, sprite.getAncho(), sprite.getAlto());
        }
    }

    @Override
    public void actualizar() {
        // La animación se actualiza desde el CampoDeBatalla con deltaTime
        // if (activa && sprite != null) {
        //     sprite.actualizar(); 
        // }
    }
    
    public void saltar(int limiteX, int limiteY) {
        if (!activa || sprite == null) return;
        
        int anchoPulga = sprite.getAncho();
        int altoPulga = sprite.getAlto();

        if (limiteX > anchoPulga) {
            this.x = random.nextInt(limiteX - anchoPulga);
        } else {
            this.x = 0; 
        }
        if (limiteY > altoPulga) {
            this.y = random.nextInt(limiteY - altoPulga);
        } else {
            this.y = 0;
        }
    }

    public boolean colisiona(Pulga otra) {
        if (!this.activa || !otra.estaViva() || this.sprite == null || otra.sprite == null) return false;
        return getBounds().intersects(otra.getBounds());
    }

    public boolean contienePunto(int px, int py) {
        if (!this.activa || this.sprite == null) return false;
        return getBounds().contains(px, py);
    }
    
    public Rectangle getBounds() {
        if (this.sprite == null) return new Rectangle(this.x, this.y, 0, 0);
        return new Rectangle(this.x, this.y, sprite.getAncho(), sprite.getAlto());
    }

    public void destruir() {
        this.activa = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Sprite getSprite(){ return sprite; }
    public boolean estaViva() { return activa; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setActiva(boolean activa) { this.activa = activa; }
}