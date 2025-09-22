package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private Clip clip;

    public Sound(String ruta) {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                System.err.println("No se encontró el archivo de sonido: " + ruta);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Formato de audio no soportado: " + ruta);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar sonido: " + ruta);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Línea de audio no disponible para: " + ruta);
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // detener si ya estaba sonando
            }
            clip.setFramePosition(0); // reinicia desde el principio
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
