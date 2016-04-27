package NaiveHH;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

public class NaiveHH extends HyperHeuristic {
	
	public NaiveHH(long r) {
		super(r); 
	}
	
	public void solve(ProblemDomain problem) {
		int hs = problem.getNumberOfHeuristics();
		double obj = Double.POSITIVE_INFINITY;
		problem.initialiseSolution(0);
		while (!hasTimeExpired()) {
			int v = rng.nextInt(hs);
			//System.out.println(getElapsedTime() + " " + v);
			double newobjfunction = problem.applyHeuristic(v, 0, 1);
			double delta = obj - newobjfunction;
			if (delta > 0) {
				problem.copySolution(1, 0);
				obj = newobjfunction;
			} else {
				if (rng.nextBoolean()) {
					problem.copySolution(1, 0);
					obj = newobjfunction;
				}
			}
		}
	}

	public String toString() {
		return "Naive ";
	}

}
