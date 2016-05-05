/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.exampleRun;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/**
 *
 *
 * @author vfontoura
 */
public class PDPExampleHyperHeuristic extends HyperHeuristic {

	/**
	 * creates a new ExampleHyperHeuristic object with a random seed
	 */
	public PDPExampleHyperHeuristic(long seed) {
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
		problem.initialiseSolution(0);
		problem.initialiseSolution(1);
		problem.initialiseSolution(2);
		problem.initialiseSolution(3);
		problem.initialiseSolution(4);
		problem.initialiseSolution(5);
		problem.initialiseSolution(6);
		problem.initialiseSolution(7);
		problem.initialiseSolution(8);
		problem.initialiseSolution(9);
		problem.initialiseSolution(10);

		// the main loop of any hyper-heuristic, which checks if the time limit
		// has been reached
		while (!hasTimeExpired()) {

			// this hyper-heuristic chooses a random low level heuristic to
			// apply
			int heuristic_to_apply = rng.nextInt(number_of_heuristics);

			double new_obj_function_value = Double.MAX_VALUE;
			if (heuristic_to_apply == 0 || heuristic_to_apply == 1) {
				new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, 0, 1, 11);
			} else {
				new_obj_function_value = problem.applyHeuristic(heuristic_to_apply, 0, 11);
			}

			// System.out.println(new_obj_function_value);

			// calculate the change in fitness from the current solution to the
			// new solution
			double delta = current_obj_function_value - new_obj_function_value;

			// all of the problem domains are implemented as minimisation
			// problems. A lower fitness means a better solution.
			if (delta > 0) {
				// if there is an improvement then we 'accept' the solution by
				// copying the new solution into memory index 0
				problem.copySolution(1, 0);
				// we also set the current objective function value to the new
				// function value, as the new solution is now the current
				// solution
				current_obj_function_value = new_obj_function_value;
			} else {
				// if there is not an improvement in solution quality then we
				// accept the solution with a 50% probability
				if (rng.nextBoolean()) {
					// the process for 'accepting' a solution is the same as
					// above
					problem.copySolution(1, 0);
					current_obj_function_value = new_obj_function_value;
				}
			}
			// one iteration has been completed, so we return to the start of
			// the main loop and check if the time has expired
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
