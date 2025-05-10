public class Cavalerie extends Unite {

    public Cavalerie() {
        this.nom = "Cavalerie";
        this.pointsDeVieMax = 40;
        this.pointsDeVie = 40;
        this.attaque = 15;
        this.defense = 7;
        this.deplacement = 5;
        this.vision = 3;

        // Ajout de l'arme à l'unité
        this.ajouterArme(Arme.EPEE);
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
