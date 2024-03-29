package modele;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


/*

^
|
|
|
|
|
|	* prison    *           *           *     B *
|	*************************           *********
|	*Locomotive **   First  *           * Last	*
|	*************************...........*********
|
|____________Direction(wagon.suivant, Action.avancer) ______________>




 le train c'est une liste doublement chaine 
 Le modelisation a ete choisi comme ca car c'est plus proche de modele reel + conseil de prof
 Un train : un esemble de wagons connectes l'un et l'autre
 
 
 */

public class Train extends Observable implements Iterable<Train.Wagon>
{
	
	
	// **************************************************
    // Constants
    // **************************************************
	public final int MAX_NB_BANDITS = 3 ;
	public final int MAX_N_ACTION = 5;
	public final int MAX_N_BUTIN = 5;
	public final int NB_WAGONS_MAX = 5;
	private final double NERVOISITE_MARSHALL = 0.3;
	private final boolean checkInvariants = true;
	
	// **************************************************
    // Fields
    // **************************************************
	private int NB_BANDITS =0 ;
	private double pM = 0.0;
	protected Wagon locomotive;
	protected Wagon firstWagon;
	protected Marshall marshall;
	protected ArrayList<Bandit> joueurs;
	
	// **************************************************
    // Constructors
    // **************************************************
    /**
    * Default constructor.
    * We creat a train with NB_WAGONS_MAX (locimotive inclus), with a MAX_NB_BANDITS bandits 
    * 
    */
	public Train(){
		//LE train est responsable de gerer le nbr de bandits + responsable de les creer
		int n = this.NB_WAGONS_MAX;
		joueurs = new ArrayList<Bandit>();
		locomotive = new Wagon(this,0);
		firstWagon = new Wagon(this,1);
		locomotive.suivant = firstWagon;
		firstWagon.precedent = locomotive;
		Wagon current = firstWagon;
		this.addButins(current);
		this.addButins(locomotive);
		this.marshall = new Marshall(this);
		for(int i =2; i<n ;i++) {
			Wagon addedWagon = new Wagon(this,i);
			current.suivant = addedWagon;
			addedWagon.precedent = current;
			current = addedWagon;
			this.addButins(current);
		}
		
		for(int i = 0 ; i < MAX_NB_BANDITS ; i++) {
			joueurs.add(new Bandit(this, "P"+(i+1)));
		}
		
		if(this.checkInvariants) assert this.checkInvariants() : "checkInvariants failed in initialisation";
	}
	
	
	// **************************************************
    // Getters
    // **************************************************
	public Marshall getMarshall() {
		return this.marshall;
	}
	public ArrayList<Bandit> getBandits() {
		return joueurs;
	}
	public int getMAX_N_ACTION() {
		return this.MAX_N_ACTION;
	}
	public int getMAX_N_BUTIN() {
		return this.MAX_N_BUTIN;
	}
	public Wagon getLastWagon() {
		Wagon out = this.locomotive;
		while(out.suivant!=(null)) {
			out = out.suivant;
		}
		return out;
	}
	public Wagon getLocomotive() {
		return this.locomotive;
	}
	public Wagon getFirstWagon() {
		return this.firstWagon;
	}
	
	
	// **************************************************
    // Gestion de jeu
    // **************************************************
	

	/**
	 * This is a function that could be used publicly
	 * It represents a round of the game; it will execute the first action to do by all the players
	 * 									  and then the marshal will take actions using a specific strategy explained in the function
	 */
	public void excuteTour() {		
		for(Bandit b : joueurs) {
			b.executeAction();
			this.notifyObservers();
		}
		Random rnd = new Random();
		double p = rnd.nextDouble();
		
			//The marshal will check if there is a bandit in the next  
			if(this.marshall.wagon.suivant!=null &&  !this.marshall.wagon.suivant.bandits.isEmpty()) {
				this.marshall.addAction(Action.Avance); // If the next wagon contains a bandit he will move forward
			}else {
				//in the other case in function of his nervosity he will move forward or backward
				if(p<NERVOISITE_MARSHALL) {
					double newP = this.pM + p/NERVOISITE_MARSHALL; //A new variable to detrmine wither he moves g=forward or backward
					if(newP>05) {
						this.marshall.addAction(Action.Recule);
						this.pM = -0.1;
					}
						
					else {
						this.marshall.addAction(Action.Avance);
						this.pM = 0.1;
					}
						
					
				}
			}
			this.marshall.executeAction();
			//If he findes a bandit in his same wagon he will shot
			//The bandit will shot even if the bandits were on the top; he wants to disturb and scary them 
			if(!this.marshall.wagon.bandits.isEmpty()) {
				this.marshall.addAction(Action.Tirer);
				this.marshall.executeAction();
			
		}
			
		if(this.checkInvariants) assert this.checkInvariants() : "checkInvariants failed in Train::excuteTour()";
		this.notifyObservers();
		
	}
	/**
	 * A useful function for testing it  initialize arbitrary the actions of bandits
	 */
	public void actionsPreDefini() {
		for(Bandit b : joueurs) {
			b.addAction(Action.Descendre);
			b.addAction(Action.Braquer);
			b.addAction(Action.Recule);
			b.addAction(Action.Braquer);
			b.addAction(Action.Tirer);
		}
		
	}

