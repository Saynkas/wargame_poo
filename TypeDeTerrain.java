public enum TypeDeTerrain {
    PLAINE(0, 0, 1),
    FORET(1, 1, 2),
    DESERT(2, 3, 3),
    RIVIERE(1, 2, 2),
    FORTERESSE(3, 5, 1);

    private int bonusAttaque;
    private int bonusDefense;
    private int coutDeDeplacement;

    TypeDeTerrain(int bonusAttaque, int bonusDefense, int coutDeDeplacement) {
        this.bonusAttaque = bonusAttaque;
        this.bonusDefense = bonusDefense;
        this.coutDeDeplacement = coutDeDeplacement;
    }

    public int getBonusAttaque() {
        return bonusAttaque;
    }

    public int getBonusDefense() {
        return bonusDefense;
    }

    public int getCoutDeDeplacement() {
        return coutDeDeplacement;
    }
}
