import java.io.Serializable;

public class Partie implements Serializable {
    private static final long serialVersionUID = 1L;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueurActuel;
    // joueurActuel est le joueur qui doit jouer
    // il est mis à jour à chaque tour
    // il n'est pas nécessaire de le stocker dans la classe Partie
    private int toursInd; // total des tours individuels
    private int turnNumber; // total des tours (actions de j1 et j2)
    private boolean partieCommence;
    private Plateau plateau;

    public Partie() {
        this.toursInd = 1;
        this.turnNumber = 1;
        this.partieCommence = false;
    }


    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        toursInd = 1;
        turnNumber = 1;
        partieCommence = false;
    }

    public int getToursInd() {
        return toursInd;
    }

    public void setToursInd(int toursInd) {
        this.toursInd = toursInd;
    }

    public boolean partieTerminee() {
        return joueur1.aDesUnitesVivantes() && joueur2.aDesUnitesVivantes();
    }

    public Joueur getJoueur1() {
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }

    public void setJoueur1(Joueur joueur1) {
        this.joueur1 = joueur1;
    }

    public void setJoueur2(Joueur joueur2) {
        this.joueur2 = joueur2;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public boolean isPartieCommence() {
        return partieCommence;
    }

    public void setPartieCommence(boolean partieCommence) {
        this.partieCommence = partieCommence;
    }

    public Joueur getJoueurActuel() {
        return joueurActuel;
    }

    public void setJoueurActuel(Joueur joueurActuel) {
        this.joueurActuel = joueurActuel;
        // Met à jour le joueur actuel
    }
    
    public Plateau getPlateau() {
        return plateau;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }
}
