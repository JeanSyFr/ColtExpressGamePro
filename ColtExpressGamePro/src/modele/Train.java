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
|				 *			*			*	  B *
|	*************************			*********
|	*Locomotive **   First	*			* Last	*
|	*************************...........*********
|
|_____________________________Direction(wagon.suivat, Action.avancer) ______________>




 
 */


public class Train
{
	private final int NB_WAGONS;
	private Wagon locomotive;
	private Wagon firstWagon;

	public Train(int n){
		if(n <1) n =1;
		this.NB_WAGONS = n;
		locomotive = new Wagon(this);
		firstWagon = new Wagon(this);
		locomotive.suivant = firstWagon;
		firstWagon.precedent = locomotive;
		n -= 1;
		Wagon current = firstWagon;
		while(n >0) {
			current.suivant = new Wagon(this);
			current.suivant.precedent = current;
			current = current.suivant;
			n -=1;
		}
	}

	
	public Wagon banditLastWagon(Bandit b) {
		Wagon out = this.locomotive;
		while(! out.suivant.equals(null)) {
			out = out.suivant;
		}
		out.bandits.add(b);
		return out;
	}
	class Wagon
	{
		private Train train;
		private Wagon suivant;
		private Wagon precedent;
		private Set<Bandit> bandits;
		
		public Wagon(Train t){
			train =t;
			bandits = new HashSet<Bandit>();
		}
		public boolean isLastWagon() {
			return  this.suivant.equals(null);
		}
		public boolean isFirstWagon() {
			return this.equals(train.firstWagon);
		}
		public void avanceBandit(Bandit bandit) {
			bandits.remove(bandit);
			this.suivant.bandits.add(bandit);	
		}
		public void reculeBandit(Bandit bandit) {
			bandits.remove(bandit);
			this.precedent.bandits.add(bandit);	
		}
	
	}
	
}

