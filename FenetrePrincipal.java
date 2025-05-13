import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

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
        playBackgroundMusic("assets/sounds/menu_theme_ok.wav");


    }

    class FenetreRegles extends JDialog {

        public FenetreRegles(JFrame parent) {
            super(parent, "Règles du jeu", true);
            setSize(700, 500);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());


            // Utilise JEditorPane pour plus de flexibilité HTML
            JEditorPane reglesText = new JEditorPane();

            try {
                Font papyrusFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/police/papyrus.ttf"))
                                    .deriveFont(Font.PLAIN, 16f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(papyrusFont);
                reglesText.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
                reglesText.setFont(papyrusFont);
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
                // En cas d'erreur, on garde le style par défaut
            }

            reglesText.setContentType("text/html");
            reglesText.setEditable(false);
            reglesText.setOpaque(false);

            reglesText.setText("""
                <html>
                <body style="
                    background-color: #fdf5e6;
                    color: #5b3e1d;
                    font-family: 'Papyrus', 'Book Antiqua', 'Serif';
                    font-size: 16px;
                    padding: 20px;
                    border: 4px double #8b5a2b;
                ">
                    <h1 style='text-align: center; color: #7b3f00;'> Règles du jeu</h1>
                    <p>Bienvenue noble seigneur dans ce <b>wargame tactique tour par tour</b> !</p>
                    <p>Affrontez vos adversaires sur un <b>champ de bataille hexagonal</b>, où chaque décision forge le destin.</p>
                    <h2><img src='file:backGroundImages/icones/epees.png' width='20' height='20' style='vertical-align: middle;'/> Objectifs :</h2>
                    <ul>
                        <li><b> Détruire toutes les unités ennemies</b></li>
                        <li><b> Ou survivre jusqu'au dernier tour</b> (selon le scénario)</li>
                    </ul>
                    <h2><img src='file:backGroundImages/icones/bouclier.png' width='20' height='20' style='vertical-align: middle;'/> Mécaniques principales :</h2>
                    <ul>
                        <li> Déploiement stratégique de vos troupes</li>
                        <li> Utilisation du terrain et de la portée</li>
                        <li> Compétences uniques par unité</li>
                    </ul>
                    <p style='text-align:center; font-size: 18px; color: #8b0000;'><i>Que la gloire vous accompagne, Commandant ! <img src='file:backGroundImages/icones/chevalier-a-cheval.png' width='20' height='20' style='vertical-align: middle;'/> </i></p>
                </body>
                </html>
            """);

            JScrollPane scrollPane = new JScrollPane(reglesText);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Fermer
            JButton closeBtn = new JButton("Fermer");
            closeBtn.setBackground(new Color(88, 31, 14));
            closeBtn.setForeground(new Color(239, 200, 112));
            closeBtn.setFont(new Font("Serif", Font.BOLD, 16));
            closeBtn.addActionListener(e -> dispose());

            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(new Color(253, 245, 230)); // beige parchemin
            bottomPanel.add(closeBtn);

            JPanel fondPanel = new JPanel() {
                Image img = new ImageIcon("backGroundImages/parchemin.jpg").getImage();

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g); // important : appeler super AVANT le dessin de fond
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            };
            fondPanel.setLayout(new BorderLayout());
            fondPanel.setOpaque(false); // au cas où

            fondPanel.add(scrollPane, BorderLayout.CENTER);
            fondPanel.add(bottomPanel, BorderLayout.SOUTH);

            setContentPane(fondPanel);

        }
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

        playButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            cardLayout.show(mainPanel, "plateau");
        });

        guideButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            new FenetreRegles(this).setVisible(true);
        });


        exitButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            System.exit(0);
        });

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

    private void demarrerJeu(JPanel jPanel) {

    }

    private void endTurn(String joueur, int tour){
        JFrame jFrame = new JFrame();
        JDialog dialog = new JDialog(jFrame, "Test");

        dialog.setUndecorated(true);
        JLabel messageLabel = new JLabel("Le tour " + tour + " du joueur " + joueur + " commence !", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 20));

        // On place le JLabel dans le JDialog
        dialog.setLayout(new BorderLayout());
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Rendre la fenêtre visible immédiatement
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(jFrame);  // Centrer par rapport au parent
        dialog.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            dialog.dispose();
        });

        timer.setRepeats(false);
        timer.start();
    }

    private JPanel creeJeuPanel(Plateau plateau) {
        JPanel jeuPanel = new BackGroundPanel("./backGroundImages/carte_medieval.jpg");
        jeuPanel.setLayout(new BorderLayout());

        // Panel du haut avec bouton retour
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false); // <-- Ajoute ça
        JButton retourButton = new JButton("Retour au menu");
        retourButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "menu");
            backgroundClip.stop();
            playBackgroundMusic("assets/sounds/menu_theme_ok.wav");
        });
        topPanel.add(retourButton);

        // Explication de la phase de préparation
        JLabel explicationPrep = new JLabel("Phase de préparation : chaque joueur peut mettre autant d'unités qu'il veut, là où il veut, puis cliquer sur le bouton de démarrage à droite quand l'organisation des unités est satisfaisante.");
        explicationPrep.setForeground(Color.WHITE);
        explicationPrep.setFont(new Font("Serif", Font.BOLD, 13));
        topPanel.add(explicationPrep);

        // Explication de la phase de jeu
        JLabel explicationJeu = new JLabel("Phase de jeu : chaque joueur peut utiliser ses unités comme il le souhaite, et son tour sera terminé dès qu'aucune de ses unités ne peut bouger, ou bien prématurément en appuyant sur le bouton à droite.");
        explicationJeu.setForeground(Color.WHITE);
        explicationJeu.setFont(new Font("Serif", Font.BOLD, 13));

        // Panel principal qui contient les trois colonnes
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.setOpaque(false); // <-- Ajoute ça aussi
        
        // Création des panneaux d'unités
        JPanel leftUnitsPanel = createUnitsPanel("left");
        leftUnitsPanel.setOpaque(false); // <-- Et ici
        JPanel rightUnitsPanel = createUnitsPanel("right");
        rightUnitsPanel.setOpaque(false); // <-- Et ici aussi

        // Bouton de fin de tour
        JButton buttonEndTurn = new JButton("Terminer son tour");
        buttonEndTurn.addActionListener(e -> {
            endTurn("1", 1);
        });

        // Création du bouton pour démarrer la partie
        JButton startGame = new JButton("Démarrer la partie !");
        startGame.addActionListener(e-> {
            mainGamePanel.remove(leftUnitsPanel);
            mainGamePanel.remove(rightUnitsPanel);
            topPanel.remove(explicationPrep);
            topPanel.remove(startGame);
            topPanel.add(explicationJeu);
            topPanel.add(buttonEndTurn);
            revalidate();
            demarrerJeu(jeuPanel);
        });
        topPanel.add(startGame);


        // Ajout des composants
        mainGamePanel.add(leftUnitsPanel, BorderLayout.WEST);
        mainGamePanel.add(hexPlateau, BorderLayout.CENTER);
        mainGamePanel.add(rightUnitsPanel, BorderLayout.EAST);

        // Configuration finale
        jeuPanel.add(topPanel, BorderLayout.NORTH);
        jeuPanel.add(mainGamePanel, BorderLayout.CENTER);

        return jeuPanel;
    }

    private JPanel createUnitsPanel(String side) {
        JPanel unitsPanel = new JPanel();
        unitsPanel.setLayout(new BoxLayout(unitsPanel, BoxLayout.Y_AXIS));
        unitsPanel.setBackground(new Color(240, 240, 240));
        unitsPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] unitTypes = {"Infanterie Lourde", "Archer", "Mage", "Infanterie Legere", "Cavalerie"};
        String[] unitImages = {
            "./assets/InfanterieLourde.png",
            "./assets/Archer.png",
            "./assets/Mage.png",
            "./assets/InfanterieLegere.png",
            "./assets/Cavalerie.png"
        };

        for (int i = 0; i < unitTypes.length; i++) {
            final int index = i;
            JButton unitBtn = createUnitButton(unitTypes[index], unitImages[index]);
            unitBtn.addActionListener(e -> {
                selectUnit(unitTypes[index]);
                JOptionPane.showMessageDialog(this, unitTypes[index] + " sélectionnée !");
            });
            unitsPanel.add(unitBtn);
            if (index < unitTypes.length - 1) {
                unitsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        return unitsPanel;
    }

    private JButton createUnitButton(String unitName, String imagePath) {
        JButton button = new JButton(unitName);
        button.setPreferredSize(new Dimension(150, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(10);
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image: " + imagePath);
        }
        
        return button;
    }

    private void selectUnit(String unitType) {
        switch (unitType) {
            case "Infanterie Lourde" -> hexPlateau.setUniteSelectionnee(new InfanterieLourde());
            case "Archer" -> hexPlateau.setUniteSelectionnee(new Archer());
            case "Mage" -> hexPlateau.setUniteSelectionnee(new Mage());
            case "Infanterie Legere" -> hexPlateau.setUniteSelectionnee(new InfanterieLegere());
            case "Cavalerie" -> hexPlateau.setUniteSelectionnee(new Cavalerie());
            default -> hexPlateau.setUniteSelectionnee(new Infanterie());
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetrePrincipal());
    }
}