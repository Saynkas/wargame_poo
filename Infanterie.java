import java.util.*;

public class Infanterie extends Unite {
    public Infanterie() {
        this.pointsDeVie = 100;
        this.pointsDeVieMax = 100;
        this.attaque = 10;
        this.defense = 5;
        this.deplacement = 3;
        this.vision = 2;
        this.nom = "Infanterie";
        this.armes = new ArrayList<>();
    }
}
