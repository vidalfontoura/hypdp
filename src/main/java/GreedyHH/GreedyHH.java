package GreedyHH;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/*
 * This hyperheuristic tries all of the low level heuristics and chooses the one which
 * causes the most improvement, even if the improvement is negative
 */

public class GreedyHH extends HyperHeuristic {
	
	public GreedyHH(long r) {
		super(r); 
	}
	
	public void solve(ProblemDomain problem) {
		int hs = problem.getNumberOfHeuristics();
		//int hs = 1;
		int[] hstats = new int[hs];
		double obj = Double.MAX_VALUE;
		problem.initialiseSolution(0);
		problem.setMemorySize(hs+1);
		while (!hasTimeExpired()) {
			//System.out.println();
			//System.out.println(" begin search ");
			double bestobjfunction = Double.MAX_VALUE;
			int bestsolution = 0;
			for (int x = 0; x < hs; x++) {
				if (hasTimeExpired()) {
					break;
				}
				//System.out.print(problem.getFunctionValue(0));
				double newobjfunction = problem.applyHeuristic(x, 0, x+1);
				//System.out.println(" " + newobjfunction);
				if (newobjfunction < bestobjfunction) {
					bestobjfunction = newobjfunction;
					bestsolution = x+1;
				}
			}
			hstats[bestsolution-1]++;
			//System.out.println(bestobjfunction + " " + (bestsolution-1));
			//accept the result of the best heuristic
			problem.copySolution(bestsolution, 0);
			obj = bestobjfunction;
		}
		
	}

	public String toString() {
		return "Greedy Selection, Accept All";
	}

}
