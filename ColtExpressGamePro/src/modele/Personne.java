package modele;

import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Set;


public abstract class Personne extends Possesseur{

	// **************************************************
    // Fields
    // **************************************************
	protected String name; // nom de bandit
	protected Train.Wagon wagon; //Le wagon ou il existe
	protected ActionList actions; //L'esemble des actions qui va prendre chaque tour max = 5
	protected Train train; 

	
	// **************************************************
    // Constructors
    // **************************************************
    /**
    * Parameterized constructor.
    * We create a Personne with the name in Train t in the right place
    */
	public Personne(Train t, String name){
		super(9);
		train = t;
		wagon = mettrePersonneBonWagon(t, this); //method de la classe wagon rdv sa propre description
		actions = new ActionList(t.getMAX_N_ACTION()); //maximum  actions
		this.name = name;
		//tour = 0;
	}
	
	// **************************************************
    // Getters
    // **************************************************
	public String getName() {
		return this.name;
	}
	
	
	
	// **************************************************
    // Abstract methods
    // **************************************************
	
	//this method will be used in contructor and we will redefine it in each sub-class 
	//according to polymorphisme, the method applied in the contructor are the good one
	/**
	 * This method will be used in contructor and we will redefine it in each sub-class ,
	 * according to polymorphisme and dynamic interpretation of Java, the method applied in the constructor are the one corresponding with the right class
	 * 
	 * @param t the train (the model)
	 * @param p the personne that we should put in the right place
	 * @return Train.Wagon the right wagon where we have to put the personne
	 */
	abstract Train.Wagon mettrePersonneBonWagon(Train t, Personne p); // return the wagon where p should be
	
	/**
	 * execute le premiere action s'il en a	
	 */
	abstract void executeAction();
	
	
	
	// **************************************************
    // Utilities functions
    // **************************************************
	public void addAction(Action a) {
		actions.addAction(a);
	}
	
	
	

	
	
	// **************************************************
    // Inner classe  - Personne.ActionList
    // **************************************************
	/*
	 * The object of this class is to simulate the work of the queue data structure (FIFO)
	 * The queue data structure of java caused problems
	 */
	protected class ActionList {
		private Hashtable<Integer, Action> actions;
		private int MAX_N;
		private int index;
		
		
		
		// **************************************************
	    // Constructors
	    // **************************************************
	    /**
	    * Parameterized constructor.
	    * We create a Personne with the name in Train t in the right place
	    */
		ActionList(int n){
			MAX_N = n;
			index=0;
			actions = new Hashtable<Integer, Action>();
		}

		/**
		 * 
		 * @param act adding act to the list of actions
		 */
		protected void addAction(Action act) {
			if(index>= MAX_N) return;
			this.actions.put(index, act);
			index++;
		}
		//Fonction auxiliares
		private int minSet(Set<Integer> s) {
			int out = this.MAX_N;
			for(int k : s) {
				if(out>k) out = k;
			}
			return out;
		}
		
		/**
		 * 
		 * @return Action to excute in this tour
		 */
		protected Action actionToExecute() {
			if(actions.size()==0) return null;
			int firstActionIndex = minSet(actions.keySet());
			Action out = actions.get(firstActionIndex);
			actions.remove(firstActionIndex);
			return out;
		}

		
	}
	
	
}
