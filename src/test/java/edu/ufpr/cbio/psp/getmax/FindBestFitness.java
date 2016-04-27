/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.cbio.psp.getmax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 *
 * @author Vidal
 */
public class FindBestFitness {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        String dir =
            "C:\\work\\workspace\\psp-optimization\\results-server-ufpr\\results-chapter-backtrack-noperators_\\P2H3PH8P3H10PHP3H12P4H6PH2PHP\\NSGAIIBacktrack";

        File rootDir = new File(dir);

        double maxFitness = Double.MAX_VALUE;

        String configMaxFitness = "";

        if (rootDir.isDirectory()) {
            for (File configurationDir : rootDir.listFiles()) {
                if (configurationDir.isDirectory()) {
                    String funFileStr = configurationDir.getAbsolutePath() + File.separator + "FUN.txt";
                    File funFile = new File(funFileStr);

                    if (funFile.exists()) {

                        try (BufferedReader br = new BufferedReader(new FileReader(funFile))) {
                            String[] values = br.readLine().split(" ");
                            double fitness = Double.valueOf(values[0]);

                            if (fitness <= maxFitness) {

                                maxFitness = fitness;
                                configMaxFitness = funFile.getAbsolutePath();
                            }
                        }
                    }

                }
            }
        }

        System.out.println(maxFitness + "  " + configMaxFitness);

    }

}
