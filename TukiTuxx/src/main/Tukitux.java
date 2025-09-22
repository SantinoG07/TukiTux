package main;

import javax.swing.*;
import audio.Music;
import java.awt.*;
import java.awt.image.BufferedImage;

import engine.Camara;
import engine.GameLoop;
import engine.InputHandler;
import engine.LevelManager;
import ui.Menu;

public class Tukitux extends JPanel {

    // Resolución virtual del juego
    public static final int VIRTUAL_WIDTH = 800;
    public static final int VIRTUAL_HEIGHT = 600;
    private Camara camara;
    private JFrame frame;
    private BufferedImage virtualCanvas;
    
    private GameLoop gameLoop;
    private int lvl;
    private InputHandler inputHandler;
    private LevelManager levelManager;
    private Image pauseBackground;
    

 // Nuevo estado
    private boolean paused = false;
    private int pauseSelection = 0; // 0 = Reanudar, 1 = Reiniciar, 2 = Salir
    private final String[] pauseOptions = {"Reanudar", "Reiniciar Nivel", "Salir al Menú"};
    private Rectangle[] pauseOptionBounds = new Rectangle[pauseOptions.length];
    
    public boolean isPaused() {
        return paused;
    }

    public Tukitux(String mapa, int nivel) {
        this.lvl = nivel; // guardamos el nivel actual
        virtualCanvas = new BufferedImage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        setFocusable(true);
        requestFocus();

        levelManager = new LevelManager(mapa, nivel); // ahora sí pasamos el valor correcto
        inputHandler = new InputHandler();

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                	case java.awt.event.KeyEvent.VK_ESCAPE:
                		paused = !paused; // alterna pausa
                		if (paused) {
                			levelManager.pauseMusic();   // 🔇 pausa música correctamente
                		} else {
                			levelManager.resumeMusic();  // 🔊 reanuda música
                		}
                        break;
                    case java.awt.event.KeyEvent.VK_UP:
                        if (paused) pauseSelection = (pauseSelection + pauseOptions.length - 1) % pauseOptions.length;
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        if (paused) pauseSelection = (pauseSelection + 1) % pauseOptions.length;
                        break;
                    case java.awt.event.KeyEvent.VK_ENTER:
                        if (paused) handlePauseSelection();
                        break;
                }

