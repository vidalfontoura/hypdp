/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 *
 * @author vfontoura
 */
public class MultiPointsCrossover extends CrossoverOperator {

	private Random rng;

	private double probability;

	public MultiPointsCrossover(Random rng, double probability) {
		this.rng = rng;
		this.probability = probability;
	}

	public int[][] apply(int[] parent1, int[] parent2) {

		int[][] offspring = new int[2][];

		offspring[0] = Arrays.copyOf(parent1, parent1.length);
		offspring[1] = Arrays.copyOf(parent2, parent2.length);
		if (this.rng.nextDouble() < probability) {

			int numberOfCrossPoints = (int) Math.round(parent1.length * 0.1);

			List<Integer> crossoverPoints = new ArrayList<>(numberOfCrossPoints);

			for (int i = 0; i < numberOfCrossPoints; i++) {
				int crosspoint = this.rng.nextInt(parent1.length - 2);
				if (!crossoverPoints.contains(crosspoint)) {
					crossoverPoints.add(crosspoint);
				}
			}

			int startPoint = 0;
			boolean exchangeValues = true;
			for (int j = 0; j < crossoverPoints.size(); j++) {

				Integer point = crossoverPoints.get(j);
				for (int i = startPoint; i < point; i++) {
					int variableValue1 = parent1[i];
					int variableValue2 = parent2[i];

					if (exchangeValues) {
						offspring[0][i] = variableValue2;
						offspring[1][i] = variableValue1;
					} else {
						offspring[0][i] = variableValue1;
						offspring[1][i] = variableValue2;
					}

				}
				exchangeValues = exchangeValues ? false : true;
				startPoint = point;
			}

		}
		return offspring;

	}
}
