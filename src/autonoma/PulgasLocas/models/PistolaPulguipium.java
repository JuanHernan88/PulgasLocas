/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.interfaces.Arma;
import java.util.ArrayList;

public class PistolaPulguipium implements Arma {
    @Override
    public int usar(ArrayList<Pulga> pulgas, int x, int y) {
        for (Pulga p : pulgas) {
            // Verificar si la pulga está viva y si el clic la impacta
            if (p.estaViva() && p.contienePunto(x, y)) {
                boolean estabaVivaAntes = p.estaViva(); // Guardar estado antes del impacto
                p.recibirImpacto();                     // Aplicar el impacto (transforma o desactiva)
                
                // Otorgar punto solo si la pulga murió específicamente en este impacto
                if (estabaVivaAntes && !p.estaViva()) { 
                    return 1; // Se eliminó una pulga, suma 1 al puntaje
                }
                // Si fue impactada pero no murió (ej. mutante se transformó), no suma punto.
                // La función del arma termina al impactar la primera pulga.
                return 0; 
            }
        }
        return 0; // No se impactó ninguna pulga
    }
} 
