package utils;

public class Constants {

    // Tamaño de la ventana
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    // Jugador
    public static double PLAYER_SPEED = 5.0;
    public static final double PLAYER_JUMP_FORCE = 15.0;
    public static final double GRAVITY = 0.7;
    public static final int PLAYER_WIDTH = 40;
    public static final int PLAYER_HEIGHT = 60;

    // Suelo
    public static final int GROUND_LEVEL = 550; // coordenada Y del suelo

    // Enemigos
    public static final double ENEMY_SPEED = 3.0;
    public static final int ENEMY_WIDTH = 40;
    public static final int ENEMY_HEIGHT = 60;

    // Power-ups
    public static final int POWERUP_SIZE = 30;

    // Niveles
    public static final int TOTAL_LEVELS = 3;

    // Música y efectos de sonido (puedes poner rutas aquí)
    public static final String MUSIC_BACKGROUND = "resources/audio/background.wav";
    public static final String SOUND_JUMP = "resources/audio/jump.wav";
    public static final String SOUND_HIT = "resources/audio/hit.wav";

}
