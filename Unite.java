import java.util.List;
import java.util.ArrayList;

public abstract class Unite {
    protected int pointsDeVie;
    protected int pointsDeVieMax;
    protected int attaque;
    protected int defense;
    protected int deplacement;
    protected int vision;
    protected String nom;
    protected List<Arme> armes; // Liste des armes de l'unité

    public Unite() {
        this.armes = new ArrayList<>();
    }

    // Ajouter une arme à l'unité
    public void ajouterArme(Arme arme) {
        armes.add(arme);
    }

    // Calcul des dégâts à partir des armes (on utilise les armes de l'unité)
    public int calculerDegats(Unite cible, TypeDeTerrain terrain) {
        int degatsTotaux = 0;
        for (Arme arme : armes) {
            int degatsArme = arme.getDegats();
            degatsTotaux += Math.max(0, degatsArme - cible.getDefense());
        }
        return degatsTotaux;
    }

    public boolean estVivant() {
        return pointsDeVie > 0;
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = pointsDeVie;
    }

    public int getAttaque() {
        return attaque;
    }

    public void setAttaque(int attaque) {
        this.attaque = attaque;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDeplacement() {
        return deplacement;
    }

    public void setDeplacement(int deplacement) {
        this.deplacement = deplacement;
    }

    public int getVision() {
        return vision;
    }

    public void setVision(int vision) {
        this.vision = vision;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Arme> getArmes() {
        return armes;
    }
}
