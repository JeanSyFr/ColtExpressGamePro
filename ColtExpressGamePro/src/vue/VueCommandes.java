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
	/**
	 * Le bouton, lorsqu'il est cliqué par l'utilisateur, produit un
	 * événement, de classe [ActionEvent].
	 *
	 * On a ici une variante du schéma observateur/observé : un objet
	 * implémentant une interface [ActionListener] va s'inscrire pour
	 * "écouter" les événements produits par le bouton, et recevoir
	 * automatiquements des notifications.
	 * D'autres variantes d'auditeurs pour des événements particuliers :
	 * [MouseListener], [KeyboardListener], [WindowListener].
	 *
	 * Cet observateur va enrichir notre schéma Modèle-Vue d'une couche
	 * intermédiaire Contrôleur, dont l'objectif est de récupérer les
	 * événements produits par la vue et de les traduire en instructions
	 * pour le modèle.
	 * Cette strate intermédiaire est potentiellement riche, et peut
	 * notamment traduire les mêmes événements de différents façons en
	 * fonction d'un état de l'application.
	 * Ici nous avons un seul bouton réalisant une seule action, notre
	 * contrôleur sera donc particulièrement simple. Cela nécessite
	 * néanmoins la création d'une classe dédiée, qui pourrait être
	 * indépendante de la vue. Ici on en fait une classe interne pour
	 * permettre une lecture linéaire du fichier.
	 */
	
	/**
	 * Classe pour notre contrôleur rudimentaire.
	 * 
	 * Le contrôleur implémente l'interface [ActionListener] qui demande
	 * uniquement de fournir une méthode [actionPerformed] indiquant la
	 * réponse du contrôleur à la réception d'un événement.
	 */
	class Controleur implements ActionListener {
	    /**
	     * On garde un pointeur vers le modèle, car le contrôleur doit
	     * provoquer un appel de méthode du modèle.
	     * Remarque : comme cette classe est interne, cette inscription
	     * explicite du modèle est inutile. On pourrait se contenter de
	     * faire directement référence au modèle enregistré pour la classe
	     * englobante [VueCommandes].
	     */
	    Train train;
	    public Controleur(Train train) { 
	    	this.train = train; 
	    }
	    /**
	     * Action effectuée à réception d'un événement : ?
	     */
	    public void actionPerformed(ActionEvent e) {
			// TODO
	    }
	}
	/** Fin du contrôleur. */

	/**
	 * Retour à la configuration du bouton.
	 * Création d'un contrôleur, attaché au modèle.
	 */
	Controleur ctrl = new Controleur(train);
	/** Enregistrement du contrôleur comme auditeur du bouton. */
	boutonAvance.addActionListener(ctrl);


	/**
	 * Variante : classe interne anonyme.
	 *
	 * Pour le bouton précédent, nous avons défini une classe [Controleur]
	 * à utiliser une seule fois : elle sert à créer un unique objet qu'on
	 * inscrit comme auditeur du bouton, et que plus personne ne mentionne
	 * directement par la suite. Nommer la classe et la définir à part peut
	 * paraître abusif dans ce cas.
	 * 
	 * Voici une variante permettant d'éviter cela. 
	 */
	/** Création et ajout d'un bouton comme avant. */
	JButton boutonAvance2 = new JButton(">>");
	this.add(boutonAvance2);
	/**
	 * Pas de définition d'un objet de classe [Controleur] ici.
	 *
	 * À la place, utilisation de [new], avec :
	 *  - Le nom de l'interface [ActionListener] que l'on doit implémenter.
	 *  - Entre accolades, le contenu d'une classe anonyme que l'on définit
	 *    à la volée.
	 *    Cette classe contient une unique méthode [actionPerformed], qui
	 *    peut agir directement sur l'attribut [modele] de la classe
	 *    englobante [VueCommandes] (la classe anonyme est une classe
	 *    interne).
	 */
	boutonAvance2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    // TODO
	    		//modele.avance();
	    	}
	    });

	
	/**
	 * Variante : lambda.
	 *
	 * Pour implémenter une interface "fonctionnelle", c'est-à-dire une
	 * interface contenant une unique méthode abstraite, on peut encore
	 * simplifier le mécanisme de classe interne anonyme en ne donnant que
	 * le corps de la méthode à définir.
	 *
	 * Cette méthode beaucoup plus concise a été introduite avec Java 8.
	 */
	/** Création et ajout d'un bouton comme avant. */
	JButton boutonAvance3 = new JButton(">>>");
	this.add(boutonAvance3);
	/**
	 * À la place de la classe interne anonyme, une lambda-expression,
	 * constituée en trois partie :
	 *  - Une liste de paramètres, éventuellement accompagnés de leur
	 *    type. Ici il s'agit uniquement de [e], pour lequel Java est
	 *    capable de déduire tout seul le type [ActionEvent].
	 *  - Le symbole de fonction [->].
	 *  - Un bloc de code, ici [ { modele.avance(); } ].
	 */
	boutonAvance3.addActionListener(e -> { 
			//modele.avance(); 
		});
	}
}