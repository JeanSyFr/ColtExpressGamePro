package modele;

import modele.Train.Wagon;

public class Marshall extends Personne{

	// **************************************************
    // Constructors
    // **************************************************
    /**
    * Parameterized constructor.
    * 
    * @param t the model
    * 
    * 
    * @see Personne:Personne(Train, String)
    * 
    * */
	public Marshall(Train t) {
		super(t, "Marshall");
	}
	
	
	/**
	 * @see Personne::mettrePersonneBonWagon(Train, Personne)
	 * 
	 * This function will call the responsable function to put this bundit in the last wagon
	 */
	@Override
	protected Wagon mettrePersonneBonWagon(Train t, Personne p) {
		return t.marshaLocomotive(this);

	}
	/**
	 * This function is in charge of execute the right action in the right order
	 * 
	 *  @see Personne::executeAction()
	 */
	@Override
	public void executeAction() {
		//si cette action est nulle rien va etre executer
		Action actionExcute = actions.actionToExecute();
		if(actionExcute ==(null)) return; 
		if(actionExcute.equals(Action.Tirer)) {
			this.tirer();
		}
		if(!wagon.isLastWagon() && actionExcute.equals(Action.Avance)) {
			Train.Wagon newWagon = wagon.avanceMarshall();
			if(newWagon == null) System.err.println("OUUPS CHECK1 executeAction  Marshall");
			System.out.println(name+" avance vers la fin de train");
			wagon =  newWagon;
			System.out.println(wagon);
			return;
		}
		if(!wagon.isLoco() && actionExcute.equals(Action.Recule)) {
			Train.Wagon newWagon =wagon.reculeMarshall();
			if(newWagon == null) System.err.println("OUUPS CHECK 2 executeAction  Marshall");
			System.out.println(name+" recule vers le debut de train");
			wagon =  newWagon;
			System.out.println(wagon);
			return;
		}
		System.out.println(name+ " will do nothing now!");
	}


}
