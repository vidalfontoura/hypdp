/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP.exampleRun;

import java.util.Arrays;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import PDP.PDP;
import PDP.PDPSolution;

/**
 *
 *
 * @author vfontoura
 */
public class Example {

	public static void main(String[] args) {
		// create a ProblemDomain object with a seed for the random number
		// generator
		ProblemDomain problem = new PDP(2l);

		// creates an ExampleHyperHeuristic object with a seed for the random
		// number generator
		HyperHeuristic hyper_heuristic_object = new PDPExampleHyperHeuristic2(2l);

		// we must load an instance within the problem domain, in this case we
		// choose instance 2
		problem.loadInstance(1);

		// we must set the time limit for the hyper-heuristic in milliseconds,
		// in this example we set the time limit to 1 minute
		hyper_heuristic_object.setTimeLimit(60000);

		// a key step is to assign the ProblemDomain object to the
		// HyperHeuristic object.
		// However, this should be done after the instance has been loaded, and
		// after the time limit has been set
		hyper_heuristic_object.loadProblemDomain(problem);

		// now that all of the parameters have been loaded, the run method can
		// be called.
		// this method starts the timer, and then calls the solve() method of
		// the hyper_heuristic_object.
		hyper_heuristic_object.run();

		// obtain the best solution found within the time limit
		System.out.println(hyper_heuristic_object.getBestSolutionValue());

		PDP prob = (PDP) problem;
		PDPSolution[] memoryMechanism = prob.getMemoryMechanism();
		System.out.println("--");
		for (PDPSolution p : memoryMechanism) {
			System.out.println(p.getFitness() + " " + Arrays.toString(p.getVariables()));
		}
	}

}
