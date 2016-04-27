package PDP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AbstractClasses.ProblemDomain;
import PDP.Residue.Point;

/**
 *
 *
 * @author vfontoura
 */
public class PDP extends ProblemDomain {

	private static final String BASE_SEQUENCES_PATH = "data" + File.separator + "pdp" + File.separator + "sq%s.txt";

	// TODO: Give ids later from the heuristics
	private final int[] mutations = new int[] { 0, 3, 5 };
	private final int[] ruinRecreates = new int[] {};
	private final int[] localSearches = new int[] { 4, 6 };
	private final int[] crossovers = new int[] { 7 };

	private String sequence;

	private double alpha;

	private double beta;

	private PDPSolution[] memoryMechanism;

	private int numberOfVariables;

	private HPModel model;

	private int upperBound;

	private double bestSolutionValue = Double.POSITIVE_INFINITY;

	private PDPSolution bestSolution;

	public PDP(long seed, HPModel model) {
		super(seed);
		this.model = model;

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
		pdpSolution.setVariables(variables);
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double applyHeuristic(int heuristicID, int solutionSourceIndex1, int solutionSourceIndex2,
			int solutionDestinationIndex) {
		// TODO Auto-generated method stub
		return 0;
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
		return bestSolution.getVariables().toString();
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
		List<Residue> residues = executeParse(sequence, pdpSolution.getVariables());
		Grid grid = generateGrid(residues);

		int topologicalContacts = getTopologyContacts(residues, grid).size();
		int collisionsCount = getCollisionsCount(residues);

		return (alpha * topologicalContacts) - (beta * collisionsCount);

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
		// TODO Auto-generated method stub
		return null;
	}

	private List<Residue> executeParse(String chain, int[] solution) {

		int x = 0, y = 0, minX = 0, minY = 0, direction = 1;
		int chainIndex = 0;

		List<Residue> residues = new ArrayList<>();
		Set<Point> points = new HashSet<>();

		// Adiciona ponto inicial em (0,0) 1 Residuo
		residues.add(new Residue(new Point(x, y), ResidueType.valueOf(String.valueOf(chain.charAt(0)))));
		points.add(new Point(x, y));
		chainIndex++;

		// Se tem 2 ou mais Residuos
		if (chain.length() >= 2 && solution.length == chain.length() - 2) {

			x++;
			residues.add(new Residue(new Point(x, y), ResidueType.valueOf(String.valueOf(chain.charAt(1)))));
			chainIndex++;
			points.add(new Point(x, y));

			for (int i = 0; i < solution.length; i++) {
				int step = solution[i];
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
						// TODO Exception movimento invalido
						break;
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
						// TODO Exception movimento inv�lido
						break;
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
						// TODO Exception movimento inv�lido
						break;
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
						// TODO Exception movimento inv�lido
						break;
					}
					break;
				default:
					break;
				}
				if (x < minX) {
					minX = x;
				}
				if (y < minY) {
					minY = y;
				}
				residues.add(
						new Residue(new Point(x, y), ResidueType.valueOf(String.valueOf(chain.charAt(chainIndex++)))));
			}
		}
		for (Residue residue : residues) {
			residue.setPoint(new Residue.Point(residue.getPoint().x - minX + 1, residue.getPoint().y - minY + 1));
		}
		return residues;
	}

	public Grid generateGrid(List<Residue> residues) {

		Grid g = new Grid(residues.size() + 2, residues.size() + 2);
		for (int i = 0; i < residues.size(); i++) {
			g.getMatrix()[residues.get(i).getPoint().y][residues.get(i).getPoint().x] = i;
		}
		return g;
	}

	public Set<TopologyContact> getTopologyContacts(List<Residue> residues, Grid grid) {

		Set<TopologyContact> topologyContacts = new HashSet<>();
		int[][] matrix = grid.getMatrix();
		int index = 0;
		for (int i = 0; i < residues.size(); i++) {
			if (residues.get(i).getResidueType().equals(ResidueType.P)) {
				continue;
			}
			if (residues.get(i).getPoint().y + 1 < matrix.length) {
				index = matrix[residues.get(i).getPoint().y + 1][residues.get(i).getPoint().x];
				// test up
				if (isTopologicalContact(i, index, residues)) {
					topologyContacts.add(new TopologyContact(residues.get(i), residues.get(index)));
				}
			}
			if (residues.get(i).getPoint().x + 1 < matrix.length) {
				// test right
				index = matrix[residues.get(i).getPoint().y][residues.get(i).getPoint().x + 1];
				if (isTopologicalContact(i, index, residues)) {
					topologyContacts.add(new TopologyContact(residues.get(i), residues.get(index)));
				}
			}
			if (residues.get(i).getPoint().y - 1 >= 0) {
				// test down
				index = matrix[residues.get(i).getPoint().y - 1][residues.get(i).getPoint().x];
				if (isTopologicalContact(i, index, residues)) {
					topologyContacts.add(new TopologyContact(residues.get(i), residues.get(index)));
				}
			}
			if (residues.get(i).getPoint().x - 1 >= 0) {
				// test back
				index = matrix[residues.get(i).getPoint().y][residues.get(i).getPoint().x - 1];
				if (isTopologicalContact(i, index, residues)) {
					topologyContacts.add(new TopologyContact(residues.get(i), residues.get(index)));
				}
			}

		}
		return topologyContacts;
	}

	public boolean isTopologicalContact(int i, int index, List<Residue> residues) {

		if (i != index + 1 && i != index - 1 && index != -1) {

			return residues.get(index).getResidueType().equals(ResidueType.H);
		}
		return false;
	}

	public int getCollisionsCount(List<Residue> residues) {

		Set<Point> pointsSet = new HashSet<>();
		int count = 0;
		for (int i = 0; i < residues.size(); i++) {
			boolean added = pointsSet.add(residues.get(i).getPoint());
			if (!added) {
				count++;
			}
		}
		return count;
	}

	public String getInstance() {

		return sequence;
	}

	public PDPSolution[] getMemoryMechanism() {
		return memoryMechanism;
	}

}
