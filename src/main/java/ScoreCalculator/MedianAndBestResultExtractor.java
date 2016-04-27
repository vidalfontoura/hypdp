package ScoreCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/*
 * @author Dr Matthew Hyde
 * mvh@cs.nott.ac.uk
 * This is to get the median and best values of the 31 runs, 
 * and also obtain the ten values in the correct format to produce boxplots
 * 
 * input
 * instance0.txt instance1.txt etc, for each domain
 * time0.txt time1.txt etc, for each domain
 * 
 * output
 * two sets of files. One prefixed with "best-", and one with "median-".
 * best-HH1 best-HH2 etc, and median-HH1 median-HH2 etc...
 * two files per HH. Median and best scores from 31 runs, for each instance. 
 * In the format of the leaderboard, so the scores can be calculated.
 * time.txt, a file with the traces of all of the median runs, so graphs can be drawn.
 * 
 */
public class MedianAndBestResultExtractor {
	
	
	public static void main (String[] argv) {
		

		int hhs = 20;
		int instances = 5;
		int runs = 31;
		int domains = 6;
		
		int medianindex = 15;
		int bestindex = 30;
		
		try {
			String[] hhnames = {
					"1 HsiaoCHeSCHyperheuristic",
					"2 sa_ilsHyperHeuristic",
					"3 Dynamic Iterated Local Search With Non Improvement Bias",
					"4 ML",
					"5 AdaptiveHH",
					"6 SimSATS_HH",
					"7 EPH by David M.",
					"8 CS-PUT Genetic Hive Hyper Heuristic",
					"9 Pearl Hunter  0.0.6",
					"10 ACO_HH",
					"11 LehrbaumHAHA",
					"12 ISEA Hyper-Heuristic",
					"13 GISS1",
					"14 ElomariSS",
					"15 ShafiXCJ",
					"16 AVEG_NeptuneHyperHeuristic",
					"17 Single objective Markov chain Hyper-heuristic (MCHH-S)",
					"18 HAEA Hybrid Adaptive Evolutionary Algorithm",
					"19 Ant-Q Hyper Heuristic",
					"20 Test Hyper Heuristic (iridia.ulb.ac.be)",
			};
			if (hhnames.length != hhs) {System.out.println("not the correct amount of names");	System.exit(-1);}

			PrintWriter[] median_HH_files = new PrintWriter[hhs];
			for (int x = 0; x < hhnames.length; x++) {
				median_HH_files[x] = new PrintWriter(new FileWriter("RRresults/median-"+hhnames[x]+".txt"));
			}
			PrintWriter[] best_HH_files = new PrintWriter[hhs];
			for (int x = 0; x < hhnames.length; x++) {
				best_HH_files[x] = new PrintWriter(new FileWriter("RRresults/best-"+hhnames[x]+".txt"));
			}

			//1) first get the median results
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

				ResultWithID[][] median_results = new ResultWithID[instances][hhs];
				ResultWithID[][] best_results = new ResultWithID[instances][hhs];
				for (int i = 0; i < instances; i++) {

					String filename = "RRresults/"+d+"/instance"+i+".txt";
					FileReader read = new FileReader(filename);
					BufferedReader buffread = new BufferedReader(read);

					ResultWithID[][] res = new ResultWithID[hhs][runs];
					for (int r = 0; r < runs; r++) {

						String line = buffread.readLine();
						String[] results = line.split(" ");
						//convert to doubles
						for (int s = 0; s < hhs; s++) {
							res[s][r] = new ResultWithID(r, Double.parseDouble(results[s]));
						}
					}

					buffread.close();
					read.close();

					for (ResultWithID[] da : res) {
						Arrays.sort(da);
					}

					//					System.out.println();
					//					for (ResultWithID[] da : res) {
					//						for (ResultWithID di : da) {
					//							System.out.print("("+di.id+")"+ di.result + " ");
					//						}System.out.println();
					//					}


					//print medians to individual files
					for (int m = 0; m < hhs; m++) {
						median_results[i][m] = new ResultWithID(res[m][medianindex].id, res[m][medianindex].result);
						median_HH_files[m].print(median_results[i][m].result);
						if (i != instances-1) {
							median_HH_files[m].print(", ");
						} else {
							median_HH_files[m].println();
						}
					}
					
					//print bests to individual files
					for (int m = 0; m < hhs; m++) {
						best_results[i][m] = new ResultWithID(res[m][bestindex].id, res[m][bestindex].result);
						best_HH_files[m].print(best_results[i][m].result);
						if (i != instances-1) {
							best_HH_files[m].print(", ");
						} else {
							best_HH_files[m].println();
						}
					}

//					for (int m = 0; m < hhs; m++) {
//						System.out.print(median_results[i][m].result + " ");
//					}System.out.println(); 

					//					for (ResultWithID di : median_results[i]) {
					//						System.out.print("("+di.id+")"+ di.result + " ");
					//					}System.out.println();

				}


				//2) secondly, extract the trace data which relates to the median run. This can then be used for the graphs

				FileWriter f = new FileWriter("RRresults/" + d + "/time.txt");
				PrintWriter buffprint = new PrintWriter(f);

				for (int i = 0; i < instances; i++) {
					String[] lines = new String[hhs];
					String filename = "RRresults/"+d+"/time"+i+".txt";
					FileReader read = new FileReader(filename);
					BufferedReader buffread = new BufferedReader(read);
					String s;
					for (int r = 0; r < runs; r++) {
						for (int h = 0; h < hhs; h++) {
							s = buffread.readLine();
							if (median_results[i][h].id == r) {
								lines[h] = s;
							}
						}
					}

					buffread.close();
					read.close();

					for (String g : lines) {
						buffprint.println(g);
					}

				}
				buffprint.close();
				f.close();



			}//end looping domains

			for (int x = 0; x < median_HH_files.length; x++) {
				median_HH_files[x].close();
				best_HH_files[x].close();
			}
		} catch (IOException a1) {
			System.err.println(a1.getMessage());
			System.exit(0);
		}
	}

}
class ResultWithID implements Comparable<ResultWithID> {
	int id; 
	double result;
	public ResultWithID(int i, double r) {
		id = i;
		result = r;
	}
	public int compareTo(ResultWithID other) {
		if (this.result < other.result) {
			return 1;
		} else if (this.result > other.result) {
			return -1;
		} else {
			return 0;
		}
	}
}


