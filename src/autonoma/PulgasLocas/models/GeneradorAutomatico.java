/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import javax.swing.SwingUtilities;

/**
 * Genera pulgas automáticamente en un hilo separado.
 * @author marib 
 */
public class GeneradorAutomatico extends Thread {
    private CampoDeBatalla campoDeBatalla;
    private volatile boolean activo;
    private long intervaloGeneracion;
    private boolean esParaMutantes;

    public GeneradorAutomatico(CampoDeBatalla campoDeBatalla, long intervaloGeneracion, boolean esParaMutantes) {
        this.campoDeBatalla = campoDeBatalla;
        this.activo = true;
        this.intervaloGeneracion = intervaloGeneracion;
        this.esParaMutantes = esParaMutantes;
        setName("GeneradorAutomaticoPulgas-" + (esParaMutantes ? "Mutante" : "Normal")); 
    }

    @Override
    public void run() {
        while (activo) {
            if (EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
                SwingUtilities.invokeLater(() -> {
                    if (campoDeBatalla != null && EstadoDeJuego.getEstadoActual() == EstadoDeJuego.EN_JUEGO) {
                        if (esParaMutantes) {
                            campoDeBatalla.agregarPulgaMutante();
                        } else {
                            campoDeBatalla.agregarPulgaNormal();
                        }
                    }
                });
            }
            
            try {
                Thread.sleep(intervaloGeneracion);
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrumpido.");
                activo = false; 
                Thread.currentThread().interrupt(); 
            }
        }
        System.out.println(getName() + " detenido.");
    }

    public void detenerGenerador() {
        activo = false;
        this.interrupt(); 
    }
}