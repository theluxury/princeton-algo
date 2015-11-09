import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] blocks;
    private int manhatten;

    public Board(int[][] blocks)  {
        this.blocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            this.blocks[i] = blocks[i].clone();
        }
        manhatten = getManhatten();
    }         // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)

    public int dimension() {
        return blocks.length;
    }                 // board dimension N

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i< blocks.length; i++) {
            for (int j = 0; j<blocks.length; j++) {
                if (blocks[i][j] == 0)
                    continue;
                if (blocks[i][j] != i * blocks.length + j + 1) {
                    hamming++;
                }
            }
        }
        return hamming;
    }                   // number of blocks out of place

    private int getManhatten() {
        int manhattan = 0;
        int size = blocks.length;

        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j++) {
                if (blocks[i][j] == 0)
                    continue;

                int rootRow = (blocks[i][j] - 1) / size;
                int rootColumn = (blocks[i][j] - 1) % size;
                manhattan += Math.abs(i - rootRow) + Math.abs(j - rootColumn);
            }
        }
        return manhattan;
    }

    public int manhattan() {
        return manhatten;
    }                 // sum of Manhattan distances between blocks and goal

    public boolean isGoal() {
        return manhatten == 0;
    }                // is this board the goal board?

    public Board twin() {
        if (dimension() < 2) {
            throw new java.lang.IllegalArgumentException("Can't have a twn for a board of size 1 or 0");
        }

        int[][] twin = copy();

        if (twin[0][0] != 0 && twin[0][1] != 0) {
            swap(twin,0,0,0,1);
        } else if (twin[0][0] != 0 && twin[1][0] != 0) {
            swap(twin,0,0,1,0);
        } else if (twin[0][1] != 0 && twin[1][1] != 0) {
            swap(twin,0,1,1,1);
        } else if (twin[1][0] != 0 && twin[1][1] != 0) {
            swap(twin,1,0,1,1);
        }

        return new Board(twin);
    }                   // a board that is obtained by exchanging any pair of blocks

    private void swap(int[][] block, int row1, int column1, int row2, int column2) {
        int temp = block[row1][column1];
        block[row1][column1] = block[row2][column2];
        block[row2][column2] = temp;
    }

    private int[][] copy() {
        int[][] copy = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            copy[i] = blocks[i].clone();
        }

        return copy;
    }

    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        return toString().equals(that.toString());
    }// does this board equal y?
    public Iterable<Board> neighbors() {
        int emptyRow = Integer.MIN_VALUE;
        int emptyColumn = Integer.MIN_VALUE;
        for (int i = 0; i< blocks.length; i++) {
            if (emptyRow != Integer.MIN_VALUE) break;
            for (int j = 0; j<blocks.length; j++) {
                if (blocks[i][j] == 0) {
                    emptyRow = i;
                    emptyColumn = j;
                    break;
                }
            }
        }

        if (emptyRow == Integer.MIN_VALUE) {
            throw new java.lang.IllegalArgumentException("No neighbors for some reason. Elephino");
        }

        ArrayList<Board> neighbors = new ArrayList<Board>();
        if (emptyRow != 0) {
            int[][] newBlock = copy();
            newBlock[emptyRow - 1][emptyColumn] = blocks[emptyRow][emptyColumn];
            newBlock[emptyRow][emptyColumn] = blocks[emptyRow - 1][emptyColumn];
            neighbors.add(new Board(newBlock));
        }

        if (emptyRow != blocks.length - 1) {
            int[][] newBlock = copy();
            newBlock[emptyRow + 1][emptyColumn] = blocks[emptyRow][emptyColumn];
            newBlock[emptyRow][emptyColumn] = blocks[emptyRow + 1][emptyColumn];
            neighbors.add(new Board(newBlock));
        }

        if (emptyColumn % blocks.length != 0) {
            int[][] newBlock = copy();
            newBlock[emptyRow][emptyColumn - 1] = blocks[emptyRow][emptyColumn];
            newBlock[emptyRow][emptyColumn] = blocks[emptyRow][emptyColumn - 1];
            neighbors.add(new Board(newBlock));
        }

        if (emptyColumn % blocks.length != blocks.length - 1) {
            int[][] newBlock = copy();
            newBlock[emptyRow][emptyColumn + 1] = blocks[emptyRow][emptyColumn];
            newBlock[emptyRow][emptyColumn] = blocks[emptyRow][emptyColumn + 1];
            neighbors.add(new Board(newBlock));
        }

        return neighbors;
    }    // all neighboring boards

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(blocks.length + "\n");
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }               // string representation of this board (in the output format specified below)

    public static void main(String[] args) {

    } // unit tests (not graded)
}