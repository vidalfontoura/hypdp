/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package HyPDP;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import PDP.PDP;

/**
 *
 *
 * @author vfontoura
 */
public class CustomHH extends HyperHeuristic {

	private int memorySize;

	private String selectionFunction;

	private String acceptanceFunction;

	private Map<Integer, HeuristicSelectionInfo> heuristicsInformation;

	private Map<Integer, Double> heuristicsSelectionFunctionValue;

	private double bestFitness;

	private String bestSolution;

	private int[] localSearchHeuristics;
	private int[] mutationHeuristics;
	private int[] crossoverHeuristics;

	private long startTime;

	private int rcWindowSize;

	private int numberOfInteractions;

	public CustomHH(long seed, int memorySize, String selectionFunction, String acceptanceFunction, int rcWindowSize) {
		super(seed);
		this.memorySize = memorySize;
		this.selectionFunction = selectionFunction;

		this.bestFitness = Double.MAX_VALUE;
		this.rcWindowSize = rcWindowSize;

		this.heuristicsSelectionFunctionValue = Maps.newHashMap();
		this.numberOfInteractions = 0;
		this.acceptanceFunction = acceptanceFunction;

	}

	@Override
	protected void solve(ProblemDomain problem) {

		this.initializeMemoryMechanism(problem);

		printMemoryMechanism(problem);

		this.setHeuristicTypes(problem);

		initializeHeuristicsInformation(problem.getNumberOfHeuristics());

		startTime = System.currentTimeMillis();

		// Selects a random solution from the memory
		int solutionIndex1 = this.rng.nextInt(this.memorySize - 3);

		// Copy the selected for current solution for the current index (defined
		// by me 11)
		problem.copySolution(solutionIndex1, this.memorySize - 1);

		// Executing all heuristics first to populate the statistics information
		for (int i = 0; i < problem.getNumberOfHeuristics(); i++) {
			executeHeuristic(problem, i, this.memorySize - 1);
		}

		while (!hasTimeExpired()) {

			printHeuristicsStatistics();

			int heuristicIndex = selectHeuristicToApply(problem);

			executeHeuristic(problem, heuristicIndex, this.memorySize - 1);

			printMemoryMechanism(problem);

			numberOfInteractions++;

		}

		System.out.println("Best: " + bestFitness + " 	" + bestSolution.replaceAll(" ", ""));

	}

	/**
	 * @param problem
	 */
	private void executeHeuristic(ProblemDomain problem, int heuristicIndex, int currentIndex) {

		updateCr(heuristicIndex);

		double delta = 0;
		double currentFitness = 1.0;
		double newFitness = 1.0;

		int solutionIndex2 = -1;
		if (isCrossover(heuristicIndex)) {

			currentFitness = problem.getFunctionValue(currentIndex);

			solutionIndex2 = currentIndex;
			do {
				solutionIndex2 = this.rng.nextInt(this.memorySize - 3);
			} while (currentIndex == solutionIndex2);

			newFitness = problem.applyHeuristic(heuristicIndex, currentIndex, solutionIndex2, this.memorySize - 2);

			delta = currentFitness - newFitness;

			if (bestFitness > newFitness) {
				bestFitness = newFitness;
				bestSolution = problem.bestSolutionToString();
				updateCbest(heuristicIndex);
			}

		} else {

			currentFitness = problem.getFunctionValue(currentIndex);
			newFitness = problem.applyHeuristic(heuristicIndex, currentIndex, this.memorySize - 2);

			delta = currentFitness - newFitness;

			if (bestFitness > newFitness) {
				bestFitness = newFitness;
				bestSolution = problem.bestSolutionToString();
				updateCbest(heuristicIndex);
			}
		}

		// If delta > 0 means that the netFitness is better than
		// currentFitness
		if (delta > 0) {
			updateCcurrent(heuristicIndex);
			updateCava(heuristicIndex, delta);
			System.out.println("Replacing solution with " + problem.getFunctionValue(currentIndex) + " by "
					+ problem.getFunctionValue(this.memorySize - 2));

			problem.copySolution(this.memorySize - 2, currentIndex);

		} else if (delta == 0) {

			// Accepting equal solution, backuping current solution
			int backupIndex = this.rng.nextInt(this.memorySize - 3);
			System.out.println("Accepting equal solution backuping current solution to random index: " + backupIndex);
			problem.copySolution(currentIndex, backupIndex);

			problem.copySolution(this.memorySize - 2, currentIndex);

			// TODO: If delta < 0 means that the newFitness is not better
			// than
			// currentFitness. Check the move acceptance here using a naive
			// move acceptance right now
			// if (rng.nextBoolean()) {
			// // TODO:probably need to select better which solution it will
			// // replace
			// problem.copySolution(this.memorySize - 1, solutionIndex1);
			// updateCAccept(heuristicIndex);
			// }

			// TODO: Need to figure how to fill the total number of iteractions
			// TODO: Also need to figure how to replace the solution in the
			// memory mechanism
			// if (shouldAccept(delta, currentFitness, newFitness,
			// numberOfInteractions, 0)) {
			// System.out.println("Accepting solution worst than current: " +
			// currentFitness + ",new: " + newFitness);
			// int solutionToReplace = this.rng.nextInt(this.memorySize - 2);
			// problem.copySolution(1, solutionToReplace);
			// updateCAccept(heuristicIndex);
			// }

		}
		updateRC(heuristicIndex, currentFitness, newFitness);
		System.out.println("Interaction number: " + numberOfInteractions);

	}

