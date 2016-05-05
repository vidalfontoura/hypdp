/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.fitness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import PDP.fitness.Residue.Point;

/**
 *
 *
 * @author vfontoura
 */
public class FitnessFunction {

	private double alpha;

	private double beta;

	public FitnessFunction(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	public double calculateFitness(String chain, int[] solution) {
		List<Residue> residues = executeParse(chain, solution);

		Grid grid = generateGrid(residues);

		int topologicalContacts = getTopologyContacts(residues, grid).size();

		int collisionsCount = getCollisionsCount(residues);

		if (collisionsCount > 0) {
			return 0.0;
		}
		// return ((alpha * topologicalContacts) * -1) + (beta *
		// collisionsCount);

		return -topologicalContacts;

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

}
