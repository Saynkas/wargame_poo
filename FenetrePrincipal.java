import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.util.*;


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
    private JButton buttonEndTurn = createStyledButton("Terminer son tour");
    private JLabel messageStatusLabel = new JLabel("Aucune unité sélectionnée");


    public void mettreAJourMessage(String message) {
        messageStatusLabel.setText(message);
    }

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

        mainPanel.add(menuPanel, "menu");
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
            partie = new Partie(new Joueur(1, "joueur 1"), new Joueur(2 , "joueur 2"));
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
                hexPlateau = new HexPlateau(plateau, partie, buttonEndTurn, this);
                JPanel jeuPanel = creeJeuPanel(plateau);

                mainPanel.add(jeuPanel, "plateau");
                cardLayout.show(mainPanel, "plateau");
            }

        });



        pvcButton.addActionListener(e -> {
            playSound("assets/sounds/click_fantasy_ok.wav");
            Plateau plateau = new Plateau(12, 18);
            hexPlateau = new HexPlateau(plateau, partie, buttonEndTurn, this);
            JPanel jeuPanelIA = creeJeuPanelIA();
            mainPanel.add(jeuPanelIA, "plateau");
            cardLayout.show(mainPanel, "plateau");
            partie.getJoueur2().setEstIA(true);
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

    private void endTurn(int joueur, int tour, Partie partie) {
        // Créer ou mettre à jour le JLabel existant pour afficher le message
        String message = "Le tour " + tour + " du joueur " + joueur + " commence !";
        messageStatusLabel.setText(message);

        // Optionnel : Changer la couleur du message en fonction du joueur
        if (joueur == 1) {
            messageStatusLabel.setForeground(new Color(0x00FF00)); // Vert pour le joueur 1
        } else {
            messageStatusLabel.setForeground(new Color(0xFF0000)); // Rouge pour le joueur 2
        }

        // Créer un Timer pour faire disparaître le message après 2 secondes
        Timer timer = new Timer(2000, e -> {
            messageStatusLabel.setText("");  // Effacer le message après 2 secondes
        });
        timer.setRepeats(false);
        timer.start();

        // Réinitialiser les actions du joueur pour ce tour
        if (joueur == 1) {
            partie.getJoueur1().resetAAgitCeTour();
        } else {
            partie.getJoueur2().resetAAgitCeTour();
        }

        // Réinitialiser les unités du joueur actuel
        Joueur joueurActuel = partie.getJoueurActuel();
        for (Unite unite : joueurActuel.getUnites()) {
            unite.recupererPV(); // Récupérer des points de vie
            unite.reinitialiserTour(); // Réinitialiser l'état des unités pour le tour suivant
        }
    }


    private JPanel creeJeuPanelIA() {
    JPanel jeuPanel = new BackGroundPanel("./backGroundImages/carte_medieval.jpg");
    jeuPanel.setLayout(new BorderLayout());

    // Création du label pseudo joueur 1 dans une bannière opaque
    JLabel labelJoueur1 = new JLabel("Joueur", SwingConstants.CENTER);
    labelJoueur1.setFont(new Font("Serif", Font.BOLD, 20));
    labelJoueur1.setForeground(new Color(0xEFC870)); // Doré clair

    JPanel bannièreJoueur1 = new JPanel();
    bannièreJoueur1.setBackground(new Color(0x581F0E)); // Fond brun foncé
    bannièreJoueur1.setOpaque(true);
    bannièreJoueur1.setLayout(new BorderLayout());
    bannièreJoueur1.add(labelJoueur1, BorderLayout.CENTER);


    // Création du label pseudo joueur 2 dans une bannière opaque
    JLabel labelJoueur2 = new JLabel("IA", SwingConstants.CENTER);
    labelJoueur2.setFont(new Font("Serif", Font.BOLD, 20));
    labelJoueur2.setForeground(new Color(0xEFC870)); // Doré clair

    JPanel bannièreJoueur2 = new JPanel();
    bannièreJoueur2.setBackground(new Color(0x581F0E)); // Fond brun foncé
    bannièreJoueur2.setOpaque(true);
    bannièreJoueur2.setLayout(new BorderLayout());
    bannièreJoueur2.add(labelJoueur2, BorderLayout.CENTER);

    // Création de bordures "médiévales"
    Border bordureJoueur1 = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
        "Seigneur de l'Ouest", TitledBorder.CENTER, TitledBorder.TOP,
        new Font("Serif", Font.BOLD, 14), new Color(0xEFC870) // Doré clair pour le titre
    );

    Border bordureJoueur2 = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
        "Seigneur de l'Est", TitledBorder.CENTER, TitledBorder.TOP,
        new Font("Serif", Font.BOLD, 14), new Color(0xEFC870)
    );

    // Panels individuels pour chaque joueur
    JPanel leftPanel = new JPanel(new BorderLayout());
    leftPanel.setOpaque(false);
    leftPanel.setBorder(bordureJoueur1);
    leftPanel.setPreferredSize(new Dimension(200, 60));
    leftPanel.add(bannièreJoueur1, BorderLayout.CENTER);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setOpaque(false);
    rightPanel.setBorder(bordureJoueur2);
    rightPanel.setPreferredSize(new Dimension(200, 60));
    rightPanel.add(bannièreJoueur2, BorderLayout.CENTER);

    // Panel global pour contenir les deux (en bas de l'écran)
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setOpaque(false);
    bottomPanel.add(leftPanel, BorderLayout.WEST);
    bottomPanel.add(rightPanel, BorderLayout.EAST);

    // Zone d'information au centre du bas de l'écran
    messageStatusLabel.setForeground(Color.WHITE);
    messageStatusLabel.setFont(new Font("Serif", Font.BOLD, 16));
    messageStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JPanel centerInfoPanel = new JPanel(new BorderLayout());
    centerInfoPanel.setOpaque(false);
    centerInfoPanel.add(messageStatusLabel, BorderLayout.CENTER);

    bottomPanel.add(centerInfoPanel, BorderLayout.CENTER);

    // Ajout au panel principal
    jeuPanel.add(bottomPanel, BorderLayout.SOUTH);






    // Panel du haut avec bouton retour
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.setOpaque(false); // <-- Ajoute ça
    JButton retourButton = createStyledButton("Retour au menu");
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
    //topPanel.add(explicationPrep);

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
    buttonEndTurn.addActionListener(e -> {
        partie.setToursInd(partie.getToursInd() + 1);

        mainGamePanel.remove(leftUnitsPanel);
        mainGamePanel.remove(rightUnitsPanel);
        if(partie.getToursInd() <= 2){
            if (partie.getToursInd() % 2 == 1) {
                // Tour du joueur 1
                mainGamePanel.add(leftUnitsPanel, BorderLayout.WEST);
            } else {
                // Tour du joueur 2 (IA)
                for (int i = 0; i < partie.getJoueur1().getUnites().size(); i++) {
                    if (partie.getJoueur1().getUnites().get(i) instanceof Archer) {
                        selectUnit("Archer", "right");
                    }
                    else if (partie.getJoueur1().getUnites().get(i) instanceof Cavalerie) {
                        selectUnit("Cavalerie", "right");
                    }
                    else if (partie.getJoueur1().getUnites().get(i) instanceof InfanterieLegere) {
                        selectUnit("InfanterieLegere", "right");
                    }
                    else if (partie.getJoueur1().getUnites().get(i) instanceof InfanterieLourde) {
                        selectUnit("InfanterieLourde", "right");
                    }
                    else if (partie.getJoueur1().getUnites().get(i) instanceof Mage) {
                        selectUnit("Mage", "right");
                    }
                    else {
                        System.out.println("n'est censé jamais arriver");
                    }
                   // System.out.println("i : " + i);
                    hexPlateau.setUnitsIA(new AbstractMap.SimpleEntry<Integer, Integer>(hexPlateau.getHistoryPlayerUnits().get(i).getKey(),hexPlateau.getHistoryPlayerUnits().get(i).getValue()));
                }
                buttonEndTurn.doClick();
            }
        }else{

            partie.setPartieCommence(true);
            mainGamePanel.remove(leftUnitsPanel);
            mainGamePanel.remove(rightUnitsPanel);
            //topPanel.remove(explicationPrep);

            // Tu peux remettre l’explication du jeu ici si besoin
            //topPanel.add(explicationJeu);
            if (partie.getJoueurActuel() == partie.getJoueur2()) {
                for (int i = 0; i < hexPlateau.getPlateau().getLignes(); i++) {
                    for (int j = 0; j < hexPlateau.getPlateau().getColonnes(); j++) {
                        Unite u = hexPlateau.getPlateau().getCase(i, j).getUnite();
                        if (partie.getJoueur2().getUnites().contains(u)) {
                            Random random = new Random();
                            Map<Point,Integer> coordsPossibles = hexPlateau.calculerCasesAccessibles(i, j, u);
                            for (Point p : coordsPossibles.keySet()) {
                                if (partie.getJoueur1().getUnites().contains(hexPlateau.getPlateau().getCase(p.x, p.y).getUnite())) { // vérification s'il y a un ennemi en range
                                    System.out.println("tentative d'attaque");
                                    u.attaquer(hexPlateau.getPlateau().getCase(p.x, p.y).getUnite(), hexPlateau.getPlateau().getCase(p.x, p.y).getTerrain(), hexPlateau.calculerDistance(i, j, p.x, p.y));
                                    //hexPlateau.getPlateau().getCase(i, j).getUnite().setAAgitCeTour(true);
                                }
                            }
                            if (!hexPlateau.getPlateau().getCase(i, j).getUnite().getAAgitCeTour()) {
                                // nombre aléatoire entre 0 et tailleListe - 1
                                Point randomKey = new ArrayList<>(coordsPossibles.keySet()).get(random.nextInt(coordsPossibles.size()));
                               // System.out.println(randomKey);
                                hexPlateau.getPlateau().getCase(i, j).retirerUnite();
                                hexPlateau.getPlateau().getCase(randomKey.x, randomKey.y).placerUnite(u);
                            }
                        }
                    }
                }
                buttonEndTurn.doClick();
                endTurn(partie.getJoueur1().getId(), partie.getTurnNumber(), partie);
                partie.setTurnNumber(partie.getTurnNumber() + 1);
            }
            else {
                endTurn(partie.getJoueur2().getId(), partie.getTurnNumber(), partie);
            }

        mainGamePanel.revalidate();
        mainGamePanel.repaint();


        }

    });

    // Création du bouton pour démarrer la partie
    JButton startGame = new JButton("Démarrer la partie !");
    startGame.addActionListener(e -> {
        partie.setPartieCommence(true);
        mainGamePanel.remove(leftUnitsPanel);
        mainGamePanel.remove(rightUnitsPanel);
        topPanel.remove(explicationPrep);
        topPanel.remove(startGame);

        // Tu peux remettre l’explication du jeu ici si besoin
        //topPanel.add(explicationJeu);

        mainGamePanel.revalidate();
        mainGamePanel.repaint();

    });

    //topPanel.add(startGame);
    topPanel.add(buttonEndTurn);

    // Ajout des composants


    mainGamePanel.add(leftUnitsPanel, BorderLayout.WEST);
    mainGamePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));


    JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
    centerWrapperPanel.setOpaque(false); // Pour laisser voir le fond

    hexPlateau.setPreferredSize(new Dimension(1000, 600));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    centerWrapperPanel.add(hexPlateau, gbc);

    mainGamePanel.add(centerWrapperPanel, BorderLayout.CENTER);

    // Configuration finale
    jeuPanel.add(topPanel, BorderLayout.NORTH);
    jeuPanel.add(mainGamePanel, BorderLayout.CENTER);

    return jeuPanel;
}


    private JPanel creeJeuPanel(Plateau plateau) {
        JPanel jeuPanel = new BackGroundPanel("./backGroundImages/carte_medieval.jpg");
        jeuPanel.setLayout(new BorderLayout());

        // Création du label pseudo joueur 1 dans une bannière opaque
        JLabel labelJoueur1 = new JLabel(pseudoJoueur1, SwingConstants.CENTER);
        labelJoueur1.setFont(new Font("Serif", Font.BOLD, 20));
        labelJoueur1.setForeground(new Color(0xEFC870)); // Doré clair

        JPanel bannièreJoueur1 = new JPanel();
        bannièreJoueur1.setBackground(new Color(0x581F0E)); // Fond brun foncé
        bannièreJoueur1.setOpaque(true);
        bannièreJoueur1.setLayout(new BorderLayout());
        bannièreJoueur1.add(labelJoueur1, BorderLayout.CENTER);


        // Création du label pseudo joueur 2 dans une bannière opaque
        JLabel labelJoueur2 = new JLabel(pseudoJoueur2, SwingConstants.CENTER);
        labelJoueur2.setFont(new Font("Serif", Font.BOLD, 20));
        labelJoueur2.setForeground(new Color(0xEFC870)); // Doré clair

        JPanel bannièreJoueur2 = new JPanel();
        bannièreJoueur2.setBackground(new Color(0x581F0E)); // Fond brun foncé
        bannièreJoueur2.setOpaque(true);
        bannièreJoueur2.setLayout(new BorderLayout());
        bannièreJoueur2.add(labelJoueur2, BorderLayout.CENTER);

        // Création de bordures "médiévales"
        Border bordureJoueur1 = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
            "Seigneur de l'Ouest", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Serif", Font.BOLD, 14), new Color(0xEFC870) // Doré clair pour le titre
        );

        Border bordureJoueur2 = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
            "Seigneur de l'Est", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Serif", Font.BOLD, 14), new Color(0xEFC870)
        );

        // Panels individuels pour chaque joueur
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(bordureJoueur1);
        leftPanel.setPreferredSize(new Dimension(200, 60));
        leftPanel.add(bannièreJoueur1, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(bordureJoueur2);
        rightPanel.setPreferredSize(new Dimension(200, 60));
        rightPanel.add(bannièreJoueur2, BorderLayout.CENTER);

        // Panel global pour contenir les deux (en bas de l'écran)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        // Zone d'information au centre du bas de l'écran
        messageStatusLabel.setForeground(Color.WHITE);
        messageStatusLabel.setFont(new Font("Serif", Font.BOLD, 16));
        messageStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerInfoPanel = new JPanel(new BorderLayout());
        centerInfoPanel.setOpaque(false);
        centerInfoPanel.add(messageStatusLabel, BorderLayout.CENTER);

        bottomPanel.add(centerInfoPanel, BorderLayout.CENTER);

        // Ajout au panel principal
        jeuPanel.add(bottomPanel, BorderLayout.SOUTH);






        // Panel du haut avec bouton retour
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false); // <-- Ajoute ça
        JButton retourButton = createStyledButton("Retour au menu");
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
        //topPanel.add(explicationPrep);

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
        buttonEndTurn.addActionListener(e -> {
            partie.setToursInd(partie.getToursInd() + 1);
        
            mainGamePanel.remove(leftUnitsPanel);
            mainGamePanel.remove(rightUnitsPanel);
            if(partie.getToursInd() <= 2){
                if (partie.getToursInd() % 2 == 1) {
                    // Tour du joueur 1
                    mainGamePanel.add(leftUnitsPanel, BorderLayout.WEST);
                } else {
                    // Tour du joueur 2
                    mainGamePanel.add(rightUnitsPanel, BorderLayout.EAST);
                }
            }else{
                partie.setPartieCommence(true);
                mainGamePanel.remove(leftUnitsPanel);
                mainGamePanel.remove(rightUnitsPanel);
                //topPanel.remove(explicationPrep);
                
                // Tu peux remettre l’explication du jeu ici si besoin
                //topPanel.add(explicationJeu);
            
                mainGamePanel.revalidate();
                mainGamePanel.repaint();
            
                endTurn(1, 1, partie);
            }
            
            mainGamePanel.revalidate();
            mainGamePanel.repaint();
        
            int joueur = (partie.getToursInd() % 2 == 1) ? 1 : 2;
            if (joueur == 1) {
                partie.setTurnNumber(partie.getTurnNumber() + 1);
            }
        
            endTurn(joueur, partie.getTurnNumber(), partie);
        });

        // Création du bouton pour démarrer la partie
        JButton startGame = new JButton("Démarrer la partie !");
        startGame.addActionListener(e -> {
            partie.setPartieCommence(true);
            mainGamePanel.remove(leftUnitsPanel);
            mainGamePanel.remove(rightUnitsPanel);
            topPanel.remove(explicationPrep);
            topPanel.remove(startGame);
            
            // Tu peux remettre l’explication du jeu ici si besoin
            //topPanel.add(explicationJeu);
        
            mainGamePanel.revalidate();
            mainGamePanel.repaint();
        
            endTurn(1, 1, partie);
        });
        
        //topPanel.add(startGame);
        topPanel.add(buttonEndTurn);

        // Ajout des composants

        
        mainGamePanel.add(leftUnitsPanel, BorderLayout.WEST);
        mainGamePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));


        JPanel centerWrapperPanel = new JPanel(new GridBagLayout());
        centerWrapperPanel.setOpaque(false); // Pour laisser voir le fond

        hexPlateau.setPreferredSize(new Dimension(1000, 600)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapperPanel.add(hexPlateau, gbc);

        mainGamePanel.add(centerWrapperPanel, BorderLayout.CENTER);

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

        // Définir le dossier joueur
        String joueurPath = side.equals("left") ? "Joueur1" : "Joueur2";

        // Noms des unités et des images (mêmes noms pour tous, seul le dossier change)
        String[] unitTypes = {"Infanterie Lourde", "Archer", "Mage", "Infanterie Legere", "Cavalerie"};
        String[] imageNames = {"InfanterieLourde", "Archer", "Mage", "InfanterieLegere", "Cavalerie"};

        for (int i = 0; i < unitTypes.length; i++) {
            final int index = i;
            String imagePath = "./assets/" + joueurPath + "/" + imageNames[i] + ".png";

            JButton unitBtn = createUnitButton(unitTypes[i], imagePath);
            unitBtn.addActionListener(e -> {
                selectUnit(unitTypes[index], side);
                messageStatusLabel.setText(unitTypes[index] + " sélectionnée !");
            });


            unitsPanel.add(unitBtn);
            if (index < unitTypes.length - 1) {
                unitsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        return unitsPanel;
    }


    private JButton createUnitButton(String unitName, String imagePath) {
        JButton button = createStyledButton(unitName); // Utilisation de ton style personnalisé

        button.setPreferredSize(new Dimension(200, 60));
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
                if (Objects.equals(side, "left")) {
                    partie.getJoueur1().ajouterUnite(unitIL);
                    unitIL.setProprietaire(partie.getJoueur1());
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitIL);
                    unitIL.setProprietaire(partie.getJoueur2());
                }
                hexPlateau.setUniteSelectionnee(unitIL);
                break;
            case "Archer":
                Archer unitA = new Archer();
                if (Objects.equals(side, "left")) {
                    partie.getJoueur1().ajouterUnite(unitA);
                    unitA.setProprietaire(partie.getJoueur1());
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitA);
                    unitA.setProprietaire(partie.getJoueur2());
                }
                hexPlateau.setUniteSelectionnee(unitA);
                break;
            case "Mage":
                Mage unitM = new Mage();
                if (Objects.equals(side, "left")) {
                    partie.getJoueur1().ajouterUnite(unitM);
                    unitM.setProprietaire(partie.getJoueur1());
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitM);
                    unitM.setProprietaire(partie.getJoueur2());
                }
                hexPlateau.setUniteSelectionnee(unitM);
                break;
            case "Infanterie Legere":
                InfanterieLegere unitILe = new InfanterieLegere();
                if (Objects.equals(side, "left")) {
                    partie.getJoueur1().ajouterUnite(unitILe);
                    unitILe.setProprietaire(partie.getJoueur1());
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitILe);
                    unitILe.setProprietaire(partie.getJoueur2());
                }
                hexPlateau.setUniteSelectionnee(unitILe);
                break;
            case "Cavalerie":
                Cavalerie unitC = new Cavalerie();
                if (Objects.equals(side, "left")) {
                    partie.getJoueur1().ajouterUnite(unitC);
                    unitC.setProprietaire(partie.getJoueur1());
                }
                else {
                    partie.getJoueur2().ajouterUnite(unitC);
                    unitC.setProprietaire(partie.getJoueur2());
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