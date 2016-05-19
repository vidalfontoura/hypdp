/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package HyPDP;

import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import PDP.PDP;
import PDP.PDPSolution;

/**
 *
 *
 * @author vfontoura
 */
public class CFHH extends HyperHeuristic {

	private int memorySize;

	private double alpha;

	private double beta;

	private String choiceFunctionExpr;

	private Map<Integer, HeuristicStatistics> heuristicsInformation;

	private double bestFitness;

	private int[] localSearchHeuristics;
	private int[] mutationHeuristics;
	private int[] crossoverHeuristics;

	private long startTime;

	public CFHH(long seed, int memorySize, double alpha, double beta) {
		super(seed);
		this.memorySize = memorySize;
		this.alpha = alpha;
		this.beta = beta;
		this.choiceFunctionExpr = "(alpha * %s) + (beta * %s)".replace("alpha", String.valueOf(alpha)).replace("beta",
				String.valueOf(beta));

		this.bestFitness = Double.MAX_VALUE;

	}

	@Override
	protected void solve(ProblemDomain problem) {

		this.setHeuristicTypes(problem);

		// Initialization of the memory mechanism
		initializeMemoryMechanism(problem);
		for (int i = 0; i < memorySize; i++) {
			PDP p = (PDP) problem;
			System.out.print(p.getMemoryMechanism()[i].getFitness() + " - ");
			System.out.println(Arrays.toString(p.getMemoryMechanism()[i].getVariables()).replace(" ", ""));

		}

		initializeHeuristicsInformation(problem.getNumberOfHeuristics());

		startTime = System.currentTimeMillis();

		while (!hasTimeExpired()) {

			int heuristicIndex = selectHeuristicToApply();

			double delta = 0;
			if (isCrossover(heuristicIndex)) {
				// Selects a random solution from the memory
				int solutionIndex1 = this.rng.nextInt(this.memorySize - 2);

				double currentFitness = problem.getFunctionValue(solutionIndex1);

				int solutionIndex2 = solutionIndex1;
				do {
					solutionIndex2 = this.rng.nextInt(this.memorySize - 2);
				} while (solutionIndex1 == solutionIndex2);

				double newFunctionValue = problem.applyHeuristic(heuristicIndex, solutionIndex1, solutionIndex2,
						this.memorySize - 1);

				delta = currentFitness - newFunctionValue;

				if (bestFitness > newFunctionValue) {
					bestFitness = newFunctionValue;
				}

			} else {
				int solutionIndex1 = this.rng.nextInt(this.memorySize - 2);

				double currentFitness = problem.getFunctionValue(solutionIndex1);
				double newFunctionValue = problem.applyHeuristic(heuristicIndex, solutionIndex1, this.memorySize - 1);

				delta = currentFitness - newFunctionValue;

				if (bestFitness > newFunctionValue) {
					bestFitness = newFunctionValue;
				}
			}

			if (delta > 0) {
				// If delta greater than zero it was able to obtain a
				// improvement it should replace a solution from memory
				double worstSolutionValue = Double.MIN_NORMAL;
				int worstSolutionIndex = -1;

				PDPSolution[] solutions = ((PDP) problem).getMemoryMechanism();

				for (int i = 0; i < solutions.length; i++) {
					Double fitness = solutions[i].getFitness();
					if (fitness > worstSolutionValue) {
						worstSolutionValue = fitness;
						worstSolutionIndex = i;
					}
				}
				problem.copySolution(this.memorySize - 1, worstSolutionIndex);

			} else {
				if (this.rng.nextBoolean()) {
					double worstSolutionValue = Double.MIN_NORMAL;
					int worstSolutionIndex = -1;

					PDPSolution[] solutions = ((PDP) problem).getMemoryMechanism();

					for (int i = 0; i < solutions.length; i++) {
						Double fitness = solutions[i].getFitness();
						if (fitness > worstSolutionValue) {
							worstSolutionValue = fitness;
							worstSolutionIndex = i;
						}
					}
					problem.copySolution(this.memorySize - 1, worstSolutionIndex);
				}
			}

			updateHeuristicsInformation(heuristicIndex, delta);

		}

	}

	private void updateHeuristicsInformation(int appliedHeuristicIndex, double delta) {

		for (Integer key : heuristicsInformation.keySet()) {
			if (key == appliedHeuristicIndex) {
				HeuristicStatistics heuristicStatistics = heuristicsInformation.get(key);
				heuristicStatistics.setElapsedTime(0);
				heuristicStatistics.setRank(delta);
			} else {
				HeuristicStatistics heuristicStatistics = heuristicsInformation.get(key);
				heuristicStatistics.setElapsedTime(heuristicStatistics.getElapsedTime() + 1);
				heuristicStatistics.setRank(delta);
			}

		}

	}

	private void initializeHeuristicsInformation(int numberOfHeuristics) {
		heuristicsInformation = Maps.newHashMap();
		for (int i = 0; i < numberOfHeuristics; i++) {
			HeuristicStatistics heuristicsStatistics = new HeuristicStatistics();

			heuristicsStatistics.setHeuristicId(i);
			heuristicsStatistics.setRank(1);
			heuristicsStatistics.setElapsedTime(0);
			heuristicsInformation.put(i, heuristicsStatistics);
		}
	}

	public int selectHeuristicToApply() {

		int bestCFIndex = -1;
		double bestCFValue = Double.MIN_VALUE;

		for (Integer key : heuristicsInformation.keySet()) {
			HeuristicStatistics heuristicsStatistics = heuristicsInformation.get(key);

			double rank = heuristicsStatistics.getRank();
			int elapsedTime = heuristicsStatistics.getElapsedTime();

			String function = String.format(choiceFunctionExpr, rank, elapsedTime);
			double choiceFunctionValue = ExpressionExecutor.calculate(function);

			if (choiceFunctionValue > bestCFValue) {
				bestCFIndex = key;
				bestCFValue = choiceFunctionValue;
			}
		}
		return bestCFIndex;

	}

	private void setHeuristicTypes(ProblemDomain problem) {
		localSearchHeuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH);
		mutationHeuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
		crossoverHeuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
	}

	private boolean isCrossover(int heurIndex) {
		boolean isCrossover = false;
		for (int cr = 0; cr < crossoverHeuristics.length; cr++) {
			if (crossoverHeuristics[cr] == heurIndex) {
				isCrossover = true;
				break;
			}
		}
		return isCrossover;
	}

	private void initializeMemoryMechanism(ProblemDomain problem) {

		for (int i = 0; i < memorySize; i++) {
			problem.initialiseSolution(i);
		}
	}

	@Override
	public String toString() {
		return "CustomHyperHeuristic";
	}

	public static void main(String[] args) {

		int memorySize = 10;
		long seed = 10l;
		long timeLimit = 60000;

		CFHH cfhh = new CFHH(seed, memorySize, 1.0, 0.0005);
		PDP problem = new PDP(seed);
		problem.loadInstance(1);

		cfhh.loadProblemDomain(problem);
		cfhh.setTimeLimit(timeLimit);
		cfhh.run();

	}

}
