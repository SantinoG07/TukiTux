package entidades;

import java.util.ArrayList;

import audio.Music;
import audio.Sound;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.image.BufferedImage;
import utils.Vector2D;
import engine.Camara;

public class Enemigo {

    public enum Tipo {
        BASICO,
        RAPIDO,
        TANQUE
    }

    private Vector2D position;
    private Music musicas = new Music();
    private int width, height;
    private double velX;
    private boolean horizontal, movingRight = true;
    private Rectangle bounds;
    private Tipo tipo;

    private ArrayList<Proyectil> proyectiles = new ArrayList<>();
    private long lastShotTime = 0;

    private boolean activo = true;
    private boolean cayendo = false;
    private double velocidadCaida = 5.0;

    public Enemigo(double x, double y, int width, int height, double speedX, boolean horizontal, Tipo tipo) {
        this.position = new Vector2D(x, y);
        this.width = width;
        this.height = height;
        this.velX = speedX;
        this.horizontal = horizontal;
        this.tipo = tipo;
        this.bounds = new Rectangle((int)x, (int)y, width, height);

        // 🔥 Ajustar arranque del tanque
        if (tipo == Tipo.TANQUE) {
            this.position.y = 300; // empieza más arriba (podés cambiar el valor)
        }
    }
    private int jumpCounter = 0;      // contador del salto
    private int jumpHeight = 25;      // altura del salto
    private int jumpSpeed = 1;        // velocidad vertical del salto
    private boolean goingUp = true; 

    private boolean movingDown = true;

    public void caer() {
        cayendo = true;
    }

    public void update() {
        if (cayendo) {
            position.y += velocidadCaida;
            // Aquí puedes agregar lógica para eliminar el enemigo si sale de la pantalla
            return;
        }
        // Movimiento horizontal y vertical según tipo
        if (horizontal) {
        	if (tipo == Tipo.BASICO) {
            // movimiento horizontal
            position.x += movingRight ? velX : -velX;
            if (position.x <= 0) movingRight = true;
            else if (position.x + width >= 800) movingRight = false;

            // movimiento vertical tipo "salito"
            if (goingUp) {
                position.y -= jumpSpeed;
                jumpCounter += jumpSpeed;
                if (jumpCounter >= jumpHeight) goingUp = false;
            } else {
                position.y += jumpSpeed;
                jumpCounter -= jumpSpeed;
                if (jumpCounter <= 0) goingUp = true;
            }
        }
        	else if (tipo == Tipo.RAPIDO) {
                position.x += movingRight ? velX : -velX;
                if (position.x <= 0) movingRight = true;
                else if (position.x + width >= 800) movingRight = false;
            } else if (tipo == Tipo.TANQUE) {
                // Movimiento vertical
                int velocidadVertical = 1;
                double minY = 300;
                double maxY = 400;

                if (movingDown) {
                    position.y += velocidadVertical;
                    if (position.y >= maxY) {
                        position.y = maxY;
                        movingDown = false;
                    }
                } else {
                    position.y -= velocidadVertical;
                    if (position.y <= minY) {
                        position.y = minY;
                        movingDown = true;
                    }
                }
            }
        }

        // Disparo TANQUE
        if (tipo == Tipo.TANQUE) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastShotTime > 2000) {
                disparar();
                lastShotTime = currentTime;
            }
        }

        // Actualizar proyectiles
        for (Proyectil p : proyectiles) p.update();
        proyectiles.removeIf(p -> !p.isActivo());

        // Actualizar hitbox
        bounds.setBounds((int)position.x, (int)position.y, width, height);
    }


    /**
     * Render con sprite. Si sprite es null, usa colores por defecto.
     */
    public void render(Graphics g, Camara camera, BufferedImage sprite, BufferedImage balaSprite) {
        // Render del enemigo
        if (sprite != null) {
            g.drawImage(sprite, (int)(position.x - camera.getX()), (int)(position.y - camera.getY()), width, height, null);
        } else {
            switch (tipo) {
                case BASICO: g.setColor(Color.RED); break;
                case RAPIDO: g.setColor(Color.BLUE); break;
                case TANQUE: g.setColor(Color.GREEN); break;
            }
            g.fillRect((int)(position.x - camera.getX()), (int)(position.y - camera.getY()), width, height);
        }

        // Render proyectiles con sprite
        for (Proyectil p : proyectiles) {
            p.render(g, camera, balaSprite);
        }
    }


    private void disparar() {
    	int pos1= (int) Jugador.getPosition().x;
    	int pos2= (int) getPosition().x;
    	int dist = pos1-pos2 ;
    	if(dist<500) {
        musicas.plays("src/music/disparo.wav"); 
    	}
        proyectiles.add(new Proyectil(position.x, position.y + height / 2, -3, 0, 150));
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    // Getters y setters
    public Rectangle getBounds() { return bounds; }
    public Tipo getTipo() { return tipo; }
    public Vector2D getPosition() { return position; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setPosition(Vector2D pos) { this.position = pos; }
    public void setPositionX(double x) { this.position.x = x; }
    public void setPositionY(double y) { this.position.y = y; }
    public ArrayList<Proyectil> getProyectiles() { return proyectiles; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo;}
    public boolean isCayendo() { return cayendo; }
}