package modele;
import java.util.ArrayList;
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


public class Train
{
	/*
	 * Attributs
	 */
	//LE train est responsable de gerer le nbr de bandits + responsable de les creer
	private final int MAX_NB_BANDITS = 3 ;
	private int NB_BANDITS =0 ;
	private final int MAX_N_ACTION = 5;
	private final int NB_WAGONS;
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
		locomotive = new Wagon(this,0);
		firstWagon = new Wagon(this,1);
		locomotive.suivant = firstWagon;
		firstWagon.precedent = locomotive;
		n -= 1;
		Wagon current = firstWagon;
		for(int i =0; i<n ;i++) {
			Wagon add = new Wagon(this,i+2);
			current.suivant = add;
			add.precedent = current;
			current = add;
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
		System.out.println("adding bandit");
		System.out.print(t);
		b1.addAction(Action.Recule);
		b1.addAction(Action.Descendre);
		b1.executeAction();
		b1.executeAction();
		/*System.out.println("Le train apres les actions");
		System.out.print(t);*/
		
	}
	
	
	
	class Wagon
	{
		private Train train;
		private Wagon suivant;
		private Wagon precedent;
		private Set<Bandit> bandits;
		private Butins butins;
		private int ordre;
		public Wagon(Train t, int o){
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
			return "Wagon "+ordre+":\n"+
					"		Bandits" + bandits + "\n";
			
		}
		
		public void addButin(Butin b) {
			butins.pushButin(b);
		}
		public Butin stoleButin() {
			return butins.popButin();
		}
		public Bandit anotherBanditThan(Bandit b1) {
			if(bandits.size()<=1) return null;
			for(Bandit b2 : bandits) {
				if(!b2.equals(b1)) return b2;
			}
			return null;
		}
		
		public ContainerStack getButins() {
			return butins;
		}
		
		/*
		 * Just to rename the abstract class
		 */
		class Butins extends ContainerStack{ }


		
	
	}
	
}

