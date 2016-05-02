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
		pdp = new PDP(1L, HPModel.TWO_DIMENSIONAL, 1, 3);
	}

	@Test
	public void testLoadSequence() {

		pdp.loadInstance(1);

		Assert.assertEquals("HPHPPHHPHHPHPHHPPHPH", pdp.getInstance());
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
		Assert.assertEquals("[0, 2, 2, 0, 1, 2, 2, 0, 1, 2, 0, 2, 1, 1, 0, 1, 2, 0]", Arrays.toString(variables));

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

		// Init solution 0 and 1
		pdp.initialiseSolution(0);
		pdp.initialiseSolution(1);

		// Get the variables of solutions 0 and 1
		int[] variables0 = pdp.getMemoryMechanism()[0].getVariables();
		int[] variables1 = pdp.getMemoryMechanism()[1].getVariables();

		// System.out.println(Arrays.toString(variables0));
		// System.out.println(Arrays.toString(variables1));
		// System.out.println();

		// Checking that the solutions are not equal
		Assert.assertFalse(Arrays.equals(variables0, variables1));

		// Calling compareSolutions method
		boolean compareSolutions = pdp.compareSolutions(0, 1);

		// Checking the response which should not be true
		Assert.assertFalse(compareSolutions);

		// Copy solution 0 to 1
		pdp.copySolution(0, 1);

		// Getting the variables again
		variables0 = pdp.getMemoryMechanism()[0].getVariables();
		variables1 = pdp.getMemoryMechanism()[1].getVariables();

		// System.out.println(Arrays.toString(variables0));
		// System.out.println(Arrays.toString(variables1));
		//
		// System.out.println();

		// Calling the compareSolutions method
		compareSolutions = pdp.compareSolutions(0, 1);

		// Checking if the solutions are equal and the response should be true
		Assert.assertTrue(compareSolutions);

		// Messing with the variables0
		variables0[0] = 1;
		variables0[1] = 2;

		// Calling the compareSolutions method
		compareSolutions = pdp.compareSolutions(0, 1);

		// Now the solutions should not be equal since we messed with the
		// variables
		Assert.assertFalse(compareSolutions);

		// Making the same mess with the variables1
		variables1[0] = 1;
		variables1[1] = 2;

		// System.out.println(Arrays.toString(variables0));
		// System.out.println(Arrays.toString(variables1));
		//
		// Calling the compareSolutions again
		compareSolutions = pdp.compareSolutions(0, 1);

		// Now the solutions should be equal
		Assert.assertTrue(compareSolutions);

	}

	@Test
	public void testFunctionValue() {
		pdp.loadInstance(1);

		// Init solution 0 and 1
		pdp.initialiseSolution(0);
		pdp.initialiseSolution(1);
		pdp.initialiseSolution(2);
		pdp.initialiseSolution(3);
		pdp.initialiseSolution(4);

		pdp.initialiseSolution(5);
		pdp.initialiseSolution(6);
		pdp.initialiseSolution(7);
		pdp.initialiseSolution(8);
		pdp.initialiseSolution(9);

		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[0].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[1].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[2].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[3].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[4].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[5].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[6].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[7].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[8].getVariables()).replace("
		// ", ""));
		//
		// System.out.println(Arrays.toString(pdp.getMemoryMechanism()[9].getVariables()).replace("
		// ", ""));

		Assert.assertEquals(0.0, pdp.getFunctionValue(0), 0);

		Assert.assertEquals(-1.0, pdp.getFunctionValue(1), 0);

		Assert.assertEquals(-2.0, pdp.getFunctionValue(2), 0);

		Assert.assertEquals(0.0, pdp.getFunctionValue(3), 0);

		Assert.assertEquals(-2.0, pdp.getFunctionValue(4), 0);

		Assert.assertEquals(-5.0, pdp.getFunctionValue(5), 0);

		Assert.assertEquals(-1.0, pdp.getFunctionValue(6), 0);

		Assert.assertEquals(-2.0, pdp.getFunctionValue(7), 0);

		Assert.assertEquals(-1.0, pdp.getFunctionValue(8), 0);

		Assert.assertEquals(-3.0, pdp.getFunctionValue(9), 0);

	}

}
