package HyperILS;


import java.util.*;

import AbstractClasses.*;


public class HyperILS extends HyperHeuristic 
{
	public static final double EPSILON = 1.0e-6; // EPSILON, for comparing doubles
	
	public static boolean VERBOSE = false;
	ProblemDomain problem;
	int[] localSearchers = new int[0];
	ArrayList<Integer> mutatingHeuristics = new ArrayList<Integer>();
	ArrayList<Integer> crossoverHeuristics = new ArrayList<Integer>();
	long startTime = System.currentTimeMillis();
	
	
	public String toString()
	{
		return "HyperILS";
	}
	
	/**
	 * Iterated LS Hyperheuristic
	 * 
	 * 
	 */
	public HyperILS()
	{
		
	}
	
	public HyperILS(long seed)
	{
		super(seed);
	}

	public void solve(ProblemDomain problem)
	{
		this.problem = problem;
		
		int numSolutions = 2;
		boolean USE_CROSSOVER_HEURISTICS = false;
		
		// Identify what sort of heuristics are available
		GetHeuristics();
			
		// Set the memory size (default size is 2) 
		problem.setMemorySize(numSolutions + localSearchers.length + 1);
		
		// Create initial solutions at positions 0..numSolutions-1
		for (int i=0; i<numSolutions; i++)
		{
			problem.initialiseSolution(i);
			if (VERBOSE)
				System.out.println("Objective value for solution "+i+" = "+problem.getFunctionValue(i));
		}
		
		// If there are any local search heuristics then apply them to the new solutions
		for (int i=0; i<numSolutions; i++)
		{
			double originalPen = problem.getFunctionValue(i);	
			if (VERBOSE)
				System.out.println("Attempting to improve solution "+i+" ("+originalPen+") with local search heuristics");
			
			// Pass the position of the solution to try to improve (i)
			// and a position in the solution memory where we can 
			// temporarily store solutions (numSolutions)
			LocalSearch(i, numSolutions);
			
			if (hasTimeExpired() || hasBestSolutionReachedLB())
				break;
			
			double pen = problem.getFunctionValue(i);
			if (VERBOSE)
				System.out.println("Finished local search on solution "+i+" (original pen="+originalPen+"). Final pen="+pen);
		}
		
		// Use the rest of the time in a loop of 
		// mutation-localSearch-crossover-localSearch-mutation-localSearch... 
		while (hasTimeExpired()==false && hasBestSolutionReachedLB()==false)
		{
			// TODO
			// Using the time left - gradually decrease the intensity of mutation??
			// do it linearly from max time to 0 from 1 to 0.1 
			
			// If there are any MUTATION, RUIN_RECREATE, OTHER heuristics
			// test one on the solutions and then apply the local search to the
			// resulting solutions
			for (int i=0; i<numSolutions; i++)
			{
				int h = getNextMutator();
				if (h < 0)
					break;
				
				if (VERBOSE)
					System.out.print("Mutating solution "+i+" (pen="+problem.getFunctionValue(i)+") using heuristic="+h);
				
				problem.applyHeuristic(h, i, numSolutions);
				
				if (VERBOSE)
					System.out.println(" Result is pen=" + problem.getFunctionValue(numSolutions)
									   + " Now applying local search heuristics... ");
				
				// Test local search heuristics on the solution at position numSolutions, 
				// using position numSolutions+1 as a temp position
				LocalSearch(numSolutions, numSolutions+1);
				
				if (VERBOSE)
					System.out.println("After applying local search heuristics, result is "
							           + problem.getFunctionValue(numSolutions));
				
				// If the resulting solution is better than the original solution at position i
				// then copy it to pos i
				if (problem.getFunctionValue(numSolutions) < problem.getFunctionValue(i))
					problem.copySolution(numSolutions, i);
				
				if (hasTimeExpired() || hasBestSolutionReachedLB())
					break;
			}
			
			// Finally if there are any crossover heuristics then apply one
			// to the best two solutions found so far and then
			// again apply the local search heuristics to the resulting solution
			if (USE_CROSSOVER_HEURISTICS
				&& crossoverHeuristics.size()!= 0
				&& numSolutions >= 2 
				&& hasTimeExpired() == false
				&& hasBestSolutionReachedLB() == false)
			{
				int h = getNextCrossover();
				
				int bestSol = getBestSolutionInRange(0, numSolutions-1);
				int bestSol2 = getBestSolutionInRangeIgnoring(bestSol, 0, numSolutions-1);
				
				if (bestSol2 < 0)
					System.out.println("Warning: Attempting to crossover non-existent solution.");
				
				if (VERBOSE)
					System.out.println("Crossing " + problem.getFunctionValue(bestSol)
									   + " with " + problem.getFunctionValue(bestSol2) 
									   + " using heuristic="+h);
				
				problem.applyHeuristic(h,bestSol,bestSol2,numSolutions);
				
				if (VERBOSE)
					System.out.print("Result is "+problem.getFunctionValue(numSolutions));
				
				LocalSearch(numSolutions, numSolutions+1);
				
				// if this solution is better than the worst one in range then replace it
				int worstSol = getWorstSolutionInRange(0, numSolutions-1);
				if (problem.getFunctionValue(numSolutions) < problem.getFunctionValue(worstSol))
					problem.copySolution(numSolutions, worstSol);
			}
			
			if (VERBOSE)
				for (int i=0; i<numSolutions; i++)
					System.out.println("Solution "+i +" = "+ problem.getFunctionValue(i));
		}
		
		if (VERBOSE)
		{
			System.out.println("\nFinished. Time Elapsed = " + getElapsedTime()/1000.0f+" secs");
		}
		
	}
	
