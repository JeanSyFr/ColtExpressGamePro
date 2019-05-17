package modele;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;


public class Possesseur {
	
	private HashSet<Butin> butins;
	private final int MAX_CAPACITY;
	private int n ;
	
	Possesseur(int maxN){
		butins = new HashSet<Butin>();
		n= 0;
		MAX_CAPACITY = maxN;
	}
	
	// renvoit true si e butin a bien été ajouté
	public boolean addButin(Butin b ) {
		if(n>= this.MAX_CAPACITY) return false;
		butins.add(b);
		n++;
		return true;
	}
	
	public Butin popButin() {
		if(butins.isEmpty()) return null ;
		n--;
		
		// On enlève un buin au hasard
		int size = butins.size();
		int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
		int i = 0;
		Butin enleve = null;
		for(Butin butin : butins) {
		    if (i == item) {
		    	enleve = butin;
		        butins.remove(enleve);
		    i++;
		    }
		}

		return enleve;	
	}
	
	/*
	public Butin peekButin() {
		return getButins().peek();
	}
	*/
	
	public boolean isEmpty() {
		return butins.isEmpty();
	}
	
	public HashSet<Butin> getButins() {
		return butins;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
