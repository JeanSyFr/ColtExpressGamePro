package modele;

import java.util.Random;


public abstract class Butin {
	// **************************************************
    // Fields
    // **************************************************
	protected int valeur;
	protected String nom;
	protected Train.Wagon wagon;
	
	// **************************************************
    // Constructors
    // **************************************************
	/**
	 * 
	 * @param wagon the wagon where to place this Butin
	 */
	public Butin(Train.Wagon wagon) {
		this.wagon = wagon;
		//this.wagon.addButin(this);
	}
	
	// **************************************************
    // Getters
    // **************************************************
	public int getValeur(){
		return this.valeur;
	}
	public String getNom() {
		return nom;
	}
	
	
	@Override
	public String toString() {
		return nom+ "("+valeur+")$";
	}
	
}

class Bourse extends Butin {
	public Bourse(Train.Wagon wagon){
		super(wagon);
		this.nom ="Bourse";
    	Random rnd = new Random();
    	this.valeur = rnd.nextInt(500);
	}
}

class Bijou extends Butin {
	public Bijou(Train.Wagon wagon){
		super(wagon);
		this.nom ="Bijou";
		this.valeur = 500;
	}
}

class Magot extends Butin {
	public Magot(Train.Wagon wagon){
		super(wagon);
		this.nom = "Magot";
		this.valeur = 1000;
	}
}


