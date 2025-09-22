package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import main.Tukitux;

public class Menu extends JFrame {

    public static audio.Music menuMusic = new audio.Music();

	public Menu() {
        // Reproducir música del menú en bucle
        menuMusic.play("music/musica-menus.wav");
        
        setTitle("Tukitux - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // fullscreen
        setResizable(false);
        
        // Panel con imagen de fondo
        BackgroundPanel panel = new BackgroundPanel("/img/Menues/FONDOPRINCIPAL.png");
        panel.setLayout(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);

        // Botón Jugar
        ImageIcon iconJugar = new ImageIcon(getClass().getResource("/img/Menues/botonJugar.png"));
        JButton startButton = new JButton(iconJugar);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setOpaque(false);
        gbc.gridy = 1;
        panel.add(startButton, gbc);
        
        // Botón Salir
        ImageIcon iconSalir = new ImageIcon(getClass().getResource("/img/Menues/botonSalir.png"));
        JButton exitButton = new JButton(iconSalir);
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setOpaque(false);
        gbc.gridy = 2;
        panel.add(exitButton, gbc);

        // Acción para el botón Jugar
        startButton.addActionListener((ActionEvent e) -> {
            dispose(); // Cerramos el menú principal
            abrirMenuNiveles();
        });

        // Acción para salir
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        // Pantalla completa
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        setVisible(true);
    }

    private void abrirMenuNiveles() {
        JFrame levelFrame = new JFrame("Seleccionar Nivel");
        levelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        levelFrame.setUndecorated(true);
        levelFrame.setResizable(false);

        // Fondo también en selección de niveles
        BackgroundPanel panel = new BackgroundPanel("/img/Menues/FONDOPRINCIPAL.png");
        panel.setLayout(new GridBagLayout());
        levelFrame.add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;

        JLabel title = new JLabel("Selecciona un nivel", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(138, 43, 226, 180)); 
        title.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createLineBorder(Color.WHITE, 3, true), 
        		BorderFactory.createEmptyBorder(20, 40, 20, 40)      
        		));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        panel.add(title, gbc);

        // Botones de nivel
        String[] nombres = {"Nivel 1", "Nivel 2", "Nivel 3"};
        String[] mapas = { "src/levels/mapa.txt", "src/levels/mapa1.txt", "src/levels/mapa2.txt" };
        String[] imagenes = { 
                "/img/Menues/FVCKLOVE.png", 
                "/img/Menues/michainderoque.png", 
                "/img/Menues/HIELO.png" 
            };
        for (int i = 0; i < nombres.length; i++) {
            ImageIcon icono = new ImageIcon(getClass().getResource(imagenes[i]));
            JButton b = new JButton(icono);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setFocusPainted(false);
            b.setOpaque(false);
            b.setPreferredSize(new Dimension(400, 120)); 

            gbc.gridy = i + 1;
            panel.add(b, gbc);

            final String mapa = mapas[i];
            final int numeroNivel = i + 1;

            b.addActionListener((ActionEvent e) -> {
                levelFrame.dispose();
                Tukitux.startGame(mapa, numeroNivel);
            });
        }

        // Pantalla completa
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(levelFrame);

        levelFrame.setVisible(true);
    }

    // Clase para panel con imagen de fondo
    static class BackgroundPanel extends JPanel {
    	    private Image backgroundImage;

    	    public BackgroundPanel(String imagePath) {
    	        java.net.URL imgURL = getClass().getResource(imagePath);
    	        if (imgURL != null) {
    	            backgroundImage = new ImageIcon(imgURL).getImage();
    	        } else {
    	            System.err.println("No se pudo encontrar la imagen: " + imagePath);
    	        }
    	    }

    	    @Override
    	    protected void paintComponent(Graphics g) {
    	        super.paintComponent(g);
    	        if (backgroundImage != null) {
    	            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    	        }
    	    }
    	}

    public static void showMenu() {
        // Reanudar música del menú
        menuMusic.play("music/musica-menus.wav");
        new Menu();
    }
    
    private JButton crearBotonRetro(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 36)); // tu fuente actual
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(60, 0, 100)); // violeta oscuro
        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(400, 80));
        boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));

        // Efecto hover
        boton.addChangeListener(e -> {
            if (boton.getModel().isRollover()) {
                boton.setBackground(new Color(100, 0, 160)); // violeta más claro
            } else {
                boton.setBackground(new Color(60, 0, 100)); // vuelve al original
            }
        });

        return boton;
    }
}