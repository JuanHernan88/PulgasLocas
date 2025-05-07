/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.views;

import autonoma.PulgasLocas.models.CampoDeBatalla;
import autonoma.PulgasLocas.models.EstadoDeJuego;
import autonoma.PulgasLocas.models.Pulga;
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List; 
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Panel donde se dibuja el campo de batalla, las pulgas y se manejan los clics del ratón.
 */
public class PanelJuego extends JPanel {
    private CampoDeBatalla campoDeBatalla;

    public PanelJuego(CampoDeBatalla campoDeBatalla) {
        if (campoDeBatalla == null) {
            throw new IllegalArgumentException("El CampoDeBatalla no puede ser nulo.");
        }
        this.campoDeBatalla = campoDeBatalla;
        
        // Establecer tamaño preferido usando las constantes de CampoDeBatalla
        setPreferredSize(new Dimension(CampoDeBatalla.ANCHO_CAMPO, CampoDeBatalla.ALTO_CAMPO));
        
        setBackground(Color.WHITE); 
        setOpaque(true); 

        inicializarEventos(); 
        setFocusable(true);   
    }

    /**
     * Configura el listener para detectar clics del ratón (disparar pistola).
     */
    private void inicializarEventos() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Solo dispara si el juego está activo y es el botón izquierdo
                if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO && SwingUtilities.isLeftMouseButton(e)) {
                    // Llama al método correcto en CampoDeBatalla
                    campoDeBatalla.dispararPistola(e.getX(), e.getY()); 
                }
            }
        });
    }

    /**
     * Método principal de dibujado del panel. Se llama automáticamente por Swing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Llama al método de la superclase para limpiar y dibujar el fondo (con el color de setBackground)
        super.paintComponent(g); 

        // Dibujar las pulgas
        if (campoDeBatalla != null) {
            List<Pulga> pulgasADibujar = campoDeBatalla.getPulgas();
            for (Pulga p : pulgasADibujar) { 
                // Solo dibujar si la pulga está viva (activa)
                if (p.estaViva()) { 
                    p.dibujar(g); 
                }
            }
        }

        // Dibujar mensaje de Pausa si el juego está en ese estado
        if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.PAUSA) {
            g.setColor(new Color(0, 0, 0, 150)); 
            g.fillRect(0, getHeight() / 2 - 30, getWidth(), 60);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            String textoPausa = "PAUSA (Presiona ESC para continuar)"; 
            int anchoTexto = g.getFontMetrics().stringWidth(textoPausa);
            g.drawString(textoPausa, getWidth() / 2 - anchoTexto / 2, getHeight() / 2 + 10);
        }
    }
}