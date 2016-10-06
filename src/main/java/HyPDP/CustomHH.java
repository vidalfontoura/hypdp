/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package HyPDP;

import com.google.common.collect.Maps;

import java.util.Collections;
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

	private long seed;

	private int totalNumberOfInteractions = 2000;
	
	private int instance;

	private int numberOfAccepts = 0;

	public CustomHH(long seed, int memorySize, String selectionFunction, String acceptanceFunction, int rcWindowSize,
			int instance) {
		super(seed);
		this.memorySize = memorySize;
		this.selectionFunction = selectionFunction;

		this.bestFitness = Double.MAX_VALUE;
		this.rcWindowSize = rcWindowSize;

		this.heuristicsSelectionFunctionValue = Maps.newHashMap();
		this.numberOfInteractions = 0;
		this.acceptanceFunction = acceptanceFunction;

		this.seed = seed;
		this.instance = instance;

	}

	@Override
	protected void solve(ProblemDomain problem) {
		
		System.out.println("Running CustomHH with parameters: ");
		System.out.println("Selection Function: " + selectionFunction);
		System.out.println("Acceptance Function: " + acceptanceFunction);
		System.out.println("Seed: " + seed);
		System.out.println("Instance: " + instance);
		System.out.println("Timelimit: " + this.getTimeLimit());
		System.out.println("MemorySize: " + memorySize);
		System.out.println("RCWindoSize: " + rcWindowSize);

		this.initializeMemoryMechanism(problem);

//		printMemoryMechanism(problem);

		this.setHeuristicTypes(problem);

		initializeHeuristicsInformation(problem.getNumberOfHeuristics());

		startTime = System.currentTimeMillis();

		// Selects a random solution from the memory
		int solutionIndex1 = this.rng.nextInt(this.memorySize - 3);
//		System.out.println("Selecting solution index:" + solutionIndex1 + " with fitness: "
//				+ problem.getFunctionValue(solutionIndex1));

		// Copy the selected for current solution for the current index (defined
		// by me as the last position of memory mechanism)
		problem.copySolution(solutionIndex1, this.memorySize - 1);

		// Executing all heuristics in the current solution (last index) to
		// populate the statistics information
		for (int i = 0; i < problem.getNumberOfHeuristics(); i++) {
			executeHeuristic(problem, i, this.memorySize - 1);

		}

		while (!hasTimeExpired()) {

			// printHeuristicsStatistics();

			int heuristicIndex = selectHeuristicToApply(problem);

			executeHeuristic(problem, heuristicIndex, this.memorySize - 1);

			// printMemoryMechanism(problem);

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
				// Selects another solution from the memory mechanism to make
				// the crossover
				solutionIndex2 = this.rng.nextInt(this.memorySize - 3);
			} while (currentIndex == solutionIndex2);

			// Applies the crossover heuristic using both solutions and stores
			// in the position size -2 of the memory mechanism
			newFitness = problem.applyHeuristic(heuristicIndex, currentIndex, solutionIndex2, this.memorySize - 2);

//			System.out.println("Applied heuristic: " + heuristicIndex + " and got "
//					+ problem.getFunctionValue(this.memorySize - 2));
			delta = currentFitness - newFitness;

			if (bestFitness > newFitness) {
				bestFitness = newFitness;
				bestSolution = problem.bestSolutionToString();
				updateCbest(heuristicIndex);
			}

		} else {
			// If is not a crossover heuristic it should just apply the
			// heuristic
			currentFitness = problem.getFunctionValue(currentIndex);
			newFitness = problem.applyHeuristic(heuristicIndex, currentIndex, this.memorySize - 2);

//			System.out.println("Applied heuristic: " + heuristicIndex + " and got "
//					+ problem.getFunctionValue(this.memorySize - 2));

			delta = currentFitness - newFitness;

			if (bestFitness > newFitness) {
				bestFitness = newFitness;
				bestSolution = problem.bestSolutionToString();
				updateCbest(heuristicIndex);
			}
		}

		//System.out.println("Current: " + currentFitness);
		//System.out.println("New: " + newFitness);

		// If delta > 0 means that the netFitness is better than
		// currentFitness
		if (delta > 0) {
			updateCcurrent(heuristicIndex);
			updateCava(heuristicIndex, delta);
//			System.out.println("Replacing solution with " + problem.getFunctionValue(currentIndex) + " by "
//					+ problem.getFunctionValue(this.memorySize - 2));

			problem.copySolution(this.memorySize - 2, currentIndex);
		//	System.out.println("Better solution replacing current index: " + currentIndex);

		}
		//if (delta == 0) {
			// }
		// Accepting equal solution, backuping current solution
		if (shouldAccept(delta, currentFitness, newFitness, numberOfInteractions, totalNumberOfInteractions)) {
			int backupIndex = this.rng.nextInt(this.memorySize - 3);
			problem.copySolution(currentIndex, backupIndex);

			problem.copySolution(this.memorySize - 2, currentIndex);
			updateCAccept(heuristicIndex);
			//System.out.println("Accepting worst replacing current index: " + currentIndex);
			//numberOfAccepts++;
		}
		updateRC(heuristicIndex, currentFitness, newFitness);
		//System.out.println();
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

			String input = resultingFunction;
			while (input.contains("(") && input.contains(")")) {
				input = input.replaceAll("-0.0", "0.0").replaceAll("- -", "+ ").replaceAll("--", "+ ");

				int indexLastOpeningParantesis = input.lastIndexOf("(");
				String substring = input.substring(indexLastOpeningParantesis);
				int index1 = substring.indexOf("(");
				int index2 = substring.indexOf(")");
				substring = substring.substring(index1, index2 + 1);

				String partialResult = String.valueOf(ExpressionExecutor.calculate(
substring.replace(" ", "")
						.replaceAll("/0.0", "/0.001").replaceAll("/-0.0", "/0.001").replaceAll("/ -0.0", "/0.001")));

				input = input.replace(substring, partialResult);

			}

			double functionValue = ExpressionExecutor.calculate(input.replaceAll(" ", "").replaceAll("/0.0", "/0.001")
					.replaceAll("- -", "+ ").replaceAll("--", "+ ").replaceAll("/-0.0", "/0.001"));

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
//		System.out.println();
//		System.out.println("Selection heuristic index=" + key);
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
			double functionValue = problem.getFunctionValue(i);
			if (bestFitness > functionValue) {
				bestFitness = functionValue;
				bestSolution = problem.solutionToString(i);
			}
		}

	}


	private boolean shouldAccept(double delta, double currentFitness, double newFitness, double currentIteration,
			double totalNumberOfIteraction) {

		String acceptanceCriterion = acceptanceFunction;

		acceptanceCriterion = acceptanceCriterion.replace("Delta", String.valueOf(delta))
				.replace("PF", String.valueOf(currentFitness * -1)).replace("CF", String.valueOf(newFitness * -1))
				.replace("CI", String.valueOf(currentIteration)).replace("TI", String.valueOf(totalNumberOfIteraction));

		String input = acceptanceCriterion;
		while (input.contains("(") && input.contains(")")) {
			input = input.replaceAll("-0.0", "0.0").replaceAll("- -", "+ ").replaceAll("--", "+ ");

			int indexLastOpeningParantesis = input.lastIndexOf("(");
			String substring = input.substring(indexLastOpeningParantesis);
			int index1 = substring.indexOf("(");
			int index2 = substring.indexOf(")");
			substring = substring.substring(index1, index2 + 1);

			String partialResult = String.valueOf(ExpressionExecutor.calculate(substring.replace(" ", "")
					.replaceAll("/0.0", "/0.001").replaceAll("/-0.0", "/0.001").replaceAll("/ -0.0", "/0.001")));

			input = input.replace(substring, partialResult);

		}

		double calculate = ExpressionExecutor.calculate(input.replaceAll(" ", "").replaceAll("/0.0", "/0.001")
				.replaceAll("- -", "+ ").replaceAll("--", "+ ").replaceAll("/-0.0", "/0.001"));

		if (calculate > 0) {
			calculate = calculate * -1;
		}
		double result = Math.exp(calculate);

		if (result <= 0.5) {
			return true;
		}
		return false;

	}

	@Override
	public String toString() {
		return "CustomHH";
	}

	public int getInteractionNumber() {
		return numberOfInteractions;
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
		// System.out.println();
		// System.out.println("Priting selection function results");
		// for (Entry<Integer, Double> entry :
		// heuristicsSelectionFunctionValue.entrySet()) {
		// System.out.println(entry.getKey() + "=" + entry.getValue());
		// }

	}

	public void printMemoryMechanism(ProblemDomain problem) {
		for (int i = 0; i < memorySize; i++) {
			PDP p = (PDP) problem;
			System.out.print(i + ": ");
			System.out.println(p.getMemoryMechanism()[i].getFitness() + " - ");
			// System.out.println(Arrays.toString(p.getMemoryMechanism()[i].getVariables()).replace("
			// ", ""));

		}
		//System.out.println();
		//System.out.println();
	}

	public String getBestSolution() {
		return bestSolution;
	}

	public double getBestFitness() {
		return bestFitness;
	}

	public int getNumberOfInteractions() {
		return numberOfInteractions;
	}

	public int getNumberOfAccepts() {
		return numberOfAccepts;
	}

	public static void main(String[] args) {

		long seed = 3l;
		long timeLimit = 60000;
		int instance = 7;
		String selectionFunction = "RC * Ccurrent * Cava - Cr";
		String acceptanceFunction = "(TI / ( Delta * ( TI + TI ) ) )";
		if (args != null && args.length >= 5) {
			seed = Long.valueOf(args[0]);
			instance = Integer.valueOf(args[1]);
			timeLimit = Long.valueOf(args[2]);
			selectionFunction = args[3];
			acceptanceFunction = args[4];
		}

		int memorySize = 12;
		int rcWindowSize = 10;

		CustomHH cfhh = new CustomHH(seed, memorySize, selectionFunction, acceptanceFunction, rcWindowSize, instance);
		PDP problem = new PDP(seed);
		problem.loadInstance(instance);

		cfhh.setTimeLimit(timeLimit);
		cfhh.loadProblemDomain(problem);
		cfhh.run();

		//System.out.println("Number of accepts: " + cfhh.getNumberOfAccepts());
		//System.out.println("Number of interactions: " + cfhh.getNumberOfInteractions());
		



	}


}
