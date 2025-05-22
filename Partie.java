import java.io.Serializable;

public class Partie implements Serializable {
    private static final long serialVersionUID = 1L;

    private Joueur joueur1;
    private Joueur joueur2;
    private int toursInd; // total des tours individuels
    private int turnNumber; // total des tours (actions de j1 et j2)
    private boolean partieCommence;
    private Plateau plateau;
    private String mode;
    private int toursDef;

    public Partie() {
        this.toursInd = 1;
        this.turnNumber = 1;
        this.partieCommence = false;
        this.mode = "annihilation";
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
        if (this.mode == "annihilation") {
            return joueur1.aDesUnitesVivantes() && joueur2.aDesUnitesVivantes();
        }
        else {
            return turnNumber > toursDef + 1;
        }
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
        if (toursInd % 2 == 1) {
            return joueur1;
        } else {
            return joueur2;
        }
    }
    public Plateau getPlateau() {
        return plateau;
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getToursDef() {
        return toursDef;
    }

    public void setToursDef(int toursDef) {
        this.toursDef = toursDef;
    }

}
