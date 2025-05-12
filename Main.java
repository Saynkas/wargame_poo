import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Toujours exécuter Swing dans le thread de l'interface graphique
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipal fenetre = new FenetrePrincipal();
            fenetre.setVisible(true);
        });
    }
}

