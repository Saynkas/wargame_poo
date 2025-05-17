import java.awt.*;
import java.awt.event.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.*;

public class HexPlateau extends JPanel {
    private static final int RADIUS = 30; //diametre de l'hexagone

    private Plateau plateau;
    private Unite uniteSelectionnee;
    private boolean estEntrainDeplace;
    private int ligneInitial =-1;
    private int colonneInitial =-1;
    private boolean placementNouvelleUnite;
    private Map<Point, Integer> casesAccessiblesCache;

    //unite selectionnee par le joueur
    public void setUniteSelectionnee(Unite unite) {
        this.uniteSelectionnee = unite;
        this.placementNouvelleUnite = true;
    }


    public HexPlateau(Plateau plateau){
        this.plateau = plateau;
        this.uniteSelectionnee = null;
        this.placementNouvelleUnite = false;
        setOpaque(false);

        
        //avoir les coordonee de la souris au moment de l'appui
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                gererClick(e.getX(), e.getY());
            }
        });
    }

    //interaction avec le plateau
    private void gererClick(int x, int y) {
        for(int i = 0; i < plateau.getLignes(); i++) {
            for(int j = 0; j < plateau.getColonnes(); j++) {
                Point center = hexToPixel(j, i);
                Polygon hex = createHexagon(center.x, center.y);

                if(hex.contains(x,y)){

                    HexCase hexCase = plateau.getCase(i, j);
                    
                    if (placementNouvelleUnite) {
                        if (!hexCase.estOccupee()) {
                            hexCase.placerUnite(uniteSelectionnee);
                            uniteSelectionnee = null;
                            placementNouvelleUnite = false;
                            repaint();
                        }
                        return;
                    }
                    else if (hexCase.estOccupee() && !estEntrainDeplace) {
                        if (!hexCase.getUnite().getAAgitCeTour()) {
                            uniteSelectionnee = hexCase.getUnite();
                            ligneInitial = i;
                            colonneInitial = j;
                            estEntrainDeplace = true;
                            calculerCasesAccessibles();
                            repaint();
                        }
                    }
                    else if (estEntrainDeplace) {
                        if (!hexCase.estOccupee() && casesAccessiblesCache.containsKey(new Point(i, j))) {
                            plateau.getCase(ligneInitial, colonneInitial).retirerUnite();
                            hexCase.placerUnite(uniteSelectionnee);
                            uniteSelectionnee.setAAgitCeTour(true);
                            estEntrainDeplace = false;
                            casesAccessiblesCache = null;
                            repaint();
                        }
                    }
                }
            }
        }
    }

    // Calcul des cases accessibles et mises en cache (avec Djikstra)
    private void calculerCasesAccessibles() {
        // On initialise le cache
        casesAccessiblesCache = new HashMap<>();
        // On crée une file
        PriorityQueue<NoeudDeplacement> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.coutTotal));
        // On ajoute la case de départ à la file avec un coût de 0
        queue.add(new NoeudDeplacement(ligneInitial, colonneInitial, 0));
        casesAccessiblesCache.put(new Point(ligneInitial, colonneInitial), 0);

        // Tant que la file n'est pas vide :
        while (!queue.isEmpty()) {
            // On sélectionne une case
            NoeudDeplacement current = queue.poll();

            // Si le coût de déplacement total actuel est supérieur au coût précédemment trouvé pour cette case OU à maxint (impossible)
            if (current.coutTotal > casesAccessiblesCache.getOrDefault(new Point(current.ligne, current.colonne), Integer.MAX_VALUE)) {
                // On passe
                continue;
            }
            
            // On détermine les cases voisines
            int[][] directions = (current.colonne % 2 == 0) ? 
                new int[][]{ {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, 1} } : 
                new int[][]{ {-1, -1}, {-1, 0}, {0, -1}, {0, 1}, {1, -1}, {1, 0} };
            
            // Pour chaque case voisine :
            for (int[] dir : directions) {
                int newLigne = current.ligne + dir[0];
                int newColonne = current.colonne + dir[1];
                
                // On vérifie que la case voisine existe (pas en dehors du plateau)
                if (newLigne >= 0 && newLigne < plateau.getLignes() && newColonne >= 0 && newColonne < plateau.getColonnes()) {
                    
                    // On calcule le coût de déplacement jusqu'à cette case depuis la case actuelle
                    HexCase hexCase = plateau.getCase(newLigne, newColonne);
                    int terrainCost = hexCase.getTerrain().getCoutDeDeplacement();
                    int totalCost = current.coutTotal + terrainCost;
                    Point key = new Point(newLigne, newColonne);
                    
                    // Si la case est 1. libre, 2. dans la distance atteignable par l'unité et 
                    // 3. la case n'est pas dans le cache OU on a trouvé un chemin plus court que la valeur déjà en cache
                    if (!hexCase.estOccupee() && totalCost <= uniteSelectionnee.getDeplacement() && 
                        (!casesAccessiblesCache.containsKey(key) || totalCost < casesAccessiblesCache.get(key))) {
                        // On rajoute la case au cache avec ses coordonnées et son coût d'accès                        
                        casesAccessiblesCache.put(key, totalCost);
                        // On rajoute cette case à la file
                        queue.add(new NoeudDeplacement(newLigne, newColonne, totalCost));
                    }
                }
            }
        }
    }

    private Point hexToPixel(int col, int row) {
        int x = (int) (RADIUS * Math.sqrt(3) * (col + 0.5 * (row % 2)));
        int y = (int) (RADIUS * 1.5 * row);
        return new Point(x + 50, y + 50); // 50 pour marge
    }

    private Polygon createHexagon(int xCenter, int yCenter) {
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3.0 * i + Math.PI / 6.0;
            int x = (int) (xCenter + RADIUS * Math.cos(angle));
            int y = (int) (yCenter + RADIUS * Math.sin(angle));
            hex.addPoint(x, y);
        }
        return hex;
    }


    private Color getColorForTerrain(TypeDeTerrain terrain) {
        return switch (terrain) {
            case PLAINE -> new Color(180, 240, 180);// vert clair
            case FORET -> new Color(34, 139, 34);// vert foncé
            case MONTAGNE -> new Color(139, 137, 137);// gris
            case COLLINE -> new Color(255, 228, 181);// beige
            case FORTERESSE -> new Color(255, 215, 0);// or
            default -> Color.LIGHT_GRAY;
        }; 
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    
        for (int row = 0; row < plateau.getLignes(); row++) {
            for (int col = 0; col < plateau.getColonnes(); col++) {
                Point center = hexToPixel(col, row);
                Polygon hex = createHexagon(center.x, center.y);
    
                HexCase hexCase = plateau.getCase(row, col);
                Color color = getColorForTerrain(hexCase.getTerrain());
    
                g2d.setColor(color);
                g2d.fill(hex);

                if (estEntrainDeplace && casesAccessiblesCache.containsKey(new Point(row, col))) {
                    int cout = casesAccessiblesCache.get(new Point(row, col));
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(String.valueOf(cout), center.x - 3, center.y + 5);
                }
    
                g2d.setColor(Color.BLACK);
                g2d.draw(hex);

                hexCase.dessinerUnite(g2d, center.x, center.y);
            }
        }
    }

    private boolean estVoisine(int i1, int j1, int i2, int j2) {
        int[][] directionsPair = { {-1, 0}, {-1, +1}, {0, -1}, {0, +1}, {1, 0}, {1, +1} };
        int[][] directionsImpair = { {-1, -1}, {-1, 0}, {0, -1}, {0, +1}, {1, -1}, {1, 0} };
        
        int[][] directions = (j1 % 2 == 0) ? directionsPair : directionsImpair;
    
        for(int[] dir : directions) {
            if(i1 + dir[0] == i2 && j1 + dir[1] == j2) {
                return true;
            }
        }
        return false;
    }

}