	private void updateCr(int appliedHeuristicIndex) {
		HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(appliedHeuristicIndex);
		int cr = heuristicStatistics.getCr();
		cr++;
		heuristicStatistics.setCr(cr);
	}

	private void updateCava(int appliedHeuristicIndex, double delta) {

		HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(appliedHeuristicIndex);
		heuristicStatistics.addCava(delta);

	}

	private void updateCAccept(int appliedHeuristicIndex) {
		HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(appliedHeuristicIndex);
		int cAccept = heuristicStatistics.getCaccept();
		cAccept++;
		heuristicStatistics.setCaccept(cAccept);
	}

	private void updateCcurrent(int appliedHeuristicIndex) {
		HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(appliedHeuristicIndex);
		int cCurrent = heuristicStatistics.getCcurrent();
		cCurrent++;
		heuristicStatistics.setCcurrent(cCurrent);
	}

	private void updateCbest(int appliedHeuristicIndex) {
		HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(appliedHeuristicIndex);
		int cbest = heuristicStatistics.getCbest();
		cbest++;
		heuristicStatistics.setCbest(cbest);
	}

	private void updateRC(int appliedHeuristicIndex, double f1, double f2) {

		HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(appliedHeuristicIndex);

		// Calculating RC
		double delta = Math.abs(f1 - f2);
		if (f1 == 0.0) {
			f1 = -1.0;
		}
		double rc = Math.abs((delta / f1) * 100);
		heuristicStatistics.addRC(rc);

	}

	private void initializeHeuristicsInformation(int numberOfHeuristics) {
		heuristicsInformation = Maps.newHashMap();
		for (int i = 0; i < numberOfHeuristics; i++) {
			HeuristicSelectionInfo heuristicsStatistics = new HeuristicSelectionInfo(rcWindowSize);

			heuristicsStatistics.setHeuristicId(i);
			heuristicsStatistics.setCaccept(0);
			heuristicsStatistics.setCbest(0);
			heuristicsStatistics.setCcurrent(0);
			heuristicsStatistics.setCr(0);
			heuristicsInformation.put(i, heuristicsStatistics);
		}
	}

	public int selectHeuristicToApply(ProblemDomain problem) {

		SelectionInfo[] statistics = SelectionInfo.values();

		for (int i = 0; i < problem.getNumberOfHeuristics(); i++) {
			HeuristicSelectionInfo heuristicStatistics = heuristicsInformation.get(i);

			String resultingFunction = selectionFunction;
			for (SelectionInfo statistic : statistics) {

				double value = 0.0;
				switch (statistic) {
				case RC:
					value = heuristicStatistics.getRCMaxValue();
					break;
				case Caccept:
					value = heuristicStatistics.getCaccept();
					break;
				case Cava:
					value = heuristicStatistics.getCava();
					break;
				case Cbest:
					value = heuristicStatistics.getCbest();
					break;
				case Ccurrent:
					value = heuristicStatistics.getCcurrent();
					break;
				case Cr:
					value = heuristicStatistics.getCr();
					break;
				default:
					throw new RuntimeException("Unrecognized heuristic statistic: " + statistic);
				}
				resultingFunction = resultingFunction.replace(statistic.name(), String.valueOf(value));
			}

			double functionValue = ExpressionExecutor.calculate(resultingFunction);

			heuristicsSelectionFunctionValue.put(i, functionValue);

		}

		double max = Collections.max(heuristicsSelectionFunctionValue.values());
		printSelectionFunctionValues();
		Entry<Integer, Double> maxEntry = null;

		for (Entry<Integer, Double> entry : heuristicsSelectionFunctionValue.entrySet()) {
			double value = entry.getValue();

			if (max == value) {
				maxEntry = entry;
			}
		}
		Integer key = maxEntry.getKey();
		System.out.println();
		System.out.println("Selection heuristic index=" + key);
		return key;

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

		problem.setMemorySize(memorySize);
		for (int i = 0; i < memorySize; i++) {
			problem.initialiseSolution(i);
		}
	}

