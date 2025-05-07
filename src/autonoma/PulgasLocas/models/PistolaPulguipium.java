/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.interfaces.Arma;
import java.util.ArrayList;

/**
 *
 * @author marib
 */
public class PistolaPulguipium implements Arma {
    public int usar(ArrayList<Pulga> pulgas, int x, int y) {
        int eliminadas = 0;
        for (Pulga p : pulgas) {
            if (p.colisiona(new PulgaNormal(x, y))) {
                if (p.serImpactada()) {
                    eliminadas++;
                }
            }
        }
        return eliminadas;
    }
}
