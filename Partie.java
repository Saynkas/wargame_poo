
public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private static int currentTurn;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    public static int getCurrentTurn() {
        return currentTurn;
    }

    public static void setCurrentTurn(int currentTurn) {
        Partie.currentTurn = currentTurn;
    }

    public boolean partieTerminee() {
        return !joueur1.aDesUnitesVivantes() || !joueur2.aDesUnitesVivantes();
    }
}
