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
 * 
 * This program is for use for comparing Hyper-Heuristics for the Cross-Domain Heuristic Search Challenge 2011.
 * It takes any number of hyper-heuristics as input, and compares them in one competition.
 * 
 * To use, you will need one results file per hyper-heuristic (HH) that you wish to test. 
 * Two results files per HH are required if you want to calculate the scores using both the median and best fitness values.
 * Create a "submitted/" directory, in the same location as this file.
 * Create two subdirectories "submitted/best/" and "submitted/median/"
 * 
 * Put each file into the relevant directory, and the program will read them all automatically. 
 * Do not put anything else into the "submitted/" directories.
 * You have the choice to use median results or best results. 
 * You must calculate these, put them in results files, and you must ensure you put them in the correct directories.
 * The results files should be of the format specified on this page: 
 * http://www.asap.cs.nott.ac.uk/chesc2011/registration.html#Leaderboard_Submission
 * 
 * Once the objective function values found by your hyper-heuristic (each over 10 minutes) 
 * are read in from files, the program outputs the relative scores
 * 
 * You can set whether to use median or best results by setting the flags at the start of the main method.
 * There is also a flag to use the 'borda count' or the 'formula one' scoring system
 * Remember that this program does not calculate the median and best results. 
 * You must calculate these yourself, and put them in the correct files. This program just reads the files and calculates scores.
 * 
 * 
 * please report any bugs to Matthew Hyde at mvh@cs.nott.ac.uk
 * The input files need to be included in the directory 'submitted/median/' or 'submitted/best/'
 */

public class CompetitionMultipleScoreCalculator {
	//options for using the borda count or formula1 scoring system
	public static final int BORDA = 3;
	public static final int FORMULA1 = 4;
	//options for using the median or the best recorded fitness values
	public static final int MEDIAN = 1;
	public static final int BEST = 2;

	public static void main (String[] argv) {

		//SET YOUR OPTIONS HERE:
		int borda_or_formula1 = FORMULA1;
		int median_or_best = MEDIAN;

		double[] basescores = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
		double[] basescoresformula1 = {10,8,6,5,4,3,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		if (borda_or_formula1 == BORDA) {

		} else if (borda_or_formula1 == FORMULA1) {
			basescores = basescoresformula1;
		} else {
			System.err.println("Incorrect choice of borda count or formula one scoring");
			System.exit(-1);
		}

		int domains = 6;
		int number_of_instances = 5;
		String mob = "median/";
		if (median_or_best == BEST) {
			mob = "best/";
		}
		File dir = new File("submitted/"+mob); 
		String[] children = dir.list(); 
		String[] hhnames = null;
		int files = 0;
		double[][][] submittedscores = null;
		if (children == null) { 
			System.out.println("there are no files in the submitted directory");
		} else {
			files = children.length;
			hhnames = new String[children.length];
			submittedscores = new double[domains][number_of_instances][files];
			System.out.println("Order of scores: ");
			for (int file=0; file<children.length; file++) {
				String filename = children[file];
				System.out.println(filename);
				hhnames[file] = filename.split(".txt")[0];
				try{
					FileReader read = new FileReader("submitted/"+mob+"/"+filename);
					BufferedReader buff = new BufferedReader(read);
					for (int l = 0; l < domains; l++) {
						String s = buff.readLine();
						String[] sa = s.split(",");
						for (int ins = 0; ins < number_of_instances; ins++) {
							String u = sa[ins];
							double individualresult = Double.parseDouble(u);
							submittedscores[l][ins][file] = individualresult;
							//System.out.print(individualresult + " ");
						}//System.out.println();
					}
					buff.close();
					read.close();
				}catch(IOException e) {
					System.err.println(e.getMessage());
					System.exit(-1);
				}
			}
		}

		int hyperheuristics = files;

		double[][][] results = new double[domains][number_of_instances][hyperheuristics];//used to store all of the scores

		//add the scores of the submitted hyper-heuristics
		for (int d = 0; d < domains; d++) {
			for (int i = 0; i < number_of_instances; i++) {
				for (int h = 0; h < hyperheuristics; h++) {
					results[d][i][h] = submittedscores[d][i][h];
				}}}
		for (int h = 0; h < hyperheuristics; h++) {
			System.out.println("INSERT INTO chesc VALUES ("+h+", "+hhnames[h].split(" ")[1]+", ");
			for (int d = 0; d < domains; d++) {
				for (int i = 0; i < number_of_instances; i++) {
					System.out.print(results[d][i][h] + ", ");
				}}
			System.out.println(");");
			}


		double[] scores = new double[hyperheuristics];

		double totaldomainscores = 0;
		for (double g : basescores) {
			totaldomainscores += g;
		}totaldomainscores *= number_of_instances;//total available per domain
		for (int domain = 0; domain < domains; domain++) {
			double[] domainscores = new double[hyperheuristics];
			for (int i = 0; i < number_of_instances; i++) {
				int instance = i;
				double[] res = results[domain][instance];
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
			System.out.println();
			String d = "";
			switch (domain) {
			case 0: d = "SAT";break;
			case 1: d = "Bin Packing";break;
			case 2: d = "Personnel Scheduling";break;
			case 3: d = "Flow Shop";break;
			case 4: d = "TSP"; break;
			case 5: d = "VRP"; break;}
			System.out.println(d);
			double domaintotal = 0;
			for (int g = 0; g < domainscores.length; g++) {
				domaintotal += domainscores[g];
				scores[g] += domainscores[g];
				System.out.println(domainscores[g]);
			}System.out.println();
			if (Math.round(domaintotal) != totaldomainscores) {
				System.err.println("Error, total scores for this domain ("+domaintotal+") do not add up to " + totaldomainscores);
				System.err.println("This represents a bug. Please email this java class file to Dr Matthew Hyde at mvh@cs.nott.ac.uk");
				System.exit(-1);
			}
		}
		System.out.println("Overall Total ");
		for (int g = 0; g < scores.length; g++) {
			System.out.println(scores[g]);
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