	/**
	 * Cette fonction permet a un bandit de sauter sur le dernier wagon de train
	 */
	protected Wagon banditLastWagon(Bandit b) {
		assert this.NB_BANDITS <= this.MAX_NB_BANDITS : "Number of bandits has depassed the limit "+MAX_NB_BANDITS;
		Wagon out = this.getLastWagon();
		out.bandits.add(b);
		this.NB_BANDITS++;
		return out;
	}
	
	/**
	 * Cette fonction permet a un Marshall de monter sur le locomotive de train
	 */
	protected Wagon marshaLocomotive(Marshall b) {
		this.locomotive.marshall = true;
		return this.locomotive;
	}
	
	// **************************************************
    // Polymorphisme
    // **************************************************
	@Override
	public String toString() {
		String out ="";
		out += this.locomotive;
		out += this.firstWagon;
		Wagon curent = this.firstWagon;
		while(curent.suivant!=(null)) {
			curent = curent.suivant;
			out += curent;
		}
		return out;
	}
	@Override
	public Iterator<Wagon> iterator() {
		return new TrainIterator(this);
		
	}
	

	// **************************************************
    // Private methods
    // **************************************************
	
	// ***********************
    // Gestion des butins
    // ***********************
	
	/**
	 * Ajouter des butins au hasard entre 1 et 4
	 * @param w le Wagon dans lequel on va ajouter des butins
	 * @see Train::Train() c'est utilse uniquement ici
	 */
	private void addButins(Wagon w) {
		if(w==this.locomotive) {
			Butin b = new Magot(locomotive);
			w.addButin(b);
			return;
		}
		Random rnd = new Random();
		int butinNbr = rnd.nextInt(MAX_N_BUTIN );
		Butin b = new Bourse(w);
		w.addButin(b);
		for(int i = 0; i< butinNbr; i++) {
			int butinType = rnd.nextInt(2);
			b = (butinType ==0 )?  new Bijou(w) : new Bourse(w);
			w.addButin(b);
		}
		 
	}
	
	
	
	// **************************************************
    // Invariants
    // **************************************************
	/** 
	    * This method is used to find check if the wagon.ordre match with the calculated value . 
	    * @param w This is the Trian.Wagon that we want to test
	    * @return boolean This returns true if the test succeed, false in the other case or if w==null
	    */
	private boolean checkWagonOrdre(Wagon w) {
		if(w==null) return false;
		int o = 0;
		Wagon current = this.locomotive;
		while(w!=current && o <this.NB_WAGONS_MAX) {
			o++;
			assert (current != null) : "pas de coherence entre NB_WAGONS_MAX et le vrai nombre de wagon";
			current = current.suivant;
		}
		assert o <this.NB_WAGONS_MAX: "checking the order of wagon that does not exist in the train";
		return o == w.ordre;		
	}
	/** 
	    * This method is used to find check if the place of the bandit, from his perspective, match with his right place from the perspective of the train. 
	    * @param b This is the Bandit that we want to test
	    * @return boolean This returns true if the test succeed, false in the other case or if b==null
	    */
	private boolean checkBanditPlacement(Bandit b) {
		if (b == null) return false;
		return b.wagon.bandits.contains(b);
	}
	/** 
	    * This method is used to find check if the place of the butin, from its perspective, match with its right place from the perspective of the train. 
	    * @param b This is the Butin that we want to test
	    * @return boolean This returns true if the test succeed, false in the other case or if b==null
	    */
	private boolean checkButinPlacement(Wagon w) {
		if (w == null) return false;
		HashSet<Butin> butins = w.getButins();
		return ForEachPredicat.forEach(butins, b ->b.wagon==w);
	}
	/** 
	    * This method is used to find check all the possible variants of the model 
	    * @return boolean This returns true if all the variants are correct, false if one failed
	    */
	protected boolean checkInvariants() {
		return ForEachPredicat.forEach(this, w->this.checkWagonOrdre(w)) && 
				ForEachPredicat.forEach(this.joueurs, b -> this.checkBanditPlacement(b)) &&
				ForEachPredicat.forEach(this, w -> this.checkButinPlacement(w));
				
	}
	
	/*
	 * TO facilate printing
	 */
	public static void print(Object o) { System.out.println(o);}
	
/*
^
|
|
|	* prison    *           *           *     B *
|	*************************           *********
|	*Locomotive **   First  *           * Last	*
|	*************************...........*********
|
|____________Direction(wagon.suivant, Action.avancer) ______________>

 */
	
	
	
