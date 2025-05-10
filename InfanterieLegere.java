public class InfanterieLegere extends Unite {

    public InfanterieLegere() {
        this.nom = "Infanterie Légère";
        this.pointsDeVieMax = 35;
        this.pointsDeVie = 35;
        this.attaque = 10;
        this.defense = 6;
        this.deplacement = 4;
        this.vision = 2;

        // Ajout des armes de l'unité
        this.ajouterArme(Arme.EPEE);
        this.ajouterArme(Arme.ARC);
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
