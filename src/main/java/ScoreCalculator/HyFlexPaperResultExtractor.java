package ScoreCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * @author Dr Matthew Hyde
 * This is to get the median values of the ten runs, and also obtain the ten values in the correct format to produce boxplots
 */
public class HyFlexPaperResultExtractor {
	public static void main (String[] argv) {
		int hhs = 8;
		int instances = 10;
		for (int domain = 0; domain < 4; domain++) {
			String d = "";
			switch(domain) {
			case 0: d = "SAT";break;
			case 1: d = "BinPacking"; break;
			case 2: d = "PersonnelScheduling"; break;
			case 3: d = "FlowShop"; break;
			default: 	System.err.println("wrong input for the problem domain");
			System.exit(-1);
			}
			for (int i = 0; i < instances; i++) {
				try {
					double[] res = new double[hhs];

					String filename = "RRresults/"+d+"/instance"+i+".txt";
					FileReader read = new FileReader(filename);
					BufferedReader buffread = new BufferedReader(read);
					String line = buffread.readLine();
					String[] results = line.split(" ");
					//convert to doubles
					for (int s = 0; s < hhs; s++) {
						res[s] = Double.parseDouble(results[s]);
					}
					buffread.close();
					read.close();

						for (double di : res) {
							System.out.print(di + ", ");
						}System.out.println();
					
				} catch (IOException a1) {
					System.err.println(a1.getMessage());
					System.exit(0);
				}
			}
		}
	}
}


