package modele;

import java.util.Random;


abstract class Butin {
	protected int valeur;
	protected String nom;
	protected Train.Wagon wagon;
	
	public Butin(Train.Wagon wagon) {
		this.wagon = wagon;
		//this.wagon.addButin(this);
	}
	public String toString() {
		return nom+ "("+valeur+")$";
	}
}

class Bourse extends Butin {
	public Bourse(Train.Wagon wagon){
		super(wagon);
		this.nom ="Bourse";
    	Random rnd = new Random();
    	this.valeur = rnd.nextInt(501);
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


