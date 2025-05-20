package src.util;

/**
 * Interface représentant un objet écoutable.
 */
public interface Ecoutable{

    /**
     * Ajoute un écouteur à l'objet écoutable.
     * 
     * @param e l'écouteur à ajouter
     */
    void ajoutEcoutable(Ecouteur e);

    /**
     * Retire un écouteur de l'objet écoutable.
     * 
     * @param e l'écouteur à retirer
     */
    void retraitEcouteur(Ecouteur e);
}