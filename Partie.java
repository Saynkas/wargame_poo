
public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;

    public Partie(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    public boolean partieTerminee() {
        return !joueur1.aDesUnitesVivantes() || !joueur2.aDesUnitesVivantes();
    }
}
