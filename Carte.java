public class Carte {
    private CaseHexagonale[][] cases;

    public Carte(int largeur, int hauteur) {
        cases = new CaseHexagonale[largeur][hauteur];
        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {
                cases[i][j] = new CaseHexagonale(TypeDeTerrain.values()[i % TypeDeTerrain.values().length]);
            }
        }
    }

    public CaseHexagonale getCase(int x, int y) {
        if (x >= 0 && x < cases.length && y >= 0 && y < cases[0].length) {
            return cases[x][y];
        }
        return null;
    }

    public void placerUnite(Unite unite, int x, int y) {
        if (x >= 0 && x < cases.length && y >= 0 && y < cases[0].length) {
            cases[x][y].setUnite(unite);
        }
    }
}
