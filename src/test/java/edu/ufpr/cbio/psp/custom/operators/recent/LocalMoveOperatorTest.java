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
// import edu.ufpr.cbio.psp.problem.custom.operators.recent.LocalMoveOperator;
//
/// **
// *
// *
// * @author Vidal
// */
// public class LocalMoveOperatorTest {
//
// private LocalMoveOperator operator;
//
// @Before
// public void setup() {
//
// operator = new LocalMoveOperator(1.0);
// }
//
// @Test
// public void test() throws ClassNotFoundException {
//
// String aminoAcidSequence = "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH";
//
// Solution parent1 = new Solution(new PSPProblem(aminoAcidSequence, 2));
//
// for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
// Int value = (Int) parent1.getDecisionVariables()[i];
// System.out.print(value + ",");
// }
//
// Solution offspring = (Solution) operator.execute(parent1);
// System.out.println();
// for (int i = 0; i < offspring.getDecisionVariables().length; i++) {
// Int value = (Int) offspring.getDecisionVariables()[i];
// System.out.print(value + ",");
// }
//
// System.out.println();
// for (int i = 0; i < parent1.getDecisionVariables().length; i++) {
// Int value = (Int) parent1.getDecisionVariables()[i];
// System.out.print(value + ",");
// }
//
// }
//
// }
