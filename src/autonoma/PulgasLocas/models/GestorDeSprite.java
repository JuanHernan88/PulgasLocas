/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Clase encargada de cargar y gestionar los sprites (imágenes) del juego.
 * Evita cargar la misma imagen múltiples veces.
 */
public class GestorDeSprite {
    // Mapa para almacenar los sprites cargados, usando una clave (String) para identificarlos.
    private static Map<String, Sprite> sprites = new HashMap<>();

    // Definir claves constantes para los sprites para evitar errores de tipeo
    public static final String PULGA_NORMAL_SPRITE_KEY = "PULGA_NORMAL";
    public static final String PULGA_MUTANTE_SPRITE_KEY = "PULGA_MUTANTE";

    /**
     * Carga todos los sprites necesarios al inicio del juego.
     * Debe llamarse una vez antes de empezar a solicitar sprites.
     */
    public static void precargarSprites() {
        System.out.println("Precargando sprites...");
        // --- Rutas corregidas según tu estructura de paquetes VISTA EN LA IMAGEN ---
        // Asegúrate que los nombres de archivo (Pulga_N.png, Pulga_M.png) sean EXACTOS.
        cargarSpriteEstatico(PULGA_NORMAL_SPRITE_KEY, "/autonoma/PulgasLocas/resources/Pulga_N.png"); 
        cargarSpriteEstatico(PULGA_MUTANTE_SPRITE_KEY, "/autonoma/PulgasLocas/resources/Pulga_M.png"); 
        // ----------------------------------------------------------------------------
        System.out.println("Sprites precargados.");
    }

    /**
     * Carga una imagen estática (un solo frame) desde un archivo de recurso
     * y la almacena en el mapa de sprites con la clave dada.
     * * @param clave La clave única para identificar este sprite.
     * @param rutaArchivo La ruta del archivo de imagen DENTRO del classpath (ej. "/ruta/imagen.png").
     */
    private static void cargarSpriteEstatico(String clave, String rutaArchivo) {
        InputStream is = null;
        try {
            // Obtener el InputStream del recurso usando la ruta proporcionada
            System.out.println("Intentando cargar recurso estático: " + rutaArchivo); 
            is = GestorDeSprite.class.getResourceAsStream(rutaArchivo);
            
            // Verificar si el recurso se encontró
            if (is == null) {
                System.err.println("Error Crítico: No se pudo encontrar el recurso en la ruta: " + rutaArchivo);
                System.err.println("Verifica la ruta completa (incluyendo el paquete si es necesario), el nombre exacto del archivo (mayúsculas/minúsculas) y la configuración del classpath en tu IDE (NetBeans).");
                return; // Salir del método si no se encuentra el archivo
            }
            
            // Leer la imagen desde el InputStream
            BufferedImage imagen = ImageIO.read(is); 
            
            // Verificar si la imagen se leyó correctamente
            if (imagen != null) {
                // Crear un nuevo objeto Sprite con la imagen cargada
                sprites.put(clave, new Sprite(imagen)); 
                System.out.println("-> Sprite estático cargado exitosamente: " + clave);
            } else {
                System.err.println("Error: ImageIO.read devolvió null para el recurso: " + rutaArchivo + ". El archivo podría estar corrupto o no ser un formato de imagen válido.");
            }
        } catch (IOException e) {
            // Capturar errores durante la lectura del archivo
            System.err.println("Error de IO al cargar el sprite estático '" + clave + "' desde " + rutaArchivo + ": " + e.getMessage());
            e.printStackTrace(); // Mostrar más detalles del error
        } catch (IllegalArgumentException e) {
             // Capturar errores si la ruta del recurso es inválida
             System.err.println("Error: La ruta del recurso es inválida para '" + rutaArchivo + "': " + e.getMessage());
        } finally {
            // Asegurarse de cerrar el InputStream para liberar recursos
            if (is != null) { 
                try { 
                    is.close(); 
                } catch (IOException e) { 
                    // Ignorar errores al cerrar, ya que el error principal ya ocurrió si lo hubo.
                    System.err.println("Advertencia: No se pudo cerrar el InputStream para " + rutaArchivo);
                } 
            }
        }
    }

    /**
     * Obtiene un sprite previamente cargado usando su clave.
     * * @param clave La clave del sprite a obtener.
     * @return El objeto Sprite correspondiente, o null si la clave no existe.
     */
    public static Sprite obtenerSprite(String clave) {
        Sprite sprite = sprites.get(clave);
        if (sprite == null) {
            // Advertir si se pide un sprite que no fue cargado (puede indicar un error)
            System.err.println("Advertencia: Se solicitó un sprite con clave '" + clave + "' que no está cargado o no se pudo cargar. Se retornará null.");
        }
        return sprite;
    }

    // --- Opcional: Métodos para cargar sprites animados (si los necesitas en el futuro) ---
    /*
    private static void cargarSpriteAnimado(String clave, String rutaArchivo, int numeroDeFrames, int anchoFrame, int altoFrame, long duracionAnimacionMs) {
         InputStream is = null;
        try {
            is = GestorDeSprite.class.getResourceAsStream(rutaArchivo);
            if (is == null) {
                System.err.println("Error: No se pudo encontrar el recurso de imagen: " + rutaArchivo);
                return;
            }
            BufferedImage spriteSheet = ImageIO.read(is);
            if (spriteSheet != null) {
                 sprites.put(clave, new Sprite(spriteSheet, numeroDeFrames, anchoFrame, altoFrame, duracionAnimacionMs));
                 System.out.println("Sprite animado cargado: " + clave);
            } else {
                 System.err.println("Error: ImageIO.read devolvió null para spritesheet: " + rutaArchivo);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar el sprite animado " + clave + " desde " + rutaArchivo + ": " + e.getMessage());
        } finally {
             if (is != null) { try { is.close(); } catch (IOException e) {  } }
        }
    }
    */
}