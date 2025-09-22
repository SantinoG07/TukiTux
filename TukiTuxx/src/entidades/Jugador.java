package entidades;

import engine.Camara;
import graphics.SpriteManager;
import engine.InputHandler;
import utils.Vector2D;
import utils.Constants;
import engine.LevelManager;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.Music;
import audio.Sound;

public class Jugador {

    private static Vector2D position;
    private Vector2D velocity;
    private Music musica = new Music();

    private boolean activo = true;
    	
    private int width = 40;
    private int height = 60;
    public int vidas = 3;
    
    private Music musicas = new Music();

    private boolean onGround = false;
    private boolean mirandoIzq = false;
    private boolean saltando = false;
    private boolean reboteActivo = false;


    
    private ArrayList<Proyectil> proyectiles = new ArrayList<>();

    public Jugador(int startX, int startY) {
        Jugador.position = new Vector2D(startX, startY);
        this.velocity = new Vector2D(0, 0);
    }

    public void update(InputHandler input, java.util.List<Bloques> bloques, Boss boss, LevelManager levelManager) {
        // Movimiento horizontal
        if (!reboteActivo) {
            if (input.left()) {
                velocity.x = -Constants.PLAYER_SPEED;
                mirandoIzq = true;
            } else if (input.right()) {
                velocity.x = Constants.PLAYER_SPEED;
                mirandoIzq = false;
            } else {
                velocity.x = 0;
            }
        }

        // Salto
        if (input.jump() && onGround) {
            velocity.y = -Constants.PLAYER_JUMP_FORCE;
            onGround = false;
            saltando = true;
        }

        // Cuando toca el suelo
        // if (position.y + height >= Constants.GROUND_LEVEL) {
        //     position.y = Constants.GROUND_LEVEL - height;
        //     velocity.y = 0;
        //     onGround = true;
        //     saltando = false;
        //     reboteActivo = false;
        // }

        // Gravedad
        velocity.y += Constants.GRAVITY;

        // Movimiento en X
        position.x += velocity.x;
        Rectangle jugadorRect = getBounds();
        for (Bloques b : bloques) {
            if (jugadorRect.intersects(b.getBounds())) {
                if (velocity.x > 0) position.x = b.getBounds().x - width;
                else if (velocity.x < 0) position.x = b.getBounds().x + b.getBounds().width;
            }
        }

        // Movimiento en Y
        position.y += velocity.y;
        jugadorRect = getBounds();
        onGround = false;
        for (Bloques b : bloques) {
            if (jugadorRect.intersects(b.getBounds())) {
                if (velocity.y > 0) {
                    position.y = b.getBounds().y - height;
                    velocity.y = 0;
                    onGround = true;
                    saltando = false;
                    reboteActivo = false;
                } else if (velocity.y < 0) {
                    position.y = b.getBounds().y + b.getBounds().height;
                    velocity.y = 0;
                }
            }
        }

        // Reiniciar posición si cae al vacío
        if (position.y > 1000) {
            position.x = 100;
            position.y = 500;
            velocity.x = 0;
            velocity.y = 0;
            vidas--; // Pierde una vida al caer al vacío
        }

        for (Bloques b : bloques) {
            if (jugadorRect.intersects(b.getBounds())) {
                if (velocity.y > 0) { // cayendo sobre el bloque
                	musicas.plays("src/music/danobasico.wav");
                    position.y = b.getBounds().y - height;
                    velocity.y = 0;
                    onGround = true;
                    saltando = false;
                } 
                if (jugadorRect.intersects(b.getBounds()) && !b.isActivado()) {
                    switch (b.getTipo()) {
                    case VIDA:
                    	if(!onGround) {
                        new Sound("src/music/bloqueR.wav").play();

                        if (Math.random() < 0.5) {
                            // 👉 Opción 1: dar vida
                            sumarVida();
                            System.out.println("Vida sumada! Vidas actuales: " + vidas);
                        } else {
                            // 👉 Opción 2: boost de velocidad temporal
                        	Constants.PLAYER_SPEED=8.5;
                            System.out.println("¡Velocidad aumentada temporalmente!");
                            new Thread(() -> {
                                try { Thread.sleep(5000); } 
                                catch (InterruptedException e) { e.printStackTrace(); }
                            	Constants.PLAYER_SPEED=5.0;
                                System.out.println("Boost de velocidad terminó.");
                            }).start();
                        }

                        b.setActivado(true);
                    }
                        break;
                    case PELIGRO:
                        new Sound("src/music/bloqueR.wav").play();

						perderVida();
						System.out.println("Vida perdida! Vidas actuales: " + vidas);

                        b.setActivado(true);
                        break;


                        case MONEDA:
                            if (LevelManager.boss != null && LevelManager.boss.isActivo()) {
                                LevelManager.boss.recibirDaño(1);
                                System.out.println("Boss recibe daño! Vida actual: " + LevelManager.boss.getVida());
                                b.setActivado(true);
                            }
                            break;



                        default:
                            break;
                    }
                }


            }
        }
        // Colisión con suelo base (al final del update)
        // if (position.y + height >= Constants.GROUND_LEVEL) {
        //     position.y = Constants.GROUND_LEVEL - height;
        //     velocity.y = 0;
        //     onGround = true;
        // }
    }



	

