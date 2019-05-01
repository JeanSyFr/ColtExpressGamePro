package modele;

import java.util.Random;

abstract class Butin {
	protected int valeur;
	protected String nom;
	protected ContainerStack cs;
}

class Bourse extends Butin {
	public Bourse(ContainerStack pos){
		this.nom ="Bourse";
		this.cs = pos;
    	Random rnd = new Random();
    	this.valeur = rnd.nextInt(501);
	}
}

class Bijou extends Butin {
	public Bijou(ContainerStack pos){
		this.nom ="Bijou";
		this.valeur = 500;
		this.cs = pos;
	}
}

class Magot extends Butin {
	public Magot(ContainerStack pos){
		this.nom = "Magot";
		this.valeur = 1000;
		this.cs = pos;
	}
}