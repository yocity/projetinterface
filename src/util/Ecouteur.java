package src.util;

/**
 * Interface Ecouteur
 * 
 * Cette interface définit une méthode pour mettre à jour une grille.
 */
public interface Ecouteur{

    /**
     * Met à jour la grille avec les nouvelles valeurs.
     * 
     * @param grid La grille actuelle.
     * @param grid1 La nouvelle grille à appliquer.
     */
    void updateGrid(char[][] grid, char[][] grid1);
}