import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

import java.util.*;

/**
 * Created by theluxury on 12/14/15.
 */
public class BoggleSolver {

    private Set<String> dictionary = new HashSet<>();
    // This just has to be arbitrarily big, bigger than possible row.
    private final static int prime = 997;
    private final int[] points = new int[]{0, 0, 0, 1, 1, 2, 3, 5, 11};
    private CustomTrieSET trieSET = new CustomTrieSET();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            this.dictionary.add(word);
            trieSET.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> foundWords = new HashSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                Set<Integer> alreadyCheckedSet = new HashSet<>();
                checkForWord(i, j, "", alreadyCheckedSet, foundWords, board, this.dictionary);
            }
        }
        return foundWords;
    }

    private void checkForWord(int row, int column, String stringUntilNow, Set<Integer> alreadyCheckedSet,
                              Set<String> foundWords, BoggleBoard board, Set<String> dictionary) {
        // The key optimization?
        if (!trieSET.containsPrefix(stringUntilNow)) {
            return;
        }
        int uniqueIJPairNumber = row * prime + column;
        // If out of bounds return
        if (row < 0 || row >= board.rows() || column < 0 || column >= board.cols()) {
            return;
        }
        // If this (i,j) combo has already been checked, return. Row*prime + column should be unique
        // for column size up to prime which is 997. Doing this instead of list for optimization.
        if (alreadyCheckedSet.contains(uniqueIJPairNumber)) {
            return;
        }

        // starting at 12 going clockwise
        final int[] columnDirection = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
        final int[] rowDirection = new int[]{-1, -1, 0, 1, 1, 1, 0, -1};
        String newString;
        if (board.getLetter(row, column) == 'Q') {
            newString = stringUntilNow + "QU";
        } else {
            newString = stringUntilNow + board.getLetter(row, column);
        }
        // Check if it contains the found letters until now.
        if (dictionary.contains(newString) && newString.length() >= 3) {
            foundWords.add(newString);
        }


        for (int i = 0; i < columnDirection.length; i++) {
            Set<Integer> newAlreadyCheckedSet = new HashSet<>(alreadyCheckedSet);
            newAlreadyCheckedSet.add(uniqueIJPairNumber);
            checkForWord(row + rowDirection[i], column + columnDirection[i], newString, newAlreadyCheckedSet,
                    foundWords, board, dictionary);
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.contains(word)) {
            return 0;
        }
        // Most is 11.
        if (word.length() >= points.length) {
            return 11;
        } else {
            return points[word.length()];
        }
    }

    public static void main(String[] args) {
        In in = new In("/Users/theluxury/Documents/algs/week-9/boggle/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("/Users/theluxury/Documents/algs/week-9/boggle/board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
