package vue;

import modele.Action;
import modele.Bandit;
import modele.Butin;
import modele.Marshall;
import modele.Personne;
import modele.Train;
import modele.Train.Wagon;
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
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

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

    Bandit banditCourant; // pour les commandes
    int numAction = 0;
    int numBandit = 0;
    int compteurActions = 0;
     
    Train train;
	JTable tableau;
	Object[][] dataTableau;
	String[] nomBandits;
	
	boolean planification = true;

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
		frame.setSize(1400, 430);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		//frame.setBackground(Color.BLACK);
	
		/** Définition des deux vues et ajout à la fenêtre. */
		this.train = train;
		//this.banditCourant = this.train.getBandits().get(0);
		//this.numAction = 0;
		//this.numBandit = 0;
		
		/*
		this.tableau = new JTable();
		dataTableau = new String[5][3];
		for (int i=0; i<5; i++) {
        	for (int j=0; j<3; j++){
        		dataTableau[i][j] = "z";
        	}
        }


		this.nomBandits = new String[train.MAX_NB_BANDITS];
		
		for (int i=0; i<train.MAX_NB_BANDITS; i++) {
			try {
				nomBandits[i] = train.getBandits().get(0).getName();
	  	    } catch (NullPointerException e) {
	  	    	//System.out.println(String.format("Bandit %d n'a pas de nom", i));
				nomBandits[i] = String.format("Bandit %d", i);
	  	    } 
			
		}
		
		*/
		
		this.nomBandits = new String[train.MAX_NB_BANDITS];
		for (int i=0; i<train.MAX_NB_BANDITS; i++) {
			try {
				nomBandits[i] = train.getBandits().get(0).getName();
	  	    } catch (NullPointerException e) {
	  	    	//System.out.println(String.format("Bandit %d n'a pas de nom", i));
				nomBandits[i] = String.format("Bandit %d", i);
	  	    } 
			
		}
		
		this.tableau = new JTable();
		dataTableau = new String[6][3];
		this.resetTableau();


		//this.nomBandits = new String[train.MAX_NB_BANDITS];
		
		
		

        this.tableau = new JTable(this.dataTableau, this.nomBandits);
     	this.tableau.setAutoResizeMode(this.tableau.AUTO_RESIZE_ALL_COLUMNS);
		
		
     	VueSac vueSac = new VueSac();
		frame.add(vueSac, BorderLayout.CENTER);
		
		vueTrain = new VueTrain();
		frame.add(vueTrain, BorderLayout.NORTH);
		
		vueCommandes = new VueCommandes();
		frame.add(vueCommandes, BorderLayout.WEST);
		this.tableau.setBorder(BorderFactory.createEtchedBorder());
		
		this.console = new JTextArea(7, 50);
		this.console.setBackground(Color.YELLOW);
		//frame.add(console);
		this.frame.add(tableau, BorderLayout.EAST);
		
        //this.frame.pack();
        //frame.add(this.tableau, BorderLayout.SOUTH);
        
		//JPanel table = new JPanel();
		//table.add(tableau.getTableHeader(), BorderLayout.NORTH);
		//table.add(tableau, BorderLayout.CENTER);
		//table.setPreferredSize(new Dimension(500, 500));
		//this.frame.add(table);
		//this.frame.add(comp, index)
        //this.frame.add(tableau.getTableHeader(), 2);
        //this.frame.add(tableau);
        //frame.add(tableau, );
     	
        		
		this.vueTrain.repaint();
		this.banditCourant = train.getBandits().get(0);

        //this.frame.pack();
		
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
	
    void resetTableau() {
    	for (int i=1; i<6; i++) {
        	for (int j=0; j<3; j++){
        		dataTableau[i][j] = ".";
        	}
        }
    	
    	for (int i=0; i<train.MAX_NB_BANDITS; i++) {
			try {
				dataTableau[0][i] = train.getBandits().get(i).getName();
				
	  	    } catch (NullPointerException e) {
	  	    	//System.out.println(String.format("Bandit %d n'a pas de nom", i));
	  	    	dataTableau[0][i] = String.format("Bandit %d", i);
	  	    } 
			
		}
    }
    
    // TODO
    void maj(){
        this.console.repaint();
        this.vueTrain.repaint();
        this.vueCommandes.repaint();
        this.frame.repaint();
    }
    
    public class VueSac extends JPanel implements Observer {

    	public VueSac() {
			//this.train = train;
			/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
			train.addObserver(this);
			/**
			 * Définition et application d'une taille fixe pour cette zone de
			 * l'interface, calculée en fonction du nombre de cellules et de la
			 * taille d'affichage.
			 */
			Dimension dim = new Dimension(train.MAX_NB_BANDITS * 50 + train.getMAX_N_BUTIN() * 30, 100);
			this.setPreferredSize(dim);
			this.setBackground(Color.LIGHT_GRAY);
	    }
    	
		@Override
		public void update() {
			repaint();
			
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			dessinerBandits(0, 0, g);
		}
		
		private void dessinerBandits(int x, int y, Graphics g) {
	    	//int ytemp = 85;
	    	
	    	/*
	    	if (w == null) {
	    		//System.out.println("ERROR WAGON");
	    	}
	    	*/
	    	
	    	for (Bandit b : train.getBandits()) {
	    		
	    		int id = train.getBandits().indexOf(b);
	    		String nomImage = String.format("bandit%d.jpg", id + 1);
	    		////System.out.println(id);
	    		
	    		try {
	    			////System.out.println(nomImage);
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      
		    	      g.drawImage(img, x + 300*id , y, 60, 110, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		//g.drawString(b.getName(), x + 300*id , y + 55);
	    		//ytemp += 15;
	    		dessinerButins(x + 65 + 300 * id, y + 30, b, g);
	    		
	    		int total = 0;
	    		for (Butin but : b.getButins()) {
	    			total += but.getValeur();
	    		}
	    		
	    		int fontSize = 20;
	    	    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
	    	    g.setColor(Color.RED);
	    		g.drawString(String.format(" %d $", total), x + 60 + 300*id, y + 20);
	    		
	    	}
	    }
	    
	    private void dessinerButins(int x, int y, Bandit bandit, Graphics g) {
	    	int decalage = 0;
	    	for (Butin b : bandit.getButins() ) {
	    		
	    		int id = b.getValeur() / 130;
	    		if (b.getValeur() == 500) {
	    			id = 4;
	    		}
	    		if (b.getValeur() == 1000) {
	    			id = 5;
	    		}
	    		
	    		String nomImage = String.format("butin%d.jpg", id);
	    		
	    		try {
	    			////System.out.println(nomImage);
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      
		    	      g.drawImage(img, x + 25*decalage , y , 20, 25, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		//g.drawString(b.getName(), x + 15, ytemp);
	    		decalage += 1;
	    		
	    	}
	    }
    	
    }

	public class VueTrain extends JPanel implements Observer {
	    /** On maintient une référence vers le modèle. */
	    //Train train;
	    
	    // dimention d'une position en nombre de pixels 
	    private final static int largeurWagon = 220;
	    private final static int hauteurWagon = 200;
	    
	
	    /** Constructeur. */
	    public VueTrain() {
			//this.train = train;
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
			
			final int NB_WAGONS = train.NB_WAGONS_MAX; 
			int x = 0;
			int y = 100;
			int i = 0;
			
			// affichage de la locomotive
			//paint(g, currentWagon, x + NB_WAGONS*160, y);
			paintLoco(g, x, y, currentWagon);
			x = (int) (largeurWagon * 0.5);
			currentWagon = currentWagon.getSuivant();
			
			/** Pour chaque locomotive... */
			while (currentWagon != null) {
				i += 1;
			    //for(int j=1; j<=1; j++) {
				/**
				 * ... Appeler une fonction d'affichage auxiliaire.
				 * On lui fournit les informations de dessin [g] et les
				 * coordonnées du coin en haut à gauche.
				 */
				
				paintWagon(g, currentWagon, x + i*largeurWagon, y);
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
	    	
	    	//int ytemp;
	    	
	    	dessinerBandits(x, y, w, g);
	    	dessinerButins(x, y, w, g);
	    	dessinerMarshall(x, y, w, g);
	    	
	    	//ytemp = 85;
	    	
	    	
	    	
	    	//ytemp = 85;
	    	
	    	/* 
	    	if (w == null) {
	    		//System.out.println("ERROR Wagon !");
	    	}
	    	
	    	if (w.getPossesseur() == null) {
	    		//System.out.println("ERROR Possesseur !");
	    	}
	    	
	    	if (w.getPossesseur().getButins() == null) {
	    		//System.out.println("ERROR butins !");
	    	}
	    	
	    	for (Butin b : w.getPossesseur().getButins()) {
	    		g.drawString(b.getNom(), x + 55, ytemp);
	    		ytemp += 10;
	    	}
	    	*/
	    	
	    }
	    
	    private void paintLoco(Graphics g, int x, int y, Wagon loco) {
	    	//g.drawRoundRect(x, y + 10, 140, 90, 10, 10);
	    	
	    	try {
	    	      Image img = ImageIO.read(new File("locomotiveModif.jpg"));
	    	      g.drawImage(img, x, y + (int) (hauteurWagon * 0.02), (int) (largeurWagon * 1.5), (int) (hauteurWagon * 0.98), this);
	    	      //Pour une image de fond
	    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    	    } catch (IOException e) {
    	      e.printStackTrace();
    	    } 
	    	x += (int) (largeurWagon * 0.5);
	    	dessinerBandits(x, y, loco, g);
	    	dessinerButins(x, y, loco, g);
	    	dessinerMarshall(x, y, loco, g);
	    }
	    
	    private void dessinerBandits(int x, int y, Wagon w, Graphics g) {
	    	//int ytemp = 85;
	    	
	    	/*
	    	if (w == null) {
	    		//System.out.println("ERROR WAGON");
	    	}
	    	*/
	    	
	    	for (Bandit b : w.getBandits() ) {
	    		
	    		int id = train.getBandits().indexOf(b);
	    		String nomImage = String.format("bandit%d.jpg", id + 1);
	    		////System.out.println(id);
	    		
	    		int etage = 0;
	    		if (!b.getInterieur()) { 
	    			etage = -128;
	    		}
	    		try {
	    			////System.out.println(nomImage);
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      
		    	      g.drawImage(img, x + 25 + 40*id , y + 64 + etage, 40, 68, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		g.drawString(b.getName(), x + 38 + 40*id , y + 55 + etage);
	    		//ytemp += 15;
	    		
	    	}
	    }
	    
	    private void dessinerButins(int x, int y, Wagon w, Graphics g) {
	    	int decalage = 0;
	    	for (Butin b : w.getButins() ) {
	    		
	    		int id = b.getValeur() / 130;
	    		if (b.getValeur() == 500) {
	    			id = 4;
	    		}
	    		if (b.getValeur() == 1000) {
	    			id = 5;
	    		}
	    		
	    		String nomImage = String.format("butin%d.jpg", id);
	    		//System.out.println("valeur ; " + b.getValeur());
	    		//System.out.println("id ; " + id);
	    		//System.out.println(nomImage);
	    		
	    		try {
	    			////System.out.println(nomImage);
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      
		    	      g.drawImage(img, x + 25 + 40*decalage , y + 10 , 20, 25, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		//g.drawString(b.getName(), x + 15, ytemp);
	    		decalage += 1;
	    		
	    	}
	    }
	    private void dessinerMarshall(int x, int y, Wagon w, Graphics g) {
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
	}

	public class VueCommandes extends JPanel {
	    /**
	     * Pour que le bouton puisse transmettre ses ordres, on garde une
	     * référence au modèle.
	     */
	    //private Train train;
		HashSet<JButton> boutonsPlannification;
		JButton boutonAction;

	    
	    /** Constructeur. */
	    public VueCommandes() {
			//this.train = train;
			//this.banditCourant = new Bandit(train, "test"); // a modifier
			
			/**
			 * On crée un nouveau bouton, de classe [JButton], en précisant le
			 * texte qui doit l'étiqueter.
			 * Puis on ajoute ce bouton au panneau [this]. 
			 */
	    	boutonsPlannification = new HashSet<JButton>();
	    	
			Dimension dim = new Dimension(300, 100);
			this.setPreferredSize(dim);
			this.setBackground(Color.BLACK);
			
			JButton boutonAvance = new JButton("RIGHT");
			this.add(boutonAvance);
			this.boutonsPlannification.add(boutonAvance);
			
			// classe interne anonyme.
			boutonAvance.addActionListener(new Avance(train));

			JButton boutonDescend = new JButton("DOWN");
			this.add(boutonDescend);
			this.boutonsPlannification.add(boutonDescend);
			
			boutonDescend.addActionListener(new Descend(train));

			JButton boutonRecule = new JButton("LEFT");
			this.add(boutonRecule);
			this.boutonsPlannification.add(boutonRecule);

			boutonRecule.addActionListener(new Recule(train));

			boutonAction = new JButton("ACTION");
			this.add(boutonAction);

			boutonAction.addActionListener(new Act(train));
			
			JButton boutonMonte = new JButton("UP");
			this.add(boutonMonte);
			this.boutonsPlannification.add(boutonMonte);
			
			boutonMonte.addActionListener(new Monte(train));

			JButton boutonTire = new JButton("PAN!");
			this.add(boutonTire);
			this.boutonsPlannification.add(boutonTire);
			
			boutonTire.addActionListener(new Tire(train));
			
			JButton boutonBraque = new JButton("$$$"); 
			this.add(boutonBraque);
			this.boutonsPlannification.add(boutonBraque);
			
			boutonBraque.addActionListener(new Braque(train));
			
			JButton boutondort = new JButton("Zzz");
			this.add(boutondort);
			this.boutonsPlannification.add(boutondort);
			
			boutondort.addActionListener(new Dort(train));
			
			majBoutons();
			//this.add(tableau.getTableHeader(), BorderLayout.NORTH);
	        //this.add(tableau, BorderLayout.CENTER);
		
	    }
	    
	    public void majBoutons() {
	    	if(planification){
				boutonAction.setEnabled(false);
				for (JButton b : boutonsPlannification) {
					b.setEnabled(true);
				}
				
			} 
			else {
				boutonAction.setEnabled(true);
				for (JButton b : boutonsPlannification) {
					b.setEnabled(false);
				}
			}
				
	    }
	    
	    abstract class Bouton implements ActionListener {
		    Train train;
		    VueCommandes vc;
		    
		    //int indiceBandit;
		     
		    Bouton(Train train){
		    	this.train = train;
				//this.indiceBandit = idBandit;
				//this.banditCourant = train.getBandits().get(this.indiceBandit);
		    }
		
		    private int indiceBandit() {
		    	return train.getBandits().indexOf(banditCourant);
		    }

		    void banditSuivant() {
		    	//this.indiceBandit = (this.indiceBandit + 1) % train.MAX_NB_BANDITS;
		    	numBandit = (numBandit + 1) % train.MAX_NB_BANDITS;
		    	banditCourant = train.getBandits().get(numBandit);
		    }
		    
		    void actionSuivante() {
		    	
		    	//System.out.println("indice bandit avant : " + numBandit);
		    	//System.out.println("indice action avant : " + numAction);
		    	
		    	if (numAction < train.MAX_N_ACTION - 1) {
		    		
		    		numAction ++;
		    	}
		    	else {
		    		
		    		numAction = 0;
		    		numBandit ++;
		    		//banditSuivant();
		    		if (numBandit == train.MAX_NB_BANDITS) {
		    			
		    			planification = false;
		    			majBoutons();
		    		}
		    		else {
		    			banditCourant = train.getBandits().get(numBandit);
		    		}
			    	
		    		
		    	}
		    }
		    
		    
		    abstract public void actionPerformed(ActionEvent e);
		}
		
		class Braque extends Bouton {
		
		    public Braque(Train train){
		    	super(train);

		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Braquer);
		    	console.setText("braquage !");
		    	dataTableau[numAction + 1][numBandit] = "$";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Monte extends Bouton {
			
		    public Monte(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Monter);
		    	dataTableau[numAction + 1][numBandit] = "A";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Descend extends Bouton {
			
		    public Descend(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Descendre);
		    	dataTableau[numAction + 1][numBandit] = "v";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Avance extends Bouton {
			
		    public Avance(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Avance);
		    	dataTableau[numAction + 1][numBandit] = ">";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Recule extends Bouton {
			
		    public Recule(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Recule);
		    	dataTableau[numAction + 1][numBandit] = "<";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Tire extends Bouton {
			
		    public Tire(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	banditCourant.addAction(Action.Tirer);
		    	dataTableau[numAction + 1][numBandit] = "-";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Act extends Bouton {

			public Act(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	//banditCourant.executeAction();
		    	assert (train.getMarshall() != null) : "chiotte";
		    	this.train.excuteTour();
		    	vueTrain.update();
		    	numAction ++;
		    	
		    	
		    	
		    	
		    	////System.out.println("indice nouvelle action " + numAction);
		    	if (numAction == train.MAX_N_ACTION) {
		    		planification = true;
		    		resetTableau();
		    		//System.out.println("tableau reinitialisé");
		    		numAction = 0;
		    		numBandit = 0;
		    		banditCourant = train.getBandits().get(numBandit);
		    	}
		    	else {
		    		for (int i=0; i<3; i++) {
		    			//System.out.println("action effacée" + numAction);
			    		dataTableau[numAction][i] = " ";
			    	}
		    	}
		    	maj();
		    	majBoutons();
		    }
	    }
		
		class Dort extends Bouton {
			
		    public Dort(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	dataTableau[numAction + 1][numBandit] = "Z";
		    	tableau.repaint();
		    	this.actionSuivante();
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
		//System.out.println(t.MAX_NB_BANDITS);
		CEVue affichage = new CEVue(t);
		
		
	}

}
