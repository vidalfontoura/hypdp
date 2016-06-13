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
public class SegmentMutationOperatorTest {

	private SegmentRuinAndRecreateOperator operator;

	private Random random;

	@Before
	public void setup() {

		random = new Random(1L);
		operator = new SegmentRuinAndRecreateOperator(random, 1.0);
	}

	@Test
	public void test1() {

		int[] parent1 = new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 };

		int[] offspring = operator.apply(parent1);

		System.out.println(Arrays.toString(parent1));
		System.out.println(Arrays.toString(offspring));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 1, 1, 0, 0, 1, 1, 2, 0 }, offspring));

		int[] subOffspring = operator.apply(offspring);

		System.out.println(Arrays.toString(subOffspring));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 1, 1, 0, 0, 1, 1, 2, 0 }, offspring));

		Assert.assertTrue(
				Arrays.equals(new int[] { 0, 2, 2, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0 }, subOffspring));

		int[] subSubOffspring = operator.apply(subOffspring);

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 1, 1, 0, 0, 1, 1, 2, 0 }, offspring));

		Assert.assertTrue(
				Arrays.equals(new int[] { 0, 2, 2, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0 }, subOffspring));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0 }, parent1));

		Assert.assertTrue(Arrays.equals(new int[] { 0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 1, 1, 0, 0, 1, 1, 2, 0 }, offspring));

		System.out.println(Arrays.toString(subSubOffspring));
		Assert.assertTrue(
				Arrays.equals(new int[] { 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0 }, subSubOffspring));

	}

}
