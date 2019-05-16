package vue;

import modele.Bandit;
import modele.Butin;
import modele.Train;


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class VueTrain extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private Train train;
    
    // dimention d'une position en nombre de pixels 
    private final static int largeurWagon = 150;
    private final static int hauteurWagon = 100;
    

    /** Constructeur. */
    public VueTrain(Train train) {
		this.train = train;
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		train.addObserver(this);
		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de cellules et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(this.largeurWagon * train.NB_WAGONS,
					      this.hauteurWagon * 2);
		this.setPreferredSize(dim);
    }

    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() { 
    	repaint(); 
    }

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
		super.repaint();
		
		Train.Wagon currentWagon = train.getLocomotive();
		
		final int NB_WAGONS = train.NB_WAGONS;
		int x = 10;
		int y = 10;
		
		// affichage de la locomotive
		paint(g, currentWagon, x + NB_WAGONS*160, y);
		
		/** Pour chaque locomotive... */
		for(int i=1; i<=NB_WAGONS; i++) {
		    //for(int j=1; j<=1; j++) {
			/**
			 * ... Appeler une fonction d'affichage auxiliaire.
			 * On lui fournit les informations de dessin [g] et les
			 * coordonnées du coin en haut à gauche.
			 */
			currentWagon = currentWagon.getSuivant();
			paint(g, currentWagon, x + i*160, y);
			
			
		    
			//}
		}
    }
    /**
     * Fonction auxiliaire de dessin d'un wagon.
     * Ici, la classe [Wagon] ne peut être désignée que par l'intermédiaire
     * de la classe [Train] à laquelle elle est interne, d'où le type
     * [Train.Wagon].
     * Ceci serait impossible si [Wagon] était déclarée privée dans [Train].
     */
    private void paint(Graphics g, Train.Wagon w, int x, int y) {
    	g.drawRect(x, y, 150, 100);
    	int ytemp;
    	
    	ytemp = 5;
    	for (Bandit b : w.getBandits() ) {
    		g.drawString(b.getName(), x + 5, ytemp);
    		ytemp += 10;
    	}
    	
    	ytemp = 5;
    	for (Butin b : w.getPossesseur().getButins()) {
    		g.drawString(b.getNom(), x + 55, ytemp);
    		ytemp += 10;
    	}
    	
    	if (w.getMarshall()) {
    		g.drawString(this.train.getMarshall().getName(), x + 105, 5);
    	}
    }

	public static void main(String[] args) {
		
		

	}

}
