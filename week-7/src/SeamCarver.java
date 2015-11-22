import edu.princeton.cs.algs4.DoublingRatio;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;

/**
 * Created by theluxury on 11/21/15.
 */
public class SeamCarver {
    // create a seam carver object based on the given picture
    private Picture picture;

    private class MatrixIndex {
        final int row;
        final int column;

        public MatrixIndex(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new java.lang.NullPointerException("Null object no good.");
        }
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= picture.width() || y < 0 || y >= picture.height()) {
            throw new java.lang.IndexOutOfBoundsException("Pixels not in picture range.");
        }

        // Border energy set at 1000 for reasons.
        if (x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1) {
            return 1000.0;
        }

        Color toTheLeft = picture.get(x - 1, y);
        Color toTheRight = picture.get(x + 1, y);
        Color above = picture.get(x, y - 1);
        Color below = picture.get(x, y + 1);

        return Math.sqrt(getGradient(toTheLeft, toTheRight) + getGradient(above, below));
    }

    private double getGradient(Color c1, Color c2) {
        int[] c1rgb = new int[]{c1.getRed(), c1.getGreen(), c1.getBlue()};
        int[] c2rgb = new int[]{c2.getRed(), c2.getGreen(), c2.getBlue()};

        double sum = 0.0;
        for (int i = 0; i < c1rgb.length; i++) {
            // Apparently this is faster than Math.pow
            sum += (c1rgb[i] - c2rgb[i]) * (c1rgb[i] - c2rgb[i]);
        }

        return sum;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transposePicture();
        int[] horizontalSeam = findVerticalSeam();
        transposePicture();
//        for (int i = 0; i < horizontalSeam.length; i++) {
//            // Think the indexing for reversed?
//            horizontalSeam[i] = picture.height() - 1 - horizontalSeam[i];
//        }
        return horizontalSeam;
    }

    private void transposePicture() {
        // constructor takes width/height
        Picture transposedPicture = new Picture(picture.height(), picture.width());
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                transposedPicture.set(i, j, picture.get(j, i));
            }
        }

        picture = transposedPicture;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // energy matrix dimensions are regular matrix ones.
        double[][] energyMatrix = new double[picture.height()][picture.width()];

        // Fill energyMatrix with values.
        for (int i = 0; i < energyMatrix.length; i++) {
            for (int j = 0; j < energyMatrix[0].length; j++) {
                // Think this is right since picture dimensions are reversed.
                energyMatrix[i][j] = energy(j, i);
            }
        }

        // Then, uh, go down. For each cell, value will the the
        // min(routes of there) + energy of cell, since you can only reach that cell
        // from one of the 3 cells above it.
        // Works since go from top to bottom.
        for (int i = 1; i < energyMatrix.length; i++) {
            for (int j = 0; j < energyMatrix[0].length; j++) {
                energyMatrix[i][j] += getMinPathTo(i, j, energyMatrix);
            }
        }

        // Then, get index of min number in bottom row, and then just go up following the min
        // route.
        MatrixIndex seamEnd = getIndexOfMin(energyMatrix.length - 1, energyMatrix);

        // Then, get an array of the results.
        MatrixIndex[] seam = new MatrixIndex[energyMatrix.length];
        seam[0] = seamEnd;
        getSeam(energyMatrix, seam);

        // Then, return the seam, but backwards.
        return getArrayFromSeam(seam);
    }

    private int[] getArrayFromSeam(MatrixIndex[] seam) {
        int[] seamArray = new int[seam.length];
        for (int i = seam.length - 1; i >= 0; i--) {
            seamArray[seam.length - 1 - i] = seam[i].column;
        }
        return seamArray;
    }

    private void getSeam(double[][] energyMatrix, MatrixIndex[] seam) {
        int i = 1;
        // The first element is already set.
        while (i < seam.length) {
            seam[i] = getIndexMinPathTo(seam[i - 1], energyMatrix);
            i++;
        }
    }

    private MatrixIndex getIndexMinPathTo(MatrixIndex pixel, double[][] energyMatrix) {
        if (pixel.row == 0) {
            throw new java.lang.IndexOutOfBoundsException("Cant get path to top dag.");
        }

        // Assume it's just the one above it.
        MatrixIndex minIndexPath = new MatrixIndex(pixel.row - 1, pixel.column);
        double minPathGradient = energyMatrix[pixel.row - 1][pixel.column];

        // Check if comes from top left.
        if (pixel.column != 0 && energyMatrix[pixel.row - 1][pixel.column - 1] < minPathGradient) {
            minIndexPath = new MatrixIndex(pixel.row - 1, pixel.column - 1);
            minPathGradient = energyMatrix[pixel.row - 1][pixel.column - 1];
        }

        // check if comes from top right
        if (pixel.column != energyMatrix[pixel.row].length - 1 &&
                energyMatrix[pixel.row - 1][pixel.column + 1] < minPathGradient) {
            minIndexPath = new MatrixIndex(pixel.row - 1, pixel.column + 1);
        }

        return minIndexPath;

    }

    // Gets index of the min value in a row for the matrix.
    private MatrixIndex getIndexOfMin(int row, double[][] energyMatrix) {
        double min = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < energyMatrix[row].length; i++) {
            if (energyMatrix[row][i] < min) {
                min = energyMatrix[row][i];
                minIndex = i;
            }
        }
        return new MatrixIndex(row, minIndex);
    }

    private double getMinPathTo(int i, int j, double[][] energyMatrix) {
        // Shouldn't happen but just in case.
        if (i == 0) {
            throw new java.lang.IndexOutOfBoundsException("Cant get path to top dag.");
        }

        // One right above it.
        double energy = energyMatrix[i - 1][j];
        // Try route from top left if j not 0.
        if (j != 0) {
            energy = Math.min(energy, energyMatrix[i - 1][j - 1]);
        }
        // try route from top route if j not border.
        if (j != energyMatrix[0].length - 1) {
            energy = Math.min(energy, energyMatrix[i - 1][j + 1]);
        }

        return energy;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.NullPointerException("Null seam no good.");
        }

        if (seam.length != picture.width()) {
            throw new java.lang.IllegalArgumentException("Wrong width no good.");
        }

        if (picture.height() == 1) {
            throw new java.lang.IllegalArgumentException("Height already at one dag. ");
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new java.lang.NullPointerException("Null seam no good.");
        }
        if (seam.length != picture.height()) {
            throw new java.lang.IllegalArgumentException("Wrong width no good.");
        }
        if (picture.width() == 1) {
            throw new java.lang.IllegalArgumentException("Width already at one dag. ");
        }
    }


}
