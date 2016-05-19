/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.operators;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import PDP.fitness.FitnessFunction;

/**
 *
 *
 * @author vfontoura
 */
public class ExaustiveSearchMutationOperator extends MutationOperator {

	private Random rng;

	private double probability;

	private FitnessFunction fitnessFunction;

	private String sequence;

	public ExaustiveSearchMutationOperator(Random rng, double probability, String sequence,
			FitnessFunction fitnessFunction) {
		this.rng = rng;
		this.probability = probability;
		this.fitnessFunction = fitnessFunction;
		this.sequence = sequence;
	}

	@Override
	public int[] apply(int[] parent) {
		if (this.rng.nextDouble() < probability) {
			int[] offspring = Arrays.copyOf(parent, parent.length);

			int index = this.rng.nextInt(parent.length - 1);

			double originalFitness = fitnessFunction.calculateFitness(sequence, parent);
			double newFitness = originalFitness;
			Set<Integer> testedMoves = Sets.newHashSet();
			int move = offspring[index];
			testedMoves.add(move);
			do {
				int newMove = getNewMove(move);
				offspring[index] = newMove;
				newFitness = fitnessFunction.calculateFitness(sequence, offspring);
				testedMoves.add(newMove);
				if (newFitness < originalFitness) {
					return offspring;
				}

			} while (testedMoves.size() != 3);

		}
		return parent;

	}

	private int getNewMove(int move) {
		int newMove = move;
		do {
			newMove = this.rng.nextInt(3);
		} while (move == newMove);
		return newMove;
	}

}
