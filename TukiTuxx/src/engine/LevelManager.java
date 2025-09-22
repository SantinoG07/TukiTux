package engine;

import entidades.Bloques;
import entidades.Boss;
import entidades.Enemigo;
import entidades.Proyectil;
import graphics.SpriteManager;
import entidades.Jugador;
import utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import audio.Music;

public class LevelManager {
    private Jugador jugador;
    public static Boss boss;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Bloques> bloques;
    private int puntos = 0;
    private SpriteManager sprites;
    private int nivelActual = 1;
    private Music musicas = new Music();
    private Music musica = new Music();
    
    private boolean mostrarHitbox = false;

    private BufferedImage fondoNivel;
    private Image fondoAnimado;

    private String currentMap; // 🔥 guardamos el mapa actual
    private int tileSize = 40; // tamaño fijo de tiles
    
    public void setNivelSprites(int nivel) {
        if (sprites != null) {
            sprites.setNivel(nivel);
        }
    }


    public LevelManager(String rutaMapa, int nivel) {
        loadLevel(rutaMapa, nivel);
    }

    private void loadLevel(String rutaMapa, int nivel) {
        nivelActual = nivel; // 🔥 usar el nivel pasado desde Tukitux/Menu

        // Inicializamos sprites según nivel
        sprites = new SpriteManager(nivelActual);

        // Reiniciar estado del nivel
        this.currentMap = rutaMapa;
        boss = null;
        puntos = 0;
        jugador = new Jugador(100, 500);
        enemigos = new ArrayList<>();
        bloques = new ArrayList<>();

        // música según nivel
        switch (nivelActual) {
            case 1: musica.play("music/fucluv.wav"); break;
            case 2: musica.play("music/chainderoque.wav"); break;
            case 3: musica.play("music/hielo.wav"); break;
        }

        // Cargar mapa
        cargarMapa(rutaMapa, tileSize);

        // Cargar fondo según nivel usando getResourceAsStream
        try {
            String fondoPath = null;
            switch (nivelActual) {
                case 1:
                    fondoPath = "/img/fondoLVL/FondoFvckLove_1.gif";
                    break;
                case 2:
                    fondoPath = "/img/fondoLVL/FondoChainRoque.gif";
                    break;
                case 3:
                    fondoPath = "/img/fondoLVL/FondoHielo.gif";
                    break;
                default:
                    fondoPath = null;
            }
            if (fondoPath != null) {
                java.net.URL url = getClass().getResource(fondoPath);
                if (url != null) {
                    fondoAnimado = Toolkit.getDefaultToolkit().getImage(url);
                } else {
                    System.out.println("No se encontró el fondo animado: " + fondoPath);
                    fondoAnimado = null;
                }
            } else {
                fondoAnimado = null;
            }
        } catch (Exception e) {
            System.out.println("Error cargando fondo animado: " + e.getMessage());
            fondoAnimado = null;
        }
    }




    public void restartCurrentLevel(int nivel) {
        loadLevel(currentMap, nivel);
    }

    public void changeLevel(String nuevoMapa, int nivel) {
        loadLevel(nuevoMapa, nivel);
    }

    public void reiniciarNivelActual() {
        loadLevel(getCurrentMap(), getNivelActual());
    }

    private void cargarMapa(String ruta, int tileSize) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int fila = 0;

