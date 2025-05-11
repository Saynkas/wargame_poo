public class Archer extends Unite {
    public Archer() {
        this.nom = "Archer";
        this.pointsDeVieMax = 30;
        this.pointsDeVie = 30;
        this.attaque = 8;
        this.defense = 5;
        this.deplacement = 2;
        this.vision = 3;

        this.ajouterArme(Arme.ARC);
    }
}