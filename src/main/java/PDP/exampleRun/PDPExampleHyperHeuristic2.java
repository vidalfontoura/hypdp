/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.exampleRun;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import PDP.PDP;

/**
 *
 *
 * @author vfontoura
 */
public class PDPExampleHyperHeuristic2 extends HyperHeuristic {

	/**
	 * creates a new ExampleHyperHeuristic object with a random seed
	 */
	public PDPExampleHyperHeuristic2(long seed) {
		super(seed);
	}

	/**
	 * This method defines the strategy of the hyper-heuristic
	 * 
	 * @param problem
	 *            the problem domain to be solved
	 */
	public void solve(ProblemDomain problem) {

		// it is often a good idea to record the number of low level heuristics,
		// as this changes depending on the problem domain
		int number_of_heuristics = problem.getNumberOfHeuristics();

		// initialise the variable which keeps track of the current objective
		// function value
		double current_obj_function_value = Double.POSITIVE_INFINITY;

		// initialise the solution at index 0 in the solution memory array

		for (int i = 0; i < 102; i++) {
			problem.initialiseSolution(i);
		}

		// the main loop of any hyper-heuristic, which checks if the time limit
		// has been reached
		while (!hasTimeExpired()) {

			// this hyper-heuristic chooses a random low level heuristic to
			// apply
			int heuristic_to_apply = rng.nextInt(number_of_heuristics);

			double new_obj_function_value = Double.MAX_VALUE;

			int indexApplyHeuristic0 = rng.nextInt(101);

			if (heuristic_to_apply == 0 || heuristic_to_apply == 1) {
				int indexApplyHeuristic1 = indexApplyHeuristic0;
				do {
					indexApplyHeuristic1 = rng.nextInt(11);
				} while (indexApplyHeuristic1 == indexApplyHeuristic0);
				new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, indexApplyHeuristic0,
						indexApplyHeuristic1, 101);
			} else {

				new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, indexApplyHeuristic0, 101);
			}

			PDP pdpProblem = (PDP) problem;

			int indexReplace = -1;
			for (int i = 0; i < pdpProblem.getMemoryMechanism().length; i++) {
				double fitness = problem.getFunctionValue(i);
				if (new_obj_function_value < fitness) {
					indexReplace = i;
				}
			}

			if (indexReplace != -1) {
				problem.copySolution(101, indexReplace);
			}
		}
	}

	/**
	 * this method must be implemented, to provide a different name for each
	 * hyper-heuristic
	 * 
	 * @return a string representing the name of the hyper-heuristic
	 */
	public String toString() {
		return "Example Hyper Heuristic One";
	}
}
