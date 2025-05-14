import java.util.ArrayList;

public class Joueur {
    private String nom;
    private ArrayList<Unite> unites;

    public Joueur(String nom) {
        this.nom = nom;
        this.unites = new ArrayList<>();
    }

    public void ajouterUnite(Unite unite) {
        unites.add(unite);
    }

    public ArrayList<Unite> getUnites() {
        return unites;
    }

    public boolean aDesUnitesVivantes() {
        for (Unite unite : unites) {
            if (unite.estVivant()) {
                return true;
            }
        }
        return false;
    }

    public String getNom() {
        return nom;
    }
}
