package vue;

import modele.Train;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private Train train;

    /** Constructeur. */
    public VueCommandes(Train train) {
		this.train = train;
		/**
		 * On crée un nouveau bouton, de classe [JButton], en précisant le
		 * texte qui doit l'étiqueter.
		 * Puis on ajoute ce bouton au panneau [this].
		 */
		
		JButton boutonAvance = new JButton(">");
		this.add(boutonAvance);
		
		// classe interne anonyme.
		boutonAvance.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });

		JButton boutonDescend = new JButton("V");
		this.add(boutonDescend);
		
		boutonAvance.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });

		JButton boutonRecule = new JButton("<");
		this.add(boutonRecule);

		boutonAvance.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });
		
		JButton boutonMonte = new JButton("^");
		this.add(boutonMonte);
		
		boutonAvance.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });

		JButton boutonTire = new JButton("-");
		this.add(boutonAvance);
		
		boutonAvance.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });

		JButton boutonBraque = new JButton("$");
		this.add(boutonAvance);
		
		boutonAvance.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });
		
		/*
		if(action.acc <= train.liste.size()){
			boutonAction.setEnabled(true);
		} 
		else {
			boutonAction.setEnabled(false);
		}
		*/
    }
}