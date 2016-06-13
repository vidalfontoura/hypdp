/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import PDP.fitness.Residue.Point;

/**
 *
 *
 * @author vfontoura
 */
public class SimpleRepair {

	private int upperBound;
	private Random rng;

	public SimpleRepair(int upperBound, Random rng) {
		this.upperBound = upperBound;
		this.rng = rng;
	}

	public int[] repairSolution(String chain, int[] solution) {

		int[] repairedSolution = Arrays.copyOf(solution, solution.length);

		Set<Point> points = new HashSet<>();

		int x = 0, y = 0;
		int direction = 1;

		Point firstPoint = new Point(x, y);
		setNewPoint(points, firstPoint);

		if (chain.length() >= 2 && repairedSolution.length == chain.length() - 2) {
			x++;
			Point secondPoint = new Point(x, y);
			setNewPoint(points, secondPoint);

			for (int i = 0; i < repairedSolution.length; i++) {
				int step = repairedSolution[i];
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
						repairedSolution[i] = step;
						break;
					}
				}

				x = directions[1];
				y = directions[2];
				direction = directions[0];
			}
		}

		return repairedSolution;
	}

	private boolean setNewPoint(Set<Point> points, Point newPoint) {

		boolean ret = points.add(newPoint);
		if (ret) {
			points.add(newPoint);
		}
		return ret;
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

	public static void main(String[] args) {

		String aminoAcidSequence = "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH";
		int[] solution = new int[] { 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 0, 1, 0, 0, 1, 0, 1, 1, 2, 2, 2, 1, 2,
				2 };

		System.out.println(Arrays.toString(solution).replace(" ", ""));
		SimpleRepair simpleRepair = new SimpleRepair(3, new Random());

		int[] repairedSolution = simpleRepair.repairSolution(aminoAcidSequence, solution);

		System.out.println(Arrays.toString(repairedSolution).replace(" ", ""));

	}

}
