package vue;

import modele.Action;
import modele.Bandit;
import modele.Marshall;
import modele.Personne;
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

    private static Bandit banditCourant; // pour les commandes
     

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
		this.console = new JTextArea(7, 50);
		this.console.setBackground(Color.YELLOW);
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
	    private final static int hauteurWagon = 200;
	    
	
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
			Dimension dim = new Dimension(this.largeurWagon * train.NB_WAGONS_MAX,
						      this.hauteurWagon + 100);
			this.setPreferredSize(dim);
			this.setBackground(Color.cyan);
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
			
			final int NB_WAGONS = train.NB_WAGONS_MAX; 
			int x = 0;
			int y = 100;
			int i = 0;
			
			// affichage de la locomotive
			//paint(g, currentWagon, x + NB_WAGONS*160, y);
			paintLoco(g, x, y);
			
			/** Pour chaque locomotive... */
			while (currentWagon != null) {
				i += 1;
			    //for(int j=1; j<=1; j++) {
				/**
				 * ... Appeler une fonction d'affichage auxiliaire.
				 * On lui fournit les informations de dessin [g] et les
				 * coordonnées du coin en haut à gauche.
				 */
				
				paintWagon(g, currentWagon, i*largeurWagon, y);
				currentWagon = currentWagon.getSuivant();
				
				
				
			    
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
	    	
	    	if (w == null) {
	    		System.out.println("ERROR WAGON");
	    	}
	    	
	    	for (Bandit b : w.getBandits() ) {
	    		
	    		int id = train.getBandit().indexOf(b);
	    		String nomImage = String.format("bandit%d.jpg", id + 1);
	    		System.out.println(id);
	    		
	    		try {
	    			System.out.println(nomImage);
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      g.drawImage(img, x + 25 + 70*id , y + 64, 40, 68, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		//g.drawString(b.getName(), x + 15, ytemp);
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
	    		try {
		    	      Image img = ImageIO.read(new File("marshall.jpg"));
		    	      g.drawImage(img, x +150, y + 64, 40, 68, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    	    } catch (IOException e) {
	    	      e.printStackTrace();
	    	    } 
	    	}
	    }
	    
	    private void paintLoco(Graphics g, int x, int y) {
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
			//this.banditCourant = new Bandit(train, "test"); // a modifier
			
			/**
			 * On crée un nouveau bouton, de classe [JButton], en précisant le
			 * texte qui doit l'étiqueter.
			 * Puis on ajoute ce bouton au panneau [this].
			 */
			
			Dimension dim = new Dimension(300, 100);
			this.setPreferredSize(dim);
			this.setBackground(Color.BLACK);
			
			JButton boutonAvance = new JButton("RIGHT");
			this.add(boutonAvance);
			
			// classe interne anonyme.
			boutonAvance.addActionListener(new Braque(train));

			JButton boutonDescend = new JButton("DOWN");
			this.add(boutonDescend);
			
			boutonDescend.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("descend");
		    		
		    	}
		    });

			JButton boutonRecule = new JButton("LEFT");
			this.add(boutonRecule);

			boutonRecule.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("recule");
		    	}
		    });
			
			

			JButton boutonAction = new JButton("ACTION");
			this.add(boutonAction);

			boutonAction.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("Action !");
		    	}
		    });
			
			if(true){
				boutonAction.setEnabled(true);
			} 
			else {
				boutonAction.setEnabled(false);
			}
			
			JButton boutonMonte = new JButton("UP");
			this.add(boutonMonte);
			
			boutonMonte.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("monte");
		    	}
		    });

			JButton boutonTire = new JButton("PAN!");
			this.add(boutonTire);
			
			boutonTire.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("tire");
		    	}
		    });

			JButton boutonBraque = new JButton("$$$");
			this.add(boutonBraque);
			
			boutonBraque.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("braque");
		    	}
		    });
			
			JButton boutondort = new JButton("Zzz");
			this.add(boutondort);
			
			boutondort.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    	    // TODO
		    		//modele.avance();
		    		System.out.println("braque");
		    	}
		    });
			

		
	    }
	    
	    abstract class Bouton implements ActionListener {
		    Train train;
		    //int indiceBandit;
		     
		    Bouton(Train train){
		    	this.train = train;
				//this.indiceBandit = idBandit;
				//this.banditCourant = train.getBandit().get(this.indiceBandit);
		    }
		
		    private int indiceBandit() {
		    	return train.getBandit().indexOf(banditCourant);
		    }

		    private void banditSuivant() {
		    	//this.indiceBandit = (this.indiceBandit + 1) % train.MAX_NB_BANDITS;
		    	int idSuivant = (this.indiceBandit() + 1) % train.MAX_NB_BANDITS;
		    	banditCourant = train.getBandit().get(idSuivant);
		    }
		    
		    abstract public void actionPerformed(ActionEvent e);
		}
		
		class Braque extends Bouton {
		
		    public Braque(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Braquer);
		    }
	    }
		
		class Monte extends Bouton {
			
		    public Monte(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	CEVue.banditCourant.addAction(Action.Monter);
		    }
	    }
		
		class Descend extends Bouton {
			
		    public Descend(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	CEVue.banditCourant.addAction(Action.Descendre);
		    }
	    }
		
		class Avance extends Bouton {
			
		    public Avance(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	CEVue.banditCourant.addAction(Action.Avance);
		    }
	    }
		
		class Recule extends Bouton {
			
		    public Recule(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	CEVue.banditCourant.addAction(Action.Recule);
		    }
	    }
		
		class Tire extends Bouton {
			
		    public Tire(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	CEVue.banditCourant.addAction(Action.Tirer);
		    }
	    }
		
		class Act extends Bouton {

			public Act(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	this.train.excuteTour();
		    }
	    }
		
		class Dort extends Bouton {
			
		    public Dort(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    }
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
		CEVue affichage = new CEVue(t);
		System.out.println("stand by");
		affichage.console.setText("salut");
		Marshall m = t.getMarshall();
		m.addAction(Action.Avance);
		m.executeAction();
		try {
		      Thread.sleep(3000);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	    
		m.addAction(Action.Avance);
		m.executeAction();
		affichage.console.setText("t'es un BG");
		System.out.println("ok");
		
		//b2.addAction(Action.Recule);
		//b2.executeAction();
		affichage.vueTrain.repaint();
		//affichage = new CEVue(t);
	}

}
