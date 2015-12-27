import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * Created by theluxury on 12/26/15.
 */
public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] charArray = getCharArray();
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        for (int i = 0; i < input.length; i++) {
            BinaryStdOut.write(getIndexAndReorder(input[i], charArray));
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] charArray = getCharArray();

        int r;
        while ((r = BinaryStdIn.readInt()) != 0) {
            BinaryStdOut.write(getCharAndReorder(r, charArray));
        }

        BinaryStdOut.close();
    }

    private static char[] getCharArray() {
        char[] charArray = new char[256];
        for (int i = 0; i < 256; i++) {
            charArray[i] = (char) i;
        }
        return charArray;
    }

    private static char getCharAndReorder(int indexOf, char[] chars) {
        char character = chars[indexOf];
        reorderArray(indexOf, chars);
        return character;
    }

    private static int getIndexAndReorder(char character, char[] chars) {
        int indexOfChar = getIndexOf(character, chars);
        reorderArray(indexOfChar, chars);
        return indexOfChar;
    }

    private static int getIndexOf(char character, char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == character) {
                return i;
            }
        }
        return -1;
    }

    private static void reorderArray(int indexOfChar, char[] chars) {
        char recentChar = chars[indexOfChar];
        for (int i = indexOfChar; i > 0; i--) {
            chars[i] = chars[i - 1];
        }
        chars[0] = recentChar;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}