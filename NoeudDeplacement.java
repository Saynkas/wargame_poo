import java.io.Serializable;

public class NoeudDeplacement implements Serializable {
    private static final long serialVersionUID = 1L;

    int ligne;
    int colonne;
    int coutTotal;

    public NoeudDeplacement(int ligne, int colonne, int coutTotal) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.coutTotal = coutTotal;
    }
}