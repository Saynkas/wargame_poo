public class InfanterieLourde extends Unite {
    public InfanterieLourde() {
        this.nom = "Infanterie Lourde";
        this.pointsDeVieMax = 50;
        this.pointsDeVie = 50;
        this.attaque = 12;
        this.defense = 8;
        this.deplacement = 3;
        this.vision = 1;

        this.ajouterArme(Arme.EPEE);
    }
}