	private boolean shouldAccept(double delta, double previousFitness, double currentFitness, double currentIteration,
			double totalNumberOfIteraction) {

		String acceptanceCriterion = acceptanceFunction;

		acceptanceCriterion = acceptanceCriterion.replace("Delta", String.valueOf(delta))
				.replace("PF", String.valueOf(previousFitness)).replace("CF", String.valueOf(currentFitness))
				.replace("CI", String.valueOf(currentIteration)).replace("TI", String.valueOf(totalNumberOfIteraction));

		double calculate = ExpressionExecutor.calculate(acceptanceCriterion);
		double result = Math.exp(calculate);

		System.out.println("Acceptance result " + result);
		if (result <= 0.5) {
			return true;
		}
		return false;

	}

	@Override
	public String toString() {
		return "CustomHH";
	}

	public void printHeuristicsStatistics() {
		System.out.println();
		System.out.println("Printing heuristics status");
		for (Entry<Integer, HeuristicSelectionInfo> entry : heuristicsInformation.entrySet()) {
			System.out.println(entry.getKey() + ",rc=" + entry.getValue().getRCMaxValue() + ",caccept="
					+ entry.getValue().getCaccept() + ",cava=" + entry.getValue().getCava() + ",cbest="
					+ entry.getValue().getCbest() + ",ccurrent=" + entry.getValue().getCcurrent() + ",cr="
					+ entry.getValue().getCr());
		}
	}

	public void printSelectionFunctionValues() {
		System.out.println();
		System.out.println("Priting selection function results");
		for (Entry<Integer, Double> entry : heuristicsSelectionFunctionValue.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}

	}

	public void printMemoryMechanism(ProblemDomain problem) {
		for (int i = 0; i < memorySize; i++) {
			PDP p = (PDP) problem;
			System.out.print(p.getMemoryMechanism()[i].getFitness() + " - ");
			System.out.println(Arrays.toString(p.getMemoryMechanism()[i].getVariables()).replace(" ", ""));

		}
	}

	public String getBestSolution() {
		return bestSolution;
	}

	public double getBestFitness() {
		return bestFitness;
	}

	public static void main(String[] args) {

		long seed = 10l;
		long timeLimit = 60000;
		int instance = 4;
		if (args != null && args.length >= 3) {
			seed = Long.valueOf(args[0]);
			instance = Integer.valueOf(args[1]);
			timeLimit = Long.valueOf(args[2]);

		}

		int memorySize = 12;

		String selectionFunction = "(1 *RC) - (2 * Cr)";
		String acceptanceFunction = "(Delta + PF) - CI";
		int rcWindowSize = 10;

		List<FitSol> bestSolutions = Lists.newArrayList();

		CustomHH cfhh = new CustomHH(seed, memorySize, selectionFunction, acceptanceFunction, rcWindowSize);
		PDP problem = new PDP(seed);
		problem.loadInstance(instance);

		cfhh.setTimeLimit(timeLimit);
		cfhh.loadProblemDomain(problem);
		cfhh.run();

		FitSol fitSol = new FitSol(cfhh.getBestSolution(), cfhh.getBestFitness());

		bestSolutions.add(fitSol);

		bestSolutions.stream().forEach(b -> System.out.println(b.getFitness() + ":" + b.getSolution()));

	}

	static class FitSol {
		private String solution;
		private double fitness;

		public FitSol(String solution, double fitness) {
			this.solution = solution;
			this.fitness = fitness;
		}

		/**
		 * @return the solution
		 */
		public String getSolution() {
			return solution;
		}

		/**
		 * @param solution
		 *            the solution to set
		 */
		public void setSolution(String solution) {
			this.solution = solution;
		}

		/**
		 * @return the fitness
		 */
		public double getFitness() {
			return fitness;
		}

		/**
		 * @param fitness
		 *            the fitness to set
		 */
		public void setFitness(double fitness) {
			this.fitness = fitness;
		}

	}

}
