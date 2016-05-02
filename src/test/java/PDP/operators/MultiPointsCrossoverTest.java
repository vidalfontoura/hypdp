/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.operators;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

/**
 *
 *
 * @author vfontoura
 */
public class MultiPointsCrossoverTest {

	private MultiPointsCrossover operator;

	@Before
	public void setup() {
		Random random = new Random(1L);
		this.operator = new MultiPointsCrossover(random, 1.0);
	}

	@Test
	public void test1() {

		int[] parent1 = new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 };

		int[] parent2 = new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1 };

		int[][] offspring = this.operator.apply(parent1, parent2);

		int[] offspring1 = offspring[0];
		int[] offspring2 = offspring[1];

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));
		Assert.assertTrue(Arrays.equals(new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1 }, parent2));

		System.out.println(Arrays.toString(parent1));
		System.out.println(Arrays.toString(parent2));

		System.out.println(Arrays.toString(offspring1));
		System.out.println(Arrays.toString(offspring2));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));
		Assert.assertTrue(Arrays.equals(new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1 }, parent2));

	}

	@Test
	public void test2() {

		int[] parent1 = new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0, 0, 2, 2, 0, 1, 2, 2, 0, 1, 2,
				0, 2, 1, 1, 0, 1, 2, 0 };

		int[] parent2 = new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1, 1, 0, 0, 1, 0, 1, 0, 2, 2, 1,
				1, 1, 0, 2, 1, 2, 0, 1 };

		int[][] offspring = this.operator.apply(parent1, parent2);

		int[] offspring1 = offspring[0];
		int[] offspring2 = offspring[1];

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0, 0, 2, 2, 0, 1,
				2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));
		Assert.assertTrue(Arrays.equals(new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1, 1, 0, 0, 1, 0,
				1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1 }, parent2));

		System.out.println(Arrays.toString(parent1));
		System.out.println(Arrays.toString(parent2));

		System.out.println(Arrays.toString(offspring1));
		System.out.println(Arrays.toString(offspring2));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0, 0, 2, 2, 0, 1,
				2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));
		Assert.assertTrue(Arrays.equals(new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1, 1, 0, 0, 1, 0,
				1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1 }, parent2));

		Assert.assertTrue(Arrays.equals(new int[] { 1, 0, 0, 1, 0, 1, 0, 2, 2, 1, 1, 1, 0, 2, 1, 2, 0, 1, 1, 0, 0, 1, 0,
				1, 0, 2, 2, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, offspring1));
		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0, 0, 2, 2, 0, 1,
				2, 2, 0, 1, 1, 1, 1, 0, 2, 1, 2, 0, 1 }, offspring2));

	}

}
