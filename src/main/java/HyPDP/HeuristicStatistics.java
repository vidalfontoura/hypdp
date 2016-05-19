/*
 * Copyright 2016, Charter Communications,  All rights reserved.
 */
package HyPDP;

/**
 *
 *
 * @author vfontoura
 */
public class HeuristicStatistics {

	private int heuristicId;

	private double rank;

	private int elapsedTime;

	/**
	 * @return the heuristicId
	 */
	public int getHeuristicId() {
		return heuristicId;
	}

	/**
	 * @param heuristicId
	 *            the heuristicId to set
	 */
	public void setHeuristicId(int heuristicId) {
		this.heuristicId = heuristicId;
	}

	/**
	 * @return the rank
	 */
	public double getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(double rank) {
		this.rank = rank;
	}

	/**
	 * @return the elapsedTime
	 */
	public int getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime
	 *            the elapsedTime to set
	 */
	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public void printHeuristicsStatistics() {
		System.out.println("id=" + heuristicId + ",rank=" + rank + ",elapsedTime=" + elapsedTime);
	}

}
