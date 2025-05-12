import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;




public class FenetrePrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HexPlateau hexPlateau;
    private Clip backgroundClip;

    private void playSound(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public FenetrePrincipal() {
        super("Fenetre mainMenu");

        setSize(1700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);

        JPanel menuPanel = creeMenuPanel();
        Plateau plateau = new Plateau(12, 18);
        hexPlateau = new HexPlateau(plateau);
        JPanel jeuPanel = creeJeuPanel(plateau);

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(jeuPanel, "plateau");

        add(mainPanel);
        setVisible(true);
    }


    private JPanel creeMenuPanel() {
        JPanel panel = new BackGroundPanel("./backGroundImages/background_wargame.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ImageIcon logoIcon = new ImageIcon("./backGroundImages/rage_of_ancients.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playButton = createStyledButton("Jouer");
        JButton guideButton = createStyledButton("Règles");
        JButton exitButton = createStyledButton("Quitter");

        playButton.addActionListener(e -> cardLayout.show(mainPanel, "plateau"));

        guideButton.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                    "Bienvenue dans ce Wargame tactique tour par tour !\n" +
                    "Affrontez vos adversaires sur un champ de bataille hexagonal,\n" +
                    "chaque décision compte.\n\n" +
                    "Objectif :\n" +
                    "- Détruire toutes les unités ennemies\nOU\n" +
                    "- Survivre jusqu’au dernier tour (selon le scénario)."));

        exitButton.addActionListener(e ->
            System.exit(0));


        panel.add(Box.createVerticalGlue());
        panel.add(logoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 100)));
        panel.add(playButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(guideButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(exitButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 22));
        button.setForeground(new Color(0xEFC870));
        button.setBackground(new Color(0x581F0E));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x000000), 2),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x7A2E19));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x000000), 2),
                        BorderFactory.createEmptyBorder(11, 30, 13, 30)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x581F0E));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0x000000), 2),
                        BorderFactory.createEmptyBorder(12, 30, 12, 30)
                ));
            }
        });

        return button;
    }

    private JPanel creeJeuPanel(Plateau plateau) {
        JPanel jeuPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton retourButton = new JButton("Retour au menu");
        retourButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        topPanel.add(retourButton);

        JPanel controlePanel = new JPanel();
        controlePanel.setBackground(new Color(240, 240, 240));
        controlePanel.setBorder(BorderFactory.createEmptyBorder(300, 10, 10, 10));

        JButton infanterieBtn = new JButton("Infanterie");
        infanterieBtn.setPreferredSize(new Dimension(120, 50));
        infanterieBtn.addActionListener(e -> {
            hexPlateau.setUniteSelectionnee(new Infanterie());
            JOptionPane.showMessageDialog(this, "Infanterie sélectionnée !");
        });

        controlePanel.add(infanterieBtn);

        jeuPanel.add(topPanel, BorderLayout.NORTH);
        jeuPanel.add(hexPlateau, BorderLayout.CENTER);
        jeuPanel.add(controlePanel, BorderLayout.WEST);

        return jeuPanel;
    }

    class BackGroundPanel extends JPanel {
        private Image backGroundImage;

        public BackGroundPanel(String imagePath) {
            backGroundImage = new ImageIcon(imagePath).getImage();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backGroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
