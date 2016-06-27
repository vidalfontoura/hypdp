/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package HyPDP;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;

/**
 *
 *
 * @author vfontoura
 */
public class HeuristicSelectionInfo {

	private int heuristicId;

	private LinkedList<Double> RC;

	private int Cbest;

	private int Ccurrent;

	private int Caccept;

	private List<Double> Cava;

	private int Cr;

	private int rcWindowSize;

	public HeuristicSelectionInfo(int w) {
		rcWindowSize = w;
		RC = new LinkedList<>();
		Cava = new ArrayList<>();
	}

	/**
	 * @return the rC
	 */
	public double getRCMaxValue() {
		OptionalDouble max = RC.stream().mapToDouble(Double::doubleValue).max();
		if (max.isPresent()) {
			return max.getAsDouble();
		}
		return 0.0;
	}

	/**
	 * @param rC
	 *            the rC to set
	 */
	public void addRC(double value) {
		if (RC.size() == rcWindowSize) {
			RC.removeFirst();
		}
		RC.add(value);
	}

	/**
	 * @return the cbest
	 */
	public int getCbest() {
		return Cbest;
	}

	/**
	 * @param cbest
	 *            the cbest to set
	 */
	public void setCbest(int cbest) {
		Cbest = cbest;
	}

	/**
	 * @return the ccurrent
	 */
	public int getCcurrent() {
		return Ccurrent;
	}

	/**
	 * @param ccurrent
	 *            the ccurrent to set
	 */
	public void setCcurrent(int ccurrent) {
		Ccurrent = ccurrent;
	}

	/**
	 * @return the caccept
	 */
	public int getCaccept() {
		return Caccept;
	}

	/**
	 * @param caccept
	 *            the caccept to set
	 */
	public void setCaccept(int caccept) {
		Caccept = caccept;
	}

	/**
	 * @return the cava
	 */
	public double getCava() {

		OptionalDouble average = Cava.stream().mapToDouble(Double::doubleValue).average();
		if (average.isPresent()) {
			return average.getAsDouble();
		}
		return 0.0;
	}

	/**
	 * @param cava
	 *            the cava to set
	 */
	public void addCava(double cava) {
		Cava.add(cava);
	}

	/**
	 * @return the cr
	 */
	public int getCr() {
		return Cr;
	}

	/**
	 * @param cr
	 *            the cr to set
	 */
	public void setCr(int cr) {
		Cr = cr;
	}

	/**
	 * @param heuristicId
	 *            the heuristicId to set
	 */
	public void setHeuristicId(int heuristicId) {
		this.heuristicId = heuristicId;
	}

	/**
	 * @return the heuristicId
	 */
	public int getHeuristicId() {
		return heuristicId;
	}

}
