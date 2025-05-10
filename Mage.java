public class Mage extends Unite {

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

    @Override
    public int calculerDegats(Unite cible, TypeDeTerrain terrain) {
        int degatsTotaux = 0;
        for (Arme arme : armes) {
            int degatsArme = arme.getDegats();
            degatsTotaux += Math.max(0, degatsArme - cible.getDefense());
        }
        return degatsTotaux;
    }
}
