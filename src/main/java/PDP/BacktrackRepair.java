/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package PDP;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author vfontoura
 */
public class BacktrackRepair {

	private int[][] grid;

	private int count;

	private int[] solution;

	private int index;

	private int sequenceLength;

	private ArrayList<Integer> movements;

	private List<Integer> solutionMoves;

	public BacktrackRepair(String sequence, int[] solution) {

		this.sequenceLength = sequence.length();
		solutionMoves = Lists.newArrayList();
		this.grid = createGrid(sequenceLength * 2);
		this.solution = solution;
		this.movements = Lists.newArrayList(2, 1, 0);

	}

	public void start() {
		int startPoint = grid.length / 2;

		int x = startPoint, y = startPoint;
		count++;
		grid[x][y] = count;

		x++;
		count++;
		grid[x][y] = count;
		index = 0;

		move(solution[index], x, y, "X+");
	}

	private boolean move(int movement, int x, int y, String lookingAxis) {

		String localLookingAxis = lookingAxis;

		count++;
		grid[x][y] = count;

		if (index == solution.length) {
			return true;
		}

		int newX = -1;
		int newY = -1;

		List<Integer> badPlace = Lists.newArrayList();

		do {
			if (badPlace.contains(movement)) {
				if (movements.size() == 0) {
					break;
				}
				movement = movements.get(0);
				movements.remove(0);
			}

			Object[] coordinates = fromMoveCodeToCoordinates(lookingAxis, movement, x, y);

			int xMove = (int) coordinates[0];
			int yMove = (int) coordinates[1];

			lookingAxis = (String) coordinates[2];

			newX = xMove;
			newY = yMove;

			if (newX < 0 || newY < 0 || newX >= grid.length || newY >= grid.length) {
				lookingAxis = localLookingAxis;
				continue;
			}
			if (!isEmpty(newX, newY)) {
				lookingAxis = localLookingAxis;
				badPlace.add(movement);
				continue;
			}

			if (badPlace.contains(movement)) {
				lookingAxis = localLookingAxis;
				continue;
			}

			movements = Lists.newArrayList(2, 1, 0);
			// It was possible to set point

			index++;
			if (index == solution.length) {
				solutionMoves.add(movement);
				return true;
			}
			if (move(solution[index], newX, newY, lookingAxis)) {

				solutionMoves.add(movement);
				badPlace = Lists.newArrayList();

				return true;
			}

			badPlace.add(movement);

		} while (!movements.isEmpty());

		lookingAxis = localLookingAxis;

		badPlace = Lists.newArrayList();
		movements = Lists.newArrayList(2, 1, 0);
		count--;
		index--;
		grid[x][y] = -1;
		return false;

	}

	public boolean isEmpty(int x, int y) {

		int i = grid[x][y];
		return i == -1 ? true : false;
	}

	public static Object[] fromMoveCodeToCoordinates(String lookingAxis, int movement, int x, int y) {

		int DISTANCE = 1;

		String newLookingToAxis = lookingAxis;

		switch (movement) {
		case 1: {
			newLookingToAxis = lookingAxis;
			switch (lookingAxis) {
			case "Y+": {
				y = y + DISTANCE;
				break;
			}
			case "Y-": {
				y = y - DISTANCE;
				break;
			}
			case "X+": {
				x = x + DISTANCE;
				break;
			}
			case "X-": {
				x = x - DISTANCE;
				break;
			}
			}
		}
			break;
		case 2: {
			switch (lookingAxis) {
			case "Y+":
				newLookingToAxis = "X-";
				x = x - DISTANCE;
				break;
			case "Y-":
				newLookingToAxis = "X+";
				x = x + DISTANCE;
				break;
			case "X+":
				newLookingToAxis = "Y+";
				y = y + DISTANCE;
				break;
			case "X-":
				newLookingToAxis = "Y-";
				y = y - DISTANCE;
				break;
			}
		}
			break;
		case 0: {
			switch (lookingAxis) {
			case "Y+":
				newLookingToAxis = "X+";
				x = x + DISTANCE;
				break;
			case "Y-":
				newLookingToAxis = "X-";
				x = x - DISTANCE;
				break;
			case "X+":
				newLookingToAxis = "Y-";
				y = y - DISTANCE;
				break;
			case "X-":
				newLookingToAxis = "Y+";
				y = y + DISTANCE;
				break;
			}
		}

		}
		lookingAxis = newLookingToAxis;

		Object[] array = new Object[] { x, y, lookingAxis };
		return array;

	}

	public int[][] createGrid(int size) {

		int[][] grid = new int[size][size];

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid.length; y++) {
				grid[x][y] = -1;
			}
		}
		return grid;

	}

	public void printGrid() {

		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid.length; y++) {
				System.out.print("  " + grid[x][y] + "  ");
			}
			System.out.println();
		}

	}

	public List<Integer> getSolutionMoves() {
		Collections.reverse(solutionMoves);
		return solutionMoves;
	}

	/**
	 * @param solutionMoves
	 *            the solutionMoves to set
	 */
	public void setSolutionMoves(ArrayList<Integer> solutionMoves) {
		this.solutionMoves = solutionMoves;
	}

	public static void main(String[] args) {

		// Test 1
		// String aminoAcidSequence = "HHHHHHHHHHHHH";
		// int[] solution = new int[] { 1, 1, 2, 1, 2, 1, 1, 2, 2, 1, 1 };

		// Test 2
		// String aminoAcidSequence = "HHHHHHHHHHHHHH";
		// int[] solution = new int[] { 1, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 1 };

		// Test 3
		// String aminoAcidSequence = "HHHHHHHHHHHHHHHHHHH";
		// int[] solution = new int[] { 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2,
		// 2, 1, 0, 1 };

		String aminoAcidSequence = "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH";
		int[] solution = new int[] { 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 0, 1, 0, 0, 1, 0, 1, 1, 2, 2, 2, 1, 2,
				2 };

		BacktrackRepair backtrackRepair = new BacktrackRepair(aminoAcidSequence, solution);

		backtrackRepair.start();

		System.out.println(Arrays.toString(solution).replace(" ", ""));

		System.out.println(backtrackRepair.getSolutionMoves().toString().replace(" ", ""));

	}

}
