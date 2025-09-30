package engine;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import audio.Music;

public class InputHandler implements KeyListener {

    private final Set<Integer> pressedKeys = new HashSet<>();
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No usado
    }
    
    // Métodos de ayuda
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean up() {
        return isKeyPressed(KeyEvent.VK_UP) || isKeyPressed(KeyEvent.VK_W);
    }

    public boolean down() {
        return isKeyPressed(KeyEvent.VK_DOWN) || isKeyPressed(KeyEvent.VK_S);
    }

    public boolean left() {
        return isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_A);
    }

    public boolean right() {
        return isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_D);
    }

    public boolean jump() {
        return isKeyPressed(KeyEvent.VK_SPACE);
    }
}
