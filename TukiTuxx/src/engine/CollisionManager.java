package engine;

import java.awt.Rectangle;

import java.util.List;

import entidades.Bloques;
import entidades.Enemigo;
import entidades.Jugador;

public class CollisionManager {

    // Colisión jugador con enemigos
    public static boolean checkPlayerEnemyCollision(Jugador jugador, List<Enemigo> enemigos) {
        Rectangle playerBounds = jugador.getBounds();
        for (Enemigo e : enemigos) {
            if (playerBounds.intersects(e.getBounds())) {
                return true; // jugador tocó a un enemigo
            }
        }
        return false;
    }

    // Colisión jugador con plataformas
    public static boolean checkPlayerPlatformCollision(Jugador jugador, List<Bloques> bloques) {
        Rectangle playerBounds = jugador.getBounds();
        for (Bloques p : bloques) {
            if (playerBounds.intersects(p.getBounds())) {
                return true; // jugador tocó el piso/plataforma
            }
        }
        return false;
    }
}
