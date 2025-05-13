
public class Plateau{
    private HexCase[][] cases;

    //creation du plateau logique
    public Plateau(int ligne, int colonne){
        cases = new HexCase[ligne][colonne];
        for(int i = 0; i < ligne ; i++){
            for(int j = 0; j < colonne ; j++){
                TypeDeTerrain terrain = HexCase.choixRandTerrain();
                cases[i][j] = new HexCase(i, j, terrain);
            }
        }
    }

    public HexCase getCase(int l, int c){
        return cases[l][c];
    }

    public int getLignes(){
        return cases.length;
    }

    public int getColonnes(){
        return cases[0].length;
    }
}
