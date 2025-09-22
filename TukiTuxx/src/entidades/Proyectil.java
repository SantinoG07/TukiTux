package entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import audio.Music;
import engine.Camara;

public class Proyectil {
    private double x, y;
    private double velX, velY;
    private int width = 5, height = 5;
    private double startX, startY;
    private double rango;
    private boolean activo = true;

    public Proyectil(double x, double y, double velX, double velY, double rango) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.rango = rango;

        this.startX = x;
        this.startY = y;
    }

    public void update() {
        if (!activo) return;

        x += velX;
        y += velY;

        double dx = x - startX;
        double dy = y - startY;
        double distancia = Math.sqrt(dx * dx + dy * dy);

        if (distancia >= rango) {
            activo = false; 
        }
    }

    public void render(Graphics g, Camara camera, BufferedImage sprite) {
        if (sprite != null) {
            g.drawImage(sprite, (int)(x - camera.getX()), (int)(y - camera.getY()), width, height, null);
        } else {
            // fallback: dibujar rectángulo si no hay sprite
            g.setColor(Color.RED);
            g.fillRect((int)(x - camera.getX()), (int)(y - camera.getY()), width, height);
        }
    
}

    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
