/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.operators;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import PDP.fitness.FitnessFunction;

/**
 *
 *
 * @author vfontoura
 */
public class ExaustiveSearchMutationOperatorTest {

	private ExaustiveSearchMutationOperator operator;

	private FitnessFunction fitnessFunction;

	private String sequence;

	private Random random;

	@Before
	public void setup() {

		fitnessFunction = new FitnessFunction();
		sequence = "HPHPPHHPHHPHPHHPPHPH";

		random = new Random(1L);
		operator = new ExaustiveSearchMutationOperator(random, 1.0, sequence, fitnessFunction);
	}

	@Test
	public void test1() {

		int[] parent1 = new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 };

		double fitness = fitnessFunction.calculateFitness(sequence, parent1);
		System.out.println(Arrays.toString(parent1));
		System.out.println(fitness);

		int[] offspring = operator.apply(parent1);

		System.out.println(Arrays.toString(offspring));
		fitness = fitnessFunction.calculateFitness(sequence, offspring);
		System.out.println(fitness);

	}

	@Test
	public void test2() {

		int[] parent1 = new int[] { 0, 1, 0, 1, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 };

		double fitness = fitnessFunction.calculateFitness(sequence, parent1);
		System.out.println(Arrays.toString(parent1));
		System.out.println(fitness);

		int[] offspring = operator.apply(parent1);

		System.out.println(Arrays.toString(offspring));
		fitness = fitnessFunction.calculateFitness(sequence, offspring);
		System.out.println(fitness);

	}

}
