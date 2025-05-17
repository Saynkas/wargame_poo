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
        // Retourne le chemin de l'image en fonction du type d'unité
        if (unite instanceof InfanterieLourde) {
            return "./assets/InfanterieLourde.png";
        } else if (unite instanceof Archer) {
            return "./assets/Archer.png";
        } else if (unite instanceof Mage) {
            return "./assets/Mage.png";
        } else if (unite instanceof InfanterieLegere) {
            return "./assets/InfanterieLegere.png";
        } else if (unite instanceof Cavalerie) {
            return "./assets/Cavalerie.png";
        } else {
            return "./assets/error.png"; // Image par défaut
        }
    }
}
