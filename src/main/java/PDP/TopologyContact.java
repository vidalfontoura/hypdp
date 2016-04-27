/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package PDP;

/**
 *
 *
 * @author user
 */
public class TopologyContact {

	private Residue r1;
	private Residue r2;

	/**
	 * @param r1
	 * @param r2
	 */
	public TopologyContact(Residue r1, Residue r2) {

		super();
		this.r1 = r1;
		this.r2 = r2;
	}

	/**
	 * @return the r1
	 */
	public Residue getR1() {

		return r1;
	}

	/**
	 * @param r1
	 *            the r1 to set
	 */
	public void setR1(Residue r1) {

		this.r1 = r1;
	}

	/**
	 * @return the r2
	 */
	public Residue getR2() {

		return r2;
	}

	/**
	 * @param r2
	 *            the r2 to set
	 */
	public void setR2(Residue r2) {

		this.r2 = r2;
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
		result = prime * result + ((r1 == null) ? 0 : r1.hashCode() + r2.hashCode());
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
		TopologyContact other = (TopologyContact) obj;

		if (this.r1.equals(other.r1) && this.r2.equals(other.r2)) {
			return true;
		}
		if (this.r2.equals(other.r1) && this.r1.equals(other.r2)) {
			return true;
		}
		return false;
	}

}
