/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package PDP;

/**
 *
 *
 * @author vfontoura
 */
public class Residue implements Cloneable {

    private Residue.Point point;
    private ResidueType residueType;

    /**
     */
    public Residue() {

        // TODO Auto-generated constructor stub
    }

    /**
     * @param point
     * @param residueType
     */
    public Residue(Point point, ResidueType residueType) {

        super();
        this.point = point;
        this.residueType = residueType;
    }

    /**
     * @return the point that represents the coordinates of the residue in the
     *         space.
     */
    public Residue.Point getPoint() {

        return point;
    }

    /**
     * @param point. The point to set
     */
    public void setPoint(Residue.Point point) {

        this.point = point;
    }

    /**
     * @return the residueType
     */
    public ResidueType getResidueType() {

        return residueType;
    }

    /**
     * @param residueType. The residueType to set
     */
    public void setResidueType(ResidueType residueType) {

        this.residueType = residueType;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((point == null) ? 0 : point.hashCode());
        result = prime * result + ((residueType == null) ? 0 : residueType.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
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
        Residue other = (Residue) obj;
        if (point == null) {
            if (other.point != null)
                return false;
        } else if (!point.equals(other.point))
            return false;
        if (residueType != other.residueType)
            return false;
        return true;
    }

    public static class Point implements Cloneable {

        public int x;
        public int y;

        /**
         * @param x
         * @param y
         */
        public Point(int x, int y) {

            super();
            this.x = x;
            this.y = y;
        }

        /**
         * @return the x
         */
        public int getX() {

            return x;
        }

        /**
         * @param x the x to set
         */
        public void setX(int x) {

            this.x = x;
        }

        /**
         * @return the y
         */
        public int getY() {

            return y;
        }

        /**
         * @param y the y to set
         */
        public void setY(int y) {

            this.y = y;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        /*
         * (non-Javadoc)
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
            Point other = (Point) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#clone()
         */
        @Override
        public Object clone() {

            return new Point(x, y);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {

            return x + "," + y;
        }

    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {

        return new Residue((Point) this.point.clone(), residueType);
    }
}
