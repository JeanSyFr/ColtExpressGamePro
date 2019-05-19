package modele;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class MyQueue <T> {
	
	
	private Block first;
	private int size = 0; 
	
	public MyQueue() {
		
	}
	T top() {
		return first.elt;
	}
	void add(T) {
		Block b = new Block 
	}
	
	
	
	class Block{
		T elt;
		Block next, prev;
		Block(T e){
			elt =e;
		}
	}

	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
