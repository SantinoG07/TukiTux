package engine;

import main.Tukitux;
import engine.LevelManager;

public class GameLoop implements Runnable {

    private static final int FPS = 60;
    private Thread thread;
    private boolean running = false;

    private Tukitux game;
    private LevelManager levelManager;
    private InputHandler inputHandler;

    public GameLoop(Tukitux game, LevelManager levelManager, InputHandler inputHandler) {
        this.game = game;
        this.levelManager = levelManager;
        this.inputHandler = inputHandler;
    }
    
    

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this, "Game Loop");
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

 // 1️⃣ Flag pausado ya existe
    private boolean paused = false;

    // 2️⃣ Setter y getter de pausado
    public void setPaused(boolean paused) { this.paused = paused; }
    public boolean isPaused() { return paused; }


    // 4️⃣ (Opcional) getter LevelManager
    public LevelManager getLevelManager() { return levelManager; }


    public void restartLevel(int nivel) {
        levelManager.restartCurrentLevel(nivel);
    }

    public void changeLevel(String nuevoMapa, int nivel) {
        levelManager.changeLevel(nuevoMapa, nivel);
    }

    @Override
    public void run() {
        while (running) {
            if (!game.isPaused()) {
                levelManager.update(game.getInputHandler()); 
            }
            game.repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    

    private void update() {
        // Actualiza la lógica del nivel actual
        levelManager.update(inputHandler);
    }
}