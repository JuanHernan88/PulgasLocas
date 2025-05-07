/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestorDeArchivos {
    private static final String NOMBRE_ARCHIVO_PUNTAJE = "puntaje.txt";

    public static void guardarPuntaje(int puntaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO_PUNTAJE))) {
            writer.write(String.valueOf(puntaje));
        } catch (IOException e) {
            System.err.println("Error al guardar el puntaje: " + e.getMessage());
        }
    }

    public static int cargarPuntajeMaximo() {
        File archivo = new File(NOMBRE_ARCHIVO_PUNTAJE);
        if (!archivo.exists()) {
            return 0; // Si no existe el archivo, el puntaje máximo es 0
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea = reader.readLine();
            if (linea != null && !linea.trim().isEmpty()) {
                return Integer.parseInt(linea.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error al cargar el puntaje máximo: " + e.getMessage() + ". Se devolverá 0.");
        }
        return 0;
    }
}