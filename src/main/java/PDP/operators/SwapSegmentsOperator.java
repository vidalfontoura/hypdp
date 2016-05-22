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
public class SwapSegmentsOperator extends MutationAndRuinRecreateOperator {

	private Random rng;

	private double probability;

	public SwapSegmentsOperator(Random rng, double probability) {
		this.rng = rng;
		this.probability = probability;
	}

	@Override
	public int[] apply(int[] parent1) {

		int length = parent1.length;

		int[] offspring = Arrays.copyOf(parent1, length);
		if (this.rng.nextDouble() < probability) {

			int numberOfSegments = (int) Math.round(parent1.length * 0.1);
			System.out.println(numberOfSegments);

			int indexCut = length / numberOfSegments;
			int lengthOfSegmnet = length / numberOfSegments;

			int[][] segments = new int[numberOfSegments][];

			for (int i = 0; i < numberOfSegments; i++) {
				segments[i] = new int[lengthOfSegmnet];

				for (int j = 0; j < lengthOfSegmnet; j++) {
					if (i == 0) {
						int value = parent1[j];
						segments[i][j] = value;
					} else if (i == 1) {
						if (j + indexCut < length) {
							int value = parent1[j + indexCut];
							segments[i][j] = value;
						}
					} else {
						if (j + (indexCut * i) < length) {
							int value = parent1[j + (indexCut * i)];
							segments[i][j] = value;
						}
					}
				}

			}
			shuffleArray(segments);

			int count = 0;
			for (int i = 0; i < segments.length; i++) {
				System.out.println(Arrays.toString(segments[i]));
				for (int j = 0; j < segments[i].length; j++) {
					offspring[count] = segments[i][j];
					count++;
				}

			}
		}
		return offspring;
	}

	private void shuffleArray(int[][] array) {
		int index;
		for (int i = array.length - 1; i > 0; i--) {
			index = rng.nextInt(i + 1);
			for (int j = 0; j < array[i].length; j++) {
				if (index != i) {
					array[index][j] ^= array[i][j];
					array[i][j] ^= array[index][j];
					array[index][j] ^= array[i][j];
				}
			}

		}
	}

	public static void main(String[] args) {
		int[] vet = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0 };

		Random r = new Random(1l);
		SwapSegmentsOperator swapSegmentsOperator = new SwapSegmentsOperator(r, 1.0);

		int[] apply = swapSegmentsOperator.apply(vet);

		System.out.println(Arrays.toString(apply));
	}

}
