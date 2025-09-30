package entidades;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.image.BufferedImage;

import engine.Camara;

public class Bloques {
    private boolean activado = false;

    public enum Tipo {
        NORMAL,   // bloque común
        VIDA,     // da vida
        MONEDA,   // da monedas
        PELIGRO   // pinchos, etc
    }

    private int x, y, ancho, alto;
    private Rectangle hitbox;
    private Tipo tipo;

    public boolean isActivado() { return activado; }
    public void setActivado(boolean activado) { this.activado = activado; }

    public Bloques(int x, int y, int ancho, int alto, Tipo tipo) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = tipo;
        hitbox = new Rectangle(x, y, ancho, alto);
    }

    /**
     * Render con sprite. Si sprite es null, dibuja el bloque con colores por defecto.
     */
    public void render(Graphics g, Camara camera, BufferedImage sprite) {
        if (sprite != null) {
            g.drawImage(sprite, x - camera.getX(), y - camera.getY(), ancho, alto, null);
        } else {
            // fallback de color
            switch (tipo) {
                case NORMAL: g.setColor(Color.DARK_GRAY); break;
                case VIDA: g.setColor(Color.RED); break;
                case MONEDA: g.setColor(Color.YELLOW); break;
                case PELIGRO: g.setColor(Color.ORANGE); break;
            }
            g.fillRect(x - camera.getX(), y - camera.getY(), ancho, alto);
        }
    }

    public Rectangle getBounds() {
        return hitbox;
    }

    public Tipo getTipo() {
        return tipo;
    }
}
