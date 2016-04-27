/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package PDP;

/**
 *
 *
 * @author vfontoura
 */
public class PDPSolution {

	private int[] variables;
	private Double fitness;
	private int numberOfVariables;

	public PDPSolution(int numberOfVariables) {
		variables = new int[numberOfVariables];
		fitness = null;
	}

	public int[] getVariables() {
		return variables;

	}

	public void setVariables(int[] variables) {
		this.variables = variables;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(fitness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PDPSolution other = (PDPSolution) obj;
		if (Double.doubleToLongBits(fitness) != Double.doubleToLongBits(other.fitness))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}

	public PDPSolution copySolution() {
		PDPSolution pdpSolution = new PDPSolution(this.numberOfVariables);

		int[] newVariables = new int[variables.length];
		for (int i = 0; i < variables.length; i++) {
			newVariables[i] = variables[i];
		}
		pdpSolution.setVariables(variables);
		return pdpSolution;

	}

}
