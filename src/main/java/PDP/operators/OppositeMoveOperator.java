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
public class OppositeMoveOperator {

	private Random rng;

	private double probability;

	public OppositeMoveOperator(Random rng, double probability) {
		this.rng = rng;
		this.probability = probability;
	}

	public int[] apply(int[] parent1) {
		int[] offspring = Arrays.copyOf(parent1, parent1.length);

		if (this.rng.nextDouble() < probability) {
			int numberOfVariables = offspring.length;

			int startPoint = this.rng.nextInt(numberOfVariables - 1);
			int endPoint = startPoint;
			do {
				endPoint = this.rng.nextInt(numberOfVariables - 1);
			} while (startPoint == endPoint);

			int aux = -1;
			if (startPoint > endPoint) {
				aux = endPoint;
				endPoint = startPoint;
				startPoint = aux;
			}
			for (int i = startPoint; i < endPoint; i++) {
				int oldDirection = offspring[i];

				int oppositeDirection = oldDirection;
				switch (oldDirection) {
				case 0:
					oppositeDirection = 2;
					break;
				case 1:
					oppositeDirection = 1;
					break;
				case 2:
					oppositeDirection = 0;
					break;

				default:
				}
				offspring[i] = oppositeDirection;

			}

		}
		return offspring;
	}

}
