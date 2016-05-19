package PDP.fitness;

/**
 *
 *
 * @author vfontoura
 */
public class Grid {

    private int[][] matrix;

    /**
     * @param matrix
     */
    public Grid(int xDim, int yDim) {

        super();
        matrix = new int[xDim + 2][yDim + 2];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = -1;
            }
        }
    }

    /**
     * @return the matrix
     */
    public int[][] getMatrix() {

        return matrix;
    }

    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(int[][] matrix) {

        this.matrix = matrix;
    }

    public void printResidueStructure() {

        // for (int i = matrix.length-1; i >=0 ; i--) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();

    }
}
