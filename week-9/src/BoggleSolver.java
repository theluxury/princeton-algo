import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SymbolDigraph;

import java.util.*;

/**
 * Created by theluxury on 12/14/15.
 */
public class BoggleSolver {

    // This just has to be arbitrarily big, bigger than possible row.
    private final static int prime = 11;
    private final int[] points = new int[]{0, 0, 0, 1, 1, 2, 3, 5, 11};
    private CustomTrieSET trieSET = new CustomTrieSET();
    private Set<String> foundWords = new HashSet<>();
    // starting at 12 going clockwise
    private final int[] columnDirection = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
    private final int[] rowDirection = new int[]{-1, -1, 0, 1, 1, 1, 0, -1};

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            // this didn't make it any faster. hmph.
            if (word.length() < 3) {
                continue;
            }
            trieSET.add(word);
        }
        // Kind of a hack.
        if (trieSET.isEmpty()) {
            trieSET.add("");
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        foundWords.clear();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                Set<Integer> alreadyCheckedSet = new HashSet<>();
                alreadyCheckedSet.add(i * prime + j);
                checkForWord(i, j, "", alreadyCheckedSet, foundWords, board, trieSET.getRoot());
            }
        }
        return foundWords;
    }

    private void checkForWord(int row, int column, String stringUntilNow, Set<Integer> alreadyCheckedSet,
                              Set<String> foundWords, BoggleBoard board, CustomTrieSET.Node node) {

        char ijChar = board.getLetter(row, column);

        // The key optimization? Check to see if the node contains a words that has this prefix.
        CustomTrieSET.Node newNode = node.getNext()[ijChar - 65];
        // Need to check for null twice since have to check if null after Q
        if (newNode == null) {
            return;
        }
        if (ijChar == 'Q') {
            newNode = newNode.getNext()['U' - 65];
            if (newNode == null) {
                return;
            }
        }


        String newString;
        if (ijChar == 'Q') {
            newString = stringUntilNow + "QU";
        } else {
            newString = stringUntilNow + ijChar;
        }
        // Check if it contains the found letters until now.
        if (newNode.isString() && newString.length() > 2) {
            foundWords.add(newString);
        }


        for (int i = 0; i < columnDirection.length; i++) {
            // Move it here to make less function calls?
            // If out of bounds return
            if (row + rowDirection[i] < 0 || row + rowDirection[i] >= board.rows() ||
                    column + columnDirection[i] < 0 || column + columnDirection[i] >= board.cols()) {
                continue;
            }
            int uniqueIJPairNumber = (row + rowDirection[i]) * prime + column + columnDirection[i];
            // If this (i,j) combo has already been checked, return. Row*prime + column should be unique
            // for column size up to prime which is 997. Doing this instead of list for optimization.
            if (alreadyCheckedSet.contains(uniqueIJPairNumber)) {
                continue;
            }
            Set<Integer> newAlreadyCheckedSet = new HashSet<>(alreadyCheckedSet);
            newAlreadyCheckedSet.add(uniqueIJPairNumber);
            checkForWord(row + rowDirection[i], column + columnDirection[i], newString, newAlreadyCheckedSet,
                    foundWords, board, newNode);
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trieSET.contains(word)) {
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
        List<String> stringList = new ArrayList<>();
        long time1 = System.currentTimeMillis();
        solver.getAllValidWords(board);
        long time2 = System.currentTimeMillis();
        StdOut.println(time2 - time1);


        for (String word : solver.getAllValidWords(board)) {
            stringList.add(word);
            // StdOut.println(word);
            score += solver.scoreOf(word);
        }

//        Collections.sort(stringList);
//        for (String word : stringList) {
//            StdOut.println(word);
//        }
        StdOut.println("Score = " + score);
    }
}
