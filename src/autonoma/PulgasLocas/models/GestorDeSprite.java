/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autonoma.PulgasLocas.models;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marib
 */
public class GestorDeSprite {
    private static Map<String, Sprite> sprites = new HashMap<>();

    public static void cargarSprite(String id, String[] rutasImagenes) {
        sprites.put(id, new Sprite(rutasImagenes) {
            @Override
            public void dibujar(Graphics g) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });
    }

    public static Sprite obtenerSprite(String id) {
        return sprites.get(id);
    }
}