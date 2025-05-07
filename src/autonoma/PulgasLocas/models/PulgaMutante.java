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
    private int resistencia;

    public PulgaMutante(int x, int y) {
        super(x, y, GestorDeSprite.obtenerSprite("pulga_mutante"));
        this.resistencia = 3; // Ejemplo de valor
    }

    @Override
    public boolean serImpactada() {
        resistencia--;
        if (resistencia <= 0) {
            activa = false;
        }
        return !activa;
    }

    @Override
    public void dibujar(Graphics g) {
        sprite.dibujar(g, x, y);
    }
}