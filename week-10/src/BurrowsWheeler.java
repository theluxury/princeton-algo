import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by theluxury on 12/26/15.
 */
public class BurrowsWheeler {
    private static int R = 255;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    // Okay encode is good.
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int first = 0;
        for (int i = 0; i < s.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            // The char at the final column is the one right before the index fcn. Adding the string.length
            // to fix -1 error.
            BinaryStdOut.write(s.charAt((circularSuffixArray.index(i) + s.length() - 1) % s.length()));
            // System.out.print(string.charAt((circularSuffixArray.index(i) + string.length() - 1) % string.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    //
    public static void decode() {
        // First, get sorted char array of string.
        int first = BinaryStdIn.readInt();
        String string = "";
        while (!BinaryStdIn.isEmpty()) {
            string += BinaryStdIn.readChar();
        }
        char[] sortedChars = string.toCharArray();
        Arrays.sort(sortedChars);

        // Get map from character to linkedlist of order the character appears
        Map<Character, Queue<Integer>> inorderLastColumn = new HashMap<>();
        for (int i = 0; i < string.length(); i++) {
            if (inorderLastColumn.get(string.charAt(i)) == null) {
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                inorderLastColumn.put(string.charAt(i), queue);
            } else {
                inorderLastColumn.get(string.charAt(i)).add(i);
            }
        }

        // Construct next
        int[] next = new int[string.length()];
        for (int i = 0; i < string.length(); i++) {
            next[i] = inorderLastColumn.get(sortedChars[i]).poll();
        }

        // Construct text.
        String output = "";
        for (int i = 0; i < string.length(); i++) {
            output += sortedChars[first];
            first = next[first];
        }

        BinaryStdOut.write(output);
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
