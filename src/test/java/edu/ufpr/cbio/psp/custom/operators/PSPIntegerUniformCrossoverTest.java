//package edu.ufpr.cbio.psp.custom.operators;
//
//import junit.framework.Assert;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.uma.jmetal.core.Solution;
//import org.uma.jmetal.core.Variable;
//import org.uma.jmetal.encoding.solutiontype.IntSolutionType;
//import org.uma.jmetal.encoding.variable.Int;
//import org.uma.jmetal.util.random.PseudoRandom;
//
//import edu.ufpr.cbio.psp.problem.PSPProblem;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(PseudoRandom.class)
//public class PSPIntegerUniformCrossoverTest {
//
//    private PSPIntegerUniformCrossover integerUniformCrossover;
//
//    @Before
//    public void setUp() {
//
//        PowerMockito.mockStatic(PseudoRandom.class);
//
//        integerUniformCrossover = new PSPIntegerUniformCrossover.Builder().crossoverProbability(1).build();
//    }
//
//    /**
//     * Tests this scenario
//     *
//     * <pre>
//     * mask: 1,0,1,1,0,0
//     * 
//     * parent 1: 1,0,2,1,1,0
//     * parent 2: 2,2,1,0,2,2
//     * 
//     * child 1: 2,0,1,0,1,0
//     * child 2: 1,2,2,1,2,2
//     * </pre>
//     */
//    @Test
//    public void testUniformCrossover1() {
//
//        PowerMockito.when(PseudoRandom.randInt(0, 1)).thenAnswer(new Answer<Integer>() {
//
//            int count = 0;
//
//            @Override
//            public Integer answer(InvocationOnMock invocation) throws Throwable {
//
//                switch (count) {
//                    case 0:
//                        count++;
//                        return 1;
//                    case 1:
//                        count++;
//                        return 0;
//                    case 2:
//                        count++;
//                        return 1;
//                    case 3:
//                        count++;
//                        return 1;
//                    case 4:
//                        count++;
//                        return 0;
//                    case 5:
//                        count++;
//                        return 0;
//                    default:
//                        return 0;
//                }
//            }
//        });
//
//        Solution[] parents = new Solution[2];
//        Solution parent1 = new Solution();
//        Solution parent2 = new Solution();
//
//        PSPProblem pspProblem = new PSPProblem("HHPPHHPP", 2);
//        parent1.setType(new IntSolutionType(pspProblem));
//        parent2.setType(new IntSolutionType(pspProblem));
//
//        Int variable1 = new Int(0, 2);
//        variable1.setValue(1);
//
//        Int variable2 = new Int(0, 2);
//        variable2.setValue(0);
//
//        Int variable3 = new Int(0, 2);
//        variable3.setValue(2);
//
//        Int variable4 = new Int(0, 2);
//        variable4.setValue(1);
//
//        Int variable5 = new Int(0, 2);
//        variable5.setValue(1);
//
//        Int variable6 = new Int(0, 2);
//        variable6.setValue(0);
//
//        Variable[] variables = new Variable[] { variable1, variable2, variable3, variable4, variable5, variable6 };
//        parent1.setDecisionVariables(variables);
//
//        variable1 = new Int(0, 2);
//        variable1.setValue(2);
//
//        variable2 = new Int(0, 2);
//        variable2.setValue(2);
//
//        variable3 = new Int(0, 2);
//        variable3.setValue(1);
//
//        variable4 = new Int(0, 2);
//        variable4.setValue(0);
//
//        variable5 = new Int(0, 2);
//        variable5.setValue(2);
//
//        variable6 = new Int(0, 2);
//        variable6.setValue(2);
//
//        variables = new Variable[] { variable1, variable2, variable3, variable4, variable5, variable6 };
//        parent2.setDecisionVariables(variables);
//
//        parents[0] = parent1;
//        parents[1] = parent2;
//
//        Solution[] offSpring = (Solution[]) integerUniformCrossover.execute(parents);
//
//        Variable[] decisionVariables1 = offSpring[0].getDecisionVariables();
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables1[0].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables1[1].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables1[2].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables1[3].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables1[4].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables1[5].toString()));
//
//        Variable[] decisionVariables2 = offSpring[1].getDecisionVariables();
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables2[0].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[1].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[2].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables2[3].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[4].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[5].toString()));
//
//    }
//
//    /**
//     * Tests this scenario
//     *
//     * <pre>
//     * mask: 1,1,1,1,1,1
//     * 
//     * parent 1: 1,0,2,1,1,0
//     * parent 2: 2,2,1,0,2,2
//     * 
//     * child 1: 2,2,1,0,2,2
//     * child 2: 1,0,2,1,1,0
//     * </pre>
//     */
//    @Test
//    public void testUniformCrossover2() {
//
//        PowerMockito.when(PseudoRandom.randInt(0, 1)).thenAnswer(new Answer<Integer>() {
//
//            int count = 0;
//
//            @Override
//            public Integer answer(InvocationOnMock invocation) throws Throwable {
//
//                switch (count) {
//                    case 0:
//                        count++;
//                        return 1;
//                    case 1:
//                        count++;
//                        return 1;
//                    case 2:
//                        count++;
//                        return 1;
//                    case 3:
//                        count++;
//                        return 1;
//                    case 4:
//                        count++;
//                        return 1;
//                    case 5:
//                        count++;
//                        return 1;
//                    default:
//                        return 0;
//                }
//            }
//        });
//
//        Solution[] parents = new Solution[2];
//        Solution parent1 = new Solution();
//        Solution parent2 = new Solution();
//
//        PSPProblem pspProblem = new PSPProblem("HHPPHHPP", 2);
//        parent1.setType(new IntSolutionType(pspProblem));
//        parent2.setType(new IntSolutionType(pspProblem));
//
//        Int variable1 = new Int(0, 2);
//        variable1.setValue(1);
//
//        Int variable2 = new Int(0, 2);
//        variable2.setValue(0);
//
//        Int variable3 = new Int(0, 2);
//        variable3.setValue(2);
//
//        Int variable4 = new Int(0, 2);
//        variable4.setValue(1);
//
//        Int variable5 = new Int(0, 2);
//        variable5.setValue(1);
//
//        Int variable6 = new Int(0, 2);
//        variable6.setValue(0);
//
//        Variable[] variables = new Variable[] { variable1, variable2, variable3, variable4, variable5, variable6 };
//        parent1.setDecisionVariables(variables);
//
//        variable1 = new Int(0, 2);
//        variable1.setValue(2);
//
//        variable2 = new Int(0, 2);
//        variable2.setValue(2);
//
//        variable3 = new Int(0, 2);
//        variable3.setValue(1);
//
//        variable4 = new Int(0, 2);
//        variable4.setValue(0);
//
//        variable5 = new Int(0, 2);
//        variable5.setValue(2);
//
//        variable6 = new Int(0, 2);
//        variable6.setValue(2);
//
//        variables = new Variable[] { variable1, variable2, variable3, variable4, variable5, variable6 };
//        parent2.setDecisionVariables(variables);
//
//        parents[0] = parent1;
//        parents[1] = parent2;
//
//        Solution[] offSpring = (Solution[]) integerUniformCrossover.execute(parents);
//
//        Variable[] decisionVariables1 = offSpring[0].getDecisionVariables();
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables1[0].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables1[1].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables1[2].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables1[3].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables1[4].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables1[5].toString()));
//
//        Variable[] decisionVariables2 = offSpring[1].getDecisionVariables();
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables2[0].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables2[1].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[2].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables2[3].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables2[4].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables2[5].toString()));
//
//    }
//
//    /**
//     * Tests this scenario
//     *
//     * <pre>
//     * mask: 0,0,0,0,0,0
//     * 
//     * parent 1: 1,0,2,1,1,0
//     * parent 2: 2,2,1,0,2,2
//     * 
//     * child 1: 1,0,2,1,1,0
//     * child 2: 2,2,1,0,2,2
//     * </pre>
//     */
//    @Test
//    public void testUniformCrossover3() {
//
//        PowerMockito.when(PseudoRandom.randInt(0, 1)).thenAnswer(new Answer<Integer>() {
//
//            int count = 0;
//
//            @Override
//            public Integer answer(InvocationOnMock invocation) throws Throwable {
//
//                switch (count) {
//                    case 0:
//                        count++;
//                        return 0;
//                    case 1:
//                        count++;
//                        return 0;
//                    case 2:
//                        count++;
//                        return 0;
//                    case 3:
//                        count++;
//                        return 0;
//                    case 4:
//                        count++;
//                        return 0;
//                    case 5:
//                        count++;
//                        return 0;
//                    default:
//                        return 0;
//                }
//            }
//        });
//
//        Solution[] parents = new Solution[2];
//        Solution parent1 = new Solution();
//        Solution parent2 = new Solution();
//
//        PSPProblem pspProblem = new PSPProblem("HHPPHHPP", 2);
//        parent1.setType(new IntSolutionType(pspProblem));
//        parent2.setType(new IntSolutionType(pspProblem));
//
//        Int variable1 = new Int(0, 2);
//        variable1.setValue(1);
//
//        Int variable2 = new Int(0, 2);
//        variable2.setValue(0);
//
//        Int variable3 = new Int(0, 2);
//        variable3.setValue(2);
//
//        Int variable4 = new Int(0, 2);
//        variable4.setValue(1);
//
//        Int variable5 = new Int(0, 2);
//        variable5.setValue(1);
//
//        Int variable6 = new Int(0, 2);
//        variable6.setValue(0);
//
//        Variable[] variables = new Variable[] { variable1, variable2, variable3, variable4, variable5, variable6 };
//        parent1.setDecisionVariables(variables);
//
//        variable1 = new Int(0, 2);
//        variable1.setValue(2);
//
//        variable2 = new Int(0, 2);
//        variable2.setValue(2);
//
//        variable3 = new Int(0, 2);
//        variable3.setValue(1);
//
//        variable4 = new Int(0, 2);
//        variable4.setValue(0);
//
//        variable5 = new Int(0, 2);
//        variable5.setValue(2);
//
//        variable6 = new Int(0, 2);
//        variable6.setValue(2);
//
//        variables = new Variable[] { variable1, variable2, variable3, variable4, variable5, variable6 };
//        parent2.setDecisionVariables(variables);
//
//        parents[0] = parent1;
//        parents[1] = parent2;
//
//        Solution[] offSpring = (Solution[]) integerUniformCrossover.execute(parents);
//
//        Variable[] decisionVariables1 = offSpring[0].getDecisionVariables();
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables1[0].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables1[1].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables1[2].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables1[3].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables1[4].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables1[5].toString()));
//
//        Variable[] decisionVariables2 = offSpring[1].getDecisionVariables();
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[0].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[1].toString()));
//        Assert.assertEquals((Integer) 1, Integer.valueOf(decisionVariables2[2].toString()));
//        Assert.assertEquals((Integer) 0, Integer.valueOf(decisionVariables2[3].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[4].toString()));
//        Assert.assertEquals((Integer) 2, Integer.valueOf(decisionVariables2[5].toString()));
//
//    }
//}
