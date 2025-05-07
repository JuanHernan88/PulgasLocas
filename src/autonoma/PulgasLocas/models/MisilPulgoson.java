/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.util.ArrayList;

/**
 *
 * @author marib
 */
public class MisilPulgoson implements Arma {
    public int usar(ArrayList<Pulga> pulgas, int x, int y) {
        int eliminadas = 0;
        for (Pulga p : pulgas) {
            if (Math.abs(p.getX() - x) < 50 && Math.abs(p.getY() - y) < 50) {
                if (p.serImpactada()) {
                    eliminadas++;
                }
            }
        }
        return eliminadas;
    }
}