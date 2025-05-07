/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.interfaces.Actualizable; 
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Sprite { 
    private BufferedImage imagenCompleta;
    private BufferedImage[] frames;
    private int frameActual;
    private int numeroDeFrames;
    private long tiempoPorFrame; 
    private long tiempoAcumulado; 
    private int anchoFrame;
    private int altoFrame;
    private boolean animacionTerminada; 

    public Sprite(BufferedImage imagen) {
        if (imagen == null) {
             System.err.println("Error crítico: Se intentó crear un Sprite con una imagen nula.");
             this.imagenCompleta = null;
             this.frames = new BufferedImage[0];
             this.numeroDeFrames = 0;
             this.anchoFrame = 0;
             this.altoFrame = 0;
        } else {
            this.imagenCompleta = imagen;
            this.frames = new BufferedImage[1];
            this.frames[0] = imagen;
            this.numeroDeFrames = 1;
            this.anchoFrame = imagen.getWidth();
            this.altoFrame = imagen.getHeight();
        }
        this.frameActual = 0;
        this.tiempoPorFrame = Long.MAX_VALUE; 
        this.tiempoAcumulado = 0;
        this.animacionTerminada = true; 
    }

    public Sprite(BufferedImage spriteSheet, int numeroDeFrames, int anchoFrame, int altoFrame, long duracionAnimacionMs) {
        this.frameActual = 0;
        this.tiempoAcumulado = 0;
        this.animacionTerminada = false;

         if (spriteSheet == null) {
            System.err.println("Error crítico: Se intentó crear un Sprite animado con un spritesheet nulo.");
            this.imagenCompleta = null;
            this.frames = new BufferedImage[0];
            this.numeroDeFrames = 0;
            this.anchoFrame = 0;
            this.altoFrame = 0;
            this.tiempoPorFrame = Long.MAX_VALUE;
            this.animacionTerminada = true;
            return;
         }

        this.imagenCompleta = spriteSheet;
        this.anchoFrame = anchoFrame;
        this.altoFrame = altoFrame;
        
        if (numeroDeFrames > 0 && anchoFrame > 0 && altoFrame > 0) {
            this.numeroDeFrames = numeroDeFrames;
            this.frames = new BufferedImage[numeroDeFrames];
            this.tiempoPorFrame = (duracionAnimacionMs > 0) ? (duracionAnimacionMs / numeroDeFrames) : Long.MAX_VALUE;

            if (spriteSheet.getWidth() < numeroDeFrames * anchoFrame || spriteSheet.getHeight() < altoFrame) {
                System.err.println("Error: Dimensiones del spritesheet incompatibles con los frames especificados.");
                this.numeroDeFrames = 0;
                this.frames = new BufferedImage[0];
                this.tiempoPorFrame = Long.MAX_VALUE;
            } else {
                for (int i = 0; i < numeroDeFrames; i++) {
                    try {
                        this.frames[i] = spriteSheet.getSubimage(i * anchoFrame, 0, anchoFrame, altoFrame);
                    } catch (Exception e) {
                        System.err.println("Error extrayendo frame " + i + " del spritesheet: " + e.getMessage());
                    }
                }
            }
        } else {
             System.err.println("Advertencia: Parámetros de animación inválidos, tratando como sprite estático.");
             this.frames = new BufferedImage[1];
             this.frames[0] = spriteSheet.getSubimage(0, 0, Math.min(anchoFrame, spriteSheet.getWidth()), Math.min(altoFrame, spriteSheet.getHeight()));
             this.numeroDeFrames = 1;
             this.tiempoPorFrame = Long.MAX_VALUE;
        }
    }

    public void actualizarAnimacion(long deltaTime) { 
        if (numeroDeFrames <= 1 || animacionTerminada) return;

        tiempoAcumulado += deltaTime;
        while (tiempoAcumulado >= tiempoPorFrame && tiempoPorFrame > 0) { 
            tiempoAcumulado -= tiempoPorFrame;
            frameActual++;
            if (frameActual >= numeroDeFrames) {
                frameActual = 0; 
            }
        }
    }

    public void actualizar() {
    }

    public void dibujar(Graphics g, int x, int y) {
        if (frames != null && frameActual >= 0 && frameActual < frames.length && frames[frameActual] != null) {
            g.drawImage(frames[frameActual], x, y, null);
        } else if (imagenCompleta != null && numeroDeFrames <=1) {
             g.drawImage(imagenCompleta, x, y, null);
        }
    }

    public void dibujar(Graphics g, int x, int y, int ancho, int alto) {
         if (frames != null && frameActual >= 0 && frameActual < frames.length && frames[frameActual] != null) {
            g.drawImage(frames[frameActual], x, y, ancho, alto, null);
        } else if (imagenCompleta != null && numeroDeFrames <=1) {
            g.drawImage(imagenCompleta, x, y, ancho, alto, null);
        }
    }
    
    public void resetAnimacion() {
        this.frameActual = 0;
        this.tiempoAcumulado = 0;
        this.animacionTerminada = false;
    }

    public int getAncho() {
        return anchoFrame;
    }

    public int getAlto() {
        return altoFrame;
    }
}