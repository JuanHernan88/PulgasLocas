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
    private static Map<String, Sprite> sprites = new HashMap<>();

    public static final String PULGA_NORMAL_SPRITE_KEY = "PULGA_NORMAL";
    public static final String PULGA_MUTANTE_SPRITE_KEY = "PULGA_MUTANTE";

    /**
     * Carga todos los sprites necesarios al inicio del juego.
     * Debe llamarse una vez antes de empezar a solicitar sprites.
     */
    public static void precargarSprites() {
        System.out.println("Precargando sprites...");
        cargarSpriteEstatico(PULGA_NORMAL_SPRITE_KEY, "/autonoma/PulgasLocas/resources/Pulga_N.png"); 
        cargarSpriteEstatico(PULGA_MUTANTE_SPRITE_KEY, "/autonoma/PulgasLocas/resources/Pulga_M.png"); 
        System.out.println("Sprites precargados.");
    }

    private static void cargarSpriteEstatico(String clave, String rutaArchivo) {
        InputStream is = null;
        try {
            System.out.println("Intentando cargar recurso estático: " + rutaArchivo); 
            is = GestorDeSprite.class.getResourceAsStream(rutaArchivo);
            
            if (is == null) {
                System.err.println("Error Crítico: No se pudo encontrar el recurso en la ruta: " + rutaArchivo);
                System.err.println("Verifica la ruta completa (incluyendo el paquete si es necesario), el nombre exacto del archivo (mayúsculas/minúsculas) y la configuración del classpath en tu IDE (NetBeans).");
                return;
            }
            
            BufferedImage imagen = ImageIO.read(is); 
            
            if (imagen != null) {
                sprites.put(clave, new Sprite(imagen)); 
                System.out.println("-> Sprite estático cargado exitosamente: " + clave);
            } else {
                System.err.println("Error: ImageIO.read devolvió null para el recurso: " + rutaArchivo + ". El archivo podría estar corrupto o no ser un formato de imagen válido.");
            }
        } catch (IOException e) {
            System.err.println("Error de IO al cargar el sprite estático '" + clave + "' desde " + rutaArchivo + ": " + e.getMessage());
            e.printStackTrace(); 
        } catch (IllegalArgumentException e) {
             System.err.println("Error: La ruta del recurso es inválida para '" + rutaArchivo + "': " + e.getMessage());
        } finally {
            if (is != null) { 
                try { 
                    is.close(); 
                } catch (IOException e) { 
                    System.err.println("Advertencia: No se pudo cerrar el InputStream para " + rutaArchivo);
                } 
            }
        }
    }

    public static Sprite obtenerSprite(String clave) {
        Sprite sprite = sprites.get(clave);
        if (sprite == null) {
            System.err.println("Advertencia: Se solicitó un sprite con clave '" + clave + "' que no está cargado o no se pudo cargar. Se retornará null.");
        }
        return sprite;
    }
}