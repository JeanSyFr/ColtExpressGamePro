package modele;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testTrain extends Train {

	Train t;
	int count;

	@Before
	public void setUp() {
		t = new Train();
		count = 0;

	}

	@Test
	public void testVariants() {
		assert t.checkInvariants() : "Test of variants failed";
	}

	@Test
	public void testInitialPlace() {
		assert ForEachPredicat.forEach(t.getBandits(),
				b -> t.getLastWagon().bandits.contains(b)) : "The initial place of bandits is flase";
	}

	@Test
	public void testDLL() {
		boolean out;
		out = t.locomotive.precedent == null;
		out &= t.locomotive.suivant.precedent == t.locomotive;
		out &= t.getLastWagon().suivant == null;
		out &= t.getLastWagon() == t.getLastWagon().precedent.suivant;

		assert ForEachPredicat.forEach(t, w -> { // implementing the predicat function
			if (w == t.locomotive || w == t.getLastWagon()) {
				return true; // beacause we already tested it
			} else {
				return w == w.suivant.precedent && w == w.precedent.suivant;
			}
		}

		) : "Test DLL failed 'suivant.prencedent == precedent.suivant'";
	}

	@Test
	public void testDeroulement() {
		t.actionsPreDefini();
		for (int i = 0; i < 5; i++) {
			t.excuteTour();
			assert t.checkInvariants() : "testDeroulement failed in tour " + i;
		}

	}

	@Test
	public void testMarshal() {
		ForEach.forEach(t, w-> {
			if(w.getMarshall()) {
				count++;
			}
		});
		
		assert count==1 : "testMarshal has failed";

	}

	@After
	public void tearDown() {
		t = null;
		count = 0;
	}
}
