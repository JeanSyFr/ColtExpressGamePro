package modele;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

import org.junit.Ignore;
import org.junit.Test;

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
import java.util.stream.Collectors;


public class Train extends Observable implements Iterable<Train.Wagon>
{
	/*
	 * Attributs
	 */
	//LE train est responsable de gerer le nbr de bandits + responsable de les creer
	public final int MAX_NB_BANDITS = 3 ;
	private int NB_BANDITS =0 ;
	public final int MAX_N_ACTION = 5;
	public final int MAX_N_BUTIN = 5;
	public final static int NB_WAGONS_MAX = 5;
	private final double NERVOISITE_MARSHALL = 0.3;
	private final boolean checkInvariants = true;
	protected Wagon locomotive;
	protected Wagon firstWagon;
	protected Marshall marshall;
	protected ArrayList<Bandit> joueurs;
	/*
	 * Constructeur 
	 * @param n : int 
	 * le nombre de wagon dans ce train il y aura au moins un locomotive et un wagon
	 */
	public Train(){
		int n = this.NB_WAGONS_MAX;
		joueurs = new ArrayList<Bandit>();
		locomotive = new Wagon(this,0);
		firstWagon = new Wagon(this,1);
		locomotive.suivant = firstWagon;
		firstWagon.precedent = locomotive;
		//n -= 1;
		/*
		 * pourquoi tu as mis 
		 */
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
			joueurs.add(new Bandit(this, "Player"+(i+1)));
		}
		
	}
	
	
	

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
	Wagon getLastWagon() {
		Wagon out = this.locomotive;
		while(out.suivant!=(null)) {
			out = out.suivant;
		}
		return out;
	}
	/*Wagon getLocomotive() {
		return this.locomotive;
	}
	Wagon getFirstWagon() {
		return this.firstWagon;
	}*/
	
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
		
		if(p<0.3) {
			if(this.marshall.wagon.suivant!=null &&  !this.marshall.wagon.suivant.bandits.isEmpty()) {
				this.marshall.addAction(Action.Avance);
			}else {
				this.marshall.addAction(Action.Recule);
			}
			this.marshall.executeAction();
			if(!this.marshall.wagon.bandits.isEmpty()) {
				this.marshall.addAction(Action.Tirer);
				this.marshall.executeAction();
				System.err.println("Yes marshal has shot !");
			}
			
		}
		this.notifyObservers();
	}
	public void actionsPreDefini() {
		//joueurs.get(0).addAction(Action.Tirer);

		for(Bandit b : joueurs) {
			b.addAction(Action.Descendre);
			b.addAction(Action.Braquer);
			b.addAction(Action.Recule);
			b.addAction(Action.Braquer);
			b.addAction(Action.Tirer);
			//b.addAction(Action.Descendre);
			//b.addAction(Action.Braquer);
			//b.addAction(Action.Tirer);
			/*b.addAction(Action.Recule);
			b.addAction(Action.Braquer);
			b.addAction(Action.Tirer);*/
		}
		
	}

	/*
	 * Cette fonction permet a un bandit de sauter sur le dernier wagon de train
	 */
	public Wagon banditLastWagon(Bandit b) {
		assert this.NB_BANDITS <= this.MAX_NB_BANDITS ;
		Wagon out = this.getLastWagon();
		out.bandits.add(b);
		this.NB_BANDITS++;
		return out;
	}
	
	/*
	 * Cette fonction permet a un Marshall de monter sur le locomotive de train
	 */
	public Wagon marshaLocomotive(Marshall b) {
		this.locomotive.marshall = true;
		return this.locomotive;
	}
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
	

	
	
	/*
	 * Gestion butin
	 */
	
	/*
	 * Ajouter des butins au hasard entre 1 et 4
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
	public static void print(Object o) { System.out.println(o);}
	
	
	public static void main(String args[]) {
		Train t = new Train();
		
		//ForEach.forEach(t, w -> print(t.checkWagonOrdre(w)));
		print(ForEachPredicat.forEach(t, w->t.checkWagonOrdre(w)));
		
		
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



	public class Wagon extends Possesseur
	{
		private Train train;
		Wagon suivant;
		Wagon precedent;
		HashSet<Bandit> bandits;
		private boolean marshall;
		private int ordre; //utile pour les test unitaire
		public Wagon(Train t, int o){
			super(t.getMAX_N_BUTIN());
			train =t;
			bandits = new HashSet<Bandit>(0);
			ordre = o;
		}
		
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
		public Wagon avanceMarshall() {
			if(!this.marshall) return null;
			this.marshall = false;
			this.suivant.marshall = true;	
			return this.suivant;
		}
		public Wagon reculeMarshall() {
			if(!this.marshall) return null;//si le marshall n'est pas ici 
			this.marshall = false;
			this.precedent.marshall = true;	
			return this.precedent;
		}
		public Wagon avanceBandit(Bandit bandit) {
			bandits.remove(bandit);
			this.suivant.bandits.add(bandit);	
			return this.suivant;
		}
		public Wagon reculeBandit(Bandit bandit) {
			bandits.remove(bandit);
			this.precedent.bandits.add(bandit);
			return this.precedent;
		}
		
		
		
		
		public Butin stoleButin() {
			return super.popButin();
		}
		public Bandit anotherBanditThan(Personne p) {
			/*if(bandits.size()<=1) {
				System.err.println("Wagon : nobody here");
				return null;
			}*/
			for(Bandit b2 : bandits) {
				if(!b2.equals(p)) return b2;
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

		
		
		public Train getTrain() {
			return train;
		}

	}



	@Override
	public Iterator<Wagon> iterator() {
		return new TrainIterator(this);
		
	}
	
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
	
	class test{
		
		
		
		Train t;
		@Before
		void setUp() {
			t = new Train();
		}
		
		@Test
		void testVariants() {
			assert t.checkInvariants() : "Test of variants failed";
		}
		@Test
		void testInitialPlace() {
			assert ForEachPredicat.forEach(t.getBandits(), b -> t.getLastWagon().bandits.contains(b)) : "The initial place of bandits is flase";
		}
		@Test
		void testDLL() {
			boolean out;
			out = t.locomotive.precedent ==null;
			out &= t.locomotive.suivant.precedent == t.locomotive;
			out &= t.getLastWagon().suivant ==null;
			out &= t.getLastWagon() == t.getLastWagon().precedent.suivant;
			
			assert ForEachPredicat.forEach (
					t , w -> 
			{ //implementing the predicat function
				if(w==t.locomotive || w==t.getLastWagon()) {
					return true; //beacause we already tested it
				}else {
					return w ==w.suivant.precedent && w==w.precedent.suivant;
				}
			}	
					
					) : "";			
		}
		
		
		
		
		@After
		void tearDown() {
			t = null;
		}
		
		public void main(String[] args) {
			
		}
		
		
		
		
		
	}
	
	
}

