package ScoreCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
 * @author Dr Matthew Hyde
 * ASAP Research Group, School of Computer Science, University of Nottingham
 * 
 * This program is for calculating the total scores of a whole competition run.
 * 
 * please report any bugs to Matthew Hyde at mvh@cs.nott.ac.uk
 */
public class CompetitionScoreCalculator {
	public static void main (String[] argv) {
		int hhs = 20;
		int instances = 5;
		int runs = 31;
		int medianindex = 15;
		int domains = 6;
		double[] scores = new double[hhs];
//				double[] basescores = {20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
		double[] basescores = {10,8,6,5,4,3,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		//		double[] basescores = {8,7,6,5,4,3,2,1,0,0,0,0,0,0,0};
		for (int domain = 0; domain < domains; domain++) {
			String d = "";
			switch(domain) {
			case 0: d = "SAT";break;
			case 1: d = "BinPacking"; break;
			case 2: d = "PersonnelScheduling"; break;
			case 3: d = "FlowShop"; break;
			case 4: d = "TSP"; break;
			case 5: d = "VRP"; break;
			default: 	System.err.println("wrong input for the problem domain");
			System.exit(-1);
			}
			double totaldomainscores = 0;
			double totalinstancescores = 0;
			for (double g : basescores) {
				totaldomainscores += g;
				totalinstancescores += g;
			}
			totaldomainscores *= instances;//total available per domain
			double[] domainscores = new double[hhs];
			for (int i = 0; i < instances; i++) {
				double[] instancescores = new double[hhs];
				try {
					double[][] res = new double[hhs][runs];
					String filename = "RRresults/"+d+"/instance"+i+".txt";
					FileReader read = new FileReader(filename);
					BufferedReader buffread = new BufferedReader(read);
					for (int r = 0; r < runs; r++) {

						String line = buffread.readLine();
						String[] results = line.split(" ");
						//convert to doubles
						for (int s = 0; s < hhs; s++) {
							res[s][r] = Double.parseDouble(results[s]);
						}
						
					}
					buffread.close();
					read.close();
					//					for (double[] da : res) {
					//						for (double di : da) {
					//							System.out.print(di + " ");
					//						}System.out.println();
					//					}
					for (double[] da : res) {
						Arrays.sort(da);
					}
//					System.out.println();
//					for (double[] da : res) {
//						for (double di : da) {
//							System.out.print(di + " ");
//						}System.out.println();
//					}

					double[] median_results = new double[hhs];
					for (int m = 0; m < hhs; m++) {
						median_results[m] = res[m][medianindex];
					}

//					for (int m = 0; m < hhs; m++) {
//						System.out.println(median_results[m]);
//					}System.out.println(); 

					//					String dom = "";
					//					switch (x) {
					//					case 0:dom = "SAT";break;
					//					case 1:dom = "BinPacking";break;
					//					case 2:dom = "PersonnelScheduling";break;
					//					case 3:dom = "FlowShop";break;
					//					}
					//					System.out.print("static double[] "+dom + i+" = {");
					//					for (int y = 0; y < median_results.length; y++) {
					//						double di = median_results[y];
					//						if (y != median_results.length-1) {
					//							System.out.print(di + ", ");
					//						} else {
					////							System.out.print(di + "};");
					//							System.out.print(di + ", -1.0};");
					//						}
					//					}System.out.println();

					//add results to arraylist
					ArrayList<Score> al = new ArrayList<Score>();
					for (int s = 0; s < median_results.length; s++) {
						//System.out.print(res[s] + ", ");
						Score obj = new Score(s, median_results[s]);
						al.add(obj);
					}
					Collections.sort(al);
					//					for (Score g : al) {
					//						System.out.println(g.num + " " + g.score);
					//					}System.out.println();

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
						//System.out.println("considering " + test1.num + " " + tieaverage);
						if (test1 == null || test1.score != lastscore) {

							double average = tieaverage / list.size();
							//							System.out.print("            average " + average + " ");
							for (int f = 0; f < list.size(); f++) {
								scores[list.get(f)] += average;
								domainscores[list.get(f)] += average;
								instancescores[list.get(f)] += average;
								//System.out.print(list.get(f) + " ");
							}//System.out.println();
							if (test1 == null) {
								break;//all HH have been given a score
							}
							tienumber = 0;
							tieaverage = 0;
							list = new ArrayList<Integer>();

							list.add(test1.num);
							tienumber++;
							tieaverage += basescores[scoreindex];
							//System.out.println("test " + test1.num + " " + basescores[scoreindex] + " " + tieaverage);

							//scores[test1.num] += basescores[scoreindex];
							//domainscores[test1.num] += basescores[scoreindex];
							//System.out.println(test1.num + " " + basescores[scoreindex]);
						} else {
							list.add(test1.num);
							tienumber++;
							tieaverage += basescores[scoreindex];
							//System.out.println("draw " + test1.num + " " + basescores[scoreindex] + " " + tieaverage);
							//System.out.println(test1.num + " " + basescores[scoreindex]);
						} 
						lastscore = test1.score;
						scoreindex++;
					}
					double instancetotal = 0;
					for (int g = 0; g < instancescores.length; g++) {
						//System.out.println(g + " " + instancescores[g]);
						instancetotal += instancescores[g];
					}
					//System.out.println();
					//sanity check
					if (Math.round(instancetotal) != totalinstancescores) {
						System.out.println("Error, total scores for this instance ("+instancetotal+") do not add up to " + totalinstancescores);
						System.exit(-1);
					}
				} catch (IOException a1) {
					System.err.println(a1.getMessage());
					System.exit(0);
				}
			}
			//System.out.println(" Domain Total ");
			double domaintotal = 0;
			for (int g = 0; g < domainscores.length; g++) {
				System.out.println(domainscores[g]);
				domaintotal += domainscores[g];
			}
			System.out.println();
			//sanity check
			if (Math.round(domaintotal) != totaldomainscores) {
				System.out.println("Error, total scores for this domain ("+domaintotal+") do not add up to " + totaldomainscores);
				System.exit(-1);
			}
		}
		//System.out.println(" Overall Total ");
		for (int g = 0; g < scores.length; g++) {
			System.out.println(scores[g]);
		}System.out.println();

		//		String domain = "";
		//		switch(d) {
		//		case 0: domain = "SAT";break;
		//		case 1: domain = "BinPacking"; break;
		//		case 2: domain = "PersonnelScheduling"; break;
		//		case 3: domain = "FlowShop"; break;
		//		default: 	System.err.println("wrong input for the problem domain");
		//		System.exit(-1);
		//		}
		//		String[] name = new String[hhs];
		//		for (int hh = 0; hh < hhs; hh++) {
		//			name[hh] = Runner.loadHyperHeuristic(hh, 10000, new Random(1000)).toString();
		//		}
		//		int count = 1;
		//		for (String s : name) {
		//			System.out.println(count++ + " " + s);
		//		}
		//		try {
		//			FileWriter f = new FileWriter("scores.txt");
		//			PrintWriter buffprint = new PrintWriter(f);
		//			for (String s : name) {
		//				System.out.println(s);
		//			}
		//			buffprint.println();
		//			buffprint.close();
		//			f.close();
		//		} catch (IOException a1) {
		//			System.err.println(a1.getMessage());
		//			System.exit(0);
		//		}
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


