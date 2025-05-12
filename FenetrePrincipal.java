import javax.swing.*;
import java.awt.*;

public class FenetrePrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HexPlateau hexPlateau;

    public FenetrePrincipal() {
        super("Fenetre mainMenu");

        setSize(1700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //fin du code a la fermeture
        setLocationRelativeTo(null); //centrer la fenetre

        // Initialisation du gestionnaire de vues
        cardLayout = new CardLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);


        // Creation des differentes vues
        JPanel menuPanel = creeMenuPanel();
        Plateau plateau = new Plateau(12, 18); // ou la taille souhaitée
        hexPlateau = new HexPlateau(plateau);
        JPanel jeuPanel = creeJeuPanel(plateau);

        // Ajout des vues au CardLayout
        mainPanel.add(menuPanel, "menu");
        mainPanel.add(jeuPanel, "plateau");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel creeMenuPanel() {
        JPanel panel = new JPanel();
        panel = new BackGroundPanel("./backGroundImages/background_wargame.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        ImageIcon logoIcon = new ImageIcon("./backGroundImages/rage_of_ancients.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JButton playButton = new JButton("Jouer");
        JButton guideButton = new JButton("Règles");
        JButton exitButton = new JButton("Quitter");

        Dimension buttonDimension = new Dimension(150, 100);
        playButton.setMaximumSize(buttonDimension);
        guideButton.setMaximumSize(buttonDimension);
        exitButton.setMaximumSize(buttonDimension);

        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        guideButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guideButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        playButton.addActionListener(e -> cardLayout.show(mainPanel, "plateau"));
        guideButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Bienvenue dans ce Wargame tactique tour par tour !\n" +
                "Affrontez vos adversaires sur un champ de bataille hexagonal,\n" +
                "chaque décision compte.\n\n" +
                "Objectif :\n" +
                "- Détruire toutes les unités ennemies\nOU\n" +
                "- Survivre jusqu’au dernier tour (selon le scénario)."));
        exitButton.addActionListener(e -> System.exit(0));


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

    private JPanel creeJeuPanel(Plateau plateau) {
        JPanel jeuPanel = new JPanel(new BorderLayout());

        // Bouton retour en haut
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton retourButton = new JButton("Retour au menu");
        retourButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        topPanel.add(retourButton);

        JPanel controlePanel = new JPanel();
        controlePanel.setBackground(new Color(240, 240, 240));
        controlePanel.setBorder(BorderFactory.createEmptyBorder(300, 10, 10, 10));

        //buttons  des unites
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