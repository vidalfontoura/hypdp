/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.operators;

import java.util.Arrays;
import java.util.Random;

/**
 *
 *
 * @author vfontoura
 */
public class LocalMoveOperator extends MutationOperator {

	private Random rng;

	private double probability;

	public LocalMoveOperator(Random rng, double probability) {
		this.rng = rng;
		this.probability = probability;
	}

	@Override
	public int[] apply(int[] parent1) {
		int[] offspring = Arrays.copyOf(parent1, parent1.length);

		if (this.rng.nextDouble() < probability) {
			int numberOfVariables = parent1.length;

			int localMovePoint1 = this.rng.nextInt(numberOfVariables - 1);
			int localMovePoint2 = localMovePoint1 + 1;

			// TODO: Write a test for it
			if (localMovePoint2 >= numberOfVariables) {
				localMovePoint2 = numberOfVariables - 1;
			}

			int variableValue1 = offspring[localMovePoint1];
			int variableValue2 = offspring[localMovePoint2];

			offspring[localMovePoint1] = variableValue2;
			offspring[localMovePoint2] = variableValue1;

		}
		return offspring;
	}

}
