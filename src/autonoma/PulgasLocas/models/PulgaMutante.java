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
public class PulgaMutante extends Pulga {
    private int impactos;

    public PulgaMutante(int x, int y) {
        super(x, y);
        impactos = 0;
    }

    @Override
    public void dibujar(Graphics g) {
        // dibujar imagen mutante
    }

    @Override
    public void recibirImpacto() {
        impactos++;
    }

    @Override
    public boolean estaViva() {
        return impactos < 2;
    }
}
