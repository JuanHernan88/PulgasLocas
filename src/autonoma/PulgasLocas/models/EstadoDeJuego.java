/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

/**
 *
 * @author marib
 */
public class EstadoDeJuego {
    public static final int MENU_PRINCIPAL = 0;
    public static final int EN_JUEGO = 1;
    public static final int PAUSA = 2;
    public static final int FIN_JUEGO = 3;

    private static int estadoActual = MENU_PRINCIPAL;

    public static void cambiarEstado(int nuevoEstado) {
        estadoActual = nuevoEstado;
    }

    public static int getEstadoActual() {
        return estadoActual;
    }
}
