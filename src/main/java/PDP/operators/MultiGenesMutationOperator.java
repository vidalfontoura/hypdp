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
public class MultiGenesMutationOperator extends MutationAndRuinRecreateOperator {

	private Random rng;

	private double probability;

	public MultiGenesMutationOperator(Random rng, double probability) {
		this.rng = rng;
		this.probability = probability;
	}

	@Override
	public int[] apply(int[] parent1) {
		int[] offspring = Arrays.copyOf(parent1, parent1.length);

		int maxNumberOfGenesMutation = (int) Math.round(parent1.length / 2);

		for (int i = 0; i < maxNumberOfGenesMutation; i++) {

			int indexGene = this.rng.nextInt(parent1.length - 1);

			// TODO:Refactor to global variable or something
			offspring[indexGene] = this.rng.nextInt(3);

		}

		return offspring;
	}

	public static void main(String[] args) {
		int[] vet = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1 };

		Random r = new Random(1l);
		MultiGenesMutationOperator operator = new MultiGenesMutationOperator(r, 1.0);

		int[] apply = operator.apply(vet);

		System.out.println(Arrays.toString(vet));

		System.out.println(Arrays.toString(apply));
	}

}
