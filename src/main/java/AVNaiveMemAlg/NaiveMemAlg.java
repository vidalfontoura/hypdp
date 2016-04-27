package AVNaiveMemAlg;



import java.util.PriorityQueue;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;


/**
 * 
 * Class implementing a Naive Memetic Hyperheuristic
 * 
 * Step 1. Create initial population 
 * Step 2. Iterate until time is consumed
 * - Choose two parents using binary tournament sampling 
 * - Recombine the parents
 * and create a new solution 
 * - With a certain probability, mutProb, modify the
 * new solution using mutation 
 * - Improve the new solution with local search or
 * ruin and recreate (choose one with a 50% chance).
 * - Replace the worst of the two parents with the new 
 * individual iff the new individual is better.
 * 
 * Default parameters 
 * population size: 10 
 * mutProb : 0.1
 * 
 * 
 * @author Antonio
 * 
 */
public class NaiveMemAlg extends HyperHeuristic {

	private int popSize = 10;
	private PriorityQueue<Solution> sortedSolutions;
	private int indexNewSolution = 10;
	private int[] XOverHeuristics;
	private int[] RRHeuristics;
	private int[] MutationHeuristics;
	private int[] LSearchHeuristics;
	private double mutProb = 0.1;

	public NaiveMemAlg(long seed) {
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {

		problem.setMemorySize(popSize + 1);
		indexNewSolution = popSize;

		XOverHeuristics = problem.getHeuristicsOfType(HeuristicType.CROSSOVER);
		RRHeuristics = problem.getHeuristicsOfType(HeuristicType.RUIN_RECREATE);
		MutationHeuristics = problem
				.getHeuristicsOfType(HeuristicType.MUTATION);
		LSearchHeuristics = problem
				.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);

		for (int i = 0; i < popSize; i++) {
			problem.initialiseSolution(i);
		}

		this.sortedSolutions = new PriorityQueue<Solution>(popSize);
		for (int i = 0; i < popSize; i++) {
			sortedSolutions.add(new Solution(i, problem.getFunctionValue(i)));
		}

		int success = 0;
		double Xo, X1, X2;

		int p1, p2;

		while (!hasTimeExpired()) {
			success = 0;

			/* Select two different parents for recombination */
			p1 = this.tournamentSampling();
			while ((p2 = tournamentSampling()) == p1)
				;

			Xo = problem.getFunctionValue(p1);
			X1 = problem.getFunctionValue(p2);

			/* Use XOver */
			X2 = problem.applyHeuristic(XOverHeuristics[rng
					.nextInt(XOverHeuristics.length)], p1, p2,
					this.indexNewSolution);

			/* Use mutation */
			if (rng.nextDouble() < mutProb)
				X2 = problem.applyHeuristic(MutationHeuristics[rng
						.nextInt(MutationHeuristics.length)],
						this.indexNewSolution, this.indexNewSolution);

			/* Use either local search or iterated greedy */
			if (rng.nextDouble() < 0.5)
				X2 = problem.applyHeuristic(LSearchHeuristics[rng
						.nextInt(LSearchHeuristics.length)], indexNewSolution,
						indexNewSolution);
			else
				X2 = problem.applyHeuristic(RRHeuristics[rng
						.nextInt(RRHeuristics.length)], indexNewSolution,
						indexNewSolution);

			/*
			 * new individual replaces the worst of the parents iff new
			 * individual is equal or better
			 */
			if (X2 <= Math.max(Xo, X1)) {
				if (Xo <= X1)
					problem.copySolution(indexNewSolution, p2);
				else
					problem.copySolution(indexNewSolution, p1);
				success = 1;
			}

		}
	}

	private int tournamentSampling() {
		int p1 = rng.nextInt(this.popSize);
		int p2;
		while ((p2 = rng.nextInt(this.popSize)) == p1)
			;
		return p1 <= p2 ? p1 : p2;
	}

	private class Solution implements Comparable<Solution> {
		double functionValue;

		Solution(int memoryRegister, double functionValue) {
			this.functionValue = functionValue;
		}

		public int compareTo(Solution o) {
			if ((this.functionValue - o.functionValue) >= 0)
				return -1;
			return 1;
		}
	}

	@Override
	public String toString() {
		return "AV naive Memetic Algorithm ";
	}
}
