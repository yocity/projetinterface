package src.Modele;

import src.util.AbstractModeleEcoutable;
import src.util.Ecouteur;

/**
 * Classe représentant le jeu de Bataille Navale.
 */
public class Game extends AbstractModeleEcoutable {

    /**
     * Carte du joueur 1.
     */
    private Map j1;

    /**
     * Carte du joueur 2.
     */
    private Map j2;

    /**
     * Carte du joueur actuel.
     */
    private Map currentPlayer;

    /**
     * Indique si le placement des navires est en cours.
     */
    private boolean placingShips = true;

    /**
     * Tailles des navires à placer.
     */
    private int[] shipSizes = {5, 4, 3, 2, 1};

    /**
     * Index du navire actuellement en cours de placement.
     */
    private int shipIndex = 0;

    /**
     * Constructeur de la classe Game.
     * Initialise les cartes des joueurs et définit le joueur actuel.
     */
    public Game() {
        super(); // Appelle explicitement le constructeur de la superclasse
        this.j1 = new Map();
        this.j2 = new Map();
        this.currentPlayer = j1;
    }
    
    /**
     * Change de joueur et met à jour la grille.
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == j1) ? j2 : j1;
        fireChangement();
    }

    /**
     * Retourne la carte du joueur 1.
     * 
     * @return La carte du joueur 1.
     */
    public Map getJ1() {
        return j1;
    }

    /**
     * Retourne la carte du joueur 2.
     * 
     * @return La carte du joueur 2.
     */
    public Map getJ2() {
        return j2;
    }

    /**
     * Retourne la carte de l'adversaire du joueur actuel.
     * 
     * @return La carte de l'adversaire.
     */
    public Map getOpponent() {
        return currentPlayer == j1 ? j2 : j1;
    }

    /**
     * Retourne la carte du joueur actuel.
     * 
     * @return La carte du joueur actuel.
     */
    public Map getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Notifie les écouteurs des changements dans la grille.
     */
    @Override
    public void fireChangement() {
        for (Ecouteur e : ecouteurs) {
            if (currentPlayer == j1) {
                e.updateGrid(j1.getGrid(), j2.getGrid());
            } else {
                e.updateGrid(j2.getGrid(), j1.getGrid());
            }
        }
    }

    /**
     * Permet au joueur actuel de tirer sur l'adversaire.
     * 
     * @param x Coordonnée X du tir
     * @param y Coordonnée Y du tir
     * @return Résultat du tir
     */
    public int currentPlayerShoot(int x, int y) {
        Map adversaire = getOpponent();
        int result = adversaire.shoot(x, y);
        fireChangement();
        return result;
    }
    
    /**
     * Gère le placement des navires.
     * 
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @param direction Orientation du navire
     * @return Résultat du placement (0 = Succès, 1 = Changement de joueur, 2 = Début du combat, -1 = Échec)
     */
    public int handleShipPlacement(int x, int y, boolean direction) {
        if (!placingShips) {
            return -1; // Placement déjà terminé
        }

        int size = shipSizes[shipIndex];
        if (getCurrentPlayer().placeShip(x, y, size, direction)) {
            shipIndex++;
            if (shipIndex >= shipSizes.length) {
                if (currentPlayer == j1) {
                    shipIndex = 0;
                    switchPlayer();
                    return 1; // Passage au joueur 2
                } else {
                    placingShips = false;
                    switchPlayer();
                    return 2; // Début de la bataille
                }
            }
            fireChangement();
            return 0; // Navire placé avec succès
            
        }
        return -1; // Impossible de placer le navire

    }

    /**
     * Indique si le placement des navires est en cours.
     * 
     * @return true si le placement est en cours, false sinon.
     */
    public boolean getPlacingShips() {
        return placingShips;
    }
    
    /**
     * Gère une attaque sur l'adversaire.
     * 
     * @param x Coordonnée X du tir
     * @param y Coordonnée Y du tir
     * @return Message du résultat de l'attaque
     */
    public String handleAttack(int x, int y) {
        if (placingShips) {
            return "Placement des navires en cours!";
        }

        int result = currentPlayerShoot(x, y);
        String message;

        switch (result) {
            case 0: 
                message = "Raté!";
                break;
            case -1: 
                return "Tir hors des limites!";
            case -2: 
                return "Vous avez déjà tiré ici!";
            default:
                if (result >= 100) {
                    message = "Coulé! Navire de taille " + (result - 100);
                } else if (result >= 10) {
                    message = "Touché! Navire de taille " + (result - 10);
                } else {
                    message = "Résultat inconnu !";
                }
        }
        fireChangement();
        if (getOpponent().dead()) {
            return "Fin du jeu! " + (currentPlayer == j1 ? "Joueur 1" : "Joueur 2") + " a gagné!";
        }
        
        switchPlayer();
        return message;
    }
}
