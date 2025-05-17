
public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private int toursInd; // total des tours individuels
    private int turnNumber; // total des tours (actions de j1 et j2)
    private boolean partieCommence;

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
}
