import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Unite {
    protected int pointsDeVie;
    protected int pointsDeVieMax;
    protected int attaque;
    protected int defense;
    protected int deplacement;
    protected int vision;
    protected String nom;
    protected List<Arme> armes;
    protected boolean aAgitCeTour;// Indique si l'unité a attaqué ou s'est déplacée ce tour
    protected Joueur proprietaire;
    protected boolean estAttaque;

    public Unite() {
        this.armes = new ArrayList<>();
        this.aAgitCeTour = false;
    }

    public void ajouterArme(Arme arme) {
        armes.add(arme);
    }

    public int calculerDegats(Unite cible, TypeDeTerrain terrain, int distance) { // Calcule les dégâts infligés à la cible en fonction de la portée de l'arme et des bonus de terrain
        int degatsTotaux = 0;
        int bonusAttaque = terrain.getBonusAttaque();
        int bonusDefense = terrain.getBonusDefense();

        for (Arme arme : armes) {
            if (distance <= arme.getPortee()) { // Vérifie si l'arme peut atteindre la cible
                int degatsArme = arme.getDegats() + this.attaque + bonusAttaque;
                int defenseCible = cible.getDefense() + bonusDefense;
                int degats = Math.max(0, degatsArme - defenseCible);
                degatsTotaux += degats;
            }
        }
        return degatsTotaux;
    }

    public void attaquer(Unite cible, TypeDeTerrain terrain, int distance) {
        int degats = calculerDegats(cible, terrain, distance);
        
        // Ajout d'un facteur aléatoire (±50%)
        Random rand = new Random();
        int variation = (int)(degats * 0.5 * (rand.nextDouble() - 0.5));
        degats = Math.max(1, degats + variation); // Au moins 1 dégât
        
        if (degats > 0) {
            cible.subirDegats(degats);
            this.aAgitCeTour = true;
        }
    }

    public void subirDegats(int degats) {
        this.pointsDeVie = Math.max(0, this.pointsDeVie - degats);
    }

    public void reinitialiserTour() {
        aAgitCeTour = false; // Réinitialise l'état de l'unité à la fin du tour
    }

    public void recupererPV() {
        if (!aAgitCeTour && estVivant() && !estAttaque) { // Vérifie si l'unité n'a pas attaqué ou déplacé ce tour et si il a pas été attaqué
            int recuperation = (int) Math.ceil(0.1 * pointsDeVieMax); // Récupère 10% des PV max
            this.pointsDeVie = Math.min(pointsDeVieMax, pointsDeVie + recuperation); // Assure que les PV ne dépassent pas le maximum
        }
    }

    public boolean estVivant() {
        return pointsDeVie > 0;
    }

    public int getPointsDeVieMax() {
        return pointsDeVieMax;
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

    public void setAAgitCeTour(boolean bool) {
        this.aAgitCeTour = bool;
    }

    public boolean getAAgitCeTour() {
        return aAgitCeTour;
    }
    public Joueur getProprietaire() {
        return proprietaire;
    }
    public void setProprietaire(Joueur proprietaire) {
        this.proprietaire = proprietaire;
    }
    public boolean getEstAttaque() {
        return estAttaque;
    }
    public void setEstAttaque(boolean estAttaque) {
        this.estAttaque = estAttaque;
    }
    public boolean peutAttaquerDansDirection(int dirRow, int dirCol) {
        return true;
    }
    public int getPortee() {
        int somme = 0;
        // Calcule la portée totale de l'unité en fonction de ses armes
        for (Arme arme : armes) {
            somme += arme.getPortee();
        }
        return somme;
    }
}