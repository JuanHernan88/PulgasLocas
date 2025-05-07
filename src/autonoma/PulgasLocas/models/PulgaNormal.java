/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

public class PulgaNormal extends Pulga {

    public PulgaNormal(int x, int y) {
        super(x, y, GestorDeSprite.obtenerSprite(GestorDeSprite.PULGA_NORMAL_SPRITE_KEY));
    }

    @Override
    public void recibirImpacto() {
        if (!activa) return;
        this.activa = false;
    }
}
