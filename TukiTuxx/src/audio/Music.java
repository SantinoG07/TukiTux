package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    private Clip clip;

    public void play(String ruta) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            if (clip != null) {
                clip.close();
            }

            // Cargar el audio como recurso (funciona en JAR y en desarrollo)
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url == null) {
                System.err.println("No se encontró el archivo de música: " + ruta);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 🔁 música en bucle
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void plays(String ruta) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
            if (clip != null) {
                clip.close();
            }

            File archivo = new File(ruta);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
    
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // pausa la música
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start(); // reanuda desde donde quedó
        }
    }
}