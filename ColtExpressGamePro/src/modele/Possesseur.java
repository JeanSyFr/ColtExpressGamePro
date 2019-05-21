package modele;


import java.util.HashSet;
import java.util.Random;


public abstract class Possesseur {
	
	// **************************************************
    // Fields
    // **************************************************
	private HashSet<Butin> butins;
	private final int MAX_CAPACITY;
	private int n ;
	
	
	// **************************************************
    // Constructors
    // **************************************************
	/**
	 * 
	 * @param maxN nombre de butin mximum
	 */
	Possesseur(int maxN){
		butins = new HashSet<Butin>();
		n= 0;
		MAX_CAPACITY = maxN;
	}
	
	public HashSet<Butin> getButins() {
		return butins;
	}
	
	
	// renvoit true si e butin a bien été ajouté
	public boolean addButin(Butin b ) {
		if(n>= this.MAX_CAPACITY) return false;
		butins.add(b);
		n++;
		return true;
	}
	
	
	// On enlève un buin au hasard
	public Butin popButin() {
		if(butins.isEmpty()) return null ;
		n--;
		int size = butins.size();
		int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
		int i = 0;
		Butin enleve = null;
		for(Butin butin : butins) {
		    if (i == item) {
		    	enleve = butin;
		    }
		    i++;
		}
		butins.remove(enleve);
		return enleve;	
	}
	
	public boolean isEmpty() {
		return butins.isEmpty();
	}
	
	
	
	public String toString() {
		String out = "Butins : ";
		for(Butin b : butins) out += " "+b;
		return out;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
