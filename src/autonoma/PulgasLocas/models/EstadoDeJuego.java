/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

/**
 * Gestiona el estado actual del juego.
 * @author marib
 */
public class EstadoDeJuego {
    public static final int MENU_PRINCIPAL = 0;
    public static final int EN_JUEGO = 1;
    public static final int PAUSA = 2;
    public static final int FIN_JUEGO = 3;

    private static int estadoActual = MENU_PRINCIPAL; // El juego podría empezar en el menú

    public static void cambiarEstado(int nuevoEstado) {
        System.out.println("Cambiando estado de " + getNombreEstado(estadoActual) + " a " + getNombreEstado(nuevoEstado));
        estadoActual = nuevoEstado;
    }

    public static int getEstadoActual() {
        return estadoActual;
    }

    public static String getNombreEstado(int estado) {
        switch (estado) {
            case MENU_PRINCIPAL: return "MENU_PRINCIPAL";
            case EN_JUEGO: return "EN_JUEGO";
            case PAUSA: return "PAUSA";
            case FIN_JUEGO: return "FIN_JUEGO";
            default: return "DESCONOCIDO";
        }
    }
}
