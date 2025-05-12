import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


public class HexCase{

    public enum Terrain{Plat, Foret, Montagne, Eau};

    private Terrain terrain;
    private Unite unite;
    private int ligne, colonne;
    
    public HexCase(int ligne, int colonne, Terrain terrain){
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

    public Terrain getTerrain(){
        return terrain;
    }
    public static Terrain choixRandTerrain(){
        Random rand = new Random();
        int indexRand = rand.nextInt(Terrain.values().length);
        Terrain terrainRand = Terrain.values()[indexRand];
        return terrainRand;
    }

    //dessin de l'unite sur le plateau
    public void dessinerUnite(Graphics g, int x, int y){
        if(unite != null){
            System.out.println("Dessin unité à " + x + "," + y); // Debug
            g.setColor(Color.RED);
            g.fillOval(x-10, y-10, 20, 20);
            g.setColor(Color.WHITE);
            g.drawString(unite.getNom().substring(0, 1), x - 3, y + 5);            
        }
    }

    
    


}
