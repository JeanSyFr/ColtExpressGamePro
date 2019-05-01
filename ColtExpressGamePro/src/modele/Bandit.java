package modele;


/*
 * Le class qui represente le bandit
 * 
 */
public class Bandit
{
	private String name; // nom de bandit
	public Train.Wagon wagon; //Le wagon ou il existe
	private boolean interieur; //pour savoir s'il est sur le toit ou dans le wagon
	private Action[] actions; //L'esemble des actions qui va prendre chaque tour max = 5
	
	/*
	 * Creer le bandit sur le toit de dernier wagon
	 */
	public Bandit(Train t, String name){
		wagon = t.banditLastWagon(this); //method de la classe wagon rdv sa propre description
		interieur = false; // au debut il est sur le toit
		actions = new Action[5]; //maximum  actions
	}
	
	// getter pour les autres classes
	public boolean getInterieur( ) { 
		return this.interieur;
	}
	
	//execute an action s'il en a 
	public void executeAction() {
		//si cette action est nulle rien va etre executer
		Action actionExcute = this.premierActionExcecution();
		if(interieur && actionExcute.equals(Action.Monter)) {
			interieur = false;
		}
		if(!interieur && actionExcute.equals(Action.Descendre)) {
			interieur = true;
		}
		if(!wagon.isLastWagon() && actionExcute.equals(Action.Avance)) {
			wagon.avanceBandit(this);
		}
		if(!wagon.isFirstWagon() && actionExcute.equals(Action.Recule)) {
			wagon.reculeBandit(this);
		}	
	}
	
	//le premier action qu'il faut executer
	private Action premierActionExcecution() {
		for(Action act : actions)
			if(!act.equals(null)) 
				return act;
		return null;
	}
	public void addAction(Action a) {
		for(Action act : actions) {
			if(act.equals(null)) {
				act = a;
				return;
			}
		}
	}
	

	
}

