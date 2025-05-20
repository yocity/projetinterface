package src.Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.Modele.*;
import src.Vue.*;

/**
 * Contrôleur du jeu qui gère les interactions entre la vue et le modèle.
 */
public class GameController implements ActionListener {
    /**
     * La vue du jeu.
     */
    private GameView view;
    
    /**
     * Le modèle du jeu.
     */
    private Game game;
    
    /**
     * Constructeur du GameController.
     * Initialise la vue et le modèle du jeu.
     */
    public GameController() {
        this.view = new GameView(this);
        this.game = new Game();
        this.game.ajoutEcoutable(view);

        String message = 
            "<html><div style='text-align: center; width: 400px; padding: 10px;'>" +
            "   <h2 style='color: #1a237e;'>🚢 Bienvenue dans la Bataille Navale à 2 joueurs</h2>" +
            "   <hr>" +
            
            "   <h3 style='color: #0d47a1;'>Phase de placement des navires</h3>" +
            "   <div style='text-align: left; margin: 15px;'>" +
            "       <p>▪ Cliquez sur votre grille (à gauche) pour choisir la position d'un navire</p>" +
            "       <p>▪ Une fenêtre vous demandera l'orientation (horizontal ou vertical)</p>" +
            "       <p>▪ Ordre des navires :</p>" +
            "       <ul style='list-style-type: none; padding-left: 20px;'>" +
            "           <li>Taille 5 ➔ Taille 4 ➔ Taille 3 ➔ Taille 2 ➔ Taille 1</li>" +
            "       </ul>" +
            "   </div>" +
            
            "   <h3 style='color: #0d47a1;'>Phase d'attaque</h3>" +
            "   <div style='text-align: left; margin: 15px;'>" +
            "       <p>▪ Cliquez sur la grille ennemie (à droite) pour tirer</p>" +
            "       <p>▪ Symboles :</p>" +
            "       <ul style='list-style-type: none; padding-left: 20px;'>" +
            "           <li><font color='#FF0000'>●</font> : Touché et Coulé</li>" +
            "           <li><font color='#00FF00'>●</font> : Raté</li>" +
            "       </ul>" +
            "       <p>▪ Un message vous indiquera le résultat de chaque tir</p>" +
            "   </div>" +
            
            "   <h3 style='color: #0d47a1;'>Objectif</h3>" +
            "   <p style='color: #d32f2f; font-weight: bold;'>" +
            "       Coulez tous les navires ennemis pour remporter la victoire !" +
            "   </p>" +
            
            "   <hr>" +
            "   <h3 style='color: #2e7d32;'>À présent, placez vos navires !</h3>" +
            "</div></html>";
        view.showMessage(message, "Information");
    }

    /**
     * Gère les actions déclenchées par les événements de l'interface utilisateur.
     * 
     * @param e L'événement d'action déclenché.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] parts = e.getActionCommand().split(",");
        String gridType = parts[0];
        int row = Integer.parseInt(parts[1]);
        int col = Integer.parseInt(parts[2]);

        if (game.getPlacingShips() && "P".equals(gridType)) {
            handleShipPlacement(row, col);
        } else if (!game.getPlacingShips() && "E".equals(gridType)) {
            handleAttack(row, col);
        }
    }

    /**
     * Gère le placement des navires sur la grille.
     * 
     * @param x La coordonnée x où placer le navire.
     * @param y La coordonnée y où placer le navire.
     */
    private void handleShipPlacement(int x, int y) {
        boolean direction = view.askForDirection();
        int result = game.handleShipPlacement(x, y, direction);

        switch (result) {
            case 1:
                view.showMessage("Joueur 2, placez vos navires maintenant.");
                break;
            case 2:
                view.showMessage("Début de la bataille! Joueur 1 commence.");
                break;
            case 0:
                view.showMessage("Navire placé avec succès.");
                break;
            case -1:
                view.showError("Impossible de placer le navire ici !");
                break;
        }
    }

    /**
     * Gère l'attaque sur la grille de l'adversaire.
     * 
     * @param x La coordonnée x de l'attaque.
     * @param y La coordonnée y de l'attaque.
     */
    private void handleAttack(int x, int y) {
        String message = game.handleAttack(x, y);
        view.showMessage(message);
        
        if (!message.equals("Tir hors des limites!") && !message.equals("Vous avez déjà tiré ici!") && !message.equals("Fin du jeu! Joueur 1 a gagné!") && !message.equals("Fin du jeu! Joueur 2 a gagné!")) {
            switchPlayer();
        }
    }

    /**
     * Change le joueur actuel et notifie la vue.
     */
    private void switchPlayer() {
        view.showMessage((game.getCurrentPlayer() == game.getJ1() ? "Joueur 1" : "Joueur 2") + ", c'est votre tour.");
    }
}