import java.io.Serializable;

public class Cavalerie extends Unite implements Serializable {
    private static final long serialVersionUID = 1L;

    public Cavalerie() {
        this.nom = "Cavalerie";
        this.pointsDeVieMax = 40;
        this.pointsDeVie = 40;
        this.attaque = 15;
        this.defense = 7;
        this.deplacement = 5;
        this.vision = 3;

        this.ajouterArme(Arme.EPEE);
    }
}
