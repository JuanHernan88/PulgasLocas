package autonoma.PulgasLocas.models;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class ReproductorSonido {
    public static void reproducir(String nombreArchivo) {
        try {
            
            String ruta = "src/autonoma/PulgasLocas/sonidos/" + nombreArchivo;
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                System.err.println("Archivo no encontrado: " + archivo.getAbsolutePath());
                return;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();

            System.out.println("Reproduciendo: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
