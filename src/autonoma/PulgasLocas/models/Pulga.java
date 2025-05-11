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
    protected int tamaño = 20;
    protected Sprite sprite;
    protected boolean activa;
    protected CampoDeBatalla campo;
    protected static Random random = new Random();
    protected int dx;
    protected int dy;

    public Pulga(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.activa = true;
        do {
            this.dx = random.nextInt(7) - 3; // -3 a +3
        } while (dx == 0);

        do {
            this.dy = random.nextInt(7) - 3;
        } while (dy == 0);

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
        if (!activa || sprite == null) return;

        mover();
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
        
        ReproductorSonido.reproducir("boing.wav");
        
    }
    
    public void mover() {
        x += dx;
        y += dy;

        // Rebote en los bordes
        if (x <= 0 || x + sprite.getAncho() >= CampoDeBatalla.ANCHO_CAMPO) {
            dx = -dx;
        }
        if (y <= 0 || y + sprite.getAlto() >= CampoDeBatalla.ALTO_CAMPO){
            dy = -dy;
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