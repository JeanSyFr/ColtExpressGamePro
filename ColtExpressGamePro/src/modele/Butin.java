package modele;

import java.util.Random;

public abstract class Butin {
	protected int valeur;
	protected String nom;
	protected Possesseur possesseur;
	
	public String getNom() {
		return this.nom;
	}
}

class Bourse extends Butin {
	public Bourse(Possesseur pos){
		this.nom ="Bourse";
		this.possesseur = pos;
    	Random rnd = new Random();
    	this.valeur = rnd.nextInt(501);
	}
}

class Bijou extends Butin {
	public Bijou(Possesseur pos){
		this.nom ="Bijou";
		this.valeur = 500;
		this.possesseur = pos;
	}
}

class Magot extends Butin {
	public Magot(Possesseur pos){
		this.nom = "Magot";
		this.valeur = 1000;
		this.possesseur = pos;
	}
}