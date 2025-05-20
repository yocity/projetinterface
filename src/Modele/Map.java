package src.Modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la carte du jeu Bataille Navale.
 */
public class Map {

    /**
     * Grille de jeu sous forme de tableau 2D de caractères.
     */
    private char[][] grid = new char[11][11];

    /**
     * Tableau contenant les 5 navires de la carte.
     */
    private Navire[] navires = new Navire[5];

    /**
     * Constructeur de la classe Map.
     * Initialise la grille de jeu avec des espaces vides.
     */
    public Map() {
        // Initialisation de la grille
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    /**
     * Place un navire sur la grille.
     * 
     * @param x La coordonnée x de départ.
     * @param y La coordonnée y de départ.
     * @param size La taille du navire.
     * @param isHorizontal Indique si le navire est placé horizontalement.
     * @return true si le navire a été placé avec succès, false sinon.
     */
    public boolean placeShip(int x, int y, int size, boolean isHorizontal) {
        // Vérifie si la position et la direction du navire sont valides avant de le placer
        if (isHorizontal) {
            if (y + size - 1 > 10) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[x][y + i] != ' ') {
                    return false;
                }
            }
        } else {
            if (x + size - 1 > 10) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[x + i][y] != ' ') {
                    return false;
                }
            }
        }

        // Création et placement du navire
        Navire navire = new Navire(size);
        List<int[]> positions = new ArrayList<>();

        if (isHorizontal) {
            for (int i = 0; i < size; i++) {
                grid[x][y + i] = '@';
                positions.add(new int[]{x, y + i});
            }
        } else {
            for (int i = 0; i < size; i++) {
                grid[x + i][y] = '@';
                positions.add(new int[]{x + i, y});
            }
        }

        navire.setSpace(positions);

        // Trouver un emplacement libre dans le tableau navires
        for (int i = 0; i < navires.length; i++) {
            if (navires[i] == null) {
                navires[i] = navire;
                break;
            }
        }
        return true;
    }

    /**
     * Effectue un tir sur la grille.
     * 
     * @param x La coordonnée x de la cible.
     * @param y La coordonnée y de la cible.
     * @return Un code indiquant le résultat du tir :
     *         -1 pour hors des limites,
     *         -2 pour déjà tiré,
     *         0 pour raté,
     *         10 + taille du navire pour touché,
     *         100 + taille du navire pour coulé,
     *         -114514 pour erreur inconnue.
     */
    public int shoot(int x, int y) {
        if (x < 1 || x > 10 || y < 1 || y > 10) { // Hors des limites
            return -1;
        } else if (grid[x][y] == 'x' || grid[x][y] == '#') { // Déjà tiré ici
            return -2;
        } else {
            if (grid[x][y] == '@') { // Touché
                grid[x][y] = '#';

                // Vérifie quel navire est touché
                for (Navire navire : navires) {
                    if (navire != null) {
                        for (int[] pos : navire.getSpace()) {
                            if (pos[0] == x && pos[1] == y) {
                                navire.shooted();
                                if (navire.live() == 0) {
                                    return 100 + navire.getTaille(); // Code pour "navire coulé"
                                } else {
                                    return 10 + navire.getTaille(); // Code pour "navire touché mais pas coulé"
                                }
                            }
                        }
                    }
                }
            } else { // Raté
                grid[x][y] = 'x';
                return 0;
            }
        }
        return -114514; // Erreur inconnue
    }

    /**
     * Vérifie si tous les navires sont coulés.
     * 
     * @return true si tous les navires sont coulés, false sinon.
     */
    public boolean dead() {
        for (Navire navire : navires) {
            if (navire != null && navire.live() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Récupère la grille de jeu.
     * 
     * @return La grille de jeu sous forme de tableau 2D de caractères.
     */
    public char[][] getGrid() {
        return grid;
    }

    /**
     * Récupère un navire spécifique (de 1 à 5).
     * 
     * @param i L'index du navire à récupérer (de 1 à 5).
     * @return Le navire correspondant à l'index, ou null si l'index est invalide.
     */
    public Navire getNavire(int i) {
        if (i >= 1 && i <= 5) {
            return navires[i - 1];
        }
        return null;
    }
}
