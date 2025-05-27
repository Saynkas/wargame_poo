
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.*;
import javax.swing.*;



public class HexPlateau extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_UNITES_PAR_JOUEUR = 5; // Limite d'unités par joueur

    private static final int RADIUS = 30; //diametre de l'hexagone

    private Plateau plateau;
    private Unite uniteSelectionnee;
    private boolean estEntrainDeplace;
    private int ligneInitial =-1;
    private int colonneInitial =-1;
    private boolean placementNouvelleUnite;
    private Map<Point, Integer> casesAccessiblesCache;
    private Partie partie;
    private JButton buttonEndTurn;
    private HexCase oldHexCase;
    private FenetrePrincipal fenetrePrincipale;
    private ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> historyPlayerUnits;


    private final Image textureForet;
    private final BufferedImage textureForetBuffered;
    private final Image textureChateau;
    private final BufferedImage textureChateauBuffered;
    private final Image texturePlaine;
    private final BufferedImage texturePlaineBuffered;
    private final Image textureDesert;
    private final BufferedImage textureDesertBuffered;
    private final Image textureRiviere;
    private final BufferedImage textureRiviereBuffered;
    private final Image textureBrouillard1;
    private final BufferedImage textureBrouillardBuffered1;
    private final Image textureBrouillard2;
    private final BufferedImage textureBrouillardBuffered2;



    //unite selectionnee par le joueur
    public void setUniteSelectionnee(Unite unite) {
        this.uniteSelectionnee = unite;
        this.placementNouvelleUnite = true;
    }


    public HexPlateau(Plateau plateau, Partie partie, JButton buttonEndTurn, FenetrePrincipal fenetrePrincipale){
        this.buttonEndTurn = buttonEndTurn;
        this.partie = partie;
        this.plateau = plateau;
        genererCarteFixe();
        this.fenetrePrincipale = fenetrePrincipale;
        this.uniteSelectionnee = null;
        this.placementNouvelleUnite = false;
        this.historyPlayerUnits = new ArrayList<>();
        setOpaque(false);

        this.textureForet = new ImageIcon(getClass().getResource("/assets/map/forest.jpg")).getImage();
        this.textureForetBuffered = toBufferedImage(this.textureForet);
        this.textureChateau = new ImageIcon(getClass().getResource("/assets/map/castle.png")).getImage();
        this.textureChateauBuffered = toBufferedImage(this.textureChateau);
        this.texturePlaine = new ImageIcon(getClass().getResource("/assets/map/hill.jpg")).getImage();
        this.texturePlaineBuffered = toBufferedImage(this.texturePlaine);
        this.textureDesert = new ImageIcon(getClass().getResource("/assets/map/desert.jpg")).getImage();
        this.textureDesertBuffered = toBufferedImage(this.textureDesert);
        this.textureRiviere = new ImageIcon(getClass().getResource("/assets/map/river3.jpg")).getImage();
        this.textureRiviereBuffered = toBufferedImage(this.textureRiviere);
        this.textureBrouillard1 = new ImageIcon(getClass().getResource("/assets/map/fog.jpg")).getImage();
        this.textureBrouillardBuffered1 = toBufferedImage(this.textureBrouillard1);
        this.textureBrouillard2 = new ImageIcon(getClass().getResource("/assets/map/fog2.jpg")).getImage();
        this.textureBrouillardBuffered2 = toBufferedImage(this.textureBrouillard2);


        
        //avoir les coordonee de la souris au moment de l'appui
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                gererClick(e.getX(), e.getY());
            }
        });
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage bi) return bi;
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }


    //interaction avec le plateau
    private void gererClick(int x, int y) {
       // System.out.println("x : " + x + " y : " + y);
        Joueur joueurActuel = partie.getJoueurActuel();
       // System.out.println(partie.partieTerminee());

        // Parcours de toutes les cases du plateau
        for (int i = 0; i < plateau.getLignes(); i++) {
            for (int j = 0; j < plateau.getColonnes(); j++) {

                // Calcul du centre du hexagone et création du polygone correspondant
                Point center = hexToPixel(j, i);
                Polygon hex = createHexagon(center.x, center.y);

                // Vérification si le clic est à l’intérieur de ce hexagone
                if (hex.contains(x, y)) {

                    HexCase hexCase = plateau.getCase(i, j);

                    // Si on est en phase de placement d’une nouvelle unité
                    if (placementNouvelleUnite) {
                        // La case doit être vide pour placer l'unité
                        if (!hexCase.estOccupee()) {
                            // Vérifier si le joueur n'a pas déjà atteint la limite d'unités
                            int unitesJoueur = (partie.getToursInd() % 2 == 1) ? 
                                              partie.getJoueur1().getUnites().size() : 
                                              partie.getJoueur2().getUnites().size();
                            
                            if (unitesJoueur > MAX_UNITES_PAR_JOUEUR - 1) {
                                JOptionPane.showMessageDialog(null, "Vous avez atteint la limite maximale d'unités (" + MAX_UNITES_PAR_JOUEUR + ")");
                                uniteSelectionnee = null;
                                return;
                            }
                    
                            // Tour impair -> placement possible dans les colonnes < 3
                            if (partie.getToursInd() % 2 == 1) {
                                if (j <= 3) {
                                    historyPlayerUnits.add(new AbstractMap.SimpleEntry<>(x, y));
                                    hexCase.placerUnite(uniteSelectionnee);
                                    partie.getJoueur1().ajouterUnite(uniteSelectionnee);
                                    uniteSelectionnee = null;
                                    placementNouvelleUnite = false;
                                    hexCase.getUnite().setAAgitCeTour(true);
                                    rendreCasesAutourVisibles(i, j, joueurActuel, true);
                                    repaint();
                                } else {
                                    JOptionPane.showMessageDialog(null, "respectez les limites a gauche");
                                }
                            } 
                            // Tour pair -> placement possible dans les colonnes > (total - 3)
                            else if (partie.getToursInd() % 2 == 0) {
                                if (j >= (plateau.getColonnes() - 4)) {
                                    hexCase.placerUnite(uniteSelectionnee);
                                    partie.getJoueur2().ajouterUnite(uniteSelectionnee);
                                    uniteSelectionnee = null;
                                    placementNouvelleUnite = false;
                                    hexCase.getUnite().setAAgitCeTour(true);
                                    rendreCasesAutourVisibles(i, j, joueurActuel, true);
                                    repaint();
                                } else {
                                    JOptionPane.showMessageDialog(null, "respectez les limites a droite");
                                }
                            }
                        }
                        return; // Fin du traitement si on était en placement
                    } 
                    // Si la case est occupée et on n’est pas en train de déplacer une unité
                    else if (hexCase.estOccupee() && !estEntrainDeplace) {

                        // Vérifier que l’unité n’a pas déjà agi ce tour
                        // Et que c’est bien une unité du joueur actif
                        if (!hexCase.getUnite().getAAgitCeTour() && 
                            ((partie.getJoueur1().getUnites().contains(hexCase.getUnite()) && partie.getToursInd() % 2 == 1) ||
                            (partie.getJoueur2().getUnites().contains(hexCase.getUnite()) && partie.getToursInd() % 2 == 0))) {

                            // Sélectionner cette unité pour déplacement
                            uniteSelectionnee = hexCase.getUnite();
                            fenetrePrincipale.mettreAJourMessage("Unité sélectionnée : " + hexCase.getUnite().getNom());

                            ligneInitial = i;
                            colonneInitial = j;
                            estEntrainDeplace = true;
                            oldHexCase = hexCase;
                            calculerCasesAccessibles();
                            recalculerVisibiliteGlobale(joueurActuel);
                            rendreCasesAutourVisibles(i, j, joueurActuel, true);
                            repaint();
                        }
                    } 
                    // Si on est en train de déplacer une unité
                    else if (estEntrainDeplace) {

                        // Si on reclique sur la case d’origine, annuler le déplacement
                        if (hexCase == oldHexCase) {
                            estEntrainDeplace = false;
                            casesAccessiblesCache = null;
                            repaint();
                        } 
                        // Sinon si la case ciblée est libre et accessible
                        else if (!hexCase.estOccupee() && casesAccessiblesCache.containsKey(new Point(i, j))) {
                            boolean aAttaque = false;
                            int[] direction = getDirection(ligneInitial, colonneInitial, i, j);
                        
                            // Recherche d'une cible dans cette direction
                            for (int d = 1; d <= uniteSelectionnee.getPortee(); d++) {
                                int checkRow = ligneInitial + direction[0] * d;
                                int checkCol = colonneInitial + direction[1] * d;
                        
                                if (checkRow >= 0 && checkRow < plateau.getLignes() &&
                                    checkCol >= 0 && checkCol < plateau.getColonnes()) {
                        
                                    HexCase caseCible = plateau.getCase(checkRow, checkCol);
                                    if (caseCible.estOccupee() &&
                                        caseCible.getUnite().getProprietaire() != uniteSelectionnee.getProprietaire() &&
                                        uniteSelectionnee.peutAttaquerDansDirection(direction[0], direction[1])) {
                        
                                        // Cherche la meilleure case atteignable pour attaquer
                                        for (int recul = d - 1; recul >= 0; recul--) {
                                            int fromRow = ligneInitial + direction[0] * recul;
                                            int fromCol = colonneInitial + direction[1] * recul;
                                            Point p = new Point(fromRow, fromCol);
                        
                                            if (casesAccessiblesCache.containsKey(p)) {
                                                HexCase caseDepartAttaque = plateau.getCase(fromRow, fromCol);
                        
                                                // Déplacement vers la case depuis laquelle on peut attaquer
                                                plateau.getCase(ligneInitial, colonneInitial).retirerUnite();
                                                caseDepartAttaque.placerUnite(uniteSelectionnee);
                        
                                                // Réalisation de l'attaque
                                                int distance = calculerDistance(fromRow, fromCol, checkRow, checkCol);
                                                uniteSelectionnee.attaquer(caseCible.getUnite(), caseCible.getTerrain(), distance);
                        
                                                if (!caseCible.getUnite().estVivant()) {
                                                    caseCible.retirerUnite();
                                                }
                        
                                                //uniteSelectionnee.setAAgitCeTour(true);
                                                estEntrainDeplace = false;
                                                casesAccessiblesCache = null;
                                                rendreCasesAutourVisibles(fromRow, fromCol, joueurActuel, true);
                                                repaint();
                                                aAttaque = true;
                                                caseCible.getUnite().setEstAttaque(true);
                                                break;
                                            }
                                        }
                                        break; // on a attaqué une unité donc on sort
                                    }
                                }
                            }
                        
                            // Sinon, déplacement normal vers la case cliquée
                            if (!aAttaque) {
                                
                                plateau.getCase(ligneInitial, colonneInitial).retirerUnite();
                                hexCase.placerUnite(uniteSelectionnee);
                                recalculerVisibiliteGlobale(joueurActuel);
                                uniteSelectionnee.setAAgitCeTour(true);
                                estEntrainDeplace = false;
                                casesAccessiblesCache = null;
                                repaint();
                            }
                        }                        
                        // Gestion de l’attaque si on est en train de déplacer une unité
                        // et que la case ciblée est occupée par une unité ennemie accessible
                        if (hexCase.estOccupee() && casesAccessiblesCache != null) { //&& casesAccessiblesCache.containsKey(new Point(i, j))) {
                            Unite cible = hexCase.getUnite();

                            // Vérifier que la cible appartient à l'adversaire
                            if (cible.getProprietaire() != uniteSelectionnee.getProprietaire()) {
                                // Calculer la direction de déplacement
                                int[] direction = getDirection(ligneInitial, colonneInitial, i, j);
                                
                                // Vérifier si l'unité peut attaquer dans cette direction (porte d'arme)
                                if (uniteSelectionnee.peutAttaquerDansDirection(direction[0], direction[1])) {
                                    // Calculer la distance entre l'unité sélectionnée et la cible
                                    int distance = calculerDistance(ligneInitial, colonneInitial, i, j);

                                    // Effectuer l'attaque
                                    uniteSelectionnee.attaquer(cible, hexCase.getTerrain(), distance);

                                    // Si la cible est morte, la retirer et déplacer l'attaquant à sa place
                                    if (!cible.estVivant()) {
                                        hexCase.retirerUnite();
                                        plateau.getCase(ligneInitial, colonneInitial).retirerUnite();
                                        hexCase.placerUnite(uniteSelectionnee);
                                        recalculerVisibiliteGlobale(joueurActuel);
                                        if (!partie.partieTerminee()) {
                                            fenetrePrincipale.finDePartie();
                                        }
                                    }

                                   // uniteSelectionnee.setAAgitCeTour(true);
                                    cible.setEstAttaque(true);
                                    estEntrainDeplace = false;
                                    casesAccessiblesCache = null;
                                    repaint();
                                }
                            }
                        }

                    }

                }
            }
        }
    }

    public int calculerDistance(int x1, int y1, int x2, int y2) {
        // Implémentation simple de distance hexagonale
        // (Adaptée à votre système de coordonnées)
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        return (dx + dy + Math.abs(dx - dy)) / 2;
    }

    public void setEstEntrainDeplace(boolean bool){
        this.estEntrainDeplace = bool;
    } 

    private int[] getDirection(int fromRow, int fromCol, int toRow, int toCol) {
        int[] direction = new int[2];
        direction[0] = Integer.compare(toRow, fromRow); // direction verticale (-1, 0, 1)
        direction[1] = Integer.compare(toCol, fromCol); // direction horizontale (-1, 0, 1)
        return direction;
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
                    if (( !hexCase.estOccupee() || hexCase.contientUniteEnnemie(uniteSelectionnee) )
                        && totalCost <= uniteSelectionnee.getDeplacement()
                        && (!casesAccessiblesCache.containsKey(key) || totalCost < casesAccessiblesCache.get(key))) {

                        // On rajoute la case au cache avec ses coordonnées et son coût d'accès                        
                        casesAccessiblesCache.put(key, totalCost);
                        // On rajoute cette case à la file
                        queue.add(new NoeudDeplacement(newLigne, newColonne, totalCost));
                    }
                }
            }
        }
    }

    // Calcul des cases accessibles et mises en cache (avec Djikstra) test
    public Map<Point, Integer> calculerCasesAccessibles(int ligneInitial, int colonneInitial, Unite uniteSelectionnee) {
        // On initialise le cache
        Map<Point, Integer> casesAccessiblesCache = new HashMap<>();
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
                    if (( !hexCase.estOccupee() || hexCase.contientUniteEnnemie(uniteSelectionnee) )
                            && totalCost <= uniteSelectionnee.getDeplacement()
                            && (!casesAccessiblesCache.containsKey(key) || totalCost < casesAccessiblesCache.get(key))) {

                        // On rajoute la case au cache avec ses coordonnées et son coût d'accès
                        casesAccessiblesCache.put(key, totalCost);
                        // On rajoute cette case à la file
                        queue.add(new NoeudDeplacement(newLigne, newColonne, totalCost));
                    }
                }
            }
        }
        return casesAccessiblesCache;
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
            case FORTERESSE -> new Color(105, 105, 105);// gris
            case RIVIERE -> new Color(70, 130, 180); // bleu rivière
            case DESERT -> new Color(245, 245, 220); //beige
            default -> Color.LIGHT_GRAY;
        }; 
    }

    private void genererCarteFixe() {
        int lignes = plateau.getLignes();
        int colonnes = plateau.getColonnes();
    
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                TypeDeTerrain terrain;
    
                // Rivière centrale verticale
                if (j == colonnes / 2 || j == colonnes / 2 - 1) {
                    terrain = TypeDeTerrain.RIVIERE;
                }
                // Forteresses proches du centre
                else if ((i == lignes / 2 || i == lignes / 2 - 1) && (j == colonnes / 2 - 3 || j == colonnes / 2 + 2)) {
                    terrain = TypeDeTerrain.FORTERESSE;
                }
                // Désert en diagonale
                else if ((i - j) % 7 == 0) {
                    terrain = TypeDeTerrain.DESERT;
                }
                // Forêt bien dispersée
                else if ((i + j) % 5 == 0) {
                    terrain = TypeDeTerrain.FORET;
                }
                // Zones de départ : plaine
                else if (j <= 2 || j >= colonnes - 3) {
                    terrain = TypeDeTerrain.PLAINE;
                }
                // Par défaut : plaine
                else {
                    terrain = TypeDeTerrain.PLAINE;
                }
    
                HexCase hex = new HexCase(i, j, terrain);
                plateau.setCase(i, j, hex);
            }
        }
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
                if (hexCase.getTerrain() == TypeDeTerrain.FORET && textureForet != null) {
                    TexturePaint texture = new TexturePaint(textureForetBuffered, hex.getBounds());
                    g2d.setPaint(texture);
                    g2d.fill(hex);
                } 
                else if (hexCase.getTerrain() == TypeDeTerrain.RIVIERE && textureRiviere != null) {
                    TexturePaint texture = new TexturePaint(textureRiviereBuffered, hex.getBounds());
                    g2d.setPaint(texture);
                    g2d.fill(hex);
                }
                else if (hexCase.getTerrain() == TypeDeTerrain.DESERT && textureDesert != null) {
                    TexturePaint texture = new TexturePaint(textureDesertBuffered, hex.getBounds());
                    g2d.setPaint(texture);
                    g2d.fill(hex);
                }
                else if (hexCase.getTerrain() == TypeDeTerrain.FORTERESSE && textureChateau!= null) {
                    TexturePaint texture = new TexturePaint(textureChateauBuffered, hex.getBounds());
                    g2d.setPaint(texture);
                    g2d.fill(hex);
                }
                else if (hexCase.getTerrain() == TypeDeTerrain.PLAINE && texturePlaine != null) {
                    TexturePaint texture = new TexturePaint(texturePlaineBuffered, hex.getBounds());
                    g2d.setPaint(texture);
                    g2d.fill(hex);
                } else {
                    Color color = getColorForTerrain(hexCase.getTerrain());
                    g2d.setColor(color);
                    g2d.fill(hex);
                }

                g2d.setColor(Color.BLACK);
                g2d.draw(hex);
                Joueur joueurActuel = partie.getJoueurActuel();
                
                if (!hexCase.estVisiblePour(joueurActuel)) {
                    /*Color brouillard = new Color(255, 255, 255, 200); // Blanc avec transparence (alpha = 180)
                    g2d.setColor(brouillard);
                    g2d.fillPolygon(hex);*/
                    Random random = new Random();
                    int intRand = random.nextInt(2) + 1;
                    if(intRand == 1){
                        TexturePaint texture = new TexturePaint(textureBrouillardBuffered1, hex.getBounds());
                        g2d.setPaint(texture);
                        g2d.fill(hex);
                    }else{
                        TexturePaint texture = new TexturePaint(textureBrouillardBuffered2, hex.getBounds());
                        g2d.setPaint(texture);
                        g2d.fill(hex);
                    }
                }
                else {
                    hexCase.dessinerUnite(g2d, center.x, center.y);
                }

                if (estEntrainDeplace && casesAccessiblesCache.containsKey(new Point(row, col))) {
                    int cout = casesAccessiblesCache.get(new Point(row, col));
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(String.valueOf(cout), center.x - 3, center.y + 5);
                }

                // limite du joueur 1
                if (col == 3) {
                    g2d.setColor(Color.RED);
                    Point p1 = hex.getBounds().getLocation(); // coin en haut à gauche
                    int x1 = p1.x + hex.getBounds().width;
                    int y1 = p1.y;
                    int y2 = y1 + hex.getBounds().height;
                    g2d.drawLine(x1, y1+10, x1, y2);
                }
    
                // limite du joueur 2
                if (col == plateau.getColonnes() - 4) {
                    g2d.setColor(Color.BLUE);
                    Point p1 = hex.getBounds().getLocation();
                    int x1 = p1.x;
                    int y1 = p1.y;
                    int y2 = y1 + hex.getBounds().height;
                    g2d.drawLine(x1, y1+10, x1, y2);
                }
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

    public void rendreCasesAutourVisibles(int ligne, int colonne, Joueur joueur, boolean bool) {
        // Rendre la case centrale visible
        plateau.getCase(ligne, colonne).setVisiblePour(joueur, bool);
        
        //  vision de l'unité
        HexCase caseCentrale = plateau.getCase(ligne, colonne);
        int porteeVision = caseCentrale.estOccupee() ? caseCentrale.getUnite().getVision() : 1;
        
        // Parcourir toutes les cases du plateau
        for (int i = 0; i < plateau.getLignes(); i++) {
            for (int j = 0; j < plateau.getColonnes(); j++) {
                // Calculer la distance entre la case centrale et la case courante
                int distance = calculerDistance(ligne, colonne, i, j) * 2;
                
                // Si la distance est inférieure ou égale à la portée de vision rendre visible
                if (distance <= porteeVision) {
                    plateau.getCase(i, j).setVisiblePour(joueur, bool);
                }
            }
        }
    }

    private Point trouverPositionUnite(Unite unite) {
        for (int i = 0; i < plateau.getLignes(); i++) {
            for (int j = 0; j < plateau.getColonnes(); j++) {
                HexCase hexCase = plateau.getCase(i, j);
                if (hexCase.estOccupee() && hexCase.getUnite() == unite) {
                    return new Point(i, j);
                }
            }
        }
        return null; // Si l'unité n'est pas trouvée (ne devrait jamais arriver)
    }

    public void recalculerVisibiliteGlobale(Joueur joueur) {
        // D'abord, tout masquer
        for (int i = 0; i < plateau.getLignes(); i++) {
            for (int j = 0; j < plateau.getColonnes(); j++) {
                plateau.getCase(i, j).setVisiblePour(joueur, false);
            }
        }
    
        // Ensuite, réappliquer la visibilité pour chaque unité du joueur
        for (Unite unite : joueur.getUnites()) {
            if (unite.estVivant()) {
                Point position = trouverPositionUnite(unite);
                if (position != null) {
                    rendreCasesAutourVisibles(position.x, position.y, joueur, true);
                }
            }
        }
    }

    public void setUnitsIA(AbstractMap.SimpleEntry<Integer, Integer> coords) {
        MouseEvent clickEvent = new MouseEvent(
                this,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                coords.getKey(), coords.getValue(),
                1,
                false,
                MouseEvent.BUTTON1
        );
        this.dispatchEvent(clickEvent);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fixPlayer1Invisible() {
        for (int i = 0; i < 2; i++) {
            for (AbstractMap.SimpleEntry<Integer, Integer> coords : historyPlayerUnits) {
              //  System.out.println("x : " + coords.getKey() + " y : " + coords.getValue());
                MouseEvent clickEvent = new MouseEvent(
                        this,
                        MouseEvent.MOUSE_CLICKED,
                        System.currentTimeMillis(),
                        0,
                        coords.getKey(), coords.getValue(),
                        1,
                        false,
                        MouseEvent.BUTTON1
                );
                this.dispatchEvent(clickEvent);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        estEntrainDeplace = false;
        casesAccessiblesCache = null;
        repaint();
    }

    public ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> getHistoryPlayerUnits() {
        return historyPlayerUnits;
    }

    public Plateau getPlateau() {
        return plateau;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HexPlateau:\n");
        for (int i = 0; i < plateau.getLignes(); i++) {
            for (int j = 0; j < plateau.getColonnes(); j++) {
                HexCase hexCase = plateau.getCase(i, j);
                sb.append("Case(").append(i).append(", ").append(j).append("): ").append(hexCase.getTerrain()).append("\n");
            }
        }
        return sb.toString();
    }
}

