package modele;


import modele.Train.Wagon;

/*
 * Le class qui represente le bandit
 * 
 */
public class Bandit extends Personne
{
	
	private boolean interieur; //pour savoir s'il est sur le toit ou dans le wagon
	protected SacButin sac;
	
	/*
	 * Creer le bandit sur le toit de dernier wagon
	 */
	public Bandit(Train t, String name){
		super(t,name);
		interieur = false; // au debut il est sur le toit
		sac = new SacButin();
	}
	
	@Override
	protected Wagon personneWagon(Train t, Personne p) {
		return t.banditLastWagon(this);
	}
	
	// getter pour les autres classes
	public boolean getInterieur( ) { 
		return this.interieur;
	}
	@Override
	public void executeAction() {
		//si cette action est nulle rien va etre executer
		Action actionExcute = actions.actionToExecute();
		
		if(actionExcute.equals(Action.Tirer)) {
			this.tirer();
		}
		if(actionExcute.equals(Action.Braquer)) {
			this.braquer();
		}
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
	
	
	
	
	public String toString() {
		String pos = (this.interieur)? ("a l'interieur"):("sur le toit") ;
		return this.name + " est " + pos;
	}
	
	
	
	public void braquer() {
		if(wagon.getPossesseur().isEmpty()) return;
		this.sac.addButin(wagon.stoleButin());
	}
	
	
	/*
	 * Juste pour changer le nom de class and max = 4 
	 */
	class SacButin extends Possesseur{ 
		SacButin() {
			super(4);
		}
		
	}


	public String getName() {
		return this.name;
	}









	
		
		

	
}

