
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import travelingSalesmanProblem.TSP;

import AVGDeluge.GDeluge;
import AVNaiveMemAlg.NaiveMemAlg;
import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import FlowShop.FlowShop;
import GreedyHH.GreedyHH;
import HyperILS.HyperILS;
import NaiveHH.NaiveHH;
//import PersonnelScheduling.PersonnelScheduling;
import SAT.SAT;
import TSAdaptiveAcceptanceHH.TSHyperHeuristicAA;
import TSHyperHeuristic.TSHyperHeuristic;
import TSHyperHeuristicNaiveAcceptance.TSHyperHeuristicNaiveAcceptance;
import VRP.VRP;

public class Runner extends Thread {
	static Random rng;
	static int problem;
	static int instance;
	static int numberofhyperheuristics;
	static int numberofruns;
	static long instanceseed;
	static long tm;
	static String resultsfolder;
	
	public static HyperHeuristic loadHyperHeuristic(int number, long timeLimit, Random rng) {
		HyperHeuristic h;
		switch (number) {
		case 0: h = new TSHyperHeuristicNaiveAcceptance(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 1: h = new TSHyperHeuristicAA(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 2: h = new GreedyHH(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 3: h = new HyperILS(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 4: h = new NaiveHH(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 5: h = new NaiveMemAlg(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 6: h = new TSHyperHeuristic(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		case 7: h = new GDeluge(rng.nextLong()); h.setTimeLimit(timeLimit); break;
		default: System.err.println("there is no hyper heuristic with this index");
		h = null;
		System.exit(0);
		}//end switch
		return h;
	}//end method loadHyperHeuristic
	public static ProblemDomain loadProblemDomain(int number) {
		ProblemDomain p;
		switch (number) {
		case 0: p = new SAT(instanceseed); break;
		case 1: p = new BinPacking(instanceseed); break;
		//case 2: p = new PersonnelScheduling(instanceseed); break;
		case 3: p = new FlowShop(instanceseed); break;
		case 4: p = new TSP(instanceseed); break;
		case 5: p = new VRP(instanceseed); break;
		default: System.err.println("there is no problem domain with this index");
		p = new BinPacking(rng.nextLong());
		System.exit(0);
		}//end switch
		return p;
	}//end method loadHyperHeuristic

	public synchronized void run() {
		int[][] instances_to_use = new int[6][];
		int[] sat = {0,1,2,3,4,5,6,7,8,9};
		int[] bp = {0,1,2,3,4,5,6,7,8,9};
		int[] ps = {0,1,2,3,4,5,6,7,8,9};
		int[] fs = {0,1,2,3,4,5,6,7,8,9};
		int[] tsp = {0,1,2,3,4,5,6,7,8,9};
		int[] vrp = {0,1,2,3,4,5,6,7,8,9};
		
		instances_to_use[0] = sat;
		instances_to_use[1] = bp;
		instances_to_use[2] = ps;
		instances_to_use[3] = fs;
		instances_to_use[4] = tsp;
		instances_to_use[5] = vrp;
		System.out.println("PROBLEM DOMAIN " + problem);
		int instancetouse = instances_to_use[problem][instance];
		System.out.println("  instance " + instancetouse+" ");
		try {
			FileWriter f = new FileWriter("RRresults/" + resultsfolder + "/instance" + instance + ".txt");
			PrintWriter buffprint = new PrintWriter(f);
			for (int run = 0; run < numberofruns; run++) {
				System.out.println("RUN " + run);
				for (int hyperheuristic = 0; hyperheuristic < numberofhyperheuristics; hyperheuristic++) {
					
					ProblemDomain p = loadProblemDomain(problem);
					HyperHeuristic h = loadHyperHeuristic(hyperheuristic, tm, rng);
					System.out.print("    HYPER HEURISTIC " + h.toString());
					p.loadInstance(instancetouse);
					h.loadProblemDomain(p);

					long initialTime2 = System.currentTimeMillis();
					h.run();

					int[] i = p.getHeuristicCallRecord();
					int counter = 0;
					for (int y : i) {
						counter += y;
					}
					System.out.println("\t" + h.getBestSolutionValue() + "\t" + (h.getElapsedTime()/1000.0) + "\t" + (System.currentTimeMillis()-initialTime2)/1000.0 + "\t" + counter);
					buffprint.print(h.getBestSolutionValue() + " ");
					
					FileWriter f1 = new FileWriter("RRresults/" + resultsfolder + "/time.txt", true);
					PrintWriter buffprint1 = new PrintWriter(f1);
					double[] u = h.getFitnessTrace();
					for (double y : u) {
						buffprint1.print(y + " ");
					}buffprint1.println();
					buffprint1.close();
					f1.close();
				}
				buffprint.println();
			}
			buffprint.close();
			f.close();

		} catch (IOException a1) {
			System.err.println(a1.getMessage());
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		//user defined variables:
		rng = new Random(12345);//my computer
		int[] noofinstances = {10,10,10,10,10,10};
		
		for (int p = 4; p < 5; p++) {
			long problemdomainseed = rng.nextInt(); 
			for (int i = 0; i < noofinstances[p]; i++) {
				instanceseed = problemdomainseed*(i+1);
				problem = p;
				resultsfolder = "not worked";
				switch (problem) {
				case 0: resultsfolder = "SAT"; break;
				case 1: resultsfolder = "BinPacking"; break;
				case 2: resultsfolder = "PersonnelScheduling"; break;
				case 3: resultsfolder = "FlowShop"; break;
				case 4: resultsfolder = "TSP"; break;
				case 5: resultsfolder = "VRP"; break;
				default: 	System.err.println("wrong input for the problem domain");
				System.exit(-1);
				}
				instance = i;
				numberofhyperheuristics = 8;
				numberofruns = 5;
				tm = 10000;
				Runner r = new Runner();
				r.start();
				try {
					r.join();
				} catch(InterruptedException e) {
					System.out.println();System.out.println();
					System.exit(0);
				}
			}
		}
	}//end main
}//end class