	/**
	 * Compare two doubles, avoiding floating point accuracy problems
     * e.g. 1.0 != 0.999999999999998 
	 * @param a
	 * @param b
	 * @return
	 */
    public static boolean AreEqual(double a, double b)
    {
        return Math.abs(a - b) < EPSILON;
    }
	
	private boolean hasBestSolutionReachedLB()
	{
		return AreEqual(problem.getBestSolutionValue(), 0);
	}
	
	private void GetHeuristics()
	{
		// Local search heuristics
		localSearchers = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH);
		
		// Mutation heuristics
		int[] m = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
		if (m != null)
		{
			for (int i=0; i<m.length; i++)
				mutatingHeuristics.add(m[i]);
		}
		
		m = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.RUIN_RECREATE);
		if (m != null)
		{
			for (int i=0; i<m.length; i++)
				mutatingHeuristics.add(m[i]);
		}
		
		m = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.OTHER);
		if (m != null)
		{
			for (int i=0; i<m.length; i++)
				mutatingHeuristics.add(m[i]);
		}
		
		// Crossover heuristics
		m = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
		if (m != null)
		{
			for (int i=0; i<m.length; i++)
				crossoverHeuristics.add(m[i]);
		}
	}
	
	/**
	 * Test all local search heuristics and use the most improving one
	 * (actually saving the solutions as the heuristic may be stochastic).
	 * Repeat until no improved solution found.
	 * @param solutionPosition
	 * @param tempSolutionPositionsStart
	 */
	private void LocalSearch(int solutionPosition, int tempSolutionPositionsStart)
	{
		if (localSearchers==null || localSearchers.length==0)
			return;
		
		int iteration = 0;
		while (true)
		{
			int bestHeuristic = -1;
			int bestSrc = -1;
			double bestPen = problem.getFunctionValue(solutionPosition);

			if (VERBOSE)
				System.out.print("  Testing local search heuristic");
			
			for (int x=0; x < localSearchers.length; x++)
			{
				int i = localSearchers[x];

				if (VERBOSE)
					System.out.print(" " + i);
				
				int testPosition = tempSolutionPositionsStart+x;
				double pen = problem.applyHeuristic(i, solutionPosition, testPosition);
				
				if (VERBOSE)
				{
					System.out.print(" [pen="+pen+"]");
					if (x+1 < localSearchers.length) 
						System.out.print(",");
				}

				if (pen < bestPen)
				{
					bestPen = pen;
					bestSrc = testPosition;
					bestHeuristic = i;
				}
				
				if (hasTimeExpired() || hasBestSolutionReachedLB())
					break;
			}

			if (bestSrc != -1)
			{
				iteration++;
				problem.copySolution(bestSrc, solutionPosition);
				
				if (VERBOSE)
					System.out.println(". Selecting heuristic : " + bestHeuristic
									   + " Pen: " + bestPen+" (iteration="+iteration+")");
				
				if (hasTimeExpired() || hasBestSolutionReachedLB())
					return;
			}
			else
			{
				if (VERBOSE)
					System.out.println("");
				return;
			}
		}
	}
	
	private int getNextMutator()
	{
		// TODO
		// I guess we could keep some statistics on mutators used and their success to
		// give a bias towards the better ones (same for crossover too).
		
		if (mutatingHeuristics.size()==0)
			return -1;
		else
			return mutatingHeuristics.get(rng.nextInt(mutatingHeuristics.size()));
	}
	
	private int getNextCrossover()
	{
		if (crossoverHeuristics.size()==0)
			return -1;
		else
			return crossoverHeuristics.get(rng.nextInt(crossoverHeuristics.size()));
	}
	
	private int getBestSolutionInRange(int start, int finish)
	{
		double bestPen = problem.getFunctionValue(start);
		int bestPos = start;
		for (int i=start+1; i<=finish; i++)
		{
			if (problem.getFunctionValue(i) < bestPen)
			{
				bestPos = i;
				bestPen = problem.getFunctionValue(i);
			}
		}
		
		return bestPos;
	}
	
	private int getBestSolutionInRangeIgnoring(int ignore, int start, int finish)
	{
		int bestPos = -1;
		double bestPen = 0;
		for (int i=start; i<=finish; i++)
		{
			if (i==ignore)
				continue;
			
			if (bestPos < 0 || problem.getFunctionValue(i) < bestPen)
			{
				bestPos = i;
				bestPen = problem.getFunctionValue(i);
			}
		}
		
		return bestPos;
	}
	
	private int getWorstSolutionInRange(int start, int finish)
	{
		double worstPen = problem.getFunctionValue(start);
		int worstPos = start;
		for (int i=start+1; i<=finish; i++)
		{
			if (problem.getFunctionValue(i) > worstPen)
			{
				worstPos = i;
				worstPen = problem.getFunctionValue(i);
			}
		}
		
		return worstPos;
	}
	
}