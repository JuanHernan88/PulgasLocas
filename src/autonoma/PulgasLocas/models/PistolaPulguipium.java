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
            if (p.estaViva() && p.contienePunto(x, y)) {
                boolean estabaVivaAntes = p.estaViva(); 
                p.recibirImpacto();                     
                
                if (estabaVivaAntes && !p.estaViva()) { 
                    return 1; 
                }
                // Si fue impactada pero no murió (ej. mutante se transformó), no suma punto.
                // La función del arma termina al impactar la primera pulga.
                return 0; 
            }
        }
        return 0;
    }
} 
