package modele;
import java.util.HashSet;
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


public class Train extends Observable
{
	/*
	 * Attributs
	 */
	//LE train est responsable de gerer le nbr de bandits + responsable de les creer
	public final int MAX_NB_BANDITS = 3 ;
	private int NB_BANDITS =0 ;
	public final int MAX_N_ACTION = 5;
	public final int NB_WAGONS;
	private Wagon locomotive;
	private Wagon firstWagon;
	
	/*
	 * Constructeur 
	 * @param n : int 
	 * le nombre de wagon dans ce train il y aura au moins un locomotive et un wagon
	 */
	public Train(int n){
		if(n <1) n =1;
		this.NB_WAGONS = n;
		setLocomotive(new Wagon(this,0));
		firstWagon = new Wagon(this,1);
		getLocomotive().setSuivant(firstWagon);
		firstWagon.precedent = getLocomotive();
		n -= 1;
		Wagon current = firstWagon;
		for(int i =0; i<n ;i++) {
			Wagon add = new Wagon(this,i+2);
			current.setSuivant(add);
			add.precedent = current;
			current = add;
		}
	}
	public Train() {
		this(4);
	}

	/*
	 * Cette fonction permet a un bandit de sauter sur le dernier wagon de train
	 */
	public Wagon banditLastWagon(Bandit b) {
		if(this.NB_BANDITS >= this.MAX_NB_BANDITS) return null;
		Wagon out = this.getLocomotive();
		while(out.getSuivant()!=(null)) {
			out = out.getSuivant();
		}
		out.bandits.add(b);
		this.NB_BANDITS++;
		return out;
	}
	
	/*
	 * Cette fonction permet a un Marshall de monter sur le locomotive de train
	 */
	public Wagon marshaLocomotive(Marshall b) {
		this.getLocomotive().marshall = true;
		return this.getLocomotive();
	}
	public String toString() {
		String out ="";
		out += this.getLocomotive();
		out += this.firstWagon;
		Wagon curent = this.firstWagon;
		while(curent.getSuivant()!=(null)) {
			curent = curent.getSuivant();
			out += curent;
		}
		return out;
	}
	
	public int getMAX_N_ACTION() {
		return this.MAX_N_ACTION;
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
		Train t = new Train(2);
		System.out.print(t);
		Bandit b1 = new Bandit(t,"Jean");
		Marshall m = new Marshall(t, "Marshall");
		System.out.println("adding bandit");
		System.out.print(t);
		b1.addAction(Action.Recule);
		b1.addAction(Action.Descendre);
		m.addAction(Action.Avance);
		b1.executeAction();
		b1.executeAction();
		m.executeAction();
		
		m.addAction(Action.Tirer);
		m.executeAction();
		
		b1.addAction(Action.Tirer);
		b1.executeAction();
		System.out.println();
		System.out.println("Le train apres les actions");
		System.out.println();
		System.out.print(t);
		
	}
	
	
	
	public Wagon getLocomotive() {
		return locomotive;
	}

	public void setLocomotive(Wagon loco) {
		this.locomotive = loco;
	}



	public class Wagon
	{
		private Train train;
		private Wagon suivant;
		private Wagon precedent;
		private Set<Bandit> bandits;
		private boolean marshall;
		private Possesseur poss; // il pourrait etre plus judicieux de faire un héritage
		private int ordre; //tile pour les test unitaire
		public Wagon(Train t, int o){
			train =t;
			bandits = new HashSet<Bandit>(0);
			ordre = o;
		}
		public boolean isLastWagon() {
			return  this.getSuivant()==(null);
		}
		public boolean isFirstWagon() {
			return this==(train.firstWagon);
		}
		public boolean isLoco() {
			return this==(train.getLocomotive());
		}
		public Wagon avanceMarshall() {
			if(!this.marshall) return null;
			this.marshall = false;
			this.getSuivant().marshall = true;	
			return this.getSuivant();
		}
		public Wagon reculeMarshall() {
			if(!this.marshall) return null;//si le marshall n'est pas ici 
			this.marshall = false;
			this.precedent.marshall = true;	
			return this.precedent;
		}
		public Wagon avanceBandit(Bandit bandit) {
			bandits.remove(bandit);
			this.getSuivant().bandits.add(bandit);	
			return this.getSuivant();
		}
		public Wagon reculeBandit(Bandit bandit) {
			bandits.remove(bandit);
			this.precedent.bandits.add(bandit);
			return this.precedent;
		}
		
		public String toString() {
			String marsh = (this.marshall)?" ":" not ";
			String out = "Wagon contains"+marsh+"marshall"+ordre+":\n"+
					"		Bandits" + bandits + "\n";
			return out;
			
		}
		
		public void addButin(Butin b) {
			this.poss.addButin(b);
		}
		public Butin stoleButin() {
			return this.poss.popButin();
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
		
		public Possesseur getPossesseur() {
			return this.poss;
		}
		
		public Wagon getSuivant() {
			return suivant;
		}
		public void setSuivant(Wagon suivant) {
			this.suivant = suivant;
		}

		/* 
		 * Just to rename the abstract class and max = 8
		 */
		/*
		class Butins extends Possesseur{

			Butins() {
				super(8);
			} 
		}
		*/

	
		public Set<Bandit> getBandits() {
			return this.bandits;
		}


		
	
	}
	
}