                // Mantenemos la funcionalidad original de InputHandler
                inputHandler.keyPressed(e);
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                inputHandler.keyReleased(e);
            }
        });

        gameLoop = new GameLoop(this, levelManager, inputHandler);
        gameLoop.start();
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (paused) {
                    Point virtualPoint = getVirtualMousePoint(e.getPoint()); // 🔹 convertir coordenadas
                    for (int i = 0; i < pauseOptionBounds.length; i++) {
                        if (pauseOptionBounds[i] != null && pauseOptionBounds[i].contains(virtualPoint)) {
                            pauseSelection = i;
                        }
                    }
                }
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (paused) {
                    Point virtualPoint = getVirtualMousePoint(e.getPoint()); // 🔹 convertir coordenadas
                    for (int i = 0; i < pauseOptionBounds.length; i++) {
                        if (pauseOptionBounds[i] != null && pauseOptionBounds[i].contains(virtualPoint)) {
                            pauseSelection = i;
                            handlePauseSelection(); // ejecuta la acción
                        }
                    }
                }
            }
        });
    }

    public static void startMenu() {
        SwingUtilities.invokeLater(() -> {
            new ui.Menu();
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        camara = new Camara(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        super.paintComponent(g);

        Graphics2D g2dVirtual = virtualCanvas.createGraphics();

        // Limpiamos canvas virtual
        g2dVirtual.setColor(Color.GRAY);
        g2dVirtual.fillRect(0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // 🔥 Actualizamos cámara siguiendo al jugador
        camara.update(levelManager.getJugador());

        // 🔥 Renderizamos juego con cámara
        levelManager.render(g2dVirtual, camara);

        // Si está pausado, dibujamos menú de pausa encima
        if (paused) {
            // Dibujar imagen de fondo escalada al tamaño del canvas
            g2dVirtual.drawImage(pauseBackground, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT, null);

            // Opcional: agregar un overlay semi-transparente para mejor contraste con los textos
            g2dVirtual.setColor(new Color(0, 0, 0, 120)); // negro semi-transparente
            g2dVirtual.fillRect(0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

            // Título
            g2dVirtual.setFont(new Font("Arial", Font.BOLD, 72));
            g2dVirtual.setColor(Color.WHITE);
            String title = "PAUSA";
            int titleWidth = g2dVirtual.getFontMetrics().stringWidth(title);
            g2dVirtual.drawString(title, (VIRTUAL_WIDTH - titleWidth) / 2, 150);
           
            // Opciones
            g2dVirtual.setFont(new Font("Arial", Font.PLAIN, 36));
            for (int i = 0; i < pauseOptions.length; i++) {
                String option = pauseOptions[i];
                int width = g2dVirtual.getFontMetrics().stringWidth(option);
                int height = g2dVirtual.getFontMetrics().getHeight();
                int x = (VIRTUAL_WIDTH - width) / 2;
                int y = 300 + i * 60;

                // Guardamos el rectángulo
                pauseOptionBounds[i] = new Rectangle(x, y - height + 10, width, height);

                // Opciones seleccionadas en amarillo
                g2dVirtual.setColor(i == pauseSelection ? Color.YELLOW : Color.WHITE);
                g2dVirtual.drawString(option, x, y);
            }
        }
        
        checkGameOver();
        checkLevelComplete();


        // Escalamos al tamaño real de la ventana
        g.drawImage(virtualCanvas, 0, 0, getWidth(), getHeight(), null);
    }


    

    public static void startGame(String mapa, int nivel) {
        // Pausar música del menú antes de iniciar el juego
        Menu.menuMusic.pause();
        Tukitux game = new Tukitux(mapa, nivel);

        JFrame frame = new JFrame("Tukitux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.add(game);
        game.setFrame(frame);
        frame.setResizable(false);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(frame);

        frame.setVisible(true);
    }
    
    private boolean gameOverShown = false;
    private boolean levelUpShown = false;
    
    public void checkGameOver() {
        if (!gameOverShown && (!levelManager.getJugador().isActivo() || levelManager.getJugador().getVidas() <= 0)) {
            gameOverShown = true;
            gameLoop.stop();

            SwingUtilities.invokeLater(() -> {
                if(frame != null) frame.dispose(); // <-- cerrar ventana actual
                new ui.GameOver(levelManager.getJugador().getPuntos(), () -> Tukitux.startGame(levelManager.getCurrentMap(), lvl), levelManager);
            });
        }
    }
    
    public void checkLevelComplete() {
        if (!levelUpShown && levelManager.isLevelComplete()) { // suponiendo que LevelManager tenga este método
            levelUpShown = true;
            gameLoop.stop();

            SwingUtilities.invokeLater(() -> {
                if (frame != null) frame.dispose(); // cerrar ventana actual
                new ui.LevelUp(() -> {
                    // Continuar al siguiente nivel
                    int siguienteNivel = lvl + 1; // o calculalo según tu lógica
                    String siguienteMapa = levelManager.getNextMap(); // necesitarías implementar esto en LevelManager
                    Tukitux.startGame(siguienteMapa, siguienteNivel);
                }, levelManager); // <-- PASAR levelManager
            });
        }
    }
    
    public void setFrame(JFrame f) {     
        this.frame = f;
    }


    private void handlePauseSelection() {
    	
        switch (pauseSelection) {
            case 0: // Reanudar
                paused = false;
                break;
            case 1: // Reiniciar Nivel
                gameLoop.restartLevel(lvl);
                paused = false;
                break;
            case 2: // Salir al Menú
                gameLoop.stop();
                SwingUtilities.invokeLater(() -> startMenu());
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame != null) topFrame.dispose();
                break;
        }
    }
    
    
    private Point getVirtualMousePoint(Point p) {
        double scaleX = (double) VIRTUAL_WIDTH / getWidth();
        double scaleY = (double) VIRTUAL_HEIGHT / getHeight();
        return new Point((int)(p.x * scaleX), (int)(p.y * scaleY));
    }
    
    public InputHandler getInputHandler() {
        return inputHandler;
    }


    // main solo abre el menú
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ui.Menu();
        });
    }
    
}