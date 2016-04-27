package TSHyperHeuristicNaiveAcceptance;

import java.util.Random;

public class AcceptanceCriteria {
	private Random rng;
	
	public AcceptanceCriteria(Random r) {
		rng = r;
	}//end constructor
	
	public boolean accept(double delta) {
	
		return Accept5050(delta);
			
	}//end method accept
	
	private boolean Accept5050(double delta) {
		if (delta > 0) {
			return true;
		} else if (rng.nextBoolean()) {
			return true;
		} else {
			return false;
		}
	}
}//end class
