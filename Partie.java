import java.util.List;

public class Partie {
    private Joueur joueur1;
    private Joueur joueur2;
    private Carte carte;

    public Partie(Joueur joueur1, Joueur joueur2, Carte carte) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.carte = carte;
    }

    public void jouerTour(Joueur joueur) {
        List<Unite> unitesJoueur = joueur.getUnites();
        for (Unite unite : unitesJoueur) {
            if (unite.estVivant()) {
                System.out.println(unite.getNom() + " est en mouvement.");
            }
        }
    }

    public boolean partieTerminee() {
        return !joueur1.aDesUnitesVivantes() || !joueur2.aDesUnitesVivantes();
    }

    public void declareVainqueur() {
        if (joueur1.aDesUnitesVivantes()) {
            System.out.println(joueur1.getNom() + " a gagné!");
        } else {
            System.out.println(joueur2.getNom() + " a gagné!");
        }
    }
}
