import java.io.Serializable;

public class InfanterieLourde extends Unite implements Serializable {
    private static final long serialVersionUID = 1L;

    public InfanterieLourde() {
        this.nom = "Infanterie Lourde";
        this.pointsDeVieMax = 50;
        this.pointsDeVie = 50;
        this.attaque = 12;
        this.defense = 8;
        this.deplacement = 3;
        this.vision = 2;

        this.ajouterArme(Arme.EPEE);
    }
}