public enum Arme {
    EPEE("Épée", 8, 1, "Mêlée"),
    ARC("Arc", 6, 3, "Distance"),
    BÂTON("Bâton", 5, 2, "Mêlée"),
    BOULE_DE_FEU("Boule de Feu", 12, 3, "Distance");

    private String nom;
    private int degats;
    private int portee;
    private String type;

    // Constructor
    Arme(String nom, int degats, int portee, String type) {
        this.nom = nom;
        this.degats = degats;
        this.portee = portee;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public int getDegats() {
        return degats;
    }

    public int getPortee() {
        return portee;
    }

    public String getType() {
        return type;
    }
}
