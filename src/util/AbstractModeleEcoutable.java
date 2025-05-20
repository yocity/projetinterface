package src.util;

import java.util.*;

/**
 * Classe abstraite représentant un modèle écoutable.
 * Implémente l'interface Ecoutable pour gérer les écouteurs.
 */
public abstract class AbstractModeleEcoutable implements Ecoutable {
    
    /**
     * Liste des écouteurs enregistrés.
     */
    protected List<Ecouteur> ecouteurs;

    /**
     * Constructeur de la classe AbstractModeleEcoutable.
     * Initialise la liste des écouteurs.
     */
    protected AbstractModeleEcoutable() {
        super();
        ecouteurs = new ArrayList<Ecouteur>();
    }

    /**
     * Ajoute un écouteur à la liste des écouteurs.
     * 
     * @param e L'écouteur à ajouter.
     */
    @Override
    public void ajoutEcoutable(Ecouteur e) {
        ecouteurs.add(e);
    }

    /**
     * Retire un écouteur de la liste des écouteurs.
     * 
     * @param e L'écouteur à retirer.
     */
    @Override
    public void retraitEcouteur(Ecouteur e) {
        ecouteurs.remove(e);
    }

    /**
     * Méthode abstraite pour notifier les écouteurs d'un changement.
     */
    public abstract void fireChangement();
}