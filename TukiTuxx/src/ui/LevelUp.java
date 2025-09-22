package ui;

import javax.swing.*;
import java.awt.*;

public class LevelUp extends JFrame {

    public LevelUp(Runnable continuarCallback, engine.LevelManager levelManager) {
        // Pausar la música del nivel al mostrar LevelUp
        if (levelManager != null) {
            levelManager.pauseMusic();
        }

        setTitle("Nivel Completado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        // Fondo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Imagen de fondo
                ImageIcon bg = new ImageIcon(getClass().getResource("/img/Menues/win.png"));
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        panel.setLayout(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);

        // Botón SALIR
        JButton salirButton = new JButton(new ImageIcon(getClass().getResource("/img/Menues/BotonSalir.png")));
        salirButton.setBorderPainted(false);
        salirButton.setContentAreaFilled(false);
        salirButton.setFocusPainted(false);
        salirButton.setOpaque(false);
        gbc.gridy = 2; // Centrado abajo
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1.0; // Empuja el botón hacia abajo
        panel.add(salirButton, gbc);

        salirButton.addActionListener(e -> {
            dispose();
            ui.Menu.showMenu(); // vuelve al menú principal
        });

        // Pantalla completa
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
        setVisible(true);
    }
}