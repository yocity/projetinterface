package src.Vue;

import javax.swing.*;

import src.Controleur.*;
import src.util.Ecouteur;

import java.awt.*;
import java.awt.event.*;

/**
 * Classe GameView qui représente la vue du jeu Bataille Navale.
 * Elle implémente l'interface Ecouteur pour recevoir les mises à jour du modèle.
 */
public class GameView extends JFrame implements Ecouteur {
    /**
     * Le contrôleur du jeu.
     */
    private final GameController controller;

    /**
     * Le panneau de la grille du joueur.
     */
    private final JPanel playerGridPanel;

    /**
     * Le panneau de la grille de l'ennemi.
     */
    private final JPanel enemyGridPanel;

    /**
     * Les cellules de la grille du joueur.
     */
    private final JPanel[][] playerCells = new JPanel[11][11];

    /**
     * Les cellules de la grille de l'ennemi.
     */
    private final JPanel[][] enemyCells = new JPanel[11][11];

    /**
     * Constructeur de la classe GameView.
     * Initialise l'interface graphique du jeu.
     * 
     * @param controller Le contrôleur du jeu.
     */
    public GameView(GameController controller) {
        this.controller = controller;
        setTitle("Bataille Navale - L2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        playerGridPanel = createGridPanel("VOTRE GRILLE", "P", playerCells);
        enemyGridPanel = createGridPanel("GRILLE ENNEMIE", "E", enemyCells);

        mainPanel.add(playerGridPanel);
        mainPanel.add(enemyGridPanel);

        add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Crée un panneau de grille avec un titre et un type spécifié.
     * 
     * @param title Le titre du panneau de grille.
     * @param type Le type de grille (joueur ou ennemi).
     * @param cells Les cellules de la grille.
     * @return Le panneau de grille créé.
     */
    private JPanel createGridPanel(String title, String type, JPanel[][] cells) {
        JPanel gridPanel = new JPanel(new BorderLayout());
        gridPanel.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        gridPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel gridContainer = new JPanel(new BorderLayout());

        JPanel topLabels = new JPanel(new GridLayout(1, 11));
        for (char c = 'A'; c <= 'J'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.RIGHT);
            topLabels.add(label);
        }

        JPanel leftLabels = new JPanel(new GridLayout(10, 1));
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            leftLabels.add(label);
        }

        JPanel grid = new JPanel(new GridLayout(10, 10, 1, 1));
        grid.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(35, 35));
                cell.setBackground(new Color(240, 240, 240));
                cell.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                
                int finalRow = row;
                int finalCol = col;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        controller.actionPerformed(new ActionEvent(
                            this, 
                            ActionEvent.ACTION_PERFORMED, 
                            type + "," + finalRow + "," + finalCol
                        ));
                    }
                });
                
                cells[row][col] = cell;
                grid.add(cell);
            }
        }
        
        gridContainer.add(topLabels, BorderLayout.NORTH);
        gridContainer.add(leftLabels, BorderLayout.WEST);
        gridContainer.add(grid, BorderLayout.CENTER);
        gridPanel.add(gridContainer, BorderLayout.CENTER);

        return gridPanel;
    }

    /**
     * Met à jour les grilles du joueur et de l'ennemi.
     * 
     * @param ownerGrid La grille du joueur.
     * @param enemyGrid La grille de l'ennemi.
     */
    public void updateGrid(char[][] ownerGrid, char[][] enemyGrid) {
        updateCells(ownerGrid, playerCells, true);
        updateCells(enemyGrid, enemyCells, false);
    }

    /**
     * Met à jour les cellules de la grille avec les informations fournies.
     * 
     * @param grid La grille à mettre à jour.
     * @param cells Les cellules de la grille.
     * @param showShips Indique si les navires doivent être affichés.
     */
    private void updateCells(char[][] grid, JPanel[][] cells, boolean showShips) {
        for (int row = 1; row <= 10; row++) {
            for (int col = 1; col <= 10; col++) {
                // Réinitialise l'arrière-plan de la cellule et supprime les composants
                cells[row][col].setBackground(new Color(240, 240, 240));
                cells[row][col].removeAll();
    
                // Définit l'arrière-plan du navire si applicable
                if (showShips && grid[row][col] == '@') {
                    cells[row][col].setBackground(new Color(94, 94, 94));
                }
    
                // Ajoute des marqueurs de tir ou de raté
                if (grid[row][col] == '#') {
                    cells[row][col].add(createHitMarker(Color.RED));
                } else if (grid[row][col] == 'x') {
                    cells[row][col].add(createHitMarker(Color.GREEN));
                }
    
                cells[row][col].revalidate();
                cells[row][col].repaint();
            }
        }
    }
    
    /**
     * Crée un marqueur de tir avec une couleur spécifiée.
     * 
     * @param color La couleur du marqueur.
     * @return Le composant JComponent représentant le marqueur.
     */
    private JComponent createHitMarker(Color color) {
        JPanel marker = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(7, 3, 20, 20); // Ovale centré
            }
        };
        marker.setOpaque(false); // Arrière-plan transparent
        marker.setPreferredSize(new Dimension(35, 35));
        return marker;
    }

    /**
     * Demande à l'utilisateur la direction du placement du navire.
     * 
     * @return true si la direction est horizontale, false si elle est verticale.
     */
    public boolean askForDirection() {
        int res = JOptionPane.showConfirmDialog(this, "Horizontal? (Yes = Horizontal, No = Vertical)", "Direction", JOptionPane.YES_NO_OPTION);
        return res == JOptionPane.YES_OPTION;
    }

    /**
     * Affiche un message à l'utilisateur.
     * 
     * @param msg Le message à afficher.
     */
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    
    public void showMessage(String msg, String title) {
        JOptionPane.showMessageDialog(
            this, 
            msg, 
            title, 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Affiche un message d'erreur à l'utilisateur.
     * 
     * @param msg Le message d'erreur à afficher.
     */
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
