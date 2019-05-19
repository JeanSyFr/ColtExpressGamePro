package modele;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testTrain {


	Train t;
	@Before
	public void setUp() {
		t = new Train();
	}
	
	@Test
	public void testVariants() {
		assert t.checkInvariants() : "Test of variants failed";
	}
	@Test
	public void testInitialPlace() {
		assert ForEachPredicat.forEach(t.getBandit(), b -> t.getLastWagon().bandits.contains(b)) : "The initial place of bandits is flase";
	}
	@Test
	public void testDLL() {
		boolean out;
		out = t.locomotive.precedent ==null;
		out &= t.locomotive.suivant.precedent == t.locomotive;
		out &= t.getLastWagon().suivant ==null;
		out &= t.getLastWagon() == t.getLastWagon().precedent.suivant;
		
		assert ForEachPredicat.forEach (
				t , w -> 
		{ //implementing the predicat function
			if(w==t.locomotive || w==t.getLastWagon()) {
				return true; //beacause we already tested it
			}else {
				return w ==w.suivant.precedent && w==w.precedent.suivant;
			}
		}	
				
				) : "";			
	}
	
	
	
	@After
	public void tearDown() {
		t = null;
	}
}
