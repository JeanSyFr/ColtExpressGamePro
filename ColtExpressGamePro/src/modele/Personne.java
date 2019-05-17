package modele;

import java.util.PriorityQueue;


public abstract class Personne extends Possesseur{
	protected String name; // nom de bandit
	protected Train.Wagon wagon; //Le wagon ou il existe
	protected ActionList actions; //L'esemble des actions qui va prendre chaque tour max = 5
	//private static int tour;
	
	
	public Personne(Train t, String name){
		super(t.getMAX_N_BUTIN());
		wagon = mettrePersonneBonWagon(t, this); //method de la classe wagon rdv sa propre description
		actions = new ActionList(t.getMAX_N_ACTION()); //maximum  actions
		this.name = name;
		//tour = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	/*public static int getTour() {
		return tour;
	}*/
	
	//this method will be used in contructor and we will redefine it in each sub-class 
	//according to polymorphisme, the method applied in the contructor are the good one
	abstract Train.Wagon mettrePersonneBonWagon(Train t, Personne p); // return the wagon where p should be 
	abstract void executeAction(); 	//execute le premiere action s'il en a	
	
	
	
	public void addAction(Action a) {
		actions.addAction(a);
	}
	
	
	/*protected static void incrementTour() {
		tour++;
	}
	protected static void decrementTour() {
		tour--;
	}*/
	
	
	
	
	
	protected class ActionList {
		/*
		 * A modifier pour faire une file
		 */
		private PriorityQueue<Action> actions;
		private int MAX_N;
		private int index;
		ActionList(int n){
			MAX_N = n;
			index=0;
			actions = new PriorityQueue<Action>();
		}
		protected void addAction(Action act) {
			if(index>= MAX_N) return;
			this.actions.add( act);
		}
		
		protected Action actionToExecute() {
			return actions.poll();
		}
		
	}
	
	
}
