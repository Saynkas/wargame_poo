
public class InfanterieLegere extends Unite {
    public InfanterieLegere() {
        this.nom = "Infanterie Légère";
        this.pointsDeVieMax = 35;
        this.pointsDeVie = 35;
        this.attaque = 10;
        this.defense = 6;
        this.deplacement = 4;
        this.vision = 2;

        this.ajouterArme(Arme.EPEE);
        this.ajouterArme(Arme.ARC);
    }
}