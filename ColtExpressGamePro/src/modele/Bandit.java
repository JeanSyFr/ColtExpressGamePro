package modele;


import modele.Train.Wagon;

/*
 * Le class qui represente le bandit
 * 
 */
public class Bandit extends Personne
{
	private boolean interieur; //pour savoir s'il est sur le toit ou dans le wagon
	private boolean joueur = false;
	private  int bulits = 6;
	
	/*
	 * Creer le bandit sur le toit de dernier wagon
	 */
	public Bandit(Train t, String name, boolean j){
		super(t,name);
		interieur = false; // au debut il est sur le toit
		joueur = j;
	}
	public Bandit(Train t, String name) {
		this(t,name,false);
	}
	
	@Override
	protected Wagon mettrePersonneBonWagon(Train t, Personne p) {
		return t.banditLastWagon(this);
	}
	
	// getter pour les autres classes
	public boolean getInterieur() { 
		return this.interieur;
	}
	@Override
	protected void executeAction() {
		//si cette action est nulle rien va etre executer
		Action actionExcute = actions.actionToExecute();
		if(actionExcute ==(null)) return;
		if(bulits>=0 && interieur && actionExcute.equals(Action.Tirer)) {
			this.tirer();
			bulits--;
			return;
		}
		if(actionExcute.equals(Action.Braquer)) {
			if(interieur) {
				this.braquer();
				System.err.println(name+" a braquer");
			}else {
				System.err.println(name+" n'a pas braquer");
			}
			
		}
		if(interieur && !wagon.isLoco() && actionExcute.equals(Action.Monter)) {
			interieur = false;
			System.out.println(name+" monte sur le toit");
			System.out.println(wagon);
			return;
		}
		if( !interieur  && !wagon.isLoco() && actionExcute.equals(Action.Descendre)) {
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
		if(!wagon.isLoco() && actionExcute.equals(Action.Recule)) {
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
		return this.name + pos + " avec " +super.toString();
	}
	
	
	
	public void braquer() {
		if(wagon.isEmpty()) return;
		this.addButin(wagon.popButin());
	}
	
	
}

