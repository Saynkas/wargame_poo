public class Plateau {
    private HexCase[][] cases;

    // Plateau vide, les cases seront remplies plus tard
    public Plateau(int lignes, int colonnes) {
        cases = new HexCase[lignes][colonnes];
    }

    public void setCase(int ligne, int colonne, HexCase hexCase) {
        cases[ligne][colonne] = hexCase;
    }

    public HexCase getCase(int l, int c) {
        return cases[l][c];
    }

    public int getLignes() {
        return cases.length;
    }

    public int getColonnes() {
        return cases[0].length;
    }
}
