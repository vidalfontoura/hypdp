package ScoreCalculator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Dr Matthew Hyde
 * ASAP Research Group, School of Computer Science, University of Nottingham
 * mvh@cs.nott.ac.uk
 * 
 * Instance selector for the CHeSC 2011 competition. It selects three instances to use from 
 * the 10 training instances provided in each domain. It also selects five instances from 10 for the
 * hidden domains.
 * 
 * The seed is set to the submission date
 *  
 */

public class CompetitionInstanceSelector {
	public static void main (String[] argv) {

		int number_of_instances = 3;

		int[] chosen_instances_sat = new int[number_of_instances];
		int[] chosen_instances_binpacking = new int[number_of_instances];
		int[] chosen_instances_personnelscheduling = new int[number_of_instances];
		int[] chosen_instances_flowshop = new int[number_of_instances];
		int[] chosen_instances_tsp = new int[number_of_instances+2];
		int[] chosen_instances_vrp = new int[number_of_instances+2];

		int[][] ChosenInstances = {
				chosen_instances_sat, 
				chosen_instances_binpacking, 
				chosen_instances_personnelscheduling, 
				chosen_instances_flowshop,
				chosen_instances_tsp,
				chosen_instances_vrp
				};

		long seed = 15062011;//the submission deadline
		Random rng = new Random(seed);
		LinkedList<Integer> ir = new LinkedList<Integer>();
		ir.add(0);ir.add(1);ir.add(2);ir.add(3);ir.add(4);ir.add(5);ir.add(6);ir.add(7);ir.add(8);ir.add(9);
		System.out.println("Chosen Instances:");
		for (int d = 0; d < ChosenInstances.length; d++) {
			Collections.shuffle(ir, rng);
			for (int i = 0; i < ChosenInstances[d].length; i++) {
				ChosenInstances[d][i] = ir.get(i);
			}
			System.out.print("Problem Domain index "+d + " ");
			if (d < 4) {
				System.out.print("(Public)");
			} else {
				System.out.print("(Hidden)");
			}
			System.out.print(": ");
			for (int o : ChosenInstances[d]) {
				System.out.print(o + ", ");
			}System.out.println();
		}
		System.out.println("Seed: " + rng.nextLong());
	}
}
