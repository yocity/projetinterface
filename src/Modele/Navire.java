package src.Modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un navire dans le jeu.
 */
public class Navire {
    /**
     * La taille du navire.
     */
    private int taille;

    /**
     * La vie restante du navire.
     */
    private int vie;

    /**
     * Les positions occupées par le navire.
     */
    private List<int[]> positions;

    /**
     * Constructeur de la classe Navire.
     * 
     * @param taille La taille du navire.
     */
    public Navire(int taille) {
        this.taille = taille;
        this.vie = taille;
        this.positions = new ArrayList<>();
    }

    /**
     * Définit les positions occupées par le navire.
     * 
     * @param positions Liste des positions occupées par le navire.
     */
    public void setSpace(List<int[]> positions) {
        this.positions = positions;
    }

    /**
     * Retourne les positions occupées par le navire.
     * 
     * @return Liste des positions occupées par le navire.
     */
    public List<int[]> getSpace() {
        return positions;
    }

    /**
     * Réduit la vie du navire de 1 lorsqu'il est touché.
     */
    public void shooted() {
        vie--;
    }

    /**
     * Retourne la vie restante du navire.
     * 
     * @return Vie restante du navire.
     */
    public int live() {
        return vie;
    }

    /**
     * Retourne la taille du navire.
     * 
     * @return Taille du navire.
     */
    public int getTaille() {
        return taille;
    }
}