	public static void main(String args[]) {
		Train t = new Train();
		
		//ForEach.forEach(t, w -> print(t.checkWagonOrdre(w)));
		print(ForEachPredicat.forEach(t, w->t.checkWagonOrdre(w)));
		
		/**
		 * This could be uncommented if necessary
		 */
		/*for(Wagon w : t) {
			print(w.ordre);
		}
		print(t.getLastWagon().ordre);*/
		/*System.out.print(t);
		t.actionsPreDefini();
		t.excuteTour();
		t.excuteTour();
		t.excuteTour();
		t.excuteTour();
		t.excuteTour();
		print("/nTrain after actions/n");
		print(t);*/
		/*b1.addAction(Action.Descendre);
		b1.addAction(Action.Braquer);
		m.addAction(Action.Avance);
		b1.executeAction();
		b1.executeAction();
		m.executeAction();
		System.err.println("Le train apres les actions");
		System.out.println();
		System.out.print(t);	
		m.addAction(Action.Tirer);
		m.executeAction();
		
		b1.addAction(Action.Tirer);
		b1.executeAction();
		System.err.println("Le train apres les actions 2");
		System.out.println();
		System.out.print(t);	
		System.out.println();
				*/
	
	
}
	
	
	// **************************************************
    // Inner classe 1 - Train.Wagon
    // **************************************************

	public class Wagon extends Possesseur
	{
		// **************************************************
	    // Fields
	    // **************************************************
		private Train train;
		protected Wagon suivant;
		protected Wagon precedent;
		protected HashSet<Bandit> bandits;
		private boolean marshall;
		private int ordre; //utile pour les test unitaire
		
		// **************************************************
	    // Constructors
	    // **************************************************
		
		/**
		    * Parameterized constructor.
		    * 
		    * @param t the Train of the Wagon
		    * @param o l'ordre de wagon de train
		    */
		public Wagon(Train t, int o){
			super(t.getMAX_N_BUTIN());
			train =t;
			bandits = new HashSet<Bandit>(0);
			ordre = o;
		}
		
		
		// **************************************************
	    // Getters
	    // **************************************************
		public HashSet<Bandit> getBandits() {
			return this.bandits;
		}
		public Wagon getSuivant() {
			return this.suivant;
		}
		public boolean getMarshall() {
			return this.marshall;
		}
		public boolean isLastWagon() {
			return  this.suivant==(null);
		}
		public boolean isFirstWagon() {
			return this==(train.firstWagon);
		}
		public boolean isLoco() {
			return this==(train.locomotive);
		}
		public Train getTrain() {
			return train;
		}
		
		
		// **************************************************
	    // Utilities functions for Personne
		// The Wagon class is responsible for the deplacement of Personne
		// @see Bandit::executeAction() Marshal::executeAction()
	    // **************************************************
		
		/**
		 * If the marshal is inside this wagon he will be deplaced to the next wagon "if" possible
		 * @return Wagon le wagon ou le Marshal a avance
		 * 
		 *  @see Bandit::executeAction() 
		 *  @see Marshal::executeAction()
		 */
		protected Wagon avanceMarshall() {
			if(!this.marshall) return null;
			if(this.suivant==null) return this;
			this.marshall = false;
			this.suivant.marshall = true;	
			return this.suivant;
		}
		protected Wagon reculeMarshall() {
			if(!this.marshall) return null;
			if(this.precedent==null) return this;
			this.marshall = false;
			this.precedent.marshall = true;	
			return this.precedent;
		}
		protected Wagon avanceBandit(Bandit bandit) {
			if(this.suivant==null) return this;
			bandits.remove(bandit);
			this.suivant.bandits.add(bandit);	
			return this.suivant;
		}
		protected Wagon reculeBandit(Bandit bandit) {
			if(this.precedent==null) return this;
			bandits.remove(bandit);
			this.precedent.bandits.add(bandit);
			return this.precedent;
		}
		
		// **************************************************
	    // Utilities functions for ActionList class
		// The Wagon class is responsible for managing the procedure of shooting, stealing 
		// @see Bandit::executeAction() Marshal::executeAction()
	    // **************************************************
		
		
		/**
		 * Get a random butin and remove it from the wagon
		 * 
		 * @return Butin a butin chosen in a random way
		 * 
		 * @see Possesseur::popButin()
		 */
		protected Butin stoleButin() {
			return super.popButin();
		}
		/**
		 * This function is very important for the Personne class when a Personne want to shoot another bandit ,
		 * this function will return a Bandit in random way differnt from the one who has shot
		 * @param p A personne who has taken the action "tirer"
		 * @return Bandit a bandit that shoulb be affected by the shot if possible, if not we return null
		 */
		protected Bandit anotherBanditThan(Personne p) {
			for(Bandit b2 : bandits) {
				if(!b2.equals(p) && b2.getInterieur()) return b2;
			}
			return null;
		}
		
		@Override
		public String toString() {
			String marsh = (this.marshall)?" contains marshall":" ";
			String out = "Wagon '"+ordre+"'"+ marsh +":\n"+
					"		Bandits" + bandits + ".\n";
			out += "		"+super.toString() + "\n";
			return out;	
		}
		
	}

	// **************************************************
    // Inner class 2 - Train.TrainIterator
    // **************************************************
	class TrainIterator implements Iterator<Wagon>{
		private Wagon current;
		TrainIterator(Train t){
			current = t.locomotive;
		}
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return current !=null;
		}
		@Override
		public Wagon next() {
			Wagon w = current;
			current = current.suivant;
			return w ;
		}
	}
	
	
}

