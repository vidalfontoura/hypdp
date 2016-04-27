package ScoreCalculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/*
 * @author Dr Matthew Hyde
 * ASAP Research Group, School of Computer Science, University of Nottingham
 * mvh@cs.nott.ac.uk
 *  
 * This program is for comparing future Hyper-Heuristics to the results of the Cross-Domain Heuristic Search Challenge 2011.
 * It takes one hyper-heuristic as input, and enters it into a competition with the 20 hyper-heuristics which were submitted to the competition in 2011.
 * 
 * This class should come with a 'submitted' directory, containing 20 files.
 * To use, you will need to make sure that this class is in the same directory as the 'submitted' directory (i.e. outside the submitted directory, not in it).
 * In the competition, each competitor was run 31 times on each instance, so for a fair comparison yours should also be run 31 times.
 * The median of the 31 runs is your result on that instance.
 * Obtain the results for your hyper-heuristic on all 6 problem domains (30 instances in total), 
 * and save them in a file in the following format:
 * the file should contain 6 lines (one per domain), each of 5 comma separated numbers, as follows:
 * msat1, msat2, msat3, msat4, msat5
 * binp1, binp2, binp3, binp4, binp5
 * pers1, pers2, pers3, pers4, pers5
 * flow1, flow2, flow3, flow4, flow5
 * tsp1,  tsp2,  tsp3,  tsp4,  tsp5
 * vrp1,  vrp2,  vrp3,  vrp4,  vrp5
 * 
 * This is the same format as the other 20 files in the 'submitted' directory.
 * Please name your file with a descriptive acronym for your algorithm. 
 * 
 * When obtaining the results, remember that the hyflex software provides 10 instances, 
 * and that instances 1-5 here represent these instance indices in hyflex:
 * SAT: 3, 5, 4, 10, 11
 * BP:  7, 1, 9, 10, 11
 * PS:  5, 9, 8, 10, 11
 * FS:  1, 8, 3, 10, 11
 * TSP: 0, 8, 2, 7, 6 
 * VRP: 6, 2, 5, 1, 9 
 * These were the instances used for the 2011 competition
 * 
 * Put your file into the "submitted/" directory, and then run this class. 
 * This class will read the files in the submitted directory, and treat each as a competitor to calculate the scores.
 * If you wish, you may remove files from the submitted directory, which will reduce the competitors used for comparison.
 *
 * please report any bugs to Dr Matthew Hyde at mvh@cs.nott.ac.uk
 */

public class BenchmarkCalculator {

	public static void main (String[] argv) {

		int domains = 6;
		int number_of_instances = 5;

		File dir = new File("submitted/"); 
		String[] children = dir.list(); 
		String[] hhnames = null;
		int hyperheuristics = 0;
		double[][][] submittedscores = null;
		if (children == null) { 
			System.out.println("there are no files in the submitted directory");
		} else {
			hyperheuristics = children.length;
			hhnames = new String[children.length];
			submittedscores = new double[domains][number_of_instances][hyperheuristics];
			System.out.println("\nInput files:");
			for (int file=0; file<children.length; file++) {
				String filename = children[file];
				System.out.println(filename);
				hhnames[file] = filename.split(".txt")[0];
				try{
					FileReader read = new FileReader("submitted/"+filename);
					BufferedReader buff = new BufferedReader(read);
					for (int l = 0; l < domains; l++) {
						String s = buff.readLine();
						String[] sa = s.split(",");
						for (int ins = 0; ins < number_of_instances; ins++) {
							String u = sa[ins];
							double individualresult = Double.parseDouble(u);
							submittedscores[l][ins][file] = individualresult;
							System.out.print(individualresult + " ");
						}System.out.println();
					}
					buff.close();
					read.close();
				}catch(IOException e) {
					System.err.println(e.getMessage());
					System.exit(-1);
				}
			}
		}

		double[][][] results = new double[domains][number_of_instances][hyperheuristics];//used to store all of the scores

		//add the scores of the submitted hyper-heuristics
		for (int d = 0; d < domains; d++) {
			for (int i = 0; i < number_of_instances; i++) {
				for (int h = 0; h < hyperheuristics; h++) {
					results[d][i][h] = submittedscores[d][i][h];
				}}}


		double[] scores = new double[hyperheuristics];
		double[] basescores = {10,8,6,5,4,3,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		double totaldomainscores = 0;
		for (double g : basescores) {
			totaldomainscores += g;
		}totaldomainscores *= number_of_instances;//total available per domain
		for (int domain = 0; domain < domains; domain++) {
			double[] domainscores = new double[hyperheuristics];
			for (int i = 0; i < number_of_instances; i++) {
				double[] res = results[domain][i];
				ArrayList<Score> al = new ArrayList<Score>();
				for (int s = 0; s < res.length; s++) {
					Score obj = new Score(s, res[s]);
					al.add(obj);
				}
				Collections.sort(al); 
				double lastscore = Double.POSITIVE_INFINITY;
				int scoreindex = 0;
				double tieaverage = 0;
				int tienumber = 0;
				ArrayList<Integer> list = new ArrayList<Integer>();
				while (true) {
					Score test1 = null;
					if (scoreindex < al.size()) {
						test1 = al.get(scoreindex);
					}
					if (test1 == null || test1.score != lastscore) {
						double average = tieaverage / list.size();
						for (int f = 0; f < list.size(); f++) {
							domainscores[list.get(f)] += average;
						}
						if (test1 == null) {
							break;//all HH have been given a score
						}
						tienumber = 0;
						tieaverage = 0;
						list = new ArrayList<Integer>();
						list.add(test1.num);
						tienumber++;
						tieaverage += basescores[scoreindex];
					} else {
						list.add(test1.num);
						tienumber++;
						tieaverage += basescores[scoreindex];
					}
					lastscore = test1.score;
					scoreindex++;
				}
			}
			String d = "";
			switch (domain) {
			case 0: d = "SAT";break;
			case 1: d = "Bin Packing";break;
			case 2: d = "Personnel Scheduling";break;
			case 3: d = "Flow Shop";break;
			case 4: d = "TSP";break;
			case 5: d = "VRP";break;}
			System.out.println(d);
			double domaintotal = 0;
			for (int g = 0; g < domainscores.length; g++) {
				domaintotal += domainscores[g];
				scores[g] += domainscores[g];
				System.out.println(hhnames[g] + ", " + domainscores[g]);
			}System.out.println();
			if (Math.round(domaintotal) != totaldomainscores) {
				System.err.println("Error, total scores for this domain ("+domaintotal+") do not add up to " + totaldomainscores);
				System.err.println("This represents a bug. Please email this java class file to Dr Matthew Hyde at mvh@cs.nott.ac.uk");
				System.exit(-1);
			}
		}
		System.out.println("Overall Total ");
		for (int g = 0; g < scores.length; g++) {
			System.out.println(hhnames[g] + ", " + scores[g]);
		}
	}

	public static class Score implements Comparable<Score> {
		int num;
		double score;
		public Score(int n, double s) {
			num = n;
			score = s;
		}
		public int compareTo(Score o) {
			Score obj = (Score)o;
			if (this.score < obj.score) {
				return -1;
			} else if (this.score == obj.score) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}
