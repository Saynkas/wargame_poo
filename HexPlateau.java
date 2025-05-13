import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HexPlateau extends JPanel {
    private static final int RADIUS = 30; //diametre de l'hexagone

    private Plateau plateau;
    private Unite uniteSelectionnee;
    private boolean estEntrainDeplace;
    private int ligneInitial =-1;
    private int colonneInitial =-1;

    //unite selectionnee par le joueur
    public void setUniteSelectionnee(Unite unite) {
        this.uniteSelectionnee = unite;
    }


    public HexPlateau(Plateau plateau){
        this.plateau = plateau;
        this.uniteSelectionnee = null;
        setOpaque(false);

        
        //avoir les coordonee de la souris au moment de l'appuie
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                gererClick(e.getX(), e.getY());
            }
        });
    }

    //intercation avec le plateau
    private void gererClick(int x, int y) {
        for(int i = 0; i < plateau.getLignes(); i++) {
            for(int j = 0; j < plateau.getColonnes(); j++) {
                Point center = hexToPixel(j, i);
                Polygon hex = createHexagon(center.x, center.y);

                if(hex.contains(x,y)){

                    HexCase hexCase = plateau.getCase(i, j);

                    if (!hexCase.estOccupee()) {
                        if (estEntrainDeplace) {

                            if (estVoisine(ligneInitial, colonneInitial, i, j)) {

                                hexCase.placerUnite(uniteSelectionnee);
                                estEntrainDeplace = false;
                                uniteSelectionnee = null;
                                repaint();
                            }


                        } else {
                            hexCase.placerUnite(uniteSelectionnee);
                            uniteSelectionnee = null;
                            estEntrainDeplace = false;
                            repaint();
                        }
                    }
                    else{

                        uniteSelectionnee = hexCase.getUnite();
                        hexCase.retirerUnite();
                        estEntrainDeplace = true;
                        ligneInitial = i;
                        colonneInitial = j;
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

