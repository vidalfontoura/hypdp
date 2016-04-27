/// *
// * Copyright 2015, Charter Communications, All rights reserved.
// */
// package edu.ufpr.cbio.psp.custom.operators.recent;
//
// import org.junit.Before;
// import org.junit.Test;
// import org.uma.jmetal.core.Solution;
// import org.uma.jmetal.encoding.variable.Int;
//
// import edu.ufpr.cbio.psp.problem.PSPProblem;
// import
/// edu.ufpr.cbio.psp.problem.custom.operators.recent.MultiPointsCrossover;
//
/// **
// *
// *
// * @author Vidal
// */
// public class MultiPointsCrossoverTest {
//
// private MultiPointsCrossover crossover;
//
// @Before
// public void setup() {
//
// crossover = new MultiPointsCrossover(1.0);
// }
//
// @Test
// public void test() throws ClassNotFoundException {
//
// String aminoAcidSequence = "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH";
//
// Solution[] parents = new Solution[2];
//
// Solution parent1 = new Solution(new PSPProblem(aminoAcidSequence, 2));
//
// Solution parent2 = new Solution(new PSPProblem(aminoAcidSequence, 2));
//
// parents[0] = parent1;
// parents[1] = parent2;
//
// for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
// Int value = (Int) parent1.getDecisionVariables()[i];
// System.out.print(value + ",");
// }
// System.out.println();
// for (int i = 0; i < parent2.getDecisionVariables().length; i++) {
// Int value = (Int) parent2.getDecisionVariables()[i];
// System.out.print(value + ",");
// }
//
// Solution[] offspring = (Solution[]) crossover.execute(parents);
// System.out.println();
// for (int i = 0; i < offspring[0].getDecisionVariables().length; i++) {
// Int value = (Int) offspring[0].getDecisionVariables()[i];
// System.out.print(value + ",");
// }
// System.out.println();
// for (int i = 0; i < offspring[1].getDecisionVariables().length; i++) {
// Int value = (Int) offspring[1].getDecisionVariables()[i];
// System.out.print(value + ",");
// }
//
// }
//
// }
