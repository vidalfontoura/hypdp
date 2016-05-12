package PDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AbstractClasses.ProblemDomain;
import PDP.fitness.FitnessFunction;
import PDP.fitness.Residue;
import PDP.fitness.Residue.Point;
import PDP.operators.CrossoverOperator;
import PDP.operators.ExaustiveSearchMutationOperator;
import PDP.operators.LocalMoveOperator;
import PDP.operators.LoopMoveOperator;
import PDP.operators.MultiPointsCrossover;
import PDP.operators.MutationOperator;
import PDP.operators.OppositeMoveOperator;
import PDP.operators.SegmentMutationOperator;
import PDP.operators.TwoPointsCrossover;

/**
 *
 *
 * @author vfontoura
 */
public class PDP extends ProblemDomain {

	private static final String BASE_SEQUENCES_PATH = "data" + "/" + "pdp" + "/" + "sq%s.txt";

	// TODO: Give ids later from the heuristics
	private final int[] mutations = new int[] { 4, 5, 6 };
	private final int[] ruinRecreates = new int[] {};
	private final int[] localSearches = new int[] { 2, 3 };
	private final int[] crossovers = new int[] { 0, 1 };

	private String sequence;

	private PDPSolution[] memoryMechanism;

	private int numberOfVariables;

	private int upperBound;

	private double bestSolutionValue = Double.POSITIVE_INFINITY;

	private FitnessFunction fitnessFunction;

	private PDPSolution bestSolution;

	public PDP(long seed, HPModel model, double alpha, double beta, int memorySize) {
		super(seed);

		switch (model) {
		case TWO_DIMENSIONAL:
			upperBound = 3;
			break;
		case THREE_DIMENSIONAL:
			upperBound = 6;
			break;
		default:
			System.err.println("Unknown hp model provided: " + model);
			System.exit(1);
		}
		this.setMemorySize(memorySize);
		this.fitnessFunction = new FitnessFunction(alpha, beta);
	}

	@Override
	public int[] getHeuristicsOfType(HeuristicType heuristicType) {
		if (heuristicType == ProblemDomain.HeuristicType.MUTATION)
			return mutations;
		if (heuristicType == ProblemDomain.HeuristicType.RUIN_RECREATE)
			return ruinRecreates;
		if (heuristicType == ProblemDomain.HeuristicType.LOCAL_SEARCH)
			return localSearches;
		if (heuristicType == ProblemDomain.HeuristicType.CROSSOVER)
			return crossovers;
		return null;
	}

	@Override
	public int[] getHeuristicsThatUseIntensityOfMutation() {
		return mutations;
	}

	@Override
	public int[] getHeuristicsThatUseDepthOfSearch() {
		return localSearches;
	}

	@Override
	public void loadInstance(int instanceID) {

		this.sequence = loadSequence(String.format(BASE_SEQUENCES_PATH, instanceID));
		this.numberOfVariables = this.sequence.length() - 2;

	}

