/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author marib
 */
public abstract class Sprite implements Dibujable, Actualizable {
    private BufferedImage[] imagenes;
    private int frameActual;
    private int velocidadAnimacion;
    private int contador;

    public Sprite(String[] rutasImagenes) {
        cargarImagenes(rutasImagenes);
    }

    private void cargarImagenes(String[] rutasImagenes) {
        imagenes = new BufferedImage[rutasImagenes.length];
        for (int i = 0; i < rutasImagenes.length; i++) {
            // Cargar imágenes
        }
    }

    @Override
    public void actualizar() {
        contador++;
        if (contador >= velocidadAnimacion) {
            frameActual = (frameActual + 1) % imagenes.length;
            contador = 0;
        }
    }

    public void dibujar(Graphics g, int x, int y) {
        g.drawImage(imagenes[frameActual], x, y, null);
    }
}