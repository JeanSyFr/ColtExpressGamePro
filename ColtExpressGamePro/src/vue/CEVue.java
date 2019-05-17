package vue;

import modele.Action;
import modele.Bandit;
import modele.Marshall;
import modele.Train;
import vue.Observer;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CEVue {
	
	/**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueTrain et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueTrain vueTrain;
    private VueCommandes vueCommandes;
    private JTextArea console;
     

    /** Construction d'une vue attachée au modèle, contenu dans la classe Train. */
    public CEVue(Train train) {
		/** Définition de la fenêtre principale. */
		frame = new JFrame();
		frame.setTitle("Colt Express");
		/**
		 * On précise un mode pour disposer les différents éléments à
		 * l'intérieur de la fenêtre. Quelques possibilités sont :
		 *  - BorderLayout (défaut pour la classe JFrame) : chaque élément est
		 *    disposé au centre ou le long d'un bord.
		 *  - FlowLayout (défaut pour un JPanel) : les éléments sont disposés
		 *    l'un à la suite de l'autre, dans l'ordre de leur ajout, les lignes
		 *    se formant de gauche à droite et de haut en bas. Un élément peut
		 *    passer à la ligne lorsque l'on redimensionne la fenêtre.
		 *  - GridLayout : les éléments sont disposés l'un à la suite de
		 *    l'autre sur une grille avec un nombre de lignes et un nombre de
		 *    colonnes définis par le programmeur, dont toutes les cases ont la
		 *    même dimension. Cette dimension est calculée en fonction du
		 *    nombre de cases à placer et de la dimension du contenant.
		 */
		frame.setSize(1200, 500);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());
		//frame.setBackground(Color.BLACK);
	
		/** Définition des deux vues et ajout à la fenêtre. */
		
		vueTrain = new VueTrain(train);
		frame.add(vueTrain);
		vueCommandes = new VueCommandes(train);
		frame.add(vueCommandes);
		this.console = new JTextArea(500, 50);
		frame.add(console);
		
		this.vueTrain.repaint();
		
		/**
		 * Remarque : on peut passer à la méthode [add] des paramètres
		 * supplémentaires indiquant où placer l'élément. Par exemple, si on
		 * avait conservé la disposition par défaut [BorderLayout], on aurait
		 * pu écrire le code suivant pour placer la grille à gauche et les
		 * commandes à droite.
		 *     frame.add(grille, BorderLayout.WEST);
		 *     frame.add(commandes, BorderLayout.EAST);
		 */
	
		/**
		 * Fin de la plomberie :
		 *  - Ajustement de la taille de la fenêtre en fonction du contenu.
		 *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
		 *  - Préciser que la fenêtre doit bien apparaître à l'écran.
		 */
		//frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//frame.setBackground(Color.BLACK);
    }
	

	public class VueTrain extends JPanel implements Observer {
	    /** On maintient une référence vers le modèle. */
	    private Train train;
	    
	    // dimention d'une position en nombre de pixels 
	    private final static int largeurWagon = 220;
	    private final static int hauteurWagon = 170;
	    
	
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
			Dimension dim = new Dimension(this.largeurWagon * train.NB_WAGONS + 250,
						      this.hauteurWagon + 100);
			this.setPreferredSize(dim);
			this.setBackground(Color.WHITE);
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
			super.paintComponent(g);
			
			Train.Wagon currentWagon = train.getLocomotive();
			
			final int NB_WAGONS = train.NB_WAGONS;
			int x = 0;
			int y = 100;
			
			// affichage de la locomotive
			//paint(g, currentWagon, x + NB_WAGONS*160, y);
			paintLoco(g, currentWagon, x, y);
			
			/** Pour chaque locomotive... */
			for(int i=1; i<=NB_WAGONS; i++) {
			    //for(int j=1; j<=1; j++) {
				/**
				 * ... Appeler une fonction d'affichage auxiliaire.
				 * On lui fournit les informations de dessin [g] et les
				 * coordonnées du coin en haut à gauche.
				 */
				currentWagon = currentWagon.getSuivant();
				paintWagon(g, currentWagon, i*largeurWagon, y);
				
				
			    
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
	    private void paintWagon(Graphics g, Train.Wagon w, int x, int y) {
	    	try {
	    	      Image img = ImageIO.read(new File("wagon.jpg"));
	    	      g.drawImage(img, x, y, largeurWagon, hauteurWagon, this);
	    	      //Pour une image de fond
	    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	  	    } catch (IOException e) {
	  	      e.printStackTrace();
	  	    } 
	    	
	    	int ytemp;
	    	
	    	ytemp = 85;
	    	for (Bandit b : w.getBandits() ) {
	    		g.drawString(b.getName(), x + 15, ytemp);
	    		ytemp += 15;
	    	}
	    	
	    	ytemp = 85;
	    	
	    	/*
	    	if (w == null) {
	    		System.out.println("ERROR Wagon !");
	    	}
	    	
	    	if (w.getPossesseur() == null) {
	    		System.out.println("ERROR Possesseur !");
	    	}
	    	
	    	if (w.getPossesseur().getButins() == null) {
	    		System.out.println("ERROR butins !");
	    	}
	    	
	    	for (Butin b : w.getPossesseur().getButins()) {
	    		g.drawString(b.getNom(), x + 55, ytemp);
	    		ytemp += 10;
	    	}
	    	*/
	    	if (w.getMarshall()) {
	    		g.drawString("Marshall", x + 15, 85);
	    	}
	    }
	    
	    private void paintLoco(Graphics g, Train.Wagon w, int x, int y) {
	    	//g.drawRoundRect(x, y + 10, 140, 90, 10, 10);
	    	
	    	try {
	    	      Image img = ImageIO.read(new File("locomotive.jpg"));
	    	      g.drawImage(img, x, y, largeurWagon, hauteurWagon, this);
	    	      //Pour une image de fond
	    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    	    } catch (IOException e) {
    	      e.printStackTrace();
    	    } 
	    	
	    	 
	    }
	}

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
		    		System.out.println("avance");
		    	}
		    });

			JButton boutonDescend = new JButton("V");
			this.add(boutonDescend);
			
			boutonDescend.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("descend");
		    		
		    	}
		    });

			JButton boutonRecule = new JButton("<");
			this.add(boutonRecule);

			boutonRecule.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("recule");
		    	}
		    });
			
			JButton boutonMonte = new JButton("^");
			this.add(boutonMonte);
			
			boutonMonte.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("monte");
		    	}
		    });

			JButton boutonTire = new JButton("-");
			this.add(boutonTire);
			
			boutonTire.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("tire");
		    	}
		    });

			JButton boutonBraque = new JButton("$");
			this.add(boutonBraque);
			
			boutonBraque.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("braque");
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
	/*
	public class Console extends JTextArea {
		 
		
		public Console(int x, int y) {
			 super();
			 this.setPreferredSize(new Dimension(x, y));
		}

		public void ecrire(String text) {
			
			this.removeAll();
			this.setText(text);
			this.repaint();
		}
		
	}
	*/
	public static void main(String[] args) {
		Train t = new Train();
		Bandit b1 = new Bandit(t,"Jean");
		Bandit b2 = new Bandit(t,"Arnaud");
		Marshall m = new Marshall(t, "Marshall");
		CEVue affichage = new CEVue(t);
		System.out.println("stand by");
		affichage.console.setText("salut");
		try {
		      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	    

		affichage.console.setText("t'es un BG");
		System.out.println("ok");
		
		b2.addAction(Action.Recule);
		b2.executeAction();
		affichage.vueTrain.repaint();
		//affichage = new CEVue(t);
	}

}
