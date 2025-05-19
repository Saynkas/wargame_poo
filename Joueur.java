import java.util.ArrayList;

public class Joueur {
    private int id;
    private String nom;
    private ArrayList<Unite> unites;

    public Joueur(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.unites = new ArrayList<>();
    }

    public void ajouterUnite(Unite unite) {
        unites.add(unite);
    }

    public ArrayList<Unite> getUnites() {
        return unites;
    }

    public int getId(){
        return id;
    }

    

    public boolean aDesUnitesVivantes() {
        for (Unite unite : unites) {
            if (unite.estVivant()) {
                return true;
            }
        }
        return false;
    }

    public boolean peutEncoreJouer() {
        for (Unite u : unites) {
            if (!u.getAAgitCeTour()) {
                return true;
            }
        }
        return false;
    }

    public void resetAAgitCeTour() {
        for (Unite u : unites) {
            u.setAAgitCeTour(false);
        }
    }

    public String getNom() {
        return nom;
    }
}
