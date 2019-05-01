package modele;

import java.util.Stack;

public abstract class ContainerStack {
	private Stack<Butin> butins;
	private final int  MAX_CAPACITY = 4;
	private int n ;
	ContainerStack(){
		butins = new Stack<Butin>();
		n= 0;
		
	}
	public void pushButin(Butin b ) {
		if(n>= this.MAX_CAPACITY) return;
		butins.push(b);
		n++;
	}
	public Butin popButin() {
		if(butins.empty()) return null ;
		n--;
		return butins.pop();
		
	}
	public Butin peekButin() {
		return butins.peek();
	}
	public boolean isEmpty() {
		return butins.empty();
	}


}