            while ((linea = br.readLine()) != null) {
                for (int col = 0; col < linea.length(); col++) {
                    char c = linea.charAt(col);
                    switch (c) {
                        // Bloques
                        case '1': 
                            bloques.add(new Bloques(col * tileSize, fila * tileSize, tileSize, tileSize, Bloques.Tipo.NORMAL));
                            break;
                        case '2': 
                            bloques.add(new Bloques(col * tileSize, fila * tileSize, tileSize, tileSize, Bloques.Tipo.VIDA));
                            break;
                        case '3': 
                            bloques.add(new Bloques(col * tileSize, fila * tileSize, tileSize, tileSize, Bloques.Tipo.MONEDA));
                            break;
                        case '4': 
                            bloques.add(new Bloques(col * tileSize, fila * tileSize, tileSize, tileSize, Bloques.Tipo.PELIGRO));
                            break;
                        // Enemigos
                        case '5': 
                            enemigos.add(new Enemigo(col * tileSize, fila * tileSize, 30, 30, 0.3, true, Enemigo.Tipo.BASICO));
                            break;
                        case '6': 
                            enemigos.add(new Enemigo(col * tileSize, fila * tileSize, 25, 25, 4, true, Enemigo.Tipo.RAPIDO));
                            break;
                        case '7': 
                            enemigos.add(new Enemigo(col * tileSize, fila * tileSize, 20, 50, 0.5, true, Enemigo.Tipo.TANQUE));
                            break;
                        case 'B':
                            boss = new Boss(col * tileSize, fila * tileSize, Boss.TipoBoss.LEVEL1);
                            break;
                    }
                }
                fila++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(InputHandler input) {
        if (input.isKeyPressed('H')) {
            mostrarHitbox = !mostrarHitbox;
        }
        if (!jugador.isActivo()) return;

        // Actualizar jugador
        jugador.update(input, bloques, boss, this);

        // Daño al boss
        if (boss != null && boss.isActivo()) {
            for (Proyectil p : jugador.getProyectiles()) {
                if (p.getBounds().intersects(boss.getBounds())) {
                    boss.recibirDaño(1);
                    p.setActivo(false);
                }
            }
        }

        // Actualizar enemigos
        for (Enemigo e : enemigos) {
            // Permitir actualización si está cayendo, aunque esté inactivo
            if (!e.isActivo() && !e.isCayendo()) continue;

            // Calcular distancia al jugador
            double dx = (e.getPosition().getX() + e.getWidth() / 2.0) - (jugador.getPosition().getX() + jugador.getWidth() / 2.0);
            double dy = (e.getPosition().getY() + e.getHeight() / 2.0) - (jugador.getPosition().getY() + jugador.getHeight() / 2.0);
            double distancia = Math.sqrt(dx * dx + dy * dy);

            // Si está cayendo, actualizar siempre; si no, solo si está cerca
            if (e.isCayendo() || distancia < 600) {
                e.update();
            }

            Rectangle enemigoRect = e.getBounds();

            // --- Colisión ENEMIGO vs BLOQUES ---
            for (Bloques b : bloques) {
                if (enemigoRect.intersects(b.getBounds())) {
                    switch (e.getTipo()) {
                    case RAPIDO:
                        // Ajustamos la posición para que no se "meta" en el bloque
                        if (e.isMovingRight()) {
                            e.setPositionX(b.getBounds().x - e.getWidth()); // coloca al enemigo a la izquierda del bloque
                        } else {
                            e.setPositionX(b.getBounds().x + b.getBounds().width); // coloca a la derecha del bloque
                        }

                        // Cambiamos la dirección después
                        e.setMovingRight(!e.isMovingRight());
                        break;


                        case BASICO:
                            // Rebote horizontal: si choca con un bloque, cambia de dirección
                            e.setMovingRight(!e.isMovingRight());

                            // Ajustamos la posición para que no se "meta" en el bloque
                            if (e.isMovingRight()) {
                                e.setPositionX(b.getBounds().x + b.getBounds().width);
                            } else {
                                e.setPositionX(b.getBounds().x - e.getWidth());
                            }
                            break;


                        case TANQUE:
                            // El tanque ignora bloques (solo se mueve vertical)
                            break;
                    }
                    break;
                }
            }

            // --- Colisión PROYECTILES del enemigo ---
            for (Proyectil p : e.getProyectiles()) {
            	
                if (jugador.isActivo() && p.getBounds().intersects(jugador.getBounds())) {
                    jugador.perderVida();
                    p.setActivo(false);
                }
                for (Bloques b : bloques) {
                    if (p.getBounds().intersects(b.getBounds())) {
                        p.setActivo(false);
                        break;
                    }
                }
            }
            e.getProyectiles().removeIf(p -> !p.isActivo());
        }

        // Eliminar enemigos que han caído fuera de la pantalla
        enemigos.removeIf(e -> e.isCayendo() && e.getPosition().y > 600); // 600 = altura ventana

        // Limpiar enemigos inactivos

        // --- Colisión JUGADOR vs ENEMIGOS ---
        for (Enemigo e : enemigos) {
            if (!jugador.isActivo()) break;

            Rectangle jugadorRect = jugador.getBounds();
            Rectangle enemigoRect = e.getBounds();

            if (jugadorRect.intersects(enemigoRect)) {
                if ((e.getTipo() == Enemigo.Tipo.BASICO || e.getTipo() == Enemigo.Tipo.RAPIDO) &&
                    jugador.getVelocity().y > 0 &&
                    jugadorRect.y + jugadorRect.height <= enemigoRect.y + enemigoRect.height) {
                    // El jugador pisa al enemigo
                    e.caer(); // Activar caída
                    e.setActivo(false); // Desactivar enemigo
                    jugador.rebotar();
                    musicas.plays("src/music/danobasico.wav");

                    switch (e.getTipo()) {
                        case BASICO:  puntos += 100; break;
                        case RAPIDO:  puntos += 150; break;
                    }

                } else if(e.isActivo()){
                    // Jugador recibe daño
                    jugador.perderVida();
                    if (jugadorRect.x < enemigoRect.x) {
                        jugador.setPositionX(jugador.getPosition().x - 100);
                    } else {
                        jugador.setPositionX(jugador.getPosition().x + 100);
                    }
                }
            }
        }

        // Colisión jugador vs bloques de peligro
        for (Bloques b : bloques) {
            if (b.getTipo() == Bloques.Tipo.PELIGRO && jugador.getBounds().intersects(b.getBounds())) {
                jugador.perderVida();
            }
        }

        // Eliminar monedas recogidas
        bloques.removeIf(b -> b.getTipo() == Bloques.Tipo.MONEDA && b.isActivado());

        // --- Actualizar Boss ---
        if (boss != null && boss.isActivo()) {
            boss.update(jugador);
            for (Proyectil p : boss.getProyectiles()) {
                if (jugador.isActivo() && p.getBounds().intersects(jugador.getBounds())) {
                    jugador.perderVida();
                    p.setActivo(false);
                }
            }
        }
    }



    public void render(Graphics g, Camara camera) {
        // Fondo animado escalado a pantalla completa
        if (fondoAnimado != null) {
            g.drawImage(fondoAnimado, 0, 0, 800, 600, null);
        } 

        // Bloques
        for (Bloques b : bloques) {
            BufferedImage sprite;

            if (b.getTipo() == Bloques.Tipo.VIDA && b.isActivado()) {
                // bloque de vida usado → roto
                sprite = sprites.getBloque("VIDAR");
            } else if (b.getTipo() == Bloques.Tipo.PELIGRO) {
                sprite = sprites.getBloque("PELIGRO");
            } else {
                // bloque normal según su tipo
                sprite = sprites.getBloque(b.getTipo().name());
            }

            if (sprite != null) {
                b.render(g, camera, sprite);
            }
        }
        // Jugador
        jugador.render(g, camera, sprites);
     // Enemigos
        for (Enemigo e : enemigos) {
            String estado;

            if (!e.isActivo()) {
                estado = "M"; // muerto
                if(e.getTipo() == Enemigo.Tipo.TANQUE) {
                    BufferedImage sprite = sprites.getEnemigo("TANQUE" + estado);
                    BufferedImage bala = sprites.getEnemigo("TANQUEB");
                    e.render(g, camera, sprite,bala);
				} else if(e.getTipo() == Enemigo.Tipo.BASICO) {
                    BufferedImage sprite = sprites.getEnemigo("BASICO" + estado);
                    e.render(g, camera, sprite,null);
				} else if(e.getTipo() == Enemigo.Tipo.RAPIDO) {
                    BufferedImage sprite = sprites.getEnemigo("RAPIDO" + estado);
                    e.render(g, camera, sprite,null);
				}
                
            }  else {
                estado = "V"; // vivo
                if(e.getTipo() == Enemigo.Tipo.TANQUE) {
                    BufferedImage sprite = sprites.getEnemigo("TANQUE" + estado);
                    BufferedImage bala = sprites.getEnemigo("TANQUEB");
                    e.render(g, camera, sprite,bala);
				} else if(e.getTipo() == Enemigo.Tipo.BASICO) {
                    BufferedImage sprite = sprites.getEnemigo("BASICO" + estado);
                    e.render(g, camera, sprite,null);
				} else if(e.getTipo() == Enemigo.Tipo.RAPIDO) {
                    BufferedImage sprite = sprites.getEnemigo("RAPIDO" + estado);
                    e.render(g, camera, sprite,null);
				}
                
            }

        }


        // Boss
        if (boss != null) {
            BufferedImage bossSprite;
            switch (boss.getState()) {
                case SHOOTING:
                    bossSprite = sprites.getEnemigo("BOSSD");
                    break;
                case DEAD:
                    bossSprite = sprites.getEnemigo("BOSSM");
                    break;
                case NORMAL:
                default:
                    bossSprite = sprites.getEnemigo("BOSS");
                    break;
            }
            BufferedImage bala = sprites.getEnemigo("TANQUEB");
            boss.render(g, camera, bossSprite, bala);
        }

        // HUD
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Vidas: " + jugador.getVidas(), 20, 30);
        g.setColor(Color.YELLOW);
        g.drawString("Puntos: " + puntos, 600, 30);
        
        // Mostrar hitboxes (debug)
        if (mostrarHitbox) {
            g.setColor(Color.RED);
            for (Enemigo e : enemigos) {
                Rectangle r = e.getBounds();
                g.drawRect(r.x - camera.getX(), r.y - camera.getY(), r.width, r.height);
            }
            if (boss != null && boss.isActivo()) {
                Rectangle r = boss.getBounds();
                g.drawRect(r.x - camera.getX(), r.y - camera.getY(), r.width, r.height);
            }
            // Hitbox del jugador
            g.setColor(Color.GREEN);
            Rectangle rj = jugador.getBounds();
            g.drawRect(rj.x - camera.getX(), rj.y - camera.getY(), rj.width, rj.height);
        }
    }


    public Jugador getJugador() {
        return jugador;
    }
    
    public boolean isLevelComplete() {
        // El nivel se considera completo solo si el boss está derrotado
        return boss != null && !boss.isActivo();
    }
    
    public String getNextMap() {
        // Mapeo manual de niveles a nombres de archivo
        switch (nivelActual) {
            case 1: return "levels/Mapa1.txt"; // Nivel 2
            case 2: return "levels/Mapa2.txt"; // Nivel 3
            default: return "levels/Mapa.txt"; // Nivel 1 o fallback
        }
    }
    
    public String getCurrentMap() {
        return currentMap;
    }
    public int getNivelActual() {
        return nivelActual;
    }
    
    public void pauseMusic() {
        musica.pause();
    }

    public void resumeMusic() {
        musica.resume();
    }

}