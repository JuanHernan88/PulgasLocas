/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author marib
 */
public class SimuladorPulgas {
    ArrayList<Pulga> pulgas;
    private int puntaje;
    private int puntajeMaximo;
    private boolean enJuego;
    private Timer timerPulgaNormal;
    private Timer timerPulgaMutante;

    public SimuladorPulgas() {
        pulgas = new ArrayList<>();
        puntaje = 0;
        enJuego = false;
    }

    public void iniciarJuego() {
        enJuego = true;
        puntaje = 0;
        pulgas.clear();
    }

    public void finalizarJuego() {
        enJuego = false;
    }

    public void agregarPulgaNormal() {
        pulgas.add(new PulgaNormal(generarX(), generarY()));
    }

    public void agregarPulgaMutante() {
        pulgas.add(new PulgaMutante(generarX(), generarY()));
    }

    public void usarPistolaPulguipium(int x, int y) {
        new PistolaPulguipium().usar(pulgas, x, y);
    }

    public void usarMisilPulgoson(int x, int y) {
        new MisilPulgoson().usar(pulgas, x, y);
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void guardarPuntajeMaximo() {
        GestorDeArchivos.guardarPuntaje(puntaje);
    }

    public int cargarPuntajeMaximo() {
        puntajeMaximo = GestorDeArchivos.cargarPuntajeMaximo();
        return puntajeMaximo;
    }

    public void actualizarEstado() {
        for (Pulga p : pulgas) {
            p.actualizar();
        }
    }

    public boolean verificarFinJuego() {
        return !enJuego;
    }

    private int generarX() {
        return (int) (Math.random() * 800);
    }

    private int generarY() {
        return (int) (Math.random() * 600);
    }
}