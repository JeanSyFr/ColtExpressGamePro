package modele;
import java.util.Hashtable;
import java.util.Set;



/*
 * Le class qui represente le bandit
 * 
 */
public class Bandit
{
	private String name; // nom de bandit
	private Train.Wagon wagon; //Le wagon ou il existe
	private boolean interieur; //pour savoir s'il est sur le toit ou dans le wagon
	private ActionList actions; //L'esemble des actions qui va prendre chaque tour max = 5
	private SacButin sac;
	
	/*
	 * Creer le bandit sur le toit de dernier wagon
	 */
	public Bandit(Train t, String name){
		wagon = t.banditLastWagon(this); //method de la classe wagon rdv sa propre description
		interieur = false; // au debut il est sur le toit
		actions = new ActionList(this, t.getMAX_N_ACTION()); //maximum  actions
		this.name = name;
		sac = new SacButin();
	}
	
	// getter pour les autres classes
	public boolean getInterieur( ) { 
		return this.interieur;
	}
	
	//execute le premiere action s'il en a
	public void executeAction() {
		//si cette action est nulle rien va etre executer
		Action actionExcute = actions.actionToExecute();
		if(interieur && actionExcute.equals(Action.Monter)) {
			interieur = false;
			System.out.println(name+" monte sur le toit");
			System.out.println(wagon);
			return;
		}
		if(!interieur && actionExcute.equals(Action.Descendre)) {
			interieur = true;
			System.out.println(name+" descend a l'interieur");
			System.out.println(wagon);
			return;
		}
		if(!wagon.isLastWagon() && actionExcute.equals(Action.Avance)) {
			Train.Wagon newWagon = wagon.avanceBandit(this);
			System.out.println(name+" avance vers la fin de train");
			wagon =  newWagon;
			System.out.println(wagon);
			return;
		}
		if(!wagon.isFirstWagon() && actionExcute.equals(Action.Recule)) {
			Train.Wagon newWagon =wagon.reculeBandit(this);
			System.out.println(name+" recule vers le debut de train");
			wagon =  newWagon;
			System.out.println(wagon);
			return;
		}
		System.out.println(name+ " has nothing to do!");
	}
	
	
	public void addAction(Action a) {
		actions.addAction(a);
	}
	
	
	public String toString() {
		String pos = (this.interieur)? ("a l'interieur"):("sur le toit") ;
		return this.name + " est " + pos;
	}
	
	
	public void tirer() {
		Bandit b2 = wagon.anotherBanditThan(this);
		wagon.addButin(b2.sac.popButin());
	}
	public void braquer() {
		if(wagon.getButins().isEmpty()) return;
		this.sac.pushButin(wagon.stoleButin());
	}
	
	
	
	
	
	
	
	private class ActionList {
		private Hashtable<Integer, Action> actions;
		private int MAX_N;
		private int index;
		ActionList(Bandit bandit, int n ){
			MAX_N = n;
			index=0;
			actions = new Hashtable<Integer, Action>();
		}
		private void addAction(Action act) {
			if(index>= MAX_N) return;
			this.actions.put(index, act);
			index++;
		}
		private int minSet(Set<Integer> s) {
			int out = this.MAX_N;
			for(int k : s) {
				if(out>k) out = k;
			}
			return out;
		}
		
		private Action actionToExecute() {
			if(actions.size()==0) return null;
			int firstActionIndex = minSet(actions.keySet());
			Action out = actions.get(firstActionIndex);
			actions.remove(firstActionIndex);
			return out;
		}
	}
	
	/*
	 * Juste pour changer le nom de class and max = 4 
	 */
	private class SacButin extends ContainerStack{ 
		SacButin() {
			super(4);
		}
		
	}
		
		

	
}

