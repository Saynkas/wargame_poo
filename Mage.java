import java.io.Serializable;

public class Mage extends Unite implements Serializable {
    public Mage() {
        this.nom = "Mage";
        this.pointsDeVieMax = 25;
        this.pointsDeVie = 25;
        this.attaque = 18;
        this.defense = 4;
        this.deplacement = 3;
        this.vision = 4;

        this.ajouterArme(Arme.BÂTON);
        this.ajouterArme(Arme.BOULE_DE_FEU);
    }
}
