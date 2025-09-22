package entidades;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.Music;
import audio.Sound;
import engine.Camara;
import utils.Vector2D;

public class Boss {

    public enum TipoBoss {
        LEVEL1,
        LEVEL2,
        LEVEL3
    }

    public enum BossState {
        NORMAL,
        SHOOTING,
        DEAD
    }

    private Vector2D position;
    private int width, height;
    private double velX;
    private boolean movingRight = true;
    private Rectangle bounds;
    private TipoBoss tipo;
    private double baseY;  
    private double tiempo;
    private int vidaMaxima;
    private boolean activo = true;
    private int vida; 
    private Music musicas = new Music();

    private ArrayList<Proyectil> proyectiles = new ArrayList<>();
    private long lastShotTime = 0;

    private BossState state = BossState.NORMAL;
    private long lastStateChange = 0;

    public Boss(double x, double y, TipoBoss tipo) {
        this.position = new Vector2D(x, y);
        this.baseY = y; 
        this.tiempo = 0;
        this.tipo = tipo;
        this.width = 100;   
        this.height = 100;
        this.velX = 2;

        switch (tipo) {
            case LEVEL1: vidaMaxima = 10; break;
            case LEVEL2: vidaMaxima = 20; break;
            case LEVEL3: vidaMaxima = 30; break;
        }
        this.vida = vidaMaxima;
        this.bounds = new Rectangle((int)x, (int)y, width, height);
    }

    public void update(Jugador jugador) {
        if (!activo) return;

        // Movimiento flotante
        tiempo += 0.05;
        position.y = baseY + Math.sin(tiempo) * 20;

        // Disparo cada cierto tiempo
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime > 1500) {
            disparar(jugador);
            lastShotTime = currentTime;
            setState(BossState.SHOOTING);
            lastStateChange = currentTime;
        }
        // Volver a estado NORMAL después de disparar
        if (state == BossState.SHOOTING && currentTime - lastStateChange > 300) {
            setState(BossState.NORMAL);
        }

        // Actualizar proyectiles
        for (Proyectil p : proyectiles) p.update();
        proyectiles.removeIf(p -> !p.isActivo());

        // Actualizar hitbox
        bounds.setBounds((int)position.x, (int)position.y, width, height);
    }

    /**
     * Render con sprite. Si sprite es null, se usa el color magenta por defecto.
     */
    public void render(Graphics g, Camara camera, BufferedImage sprite, BufferedImage balaSprite) {
        // Dibujar sprite o fallback
        if (sprite != null) {
            g.drawImage(sprite,
                (int)(position.x - camera.getX()),
                (int)(position.y - camera.getY()),
                width, height, null);
        } else {
            g.setColor(Color.MAGENTA);
            g.fillRect(
                (int)(position.x - camera.getX()),
                (int)(position.y - camera.getY()),
                width, height
            );
        }

        // Dibujar barra de vida
        int barraX = (int)(position.x - camera.getX());
        int barraY = (int)(position.y - camera.getY()) - 10;
        int barraWidth = width;
        int barraHeight = 5;

        g.setColor(Color.RED);
        g.fillRect(barraX, barraY, barraWidth, barraHeight);

        g.setColor(Color.GREEN);
        int vidaAncho = (int)((vida / (double)vidaMaxima) * barraWidth);
        g.fillRect(barraX, barraY, vidaAncho, barraHeight);

        // Render de proyectiles (solo dibuja, no elimina)
        for (Proyectil p : proyectiles) {
            p.render(g, camera, balaSprite);
        }
    }


    private void disparar(Jugador jugador) {
    	int pos1= (int) Jugador.getPosition().x;
    	int pos2= (int) getPosition().x;
    	int dist = pos1-pos2 ;
    	if(dist<500) {
        musicas.plays("src/music/disparo.wav"); 
    	}
        double targetX = jugador.getPosition().x + jugador.getWidth() / 2;
        double targetY = jugador.getPosition().y + jugador.getHeight() / 2;

        double dirX = targetX - (position.x + width / 2);
        double dirY = targetY - (position.y + height / 2);

        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length;
        dirY /= length;

        double speed = 5;

        proyectiles.add(new Proyectil(
            position.x + width / 2,
            position.y + height / 2,
            dirX * speed,
            dirY * speed,
            300
        ));
    }

    public void recibirDaño(int cantidad) {
        vida -= cantidad;
        if (vida <= 0) {
            vida = 0;
            activo = false;
            setState(BossState.DEAD);
        }
    }

    // Getters y setters
    public Rectangle getBounds() { return bounds; }
    public TipoBoss getTipo() { return tipo; }
    public ArrayList<Proyectil> getProyectiles() { return proyectiles; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Vector2D getPosition() { return position; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public BossState getState() { return state; }
    public void setState(BossState s) { this.state = s; }
}
