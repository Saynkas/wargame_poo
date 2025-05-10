public class CaseHexagonale {
    private TypeDeTerrain terrain;
    private Unite unite;

    public CaseHexagonale(TypeDeTerrain terrain) {
        this.terrain = terrain;
        this.unite = null;
    }

    public boolean estOccupee() {
        return unite != null;
    }

    public int getCoutDeDeplacement(Unite unite) {
        return terrain.getCoutDeDeplacement();
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public TypeDeTerrain getTerrain() {
        return terrain;
    }
}
