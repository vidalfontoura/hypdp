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
public class SegmentMutationOperator {

	private Random rng;

	private double probability;

	public SegmentMutationOperator(Random rng, double probability) {
		this.rng = rng;
		this.probability = probability;
	}

	public int[] apply(int[] source) {

		int[] offspring = Arrays.copyOf(source, source.length);

		if (this.rng.nextDouble() < probability) {
			int numberOfVariables = offspring.length;

			int startPoint = this.rng.nextInt(numberOfVariables - 1);
			int numberOfGenes = this.rng.nextInt((7 - 5) + 1) + 5;
			int endPoint = startPoint + numberOfGenes;

			// TODO: Write a test for it
			if (endPoint >= numberOfVariables) {
				endPoint = numberOfVariables - 1;
			}

			for (int i = startPoint; i < endPoint; i++) {

				int oldDirection = source[i];
				int newDirection = oldDirection;
				do {
					newDirection = this.rng.nextInt(2);
				} while (oldDirection == newDirection);

				offspring[i] = newDirection;
			}

		}
		return offspring;

	}

}
