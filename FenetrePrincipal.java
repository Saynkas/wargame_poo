import javax.swing.*;
import java.awt.*;

public class FenetrePrincipal extends JFrame{
    public FenetrePrincipal(){
        super("Fenetre mainMenu");

        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        BackGroundPanel panel = new BackGroundPanel("backGroundImages/still-life-map-with-dices.jpg");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("WarGame");
        title.setFont(new Font("Impact", Font.BOLD, 60));
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);


        panel.add(Box.createVerticalGlue());



        JButton playButton = new JButton("play");
        JButton guideButton = new JButton("Parametre");
        JButton exitButton = new JButton("Quitter");

        Dimension buttonDimension = new Dimension(150,100);
        playButton.setMaximumSize(buttonDimension);
        guideButton.setMaximumSize(buttonDimension);
        exitButton.setMaximumSize(buttonDimension);

        playButton.setFont(new Font("Arial",Font.BOLD, 20));
        guideButton.setFont(new Font("Arial",Font.BOLD, 20));
        exitButton.setFont(new Font("Arial",Font.BOLD, 20));


        playButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "jouer !"));
        guideButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bienvenue dans ce Wargame tactique tour par tour ! Affrontez vos adversaires sur un champ de bataille hexagonal,\n chaque décision compte. Ce jeu mélange stratégie, gestion des unités et adaptation au terrain.\n" + //
                        "\n" + //
                        "Objectif :\n" + //
                        "\n" + //
                        "    Détruire toutes les unités ennemies\n" + //
                        "\n OU \n" + //
                        "\n" + //
                        "    Survivre jusqu’au dernier tour (selon le scénario)."));
        exitButton.addActionListener(e -> System.exit(0));

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guideButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 100)));
        panel.add(playButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(guideButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(exitButton);

        
        panel.add(Box.createVerticalGlue());

        add(panel);
    }
}

class BackGroundPanel extends JPanel{
    private Image backGroundImage;

    public BackGroundPanel(String imagePath)
    {
        backGroundImage = new ImageIcon(imagePath).getImage();
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(backGroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
