/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

public class PulgaMutante extends Pulga {
    private boolean transformada;

    public PulgaMutante(int x, int y) {
        super(x, y, GestorDeSprite.obtenerSprite(GestorDeSprite.PULGA_MUTANTE_SPRITE_KEY));
        this.transformada = false;
    }

    @Override
    public void recibirImpacto() {
        if (!activa) return;

        if (!transformada) {
            this.transformada = true;
            this.sprite = GestorDeSprite.obtenerSprite(GestorDeSprite.PULGA_NORMAL_SPRITE_KEY); 
            if (this.sprite != null) {
                this.sprite.resetAnimacion();
            } else {
                 System.err.println("Error: Sprite PULGA_NORMAL no encontrado para transformación de PulgaMutante.");
            }
        } else {
            this.activa = false;
        }
    }
    
    public boolean isTransformada(){
        return this.transformada;
    }
}