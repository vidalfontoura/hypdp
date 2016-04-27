package TSHyperHeuristic;

import java.util.Random;

public class AcceptanceCriteria {
	
	public static final int SIM_ANNEAL = 0;
	public static final int GREAT_DELUGE = 1;
	public static final int ACCEPT_ALL = 2;
	public static final int NON_WORSENING = 3;
	public static final int ONLY_IMPROVING = 4;
	public static final int ACCEPT_NON_WORSENING_5050 = 5;
	
	private double largestDeltaSoFar = 0;
	private int criteria;
	private Random rng;
	
	//for simulated annealing
	private double temperature;
	private double beta;
	
	public AcceptanceCriteria(int c, Random r, double i) {
		criteria = c;
		rng = r;
		beta = 0.01;
		temperature = 1;
	}//end constructor
	
	public boolean accept(double delta) {
		switch (criteria) {
			case SIM_ANNEAL:
				return SimulatedAnnealing(delta);
			case GREAT_DELUGE:
				return GreatDeluge(delta);
			case ACCEPT_ALL:
				return AcceptAll(delta);
			case NON_WORSENING:
				return AcceptNonWorsening(delta);
			case ONLY_IMPROVING:
				return AcceptOnlyIncrease(delta);
			case ACCEPT_NON_WORSENING_5050:
				return Accept5050(delta);
			default:
				return true;
		}//end switch
	}//end method accept
	
	private boolean SimulatedAnnealing(double delta) {
		
		temperature = (temperature/(1.0+(beta*temperature)));
		if (delta > 0) {
			return true;
		} else {
			System.out.println("delta " + delta);
			if (delta < largestDeltaSoFar) {
				//less than, because delta will become more negative the worse the solution gets
				largestDeltaSoFar = delta;
			}
			delta /= largestDeltaSoFar;
			System.out.println("normalised delta " + delta);
			
			double accept = Math.exp(delta / this.temperature);
			System.out.print(this.temperature + " accept " + accept);
			double rand = rng.nextDouble();
			if (rand < accept) {
				System.out.println("  accepted");
				return true;
			} else {
				System.out.println("  denied");
				return false;
			}
		}
	}//end method SimulatedAnnealing
	
	private boolean GreatDeluge(double delta) {
		return true;
	}
	
	private boolean AcceptAll(double delta) {
		return true;
	}
	
	private boolean AcceptNonWorsening(double delta) {
		if (delta < 0) {
			return false;
		} else {
			return true;
		}
	}//end method AcceptNonWorsening
	
	private boolean AcceptOnlyIncrease(double delta) {
		if (delta > 0) {
			return true;
		} else {
			return false;
		}
	}
	
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
