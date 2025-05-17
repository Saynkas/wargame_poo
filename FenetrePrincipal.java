import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.format.TextStyle;

import java.util.ArrayList;

import javax.sound.sampled.*;
import javax.swing.*;

public class FenetrePrincipal extends JFrame {

    private String pseudoJoueur1;
    private String pseudoJoueur2;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HexPlateau hexPlateau;
    private Clip backgroundClip;
    private Partie partie;

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

        JPanel lobbySetupPanel = creelobbySetupPanel();


        Plateau plateau = new Plateau(12, 18);
        hexPlateau = new HexPlateau(plateau);
        //JPanel jeuPanel = creeJeuPanel(plateau);

        mainPanel.add(menuPanel, "menu");
        //mainPanel.add(jeuPanel, "plateau");
        mainPanel.add(lobbySetupPanel, "lobby");

        add(mainPanel);
        setVisible(true);
        playBackgroundMusic("assets/sounds/menu_theme_ok.wav");


    }

    class PseudoDialog extends JDialog {
        private JTextField joueur1Field;
        private JTextField joueur2Field;
        private String joueur1;
        private String joueur2;

        public PseudoDialog(JFrame parent) {
            super(parent, "Entrez les pseudos", true);
            setSize(500, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel fondPanel = new JPanel() {
                Image img = new ImageIcon("backGroundImages/parchemin.jpg").getImage();
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            };
            fondPanel.setLayout(new GridBagLayout());
            fondPanel.setOpaque(false);

            Font customFont;
            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/police/papyrus.ttf"))
                                .deriveFont(Font.PLAIN, 18f);
            } catch (Exception e) {
                customFont = new Font("Serif", Font.BOLD, 18);
            }

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel label1 = new JLabel("Pseudo Joueur 1 :");
            label1.setFont(customFont);
            label1.setForeground(new Color(91, 62, 29));
            gbc.gridx = 0; gbc.gridy = 0;
            fondPanel.add(label1, gbc);

            joueur1Field = new JTextField();
            joueur1Field.setFont(customFont);
            joueur1Field.setPreferredSize(new Dimension(200, 30));
            joueur1Field.setForeground(Color.BLACK);
            joueur1Field.setBackground(new Color(255, 255, 255));
            joueur1Field.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            joueur1Field.setOpaque(true);

            gbc.gridx = 1;
            gbc.gridy = 0;
            fondPanel.add(joueur1Field, gbc);


            JLabel label2 = new JLabel("Pseudo Joueur 2 :");
            label2.setFont(customFont);
            label2.setForeground(new Color(91, 62, 29));
            gbc.gridx = 0;
            gbc.gridy = 1;
            fondPanel.add(label2, gbc);

            joueur2Field = new JTextField();
            joueur2Field.setFont(customFont);
            joueur2Field.setPreferredSize(new Dimension(200, 30));
            joueur2Field.setForeground(Color.BLACK);
            joueur2Field.setBackground(new Color(255, 255, 255));
            joueur2Field.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            joueur2Field.setOpaque(true);

            gbc.gridx = 1;
            gbc.gridy = 1;
            fondPanel.add(joueur2Field, gbc);

            JButton validerBtn = new JButton("Valider");
            validerBtn.setFont(customFont);
            validerBtn.setBackground(new Color(88, 31, 14));
            validerBtn.setForeground(new Color(239, 200, 112));
            validerBtn.addActionListener(e -> {
                joueur1 = joueur1Field.getText().trim();
                joueur2 = joueur2Field.getText().trim();
                if (!joueur1.isEmpty() && !joueur2.isEmpty()) {
                    dispose();
                    cardLayout.show(mainPanel, "plateau");
                } else {
                    JOptionPane.showMessageDialog(this, "Les deux pseudos sont requis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            fondPanel.add(validerBtn, gbc);

            setContentPane(fondPanel);
        }

        public String getJoueur1() { return joueur1; }
        public String getJoueur2() { return joueur2; }
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
            partie = new Partie(new Joueur("joueur 1"), new Joueur("joueur 2"));
            cardLayout.show(mainPanel, "lobby");
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

    private JPanel creelobbySetupPanel()
    {
        JPanel lobbyPanel = new BackGroundPanel("./backGroundImages/carte_choix_jeu.jpg");
        lobbyPanel.setLayout(new BorderLayout());

        // Chargement du logo comme ImageIcon
        ImageIcon logoIcon = new ImageIcon("backGroundImages/logo_choix_mode_ok.png");
        JLabel logoLabel = new JLabel(logoIcon, SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        logoLabel.setOpaque(false);
        lobbyPanel.add(logoLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));


        JButton pvpButton = createStyledButton("VS Joueur");
        JButton pvcButton = createStyledButton("VS IA");


        pvpButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            PseudoDialog pseudoDialog = new PseudoDialog(this);
            pseudoDialog.setVisible(true);

            pseudoJoueur1 = pseudoDialog.getJoueur1();
            pseudoJoueur2 = pseudoDialog.getJoueur2();

            if (pseudoJoueur1 != null && pseudoJoueur2 != null) {
                System.out.println("Pseudo Joueur 1 : " + pseudoJoueur1);
                System.out.println("Pseudo Joueur 2 : " + pseudoJoueur2);

                Plateau plateau = new Plateau(12, 18);
                hexPlateau = new HexPlateau(plateau);
                JPanel jeuPanel = creeJeuPanel(plateau);

                mainPanel.add(jeuPanel, "plateau");
                cardLayout.show(mainPanel, "plateau");
            }

        });



        pvcButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            JOptionPane.showMessageDialog(null, "pas encore fait");
        });


        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(pvpButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        buttonPanel.add(pvcButton);
        buttonPanel.add(Box.createHorizontalGlue());

        JPanel buttonReturnPanel = new JPanel();
        buttonReturnPanel.setOpaque(false);
        buttonReturnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton returnButton = createStyledButton("Retour au menu principal");
        returnButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            cardLayout.show(mainPanel, "menu");
        });

        buttonReturnPanel.add(returnButton);
        lobbyPanel.setBorder(BorderFactory.createEmptyBorder(50,0,50,0));


        // Organisation finale
        lobbyPanel.add(buttonPanel, BorderLayout.CENTER);
        lobbyPanel.add(buttonReturnPanel, BorderLayout.SOUTH);
        // Positionnement en bas

        return lobbyPanel;
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

    private void endTurn(int joueur, int tour){
        JFrame jFrame = new JFrame();
        JDialog dialog = new JDialog(jFrame, "Test");

        dialog.setUndecorated(true);
        JLabel messageLabel = new JLabel("Le tour " + tour + " du joueur " + joueur + " commence !", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Serif", Font.BOLD, 20));

        // On place le JLabel dans le JDialog
        dialog.setLayout(new BorderLayout());
        dialog.add(messageLabel, BorderLayout.CENTER);

        // Rendre la fenêtre visible immédiatement
        dialog.setSize(500, 150);
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

        // Labels pour afficher les pseudos des joueurs
        JLabel labelJoueur1 = new JLabel("👑 " + pseudoJoueur1);
        labelJoueur1.setFont(new Font("Serif", Font.BOLD, 20));
        labelJoueur1.setForeground(Color.WHITE);
        labelJoueur1.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelJoueur2 = new JLabel(pseudoJoueur2 + " 👑");
        labelJoueur2.setFont(new Font("Serif", Font.BOLD, 20));
        labelJoueur2.setForeground(Color.WHITE);
        labelJoueur2.setHorizontalAlignment(SwingConstants.CENTER);

        // Panels pour positionner les labels à gauche et à droite
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(labelJoueur1, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(labelJoueur2, BorderLayout.CENTER);

        jeuPanel.add(leftPanel, BorderLayout.WEST);
        jeuPanel.add(rightPanel, BorderLayout.EAST);


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
            partie.setToursInd(partie.getToursInd()+1);
            int joueur = 2;
            if (partie.getToursInd() % 2 == 1) {
                joueur = 1;
                partie.setTurnNumber(partie.getTurnNumber()+1);
            }

            endTurn(joueur, partie.getTurnNumber());
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
            endTurn(1, 1);
            revalidate();
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
                selectUnit(unitTypes[index], side);
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

    private void selectUnit(String unitType, String side) {
        switch (unitType) {
            case "Infanterie Lourde":
                InfanterieLourde unitIL = new InfanterieLourde();
                if (side == "left") {
                    partie.getJoueur1().ajouterUnite(unitIL);
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitIL);
                }
                hexPlateau.setUniteSelectionnee(unitIL);
                break;
            case "Archer":
                Archer unitA = new Archer();
                if (side == "left") {
                    partie.getJoueur1().ajouterUnite(unitA);
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitA);
                }
                hexPlateau.setUniteSelectionnee(unitA);
                break;
            case "Mage":
                Mage unitM = new Mage();
                if (side == "left") {
                    partie.getJoueur1().ajouterUnite(unitM);
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitM);
                }
                hexPlateau.setUniteSelectionnee(unitM);
                break;
            case "Infanterie Legere":
                InfanterieLegere unitILe = new InfanterieLegere();
                if (side == "left") {
                    partie.getJoueur1().ajouterUnite(unitILe);
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitILe);
                }
                hexPlateau.setUniteSelectionnee(unitILe);
                break;
            case "Cavalerie":
                Cavalerie unitC = new Cavalerie();
                if (side == "left") {
                    partie.getJoueur1().ajouterUnite(unitC);
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitC);
                }
                hexPlateau.setUniteSelectionnee(unitC);
                break;
            default:
                break;
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