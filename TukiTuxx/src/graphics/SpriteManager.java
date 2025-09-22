package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class SpriteManager {

    private HashMap<String, BufferedImage> bloques;
    private HashMap<String, BufferedImage> enemigos;
    private BufferedImage jugadorJump;
    private BufferedImage boss;
    private BufferedImage jugador; // 🔥 sprite del jugador
    public static BufferedImage jugadorq; // 🔥 sprite del jugador
    

    public SpriteManager(int nivel) {
        bloques = new HashMap<>();
        enemigos = new HashMap<>();
        cargarSprites(nivel);
    }

    private void cargarSprites(int nivel) {
        String basePath = "/img";
        String levelFolder = "/bloques/Lvl_" + nivel;

        try {
            // Bloques
            bloques.put("NORMAL", loadImage(basePath + levelFolder + "/Bloque.png"));
            bloques.put("VIDA", loadImage(basePath + levelFolder + "/BloqueB.png"));
            bloques.put("VIDAR", loadImage(basePath + levelFolder + "/BloqueBR.png"));
            bloques.put("MONEDA", loadImage(basePath + levelFolder + "/button.png"));
            bloques.put("PELIGRO", loadImage(basePath + levelFolder + "/Peligro.png"));

            // Jugador 🔥 (ruta común, no depende de nivel)
            jugador = loadImage(basePath + "/jugador/jugadorM.png");
            jugadorJump = loadImage(basePath + "/jugador/jugadorS.png");
            jugadorq = loadImage(basePath + "/jugador/jugador.png");


            enemigos.put("RAPIDOV", loadImage("/img/Normales/Lvl_"+nivel+ "/RapidoV.png"));
            enemigos.put("RAPIDOM", loadImage("/img/Normales/Lvl_"+nivel+ "/RapidoM.png"));
            enemigos.put("BASICOV", loadImage("/img/Normales/Lvl_"+nivel+ "/BasicoV.png"));
            enemigos.put("BASICOM", loadImage("/img/Normales/Lvl_"+nivel+ "/BasicoM.png"));
            enemigos.put("TANQUEV", loadImage("/img/Normales/Lvl_"+nivel+ "/Tanque.png"));
            enemigos.put("TANQUEB", loadImage("/img/Normales/Lvl_"+nivel+ "/TanqueB.png"));
            enemigos.put("BOSS", loadImage("/img/Jefes/Lvl_"+nivel+ "/normal.png"));
            enemigos.put("BOSSD", loadImage("/img/Jefes/Lvl_"+nivel+ "/disparando.png"));
            enemigos.put("BOSSM", loadImage("/img/Jefes/Lvl_"+nivel+ "/muerto.png"));
        } catch (IOException e) {
            System.err.println("Error cargando sprites para nivel " + nivel);
            e.printStackTrace();
        }
    }

    // Método auxiliar
    private BufferedImage loadImage(String path) throws IOException {
        var stream = getClass().getResourceAsStream(path);
        if (stream == null) throw new IOException("No se encontró el recurso: " + path);
        return ImageIO.read(stream);
    }

    public BufferedImage getBloque(String tipo) {
        return bloques.get(tipo);
    }

    public BufferedImage getEnemigo(String tipo) {
        return enemigos.get(tipo);
    }

    public BufferedImage getBoss() {
        return boss;
    }
    public void setNivel(int nivel) {
        bloques.clear();
        enemigos.clear();
        cargarSprites(nivel);
    }

    public BufferedImage getJugador() {
        return jugador;
    }
    public BufferedImage getJugadorJump() { return jugadorJump; }

	public BufferedImage getJugadorq() {
        return jugadorq;
	}
}