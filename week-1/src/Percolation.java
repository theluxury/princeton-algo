import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by theluxury on 9/10/15.
 */


public class Percolation {

    private WeightedQuickUnionUF uf;
    private boolean[] open;
    private int width;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("Have to put in an N that is bigger than 0");
        }
        width = N;
        uf = new WeightedQuickUnionUF(N * N + 2);
        open = new boolean[N * N + 2];
        open[N * N] = true;
        open[N * N + 1] = true;
    }   // create N-by-N grid, with all sites blocked


    public void open(int i, int j) {

        checkIndex(i, j);

        int arrayPosition = (i-1) * width + (j) - 1;
        open[arrayPosition] = true;

        int above;
        int below;

        if (i == 1) {
            above = width * width;
        } else {
            above = arrayPosition - width;
        }

        if (i == width) {
            below = width * width + 1;
        } else {
            below = arrayPosition + width;
        }

        if (open[above]) {
            uf.union(arrayPosition, above);
        }
        if (open[below]) {
            uf.union(arrayPosition, below);
        }

        if (j != 1 && open[arrayPosition - 1]) {
            uf.union(arrayPosition, arrayPosition - 1);
        }
        if (j != width && open[arrayPosition + 1]) {
            uf.union(arrayPosition, arrayPosition + 1);
        }
    }         // open site (row i, column j) if it is not open already


    public boolean isOpen(int i, int j) {
        checkIndex(i, j);
        int arrayPosition = (i-1) * width + (j) - 1;
        return open[arrayPosition];
    }    // is site (row i, column j) open?


    public boolean isFull(int i, int j) {
        checkIndex(i, j);
        int arrayPosition = (i-1) * width + (j) - 1;
        return uf.connected(arrayPosition, width * width);
    }   // is site (row i, column j) full?


    public boolean percolates() {
        return uf.connected(width * width, width * width + 1);
    }        // does the system percolate?

    private void checkIndex(int i, int j) {
        if (i < 1 || i > width || j < 1 || j > width) {
            throw new java.lang.IndexOutOfBoundsException("Your indexes for column or row were incorrect");
        }
    }

    public static void main(String[] args) {
    }  // test client (optional)
}
