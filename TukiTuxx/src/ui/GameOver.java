package ui;

import javax.swing.*;

import ui.Menu.BackgroundPanel;

import java.awt.*;
import java.awt.event.ActionEvent;

public class GameOver extends JFrame {

    public GameOver(int puntos, Runnable revanchaCallback, engine.LevelManager levelManager) {
        // Pausar la música del nivel al mostrar GameOver
        if (levelManager != null) {
            levelManager.pauseMusic();
        }

        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        BackgroundPanel panel = new BackgroundPanel("/img/Menues/GAME-OVER-FONDO.png");
        panel.setLayout(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 0, 0); // aumenta el espacio vertical
        gbc.gridx = 0;

        // Botón de Puntuación
        JButton scoreButton = new JButton("PUNTOS: " + puntos, new ImageIcon(getClass().getResource("/img/Menues/boton-rojoliso.png")));
        scoreButton.setFont(new Font("Arial", Font.BOLD, 36)); // tamaño más grande
        scoreButton.setForeground(Color.WHITE); // texto blanco
        scoreButton.setHorizontalTextPosition(SwingConstants.CENTER); // texto al centro del botón
        scoreButton.setVerticalTextPosition(SwingConstants.CENTER);
        scoreButton.setBorderPainted(false);
        scoreButton.setContentAreaFilled(false);
        scoreButton.setFocusPainted(false);
        scoreButton.setOpaque(false);
        gbc.gridy = 6; // baja el botón de puntuación aún más
        panel.add(scoreButton, gbc);

        // Botón Salir
        JButton exitButton = new JButton(new ImageIcon(getClass().getResource("/img/Menues/BotonSalir.png")));
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setOpaque(false);
        gbc.gridy = 2; // baja el botón salir aún más
        panel.add(exitButton, gbc);

        /* Acción Revancha: ejecuta el callback que recibe
        revanchaButton.addActionListener(e -> {
            dispose();
            revanchaCallback.run();
        });*/

        // Acción Salir
        exitButton.addActionListener(e -> {
            dispose();
            ui.Menu.showMenu(); // vuelve al menú principal
        });

        // Pantalla completa
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        setVisible(true);
    }


    private void reiniciarJuego() {
        // Lógica para reiniciar el juego
        // Por ejemplo: abrir menú de niveles o reiniciar el nivel actual
        JOptionPane.showMessageDialog(null, "Reiniciando juego...");
    }

    public static void main(String[] args) {
        new GameOver(0, new Runnable() {
            @Override
            public void run() {
                System.out.println("Revancha pulsada");
            }
        }, null);
    }
}