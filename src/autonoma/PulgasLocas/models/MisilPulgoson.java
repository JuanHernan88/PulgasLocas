/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import autonoma.PulgasLocas.interfaces.Arma;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MisilPulgoson implements Arma {
    @Override
    public int usar(ArrayList<Pulga> todasLasPulgas, int x, int y) { 
        
        // Filtrar solo las pulgas vivas
        List<Pulga> pulgasVivas = todasLasPulgas.stream()
                                             .filter(Pulga::estaViva)
                                             .collect(Collectors.toList());

        if (pulgasVivas.isEmpty()) {
            return 0; // No hay pulgas vivas para destruir
        }
        
        ReproductorSonido.reproducir("bomba.wav");

        // Calcular el 50% (redondeando hacia arriba)
        int numeroADestruir = (int) Math.ceil(pulgasVivas.size() * 0.5);
        
        Collections.shuffle(pulgasVivas);

        int pulgasRealmenteDestruidas = 0;
        for (int i = 0; i < numeroADestruir && i < pulgasVivas.size(); i++) {
            Pulga pulgaSeleccionada = pulgasVivas.get(i);
            pulgaSeleccionada.destruir(); 
            pulgasRealmenteDestruidas++;
        }

        return pulgasRealmenteDestruidas; // Devuelve cuántas se destruyeron para sumar al puntaje
    }
}