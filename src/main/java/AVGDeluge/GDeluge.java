package AVGDeluge;

import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import AbstractClasses.ProblemDomain.HeuristicType;



public class GDeluge extends HyperHeuristic {

	public GDeluge(long seed) {
		super(seed);
	}

	@Override
	protected void solve(ProblemDomain problem) {

		/* Local variables */
		int hLSearch, hMutation; // heuristic indices
		double f1 = 0, f2 = 0; // function values

//		/* Step 1. Identify type of heuristics */
		int[] lSArray = problem.getHeuristicsOfType(HeuristicType.LOCAL_SEARCH);
		int[] mArray = problem.getHeuristicsOfType(HeuristicType.MUTATION);
		int[] iGArray = problem.getHeuristicsOfType(HeuristicType.RUIN_RECREATE);

		/* Step 2. Initialise solutions */
		problem.initialiseSolution(0);
		problem.copySolution(0, 1);
		f1 = problem.getFunctionValue(0);

		/* Step 3. Initialise Great Deluge Acceptance Criterion */
		GreatDelugeAcceptance GD = new GreatDelugeAcceptance(getTimeLimit(), f1);

		/* Step 4. Repeat until timeLimit is reached */
		while (!hasTimeExpired()) {
			
			/*
			 * 4.1. Use a randomly selected mutation heuristic and apply it to
			 * the base solution
			 */
			
			if (mArray != null) {
				hMutation = mArray[this.rng.nextInt(mArray.length)];
				f2 = problem.applyHeuristic(hMutation, 0, 1);
			}			
			/*
			 * 4.2. Improve base solution with a randomly chosen local
			 * search or a randomly selected ruin and recreate
			 */
			if (lSArray != null) {
				if (iGArray != null) {
					if (rng.nextDouble() <= 0.5) {
						hLSearch = lSArray[this.rng.nextInt(lSArray.length)];
						f2 = problem.applyHeuristic(hLSearch, 1, 1);
					} else {
						hLSearch = iGArray[this.rng.nextInt(iGArray.length)];
						f2 = problem.applyHeuristic(hLSearch, 1, 1);
					}
				} else {
					hLSearch = lSArray[this.rng.nextInt(lSArray.length)];
					f2 = problem.applyHeuristic(hLSearch, 1, 1);
				}
			} else {
				if (iGArray != null) {
					hLSearch = iGArray[this.rng.nextInt(iGArray.length)];
					f2 = problem.applyHeuristic(hLSearch, 1, 1);
				}
			}

			/* 4.3. Accept using Great Deluge */
			if (GD.accept(f1, f2)) {
				problem.copySolution(1, 0);
				f1 = f2;
			}
		}
	}

	/**
	 * Implements a Dynamic Great Deluge Acceptance Criterion.
	 */
	class GreatDelugeAcceptance {

		private double W;
		private long To, t, Tmax;
		private double So, target, Beta;

		public GreatDelugeAcceptance(long Tmax, double So) {
			this.Tmax = Tmax;
			this.So = 1.2 * So;
			this.To = System.currentTimeMillis();
			this.Beta = (So - So * (1 - W)) / Tmax;
			this.W = 1 / (So + 1);
		}

		public boolean accept(double oldSolution, double newSolution) {

			t = System.currentTimeMillis() - To; /* the current time */

			target = So - Beta * t; /* negative slope line */

			/*
			 * If new solution is better than target solution at Tmax, then
			 * update target
			 */
			if (newSolution <= So * (1 - W)) {
				So = newSolution;
				Beta = (So - So * (1 - W)) / (Tmax - t);
			}

			/*
			 * If solution is better than target or better than old solution then
			 * accept, else reject
			 */
			return newSolution <= target || newSolution < oldSolution ? true
					: false;
		}
	}

	@Override
	public String toString() {
		return "AV Great-Deluge St. LS";

	}
}
