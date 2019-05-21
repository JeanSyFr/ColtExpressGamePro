package modele;


import modele.Train.Wagon;


public class Bandit extends Personne
{
	// **************************************************
    // Fields
    // **************************************************
	protected boolean interieur; //pour savoir s'il est sur le toit ou dans le wagon
	private  int bulits = 6;

	/*
	 * Creer le bandit sur le toit de dernier wagon
	 */
	
	

	// **************************************************
    // Constructors
    // **************************************************
	/**
	 * 
	 * @param t the model
	 * @param name the bandit name
	 * 
	 * @see Personne::Personne(Train, String)
	 */
	public Bandit(Train t, String name) {
		super(t,name);
	}
	
	// **************************************************
    // Getters
    // **************************************************
	public boolean getInterieur() { 
		return this.interieur;
	}
	public int getBullets() {
		return this.bulits;
	}

	/**
	 * @see Personne::mettrePersonneBonWagon(Train, Personne)
	 * 
	 * This function will call the responsable function to put this bundit in the last wagon
	 */
	@Override
	public Wagon mettrePersonneBonWagon(Train t, Personne p) {
		return t.banditLastWagon(this);
	}

	

	
	@Override
	public String toString() {
		String pos = (this.interieur)? (" a l'interieur"):(" sur le toit") ;
		return this.name + pos + " avec " +super.toString();
	}
	
	
	

	// **************************************************
    // Utilities functions for the game
    // **************************************************
	
	
	@Override
	public void addAction(Action a) {
		super.addAction(a);
		if(a==Action.Tirer)	bulits--;
		super.train.notifyObservers();

	}
	/**
	 * This function is in charge of execute the right action in the right order
	 * 
	 * @see Personne::executeAction()
	 */
	@Override
	public void executeAction() {
		Action actionExcute = actions.actionToExecute();
		if(actionExcute ==(null)) return; 	//si cette action est nulle rien va etre executer
		if(bulits>=0 && interieur && actionExcute.equals(Action.Tirer)) {
			this.tirer();
			return;
		}
		if(actionExcute.equals(Action.Braquer)) {
			if(interieur) {
				this.braquer();
				System.out.println(name+" a braquer");
			}else {
				System.out.println(name+" n'a pas braquer il est sur le toit");
			}
			return;

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
		System.out.println(name+ " tried to do "+actionExcute+" bu he couldnt");
		this.wagon.getTrain().notifyObservers();
	}
	
	

	// **************************************************
    // Private functions
    // **************************************************
	private void braquer() {
		if(wagon.isEmpty()) return;
		this.addButin(wagon.popButin());
	}


}

