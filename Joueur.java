import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private String nom;
    private List<Unite> unites;

    public Joueur(String nom) {
        this.nom = nom;
        this.unites = new ArrayList<>();
    }

    public void ajouterUnite(Unite unite) {
        unites.add(unite);
    }

    public List<Unite> getUnites() {
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
