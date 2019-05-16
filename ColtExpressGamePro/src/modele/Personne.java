package modele;

import java.util.Hashtable;
import java.util.Set;


public abstract class Personne {
	protected String name; // nom de bandit
	protected Train.Wagon wagon; //Le wagon ou il existe
	protected ActionList actions; //L'esemble des actions qui va prendre chaque tour max = 5
	
	public String getName() {
		return this.name;
	}
	
	public Personne(Train t, String name){
		wagon = personneWagon(t, this); //method de la classe wagon rdv sa propre description
		actions = new ActionList(t.getMAX_N_ACTION()); //maximum  actions
		this.name = name;
	}
	
	
	//this method will be used in contructor and we will redefine it in each sub-class 
	//according to polymorphisme, the method applied in the contructor are the good one
	abstract Train.Wagon personneWagon(Train t, Personne p); // return the wagon where p should be 
	abstract void executeAction(); 	//execute le premiere action s'il en a	
	public void addAction(Action a) {
		actions.addAction(a);
	}
	public void tirer() {
		Bandit b2 = wagon.anotherBanditThan(this);
		if(b2 == null) {
			System.out.println(this.name + " has shot no body");
			return;
		}
		System.out.println(this.name + " has shot "+b2.name);

			
		if(!b2.sac.isEmpty())
			wagon.addButin(b2.sac.popButin());
	}
	
	
	
	
	
	
	protected class ActionList {
		private Hashtable<Integer, Action> actions;
		private int MAX_N;
		private int index;
		ActionList(int n){
			MAX_N = n;
			index=0;
			actions = new Hashtable<Integer, Action>();
		}
		protected void addAction(Action act) {
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
		
		
		protected Action actionToExecute() {
			if(actions.size()==0) return null;
			int firstActionIndex = minSet(actions.keySet());
			Action out = actions.get(firstActionIndex);
			actions.remove(firstActionIndex);
			return out;
		}
		
		
		
	}
	
	
}
