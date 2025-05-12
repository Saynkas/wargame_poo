public class UniteSelectionPanel extends JPanel {
    private HexPlateau hexPlateau;
    private JLabel uniteInfoLabel;

    public UniteSelectionPanel(HexPlateau hexPlateau) {
        this.hexPlateau = hexPlateau;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Sélection d'unité"));

        // Label d'info
        uniteInfoLabel = new JLabel("Aucune unité sélectionnée");
        add(uniteInfoLabel);

        // Boutons de selection
        add(createUnitButton("Infanterie", new Infanterie()));
    }

    private JButton createUnitButton(String nom, Unite unite) {
        JButton button = new JButton(nom);
        button.addActionListener(e -> {
            hexPlateau.setUniteSelectionnee(unite);
            updateUniteInfo(unite);
        });
        return button;
    }

    //donner des info sur l'unite selectionne
    private void updateUniteInfo(Unite unite) {
        String info = String.format("%s - PV : %d, att : %d, def : %d, dep : %d",
                unite.getnom(),
                unite.getPointsDeVie(),
                unite.getAttaque(),
                unite.getDefense(),
                unite.getDeplacement());
        uniteInfoLabel.setText(info);
    }
}
