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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
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
    private VueSac vueSac;
    private JTextArea console;
    //private Clavier clavier;

    Bandit banditCourant; // pour les commandes
    int numAction = 0;
    int numBandit = 0;
    int compteurActions = 0;
     
    Train train;
	JTable tableau;
	Object[][] dataTableau;
	String[] nomBandits;
	
	boolean couleur;
	boolean planification = true;
	boolean Arbash = false;
	boolean Gardille = false;

    /** Construction d'une vue attachée au modèle, contenu dans la classe Train. */
    public CEVue(Train train) {
		/** Définition de la fenêtre principale. */
    	
		frame = new JFrame();
		frame.setTitle("Colt Express");

		frame.setSize(1400, 430);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		//frame.setBackground(Color.BLACK);
	
		/** Définition des deux vues et ajout à la fenêtre. */
		this.train = train;
		this.couleur = true;
		//this.banditCourant = this.train.getBandits().get(0);
		//this.numAction = 0;
		//this.numBandit = 0;
		
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


		this.banditCourant = train.getBandits().get(0);

        this.tableau = new JTable(this.dataTableau, this.nomBandits);
     	this.tableau.setAutoResizeMode(this.tableau.AUTO_RESIZE_ALL_COLUMNS);
		
		
     	vueSac = new VueSac();
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

		this.vueTrain.repaint();
	
		/**
		 * Fin de la plomberie :
		 *  - Ajustement de la taille de la fenêtre en fonction du contenu.
		 *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
		 *  - Préciser que la fenêtre doit bien apparaître à l'écran.
		 */
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
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
	  	    	System.out.println(String.format("Bandit %d n'a pas de nom", i));
	  	    	dataTableau[0][i] = String.format("Bandit %d", i);
	  	    } 
			
		}
    }
    
    void maj(){
        this.console.repaint();
        this.vueTrain.repaint();
        this.vueCommandes.repaint();
        this.frame.repaint();
    }
    
    public class VueSac extends JPanel implements Observer {

    	public VueSac() {

			/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
			train.addObserver(this);

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
	    	
	    	for (Bandit b : train.getBandits()) {
	    		
	    		int id = train.getBandits().indexOf(b);
	    		String nomImage = String.format("bandit%dbis.png", id + 1);
	    		
	    		try {
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      g.drawImage(img, x + 300*id , y, 60, 110, this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		dessinerButins(x + 65 + 300 * id, y + 30, b, g);
	    		dessinerBalles(x + 65 + 300 * id, y + 70, b, g);
	    		
	    		int total = 0;
	    		for (Butin but : b.getButins()) {
	    			total += but.getValeur();
	    		}
	    		
	    		int fontSize = 20;
	    	    g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
	    	    g.setColor(Color.RED);
	    		g.drawString(String.format(" %d $", total), x + 60 + 300*id, y + 20);
	    		
	    	}
	    	
	    	// easter eggs
	    	if (Arbash) {
	    		try {
		    		String nom = "JeanArbash.jpg";
		    		
		    	      Image img = ImageIO.read(new File(nom));
		    	      g.drawImage(img, x + 300 , y, 60, 110, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    	    } catch (IOException e) {
	    	      e.printStackTrace();
	    	    } 
	    	}
	    	
	    	// easter eggs
	    	if (Gardille) {
	    		try {
		    		String nom = "ArnaudGardille.jpg";
		    		
		    	      Image img = ImageIO.read(new File(nom));
		    	      g.drawImage(img, x, y, 60, 110, this);
		    	      //Pour une image de fond
		    	      //g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    	    } catch (IOException e) {
	    	      e.printStackTrace();
	    	    } 
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
	    		
	    		String nomImage = String.format("butin%dbis.png", id);
	    		
	    		try {
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      g.drawImage(img, x + 25*decalage , y , 20, 25, this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 

	    		decalage += 1;
	    		
	    	}
	    }
	    
	    private void dessinerBalles(int x, int y, Bandit bandit, Graphics g) {
	    	int decalage = 0;
	    	for (int i=0; i<bandit.getBullets(); i++) {
	    		
	    		
	    		String nomImage = String.format("bullet.png");
	    		
	    		try {
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      g.drawImage(img, x + 25*decalage , y , 20, 25, this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 

	    		decalage += 1;
	    		
	    	}
	    }
    	
    }

	public class VueTrain extends JPanel implements Observer {
	    
	    // dimention d'un wagon en nombre de pixels 
	    private final static int largeurWagon = 220;
	    private final static int hauteurWagon = 200;
	    
	
	    /** Constructeur. */
	    public VueTrain() {
			//this.train = train;
			/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
			train.addObserver(this);

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
			
			Image img;
			try {
				img = ImageIO.read(new File("ciel.png"));
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			Train.Wagon currentWagon = train.getLocomotive();
			
			final int NB_WAGONS = train.NB_WAGONS_MAX; 
			int x = 0;
			int y = 100;
			int i = 0;
			
			// affichage de la locomotive
			paintLoco(g, x, y, currentWagon);
			x = (int) (largeurWagon * 0.5);
			currentWagon = currentWagon.getSuivant();
			
			/** Pour chaque wagon... */
			while (currentWagon != null) {
				i += 1;

				/**
				 * ... Appel d'une fonction d'affichage auxiliaire.
				 * On lui fournit les informations de dessin [g] et les
				 * coordonnées du coin en haut à gauche.
				 */
				
				paintWagon(g, currentWagon, x + i*largeurWagon, y);
				currentWagon = currentWagon.getSuivant();
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
	    		String nom;
	    		if (couleur) {
	    			nom = "wagonCouleur.png";
	    		} else {
	    			nom = "wagon.jpg";
	    		}
	    		
	    		Image img = ImageIO.read(new File(nom));
	    	    g.drawImage(img, x, y, largeurWagon, hauteurWagon, this);

	  	    } catch (IOException e) {
	  	      e.printStackTrace();
	  	    } 
	    	
	    	dessinerBandits(x, y, w, g);
	    	dessinerButins(x, y, w, g);
	    	dessinerMarshall(x, y, w, g);

	    }
	    
	    private void paintLoco(Graphics g, int x, int y, Wagon loco) {
  	
	    	try {
	    		String nom;
	    		if (couleur) {
	    			nom = "locomotiveModifCouleur.png";
	    		} else {
	    			nom = "locomotiveModif.jpg";
	    		}
	    	      Image img = ImageIO.read(new File(nom));
	    	      g.drawImage(img, x, y + (int) (hauteurWagon * 0.02), 
	    	    		  (int) (largeurWagon * 1.5), (int) (hauteurWagon * 0.98), this);
    	    } catch (IOException e) {
    	      e.printStackTrace();
    	    } 
	    	
	    	x += (int) (largeurWagon * 0.5);
	    	dessinerBandits(x, y, loco, g);
	    	dessinerButins(x, y, loco, g);
	    	dessinerMarshall(x, y, loco, g);
	    }
	    
	    private void dessinerBandits(int x, int y, Wagon w, Graphics g) {

	    	for (Bandit b : w.getBandits() ) {
	    		
	    		int id = train.getBandits().indexOf(b);
	    		String nomImage = String.format("bandit%dbis.png", id + 1);
	    		
	    		int etage = 0;
	    		if (!b.getInterieur()) { 
	    			etage = -128;
	    		}
	    		try {
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      
		    	      g.drawImage(img, x + 25 + 40*id , y + 64 + etage, 40, 68, this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 
	    		
	    		g.drawString(b.getName(), x + 38 + 40*id , y + 55 + etage);
	    		
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
	    		
	    		String nomImage = String.format("butin%dbis.png", id);
	    		
	    		try {
		    	      Image img = ImageIO.read(new File(nomImage));
		    	      
		    	      g.drawImage(img, x + 25 + 40*decalage , y + 10 , 20, 25, this);
		  	    } catch (IOException e) {
		  	      e.printStackTrace();
		  	    } 

	    		decalage += 1;
	    	}
	    }
	    private void dessinerMarshall(int x, int y, Wagon w, Graphics g) {
	    	if (w.getMarshall()) {
	    		try {
		    	      Image img = ImageIO.read(new File("marshallbis.png"));
		    	      g.drawImage(img, x +150, y + 64, 40, 68, this);
	    	    } catch (IOException e) {
	    	      e.printStackTrace();
	    	    } 
	    	}
	    }
	}

	public class VueCommandes extends JPanel implements KeyListener {

		HashSet<JButton> boutonsPlannification;
		JButton boutonAction;
		JButton boutonTire;
		JButton boutonMonte;
		JButton boutonDescend;
		JButton boutonAvance;
		JButton boutonRecule;
		JButton boutonDors;
		JButton boutonBraque;
		
		SendAction Ac;
		SendAction T;
		SendAction M;
		SendAction De;
		SendAction Av;
		SendAction R;
		SendAction Do;
		SendAction B;
		
		
		

	    
	    /** Constructeur. */
	    public VueCommandes() {
			/**
			 * On crée un nouveau bouton, de classe [JButton], en précisant le
			 * texte qui doit l'étiqueter.
			 * Puis on ajoute ce bouton au panneau [this]. 
			 */
	    	setFocusable(true);
	    	
	    	boutonsPlannification = new HashSet<JButton>();
	    	
			Dimension dim = new Dimension(300, 100);
			this.setPreferredSize(dim);
			this.setBackground(Color.BLACK);
			
			boutonAvance = new JButton("RIGHT");
			this.add(boutonAvance);
			this.boutonsPlannification.add(boutonAvance);
			
			// classe interne anonyme.
			Av = new SendAction(train) {
				void execution() {
					banditCourant.addAction(Action.Avance);
			    	dataTableau[numAction + 1][numBandit] = "Right";
			    	tableau.repaint();
			    	this.actionSuivante();
				};
			};
			boutonAvance.addActionListener(Av);

			boutonDescend = new JButton("DOWN");
			this.add(boutonDescend);
			this.boutonsPlannification.add(boutonDescend);
			De = new SendAction(train) {
				void execution() {
					banditCourant.addAction(Action.Descendre);
			    	dataTableau[numAction + 1][numBandit] = "Down";
			    	tableau.repaint();
			    	this.actionSuivante();
				};
			};
			boutonDescend.addActionListener(De);

			boutonRecule = new JButton("LEFT");
			this.add(boutonRecule);
			this.boutonsPlannification.add(boutonRecule);

			R = new SendAction(train) {
				void execution() {
					banditCourant.addAction(Action.Recule);
			    	dataTableau[numAction + 1][numBandit] = "Left";
			    	tableau.repaint();
			    	this.actionSuivante();
				};
			};
			boutonRecule.addActionListener(R);

			boutonAction = new JButton("ACTION");
			this.add(boutonAction);

			Ac = new SendAction(train) {
				void execution() {
					assert (train.getMarshall() != null);
			    	this.train.excuteTour();
			    	vueTrain.update();
			    	numAction ++;
			    	
			    	if (numAction == train.MAX_N_ACTION) {
			    		planification = true;
			    		resetTableau();

			    		numAction = 0;
			    		numBandit = 0;
			    		banditCourant = train.getBandits().get(numBandit);
			    	}
			    	else {
			    		for (int i=0; i<3; i++) {
				    		dataTableau[numAction][i] = " ";
				    	}
			    	}
			    	maj();
			    	majBoutons();
				};
			};
			boutonAction.addActionListener(Ac);
			
			boutonMonte = new JButton("UP");
			this.add(boutonMonte);
			this.boutonsPlannification.add(boutonMonte);
			
			M = new SendAction(train) {
				void execution() {
					banditCourant.addAction(Action.Monter);
			    	dataTableau[numAction + 1][numBandit] = "Up";
			    	tableau.repaint();
			    	this.actionSuivante();
				};
			};
			boutonMonte.addActionListener(M);

			this.boutonTire = new JButton("PAN!");
			this.add(boutonTire);
			this.boutonsPlannification.add(boutonTire);
			
			T = new SendAction(train) {
				void execution() {
					banditCourant.addAction(Action.Tirer);
			    	dataTableau[numAction + 1][numBandit] = "Shoot";
			    	tableau.repaint();
			    	this.actionSuivante();
			    	System.out.println("nb balles : " + banditCourant.getBullets());;
			    	majBoutons();
				};
			};
			boutonTire.addActionListener(T);
			
			boutonBraque = new JButton("$$$"); 
			this.add(boutonBraque);
			this.boutonsPlannification.add(boutonBraque);
			
			B = new SendAction(train) {
				void execution() {
			    	banditCourant.addAction(Action.Braquer);
			    	console.setText("braquage !");
			    	dataTableau[numAction + 1][numBandit] = "Rob";
			    	tableau.repaint();
			    	this.actionSuivante();
				};
			};
			boutonBraque.addActionListener(B);
			
			boutonDors = new JButton("Zzz");
			this.add(boutonDors);
			this.boutonsPlannification.add(boutonDors);
			
			Do = new SendAction(train) {
					void execution() {
						dataTableau[numAction + 1][numBandit] = "Do nothing";
				    	tableau.repaint();
				    	this.actionSuivante();
				};
			};
			boutonDors.addActionListener(Do);
			
			majBoutons();
		
			this.addKeyListener(this);
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
	    	
	    	
	    	if (banditCourant.getBullets() <= 0) {
	    		System.out.println("plus de balles !");
	    		boutonTire.setEnabled(false);;
	    	}
	    }
	    

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_T){
				if (vueCommandes.boutonTire.isEnabled())
					vueCommandes.T.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				if (vueCommandes.boutonDescend.isEnabled())
					vueCommandes.De.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				if (vueCommandes.boutonRecule.isEnabled())
					vueCommandes.R.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_UP){
				if (vueCommandes.boutonMonte.isEnabled())
					vueCommandes.M.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == 39){
				if (vueCommandes.boutonAvance.isEnabled())
					vueCommandes.Av.execution();
				System.out.print("avance connard !");
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_B){
				if (vueCommandes.boutonBraque.isEnabled())
					vueCommandes.B.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_N){
				if (vueCommandes.boutonDors.isEnabled())
					vueCommandes.Do.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if (vueCommandes.boutonAction.isEnabled())
					vueCommandes.Ac.execution();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				Arbash = !Arbash;
				vueSac.repaint();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_G){
				Gardille = !Gardille;
				vueSac.repaint();
	        }
			
			
			repaint();
		}
 
		@Override
		public void keyReleased(KeyEvent e) {
			
		}
	    
	    abstract class SendAction implements ActionListener {
		    Train train;
		    VueCommandes vc;
		     
		    SendAction(Train train){
		    	this.train = train;
		    }
		
		    private int indiceBandit() {
		    	return train.getBandits().indexOf(banditCourant);
		    }

		    void banditSuivant() {
		    	numBandit = (numBandit + 1) % train.MAX_NB_BANDITS;
		    	banditCourant = train.getBandits().get(numBandit);
		    }
		    
		    public void actionSuivante() {
		    	
		    	if (numAction < train.MAX_N_ACTION - 1) {
		    		
		    		numAction ++;
		    	}
		    	else {
		    		
		    		numAction = 0;
		    		numBandit ++;

		    		if (numBandit == train.MAX_NB_BANDITS) {
		    			
		    			planification = false;
		    			majBoutons();
		    		}
		    		else {
		    			banditCourant = train.getBandits().get(numBandit);
		    		}
			    	
		    		
		    	}
		    }
		    
		    
		    public void actionPerformed(ActionEvent e) {
		    	execution();
		    };
		    
		    abstract void execution();
		}
	}
	    /*
		class Braque extends Bouton {
		
		    public Braque(Train train){
		    	super(train);

		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	braque();
		    }
		    
		    private void braque() {
		    	banditCourant.addAction(Action.Braquer);
		    	console.setText("braquage !");
		    	dataTableau[numAction + 1][numBandit] = "Rob";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Monte extends Bouton {
			
		    public Monte(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	monte();
		    }
		    
		    private void monte() {
		    	banditCourant.addAction(Action.Monter);
		    	dataTableau[numAction + 1][numBandit] = "Up";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Descend extends Bouton {
			
		    public Descend(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	descend();
		    }
		    
		    private void descend() {
		    	banditCourant.addAction(Action.Descendre);
		    	dataTableau[numAction + 1][numBandit] = "Down";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Avance extends Bouton {
			
		    public Avance(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	avance();
		    }
		    
		    private void avance() {
		    	banditCourant.addAction(Action.Avance);
		    	dataTableau[numAction + 1][numBandit] = "Right";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Recule extends Bouton {
			
		    public Recule(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	recule();
		    }
		    
		    private void recule() {
		    	banditCourant.addAction(Action.Recule);
		    	dataTableau[numAction + 1][numBandit] = "Left";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }
		
		class Tire extends Bouton {
			
		    public Tire(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	tirer();
		    }
		    
		    private void tirer() {
		    	banditCourant.addAction(Action.Tirer);
		    	dataTableau[numAction + 1][numBandit] = "Shoot";
		    	tableau.repaint();
		    	this.actionSuivante();
		    	System.out.println("nb balles : " + banditCourant.getBullets());;
		    	majBoutons();
		    }
	    }
		
		class Act extends Bouton {

			public Act(Train train){
		    	super(train);
		    	
		    }
		
		    public void actionPerformed(ActionEvent e) {
		    	act();
		    }
		    
		    private void act() {

		    	assert (train.getMarshall() != null);
		    	this.train.excuteTour();
		    	vueTrain.update();
		    	numAction ++;
		    	
		    	if (numAction == train.MAX_N_ACTION) {
		    		planification = true;
		    		resetTableau();

		    		numAction = 0;
		    		numBandit = 0;
		    		banditCourant = train.getBandits().get(numBandit);
		    	}
		    	else {
		    		for (int i=0; i<3; i++) {
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
		    	dort();
		    }
		    
		    private void dort() {
		    	dataTableau[numAction + 1][numBandit] = "Do nothing";
		    	tableau.repaint();
		    	this.actionSuivante();
		    }
	    }

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_T){
				if (vueCommandes.boutonTire.isEnabled())
					vueCommandes.T.tirer();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				if (vueCommandes.boutonDescend.isEnabled())
					vueCommandes.De.descend();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				if (vueCommandes.boutonRecule.isEnabled())
					vueCommandes.R.recule();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_UP){
				if (vueCommandes.boutonMonte.isEnabled())
					vueCommandes.M.monte();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == 39){
				if (vueCommandes.boutonAvance.isEnabled())
					vueCommandes.Av.avance();
				System.out.print("avance connard !");
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_B){
				if (vueCommandes.boutonBraque.isEnabled())
					vueCommandes.B.braque();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_N){
				if (vueCommandes.boutonDors.isEnabled())
					vueCommandes.Do.dort();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if (vueCommandes.boutonAction.isEnabled())
					vueCommandes.Ac.act();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				Arbash = !Arbash;
				vueSac.repaint();
	        }
			
			if(e.getKeyCode() == KeyEvent.VK_G){
				Gardille = !Gardille;
				vueSac.repaint();
	        }
			
			
			repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}
	}
	*/
	public static void main(String[] args) {
		Train t = new Train();
		//System.out.println(t.MAX_NB_BANDITS);
		CEVue affichage = new CEVue(t);
		
		/*
		String nom = "";
		try {
			FileInputStream fis = new FileInputStream(nom);
			TargerDataLine tdl = new TargerDataLine(nom);
			//BufferedInputStream bis = new BufferedInputStream(fis);
			AudioInputStream player = new AudioInputStream(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}
	
	

}
