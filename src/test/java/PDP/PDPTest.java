/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 * @author vfontoura
 */
public class PDPTest {

	private PDP pdp;

	@Before
	public void setup() {
		pdp = new PDP(1L, HPModel.TWO_DIMENSIONAL);
	}

	@Test
	public void testLoadSequence() {

		pdp.loadInstance(1);

		Assert.assertEquals("HPHPPHHPHHPHPHHPPHPH ", pdp.getInstance());
	}

	@Test
	public void testInitializeSolution() {

		pdp.loadInstance(1);

		pdp.initialiseSolution(0);

		int[] variables = pdp.getMemoryMechanism()[0].getVariables();

		Assert.assertEquals(18, variables.length);
		Assert.assertEquals("[0, 1, 1, 0, 2, 1, 2, 1, 1, 1, 1, 1, 1, 0, 0, 1, 2, 0]", Arrays.toString(variables));

		pdp.initialiseSolution(1);

		variables = pdp.getMemoryMechanism()[1].getVariables();

		Assert.assertEquals(18, variables.length);
		Assert.assertEquals("[0, 2, 2, 2, 1, 2, 2, 2, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0]", Arrays.toString(variables));

	}

	@Test
	public void testCopySolution() {

		pdp.loadInstance(1);

		pdp.initialiseSolution(0);
		pdp.initialiseSolution(1);

		int[] variables0 = pdp.getMemoryMechanism()[0].getVariables();
		int[] variables1 = pdp.getMemoryMechanism()[1].getVariables();

		// Checking that the solutions are not equal
		Assert.assertFalse(Arrays.equals(variables0, variables1));

		pdp.copySolution(0, 1);

		variables0 = pdp.getMemoryMechanism()[0].getVariables();
		variables1 = pdp.getMemoryMechanism()[1].getVariables();

		// Checking that after the copySolution call the solutions are really
		// the same
		Assert.assertTrue(Arrays.equals(variables0, variables1));

		variables0[1] = 1;
		variables0[2] = 0;

		// After make a change on the variables0 the solution must be different
		Assert.assertFalse(Arrays.equals(variables0, variables1));

	}

	@Test
	public void testCompareSolutions() {

		pdp.loadInstance(1);

		pdp.initialiseSolution(0);
		pdp.initialiseSolution(1);

		int[] variables0 = pdp.getMemoryMechanism()[0].getVariables();
		int[] variables1 = pdp.getMemoryMechanism()[1].getVariables();

		System.out.println(Arrays.toString(variables0));
		System.out.println(Arrays.toString(variables1));
		System.out.println();

		// Checking that the solutions are not equal
		Assert.assertFalse(Arrays.equals(variables0, variables1));

		boolean compareSolutions = pdp.compareSolutions(0, 1);

		Assert.assertFalse(compareSolutions);

		pdp.copySolution(0, 1);

		variables0 = pdp.getMemoryMechanism()[0].getVariables();
		variables1 = pdp.getMemoryMechanism()[1].getVariables();

		System.out.println(Arrays.toString(variables0));
		System.out.println(Arrays.toString(variables1));

		System.out.println();

		compareSolutions = pdp.compareSolutions(0, 1);

		Assert.assertTrue(compareSolutions);

		variables0[0] = 1;
		variables0[1] = 2;

		compareSolutions = pdp.compareSolutions(0, 1);

		Assert.assertFalse(compareSolutions);

		variables1[0] = 1;
		variables1[1] = 2;

		System.out.println(Arrays.toString(variables0));

		System.out.println(Arrays.toString(variables1));
		compareSolutions = pdp.compareSolutions(0, 1);

		Assert.assertTrue(compareSolutions);

	}

}