	private String loadSequence(String resourceName) {
		String sequence = "";
		try {
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream(resourceName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
			sequence = bufferedReader.readLine();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return sequence;

	}

	@Override
	public void setMemorySize(int size) {
		PDPSolution[] tempMemory = new PDPSolution[size];
		if (memoryMechanism != null) {
			if (tempMemory.length <= memoryMechanism.length)
				for (int i = 0; i < tempMemory.length; i++)
					tempMemory[i] = memoryMechanism[i];
			else
				for (int i = 0; i < memoryMechanism.length; i++)
					tempMemory[i] = memoryMechanism[i];
		}
		memoryMechanism = tempMemory;

	}

	@Override
	public void initialiseSolution(int index) {
		PDPSolution pdpSolution = new PDPSolution(this.numberOfVariables);
		int[] variables = pdpSolution.getVariables();
		for (int i = 0; i < numberOfVariables; i++) {
			variables[i] = this.rng.nextInt(upperBound);
		}

		int[] repairedSolution = repairSolution(sequence, variables);

		pdpSolution.setVariables(repairedSolution);

		memoryMechanism[index] = pdpSolution;

		double functionValue = getFunctionValue(index);

		if (functionValue < bestSolutionValue) {
			bestSolutionValue = functionValue;
			bestSolution = pdpSolution.copySolution();
		}

	}

	@Override
	public int getNumberOfHeuristics() {
		return 7;
	}

	@Override
	public double applyHeuristic(int heuristicID, int solutionSourceIndex, int solutionDestinationIndex) {

		MutationOperator operator = null;
		switch (heuristicID) {
		case 2: {
			operator = new LocalMoveOperator(rng, 1.0);
			break;
		}
		case 3: {
			operator = new LoopMoveOperator(rng, 1.0);
			break;
		}
		case 4: {
			operator = new OppositeMoveOperator(rng, 1.0);
			break;
		}
		case 5: {
			operator = new SegmentMutationOperator(rng, 1.0);
			break;
		}
		case 6: {
			operator = new ExaustiveSearchMutationOperator(rng, 1.0, sequence, fitnessFunction);
			break;
		}
		default: {
			System.err.println("Error occured unknown heuristic id: " + heuristicID);
			System.exit(1);
		}

		}

		int[] parent1 = memoryMechanism[solutionSourceIndex].getVariables();

		int[] offspring = operator.apply(parent1);
		PDPSolution pdpSolution = new PDPSolution(offspring.length);
		pdpSolution.setVariables(offspring);
		memoryMechanism[solutionDestinationIndex] = pdpSolution;
		double functionValue = getFunctionValue(solutionDestinationIndex);

		return functionValue;

	}

	@Override
	public double applyHeuristic(int heuristicID, int solutionSourceIndex1, int solutionSourceIndex2,
			int solutionDestinationIndex) {

		CrossoverOperator crossoverOperator = null;
		switch (heuristicID) {
		case 0: {
			crossoverOperator = new TwoPointsCrossover(rng, 1.0);
			break;
		}
		case 1: {
			crossoverOperator = new MultiPointsCrossover(rng, 1.0);
			break;
		}
		default: {
			System.err.println("Error occured unknown heuristic id: " + heuristicID);
			System.exit(1);
		}

		}

		int[] parent1 = memoryMechanism[solutionSourceIndex1].getVariables();
		int[] parent2 = memoryMechanism[solutionSourceIndex2].getVariables();

		int[] offspring = crossoverOperator.apply(parent1, parent2)[0];
		PDPSolution pdpSolution = new PDPSolution(offspring.length);
		pdpSolution.setVariables(offspring);
		memoryMechanism[solutionDestinationIndex] = pdpSolution;
		double functionValue = getFunctionValue(solutionDestinationIndex);

		return functionValue;
	}

	@Override
	public void copySolution(int solutionSourceIndex, int solutionDestinationIndex) {
		PDPSolution srcSolution = memoryMechanism[solutionSourceIndex];
		PDPSolution destSolution = new PDPSolution(this.numberOfVariables);

		int[] srcVariables = srcSolution.getVariables();
		int[] destVariables = destSolution.getVariables();

		if (srcVariables.length != destVariables.length) {
			System.err.println("Copy solution failed the solutions are with different lenghts");
			System.exit(1);
		}
		for (int i = 0; i < srcVariables.length; i++) {
			destVariables[i] = srcVariables[i];

		}
		memoryMechanism[solutionDestinationIndex] = destSolution;

	}

	@Override
	public int getNumberOfInstances() {
		return 11;
	}

	@Override
	public String bestSolutionToString() {
		return Arrays.toString(bestSolution.getVariables());
	}

	@Override
	public double getBestSolutionValue() {

		return bestSolutionValue;
	}

	@Override
	public String solutionToString(int solutionIndex) {
		return memoryMechanism[solutionIndex].getVariables().toString();
	}

	@Override
	public double getFunctionValue(int solutionIndex) {
		PDPSolution pdpSolution = memoryMechanism[solutionIndex];
		double fitness = fitnessFunction.calculateFitness(sequence, pdpSolution.getVariables());
		if (fitness < bestSolutionValue) {
			bestSolutionValue = fitness;
			bestSolution = memoryMechanism[solutionIndex].copySolution();
		}
		pdpSolution.setFitness(fitness);
		return fitness;
	}

	@Override
	public boolean compareSolutions(int solutionIndex1, int solutionIndex2) {
		PDPSolution solution1 = memoryMechanism[solutionIndex1];
		PDPSolution solution2 = memoryMechanism[solutionIndex2];

		int[] variables1 = solution1.getVariables();
		int[] variables2 = solution2.getVariables();

		if (variables1.length != variables2.length) {
			System.err.println("The solutions can not be compared because they have different lenghts");
			System.exit(1);
		}
		for (int i = 0; i < variables1.length; i++) {
			if (variables1[i] != variables2[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "PDP";
	}

	private int[] repairSolution(String chain, int[] solution) {

		Set<Point> points = new HashSet<>();

		int x = 0, y = 0;
		int direction = 1;

		Point firstPoint = new Point(x, y);
		setNewPoint(points, firstPoint);

		if (chain.length() >= 2 && solution.length == chain.length() - 2) {
			x++;
			Point secondPoint = new Point(x, y);
			setNewPoint(points, secondPoint);

			for (int i = 0; i < solution.length; i++) {
				int step = solution[i];
				int[] directions = getDirections(direction, step, x, y);

				Point newPoint = new Point(directions[1], directions[2]);
				boolean added = setNewPoint(points, newPoint);

				List<Integer> movesUnvisited = new ArrayList<>();
				movesUnvisited.add(0);
				movesUnvisited.add(1);
				movesUnvisited.add(2);
				while (!added) {
					// TODO: CHeck this code
					if (movesUnvisited.size() == 0) {
						break;
					}
					do {
						step = this.rng.nextInt(this.upperBound);
					} while (!movesUnvisited.contains(step));
					movesUnvisited.remove(movesUnvisited.indexOf(step));

					directions = getDirections(direction, step, x, y);

					newPoint = new Point(directions[1], directions[2]);
					added = setNewPoint(points, newPoint);
					if (added) {
						solution[i] = step;
						break;
					}
				}

				x = directions[1];
				y = directions[2];
				direction = directions[0];
			}
		}

		return solution;
	}

	private boolean setNewPoint(Set<Point> points, Point newPoint) {

		boolean ret = points.add(newPoint);
		if (ret) {
			points.add(newPoint);
		}
		return ret;
	}

	public List<Residue> translateToOrigin(List<Residue> residues) {

		int minX = residues.get(0).getPoint().getX();
		int minY = residues.get(0).getPoint().getY();
		for (Residue residue : residues) {
			int x = residue.getPoint().getX();
			int y = residue.getPoint().getY();
			if (x < minX) {
				minX = x;
			}
			if (y < minY) {
				minY = y;
			}
		}

		List<Residue> changed = new ArrayList<Residue>();
		for (Residue residue : residues) {
			Point point = residue.getPoint();
			int x = point.getX() - minX + 2;
			int y = point.getY() - minY + 2;
			Point newPoint = new Point(x, y);
			Residue newResidue = new Residue();
			newResidue.setResidueType(residue.getResidueType());
			newResidue.setPoint(newPoint);
			changed.add(newResidue);
		}
		return changed;

	}

	private int[] getDirections(int direction, int step, int x, int y) {

		switch (direction) {
		case 0:// LOOKING UP
			switch (step) {
			case 0:
				x--;
				direction = 3;
				break;
			case 1:
				y++;
				direction = 0;
				break;
			case 2:
				x++;
				direction = 1;
				break;
			default:
				System.err.println("A invalid movement was provided: " + step);
				System.exit(1);
			}
			break;
		case 1:// LOOKING FORWARD
			switch (step) {
			case 0:
				y++;
				direction = 0;
				break;
			case 1:
				x++;
				direction = 1;
				break;
			case 2:
				y--;
				direction = 2;
				break;
			default:
				System.err.println("A invalid movement was provided: " + step);
				System.exit(1);
			}
			break;
		case 2:// LOOKING DOWN
			switch (step) {
			case 0:
				x++;
				direction = 1;
				break;
			case 1:
				y--;
				direction = 2;
				break;
			case 2:
				x--;
				direction = 3;
				break;
			default:
				System.err.println("A invalid movement was provided: " + step);
				System.exit(1);
			}
			break;
		case 3:// LOOKING BACK
			switch (step) {
			case 0:
				y--;
				direction = 2;
				break;
			case 1:
				x--;
				direction = 3;
				break;
			case 2:
				y++;
				direction = 0;
				break;
			default:
				System.err.println("A invalid movement was provided: " + step);
				System.exit(1);
			}
			break;
		default:
			break;
		}
		return new int[] { direction, x, y };
	}

	public String getInstance() {

		return sequence;
	}

	public PDPSolution[] getMemoryMechanism() {
		return memoryMechanism;
	}

}