    public void render(Graphics g, Camara camera, SpriteManager sprites) {
        BufferedImage sprite;

        if (saltando) {
            sprite = sprites.getJugadorJump();
        } else if (velocity.x == 0) {
            sprite = sprites.getJugadorq(); // quieto
        } else {
            sprite = sprites.getJugador(); // caminando
        }

        // fallback si sprite es null
        if (sprite == null) {
            g.setColor(Color.BLUE);
            g.fillRect((int)(position.x - camera.getX()), (int)(position.y - camera.getY()), width, height);
            return;
        }

        int drawX = (int)(position.getX() - camera.getX());
        int drawY = (int)(position.getY() - camera.getY());

        if (mirandoIzq) {
            g.drawImage(sprite,
                drawX + width, drawY,
                drawX, drawY + height,
                0, 0, sprite.getWidth(), sprite.getHeight(),
                null
            );
        } else {
            g.drawImage(sprite,
                drawX, drawY,
                drawX + width, drawY + height,
                0, 0, sprite.getWidth(), sprite.getHeight(),
                null
            );
        }
    }





    public static Vector2D getPosition() {
        return position;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Rectangle getBounds() {
        return new Rectangle((int) position.x, (int) position.y, width, height);
    }

    public void setPosition(Vector2D pos) { this.position = pos; }
    public Vector2D getVelocity() { return velocity; }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas) { this.vidas = vidas; }
    public void resetVidas() { vidas = 3; }
    
    public void perderVida() {
    	musica.plays("src/music/danoalpj.wav");
    	setVidas(vidas - 1);
		if (vidas <= 0) {
			activo = false;
			System.out.println("Jugador ha perdido todas las vidas y está inactivo.");
		} else {
			System.out.println("Jugador ha perdido una vida. Vidas restantes: " + vidas);
		}
    }
 // Cambiar solo la coordenada X
    public void setPositionX(double x) {
        this.position.x = x;
    }
    
    // Cambiar solo la coordenada Y
    public void setPositionY(double y) {
        this.position.y = y;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public void rebotar() {
        velocity.y = -Constants.PLAYER_JUMP_FORCE / 2;
        reboteActivo = true;
        if (mirandoIzq) {
            velocity.x = -Constants.PLAYER_SPEED;
        } else {
            velocity.x = Constants.PLAYER_SPEED;
        }
    }

	public void sumarVida() {
        vidas= vidas+1;
	}
	
	public ArrayList<Proyectil> getProyectiles() {
	    return proyectiles;
	}

	public int getPuntos() {
		// TODO Auto-generated method stub
		return 0;
	}


}