package modele;


import modele.Train.Wagon;

/*
 * Le class qui represente le bandit
 * 
 */
public class Bandit extends Personne
{
	private boolean interieur; //pour savoir s'il est sur le toit ou dans le wagon
	private  int bulits = 6;
	//private int bultisCount = 6;

	/*
	 * Creer le bandit sur le toit de dernier wagon
	 */
	public Bandit(Train t, String name, boolean j){
		super(t,name);
		interieur = false; // au debut il est sur le toit
	}
	public Bandit(Train t, String name) {
		this(t,name,false);
	}

	@Override
	public Wagon mettrePersonneBonWagon(Train t, Personne p) {
		return t.banditLastWagon(this);
	}

	// getter pour les autres classes
	public boolean getInterieur() { 
		return this.interieur;
	}
	public int getBullets() {
		return this.bulits;
	}
	/*public int getMoney() {
		int out = 0;
		for()
	}*/
	public void addAction(Action a) {
		super.addAction(a);
		if(a==Action.Tirer)	bulits--;
		super.train.notifyObservers();

	}

	@Override
	public void executeAction() {
		//si cette action est nulle rien va etre executer
		Action actionExcute = actions.actionToExecute();
		if(actionExcute ==(null)) return;
		//System.out.println("We are doing " + actionExcute);
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
	public void tirer() {
		Bandit b2 = wagon.anotherBanditThan(this);
		if(b2 == null) { 
			System.out.println(this.name + " has shot no body");
			return;
		}	
		if(!b2.isEmpty()) {
			Butin out = b2.popButin();
			System.out.println(name +" has shot "+b2.name+" who droped "+out + "in wagon ");
			b2.interieur =false;
			wagon.addButin(out);
		}
		else {
			System.out.println(name +" has shot "+b2.name+" who has nothing to drop");
		}
	}


	public String toString() {
		String pos = (this.interieur)? (" a l'interieur"):(" sur le toit") ;
		return this.name + pos + " avec " +super.toString();
	}



	public void braquer() {
		if(wagon.isEmpty()) return;
		this.addButin(wagon.popButin());
	}


}

