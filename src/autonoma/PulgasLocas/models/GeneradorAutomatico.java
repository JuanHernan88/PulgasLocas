/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

/**
 *
 * @author marib
 */
public class GeneradorAutomatico extends Thread {
    private SimuladorPulgas simulador;
    private boolean activo;

    public GeneradorAutomatico(SimuladorPulgas simulador) {
        this.simulador = simulador;
        this.activo = true;
    }

    @Override
    public void run() {
        while (activo) {
            simulador.agregarPulgaNormal();
            try {
                Thread.sleep(2000); // Espera 2 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void detener() {
        activo = false;
    }
}