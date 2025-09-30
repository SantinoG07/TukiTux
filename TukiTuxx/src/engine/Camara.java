package engine;

import entidades.Jugador;

public class Camara {

    private double x; // posición de la cámara
	double y;
    private int screenWidth, screenHeight;

    public Camara(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(Jugador player) {
        // centramos al jugador en pantalla
        x = player.getPosition().getX() - screenWidth / 2f;
        y = player.getPosition().getY() - screenHeight / 2f;
    }

    public int getX() { return (int)x; }
    public int getY() { return (int)y; }
}
