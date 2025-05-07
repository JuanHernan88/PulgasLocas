/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author marib
 */
public class PanelJuego extends JPanel {
    private SimuladorPulgas simulador;
    private BufferedImage fondoImagen;

    public PanelJuego(SimuladorPulgas simulador) {
        this.simulador = simulador;
        inicializaEventos();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondoImagen, 0, 0, null);

        for (Pulga p : simulador.pulgas) {
            p.dibujar(g);
        }
    }

    public void inicializaEventos() {
        // Aquí se agregarían listeners de teclado, mouse, etc.
    }
}
