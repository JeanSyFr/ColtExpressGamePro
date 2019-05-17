package modele;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
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


public class Train extends Observable
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
	private Wagon locomotive;
	private Wagon firstWagon;
	private Marshall marshall;
	private ArrayList<Bandit> joueurs;
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
		n -= 1;
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
	public ArrayList<Bandit> getBandit() {
		return joueurs;
	}
	public void excuteTour() {
		//this.joueurs.stream().map(x -> x.executeAction()).collect(Collectors.toList());
		
		for(Bandit b : joueurs) {
			b.executeAction();
		}
		Random rnd = new Random();
		double p = rnd.nextDouble();
		if(p<0.3) {
			if(p/0.3 >0.2) {
				this.marshall.addAction(Action.Avance);
			}else {
				this.marshall.addAction(Action.Recule);
			}
			this.marshall.executeAction();
			if(!this.marshall.wagon.bandits.isEmpty()) {
				this.marshall.addAction(Action.Tirer);
				this.marshall.executeAction();
			}
		}
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
		if(this.NB_BANDITS >= this.MAX_NB_BANDITS) return null;
		Wagon out = this.locomotive;
		while(out.suivant!=(null)) {
			out = out.suivant;
		}
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
	
	public int getMAX_N_ACTION() {
		return this.MAX_N_ACTION;
	}
	public int getMAX_N_BUTIN() {
		return this.MAX_N_BUTIN;
	}
	
	
	/*
	 * Gestion butin
	 */
	
	/*
	 * Ajouter des butins au hasard entre 1 et 4
	 */
	public static void print(String s) {
		System.out.println(s);
	}
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
	
	public static void main(String args[]) {
		Train t = new Train();
		System.out.print(t);
		t.actionsPreDefini();
		t.excuteTour();
		t.excuteTour();
		t.excuteTour();
		t.excuteTour();
		t.excuteTour();
		print("/nTrain after actions/n");
		System.out.println(t);
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
		private Wagon suivant;
		private Wagon precedent;
		private HashSet<Bandit> bandits;
		private boolean marshall;
		private int ordre; //utile pour les test unitaire
		public Wagon(Train t, int o){
			super(t.getMAX_N_BUTIN());
			train =t;
			bandits = new HashSet<Bandit>(0);
			ordre = o;
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
		
		public String toString() {
			String marsh = (this.marshall)?" contains marshall":" ";
			String out = "Wagon '"+ordre+"'"+ marsh +":\n"+
					"		Bandits" + bandits + ".\n";
			out += "		"+super.toString() + "\n";
			return out;
			
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

		public HashSet<Bandit> getBandits() {
			return this.bandits;
		}

		public Wagon getSuivant() {
			return this.suivant;
		}

		public boolean getMarshall() {
			return this.marshall;
		}
	}



	public Wagon getLocomotive() {
		return this.locomotive;
	}
	
}

