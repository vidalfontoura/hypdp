package TSHyperHeuristic;


import java.util.Vector;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;


/**
 *
 * @author mvh
 */
public class TSHyperHeuristic extends HyperHeuristic {
	AcceptanceCriteria acceptanceCriteria;

	public TSHyperHeuristic(long seed) {
		super(seed);
	}//end constructor

	public void solve(ProblemDomain problem) {
		int numberOfHeuristics = problem.getNumberOfHeuristics();
		problem.initialiseSolution(0);
		double obj = problem.getFunctionValue(0);
		acceptanceCriteria = new AcceptanceCriteria(AcceptanceCriteria.ACCEPT_ALL,rng,obj);
		problem.setMemorySize(2);
		double alpha = 1;
		int[] xovers = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
		int numberofxovers = 0;
		if (xovers != null) {
			numberofxovers = xovers.length;
		}
		LowLevelHeuristic[] heuristics = new LowLevelHeuristic[numberOfHeuristics-numberofxovers];
		//create the llh objects, but do not include the xovers
		int count = 0;
		for (int x = 0; x < numberOfHeuristics; x++) {
			boolean inxovers = false;
			if (xovers != null) {
				for (int xover : xovers) {
					if (xover == x) {
						inxovers = true;
						break;
					}
				}
			}
			if (!inxovers) {
				heuristics[count] = new LowLevelHeuristic(x);
				count++;
			}
		}
		//		for (int x = 0; x < numberOfHeuristics; x++) {//initialise them
		//			heuristics[x] = new LowLevelHeuristic(x);}
		Vector<LowLevelHeuristic> tabulist = new Vector<LowLevelHeuristic>();
		double maxrank = heuristics.length;
		double minrank = 0;
		int maxtabulength = heuristics.length-1;
		double bestObjSoFar = Double.MAX_VALUE;
		boolean reachedpoint = false;
		while (!hasTimeExpired()) {
			Vector<LowLevelHeuristic> heuristicsWithHighestRank = new Vector<LowLevelHeuristic>();
			//choose the highest ranked non tabu heuristic
			double bestRank = -1;
			//System.out.println("new search");
			for (int x = 0; x < heuristics.length; x++) {
				//System.out.println(heuristics[x].number + "=" + heuristics[x].rank + " " + heuristics[x].tabu);
				if (!heuristics[x].tabu) {//if its not tabu
					if (heuristics[x].rank > bestRank) {//if its got the best rank so far
						heuristicsWithHighestRank.removeAllElements();
						heuristicsWithHighestRank.add(heuristics[x]);
						bestRank = heuristics[x].rank;
					} else if (heuristics[x].rank == bestRank) {
						heuristicsWithHighestRank.add(heuristics[x]);
					}
				}//end if
			}//end for searching for the best heuristic rank
			int i = rng.nextInt(heuristicsWithHighestRank.size());
			LowLevelHeuristic bestHeuristic = heuristicsWithHighestRank.get(i);
			//System.out.println("chosen " + bestHeuristic.number);
			//System.out.println("r2");
			double newobjfunction = problem.applyHeuristic(bestHeuristic.number, 0, 1);
			//System.out.println("r3");
			double delta = obj - newobjfunction;
			if (newobjfunction < bestObjSoFar) {
				bestObjSoFar = newobjfunction;
			}
			//System.out.println(obj + " --> " + newobjfunction);
			if (delta > 0) {//if there is an increase in the objective function
				//System.out.println("increase");
				bestHeuristic.increaseRank(alpha, maxrank);
			} else {//there is no change or decrease in the obj function
				//System.out.println("decrease");
				if (!reachedpoint) {//if first time we have hit a decrease
					reachedpoint = true;
					//clear the ranks, this is because we essentially want to start again when
					//we hit the difficult part of the search
					for (LowLevelHeuristic llh : heuristics) {
						llh.rank = 0;
					}
				}

				if (delta < 0) {//empty tabu list if decrease
					bestHeuristic.decreaseRank(alpha, minrank);
					for (int x = 0; x < tabulist.size(); x++) {//empty tabu list
						(tabulist.get(x)).tabu = false;
					}//end looping to empty tabu list
					tabulist.removeAllElements();
				} else {//there has been no change, so release the tabu heuristic thats been in the list the longest
					if (!tabulist.isEmpty()) {
						//it's +1 because the current one will be added after, so we simulate that
						if ((tabulist.size()+1) > maxtabulength) {
							//System.out.println("empty");
							(tabulist.get(0)).tabu = false;
							tabulist.remove(0);
						}
					}
				}//end if else there is a decrease or its the same

				bestHeuristic.tabu = true;//add heuristic to tabu list
				tabulist.add(bestHeuristic);
			}//end if else there was an increase or decrease
			//System.out.println("r87");
			//accept or not
			int targetMemoryIndex = 0;
			if (acceptanceCriteria.accept(delta)) {
				problem.copySolution(1, targetMemoryIndex);
				obj = newobjfunction;
			}

		}//end while loop for time
	}//end solve method

	class LowLevelHeuristic {
		boolean tabu;
		double rank;
		int number;

		public void increaseRank(double alpha, double maxrank) {
			if (rank != maxrank) {//if its not going to go under the min rank
				rank += alpha;
			}
		}

		public void decreaseRank(double alpha, double minrank) {
			if (rank != minrank) {//if its not going to go under the min rank
				rank -= alpha;
			}
		}

		public LowLevelHeuristic(int n) {
			tabu = false;
			rank = 0;
			number = n;
		}
	}

	public String toString() {
		return "Tabu Search";
	}

}//end class
