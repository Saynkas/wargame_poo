import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;


public class HexCase{

    private TypeDeTerrain terrain;
    private Unite unite;
    private int ligne, colonne;
    private boolean estVisible = false;
    private boolean estVisibileParJ1 = false;
    private boolean estVisibileParJ2 = false;
    
    public HexCase(int ligne, int colonne, TypeDeTerrain terrain){
        this.ligne = ligne;
        this.colonne = colonne;
        this.terrain = terrain;
        this.unite = null;
    }

    public Unite getUnite(){
        return unite;
    }

    public boolean estOccupee(){
        return unite != null;
    }

    public void placerUnite(Unite u){
        this.unite = u;
    }

    public void retirerUnite(){
        this.unite = null;
    }

    public TypeDeTerrain getTerrain(){
        return terrain;
    }

    public boolean getEstVisible(){
        return estVisible;
    }

    public void setEstVisible(boolean bool){
        this.estVisible = bool;
    }

    public boolean estVisiblePour(Joueur joueur){
        if(joueur == null) return false;

        return joueur.getId() == 1? estVisibileParJ1 : estVisibileParJ2;

    }

    public void setVisiblePour(Joueur joueur, boolean bool){
        if(joueur.getId() == 1) estVisibileParJ1 = bool;
        else estVisibileParJ2 = bool;
    }

    public static TypeDeTerrain choixRandTerrain(){
        Random rand = new Random();
        int indexRand = rand.nextInt(TypeDeTerrain.values().length);
        TypeDeTerrain terrainRand = TypeDeTerrain.values()[indexRand];
        return terrainRand;
    }

    //dessin de l'unite sur le plateau
    public void dessinerUnite(Graphics g, int x, int y) {
        if(unite != null) {
            try {
                // Charge l'image correspondant au type d'unité
                String imagePath = getImagePathForUnit(unite);
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage();
                
                // Dessine l'image centrée sur l'hexagone
                int imgWidth = 30;
                int imgHeight = 30;
                g.drawImage(img, x - imgWidth/2, y - imgHeight/2, imgWidth, imgHeight, null);
                
                // Dessine la barre de vie
                int barWidth = 30; // Largeur de la barre de vie moins que la largeur du hexagone
                int barHeight = 5;// Hauteur de la barre de vie 5 car la barre est placée sous l'image
                int barX = x - barWidth/2; // Centrer la barre de vie sous l'image
                int barY = y + imgHeight/2 + 2; // Position sous l'image en fonction de la hauteur de l'image
                
                // rouge qui représente la difference entre les PV max et les PV actuels
                g.setColor(Color.RED);
                g.fillRect(barX, barY, barWidth, barHeight); // rectangle rouge 
                
                // Partie verte (PV actuels)
                double pvRatio = (double)unite.getPointsDeVie() / unite.getPointsDeVieMax();
                int greenWidth = (int)(barWidth * pvRatio);
                g.setColor(Color.GREEN);
                g.fillRect(barX, barY, greenWidth, barHeight);
                
                // Contour de la barre
                g.setColor(Color.BLACK);
                g.drawRect(barX, barY, barWidth, barHeight);
                
            } catch (Exception e) {
                // Fallback si l'image n'est pas trouvée
                g.setColor(Color.RED);
                g.fillOval(x-10, y-10, 20, 20);
                g.setColor(Color.WHITE);
                g.drawString(unite.getNom().substring(0, 1), x - 3, y + 5);
            }
        }
    }

    private String getImagePathForUnit(Unite unite) {
        String path = unite.getProprietaire().getId() == 1 ? "Joueur1" : "Joueur2";

        // Retourne le chemin de l'image en fonction du type d'unité
        if (path.equals("Joueur1")) {
            if (unite instanceof InfanterieLourde) {
                return "./assets/Joueur1/InfanterieLourde.png";
            } else if (unite instanceof Archer) {
                return "./assets/Joueur1/Archer.png";
            } else if (unite instanceof Mage) {
                return "./assets/Joueur1/Mage.png";
            } else if (unite instanceof InfanterieLegere) {
                return "./assets/Joueur1/InfanterieLegere.png";
            } else if (unite instanceof Cavalerie) {
                return "./assets/Joueur1/Cavalerie.png";
            } else {
                return "./assets/error.png"; // Image par défaut
            }
        }
        else {
            if (unite instanceof InfanterieLourde) {
                return "./assets/Joueur2/InfanterieLourde.png";
            } else if (unite instanceof Archer) {
                return "./assets/Joueur2/Archer.png";
            } else if (unite instanceof Mage) {
                return "./assets/Joueur2/Mage.png";
            } else if (unite instanceof InfanterieLegere) {
                return "./assets/Joueur2/InfanterieLegere.png";
            } else if (unite instanceof Cavalerie) {
                return "./assets/Joueur2/Cavalerie.png";
            } else {
                return "./assets/error.png"; // Image par défaut
            }
        }
    }
    public boolean contientUniteEnnemie(Unite unite) {
        return estOccupee() && getUnite().getProprietaire() != unite.getProprietaire();
    }

}
