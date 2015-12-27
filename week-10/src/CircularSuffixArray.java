import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by theluxury on 12/26/15.
 */
public class CircularSuffixArray {

    List<CSAReference> csaReferenceList;

    private class CSAReference implements Comparable<CSAReference> {

        final int index;
        private String string;

        public CSAReference(int index, String string) {
            this.index = index;
            this.string = string;
        }

        public int compareTo(CSAReference that) {
            for (int i = 0; i < string.length(); i++) {
                char thisChar = string.charAt((index + i) % string.length()); // Modulo for wrap around.
                char thatChar = string.charAt((that.index + i) % string.length()); // string should be same
                if (thisChar > thatChar) {
                    return 1;
                } else if (thisChar < thatChar) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public CircularSuffixArray(String s)  // circular suffix array of s
    {
        if (s == null) {
            throw new java.lang.NullPointerException();
        }

        csaReferenceList = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            csaReferenceList.add(new CSAReference(i, s));
        }

        Collections.sort(csaReferenceList);
    }

    public int length()                   // length of s
    {
        return csaReferenceList.size();
    }

    public int index(int i)               // returns index of ith sorted suffix
    {
        if (i < 0 || i >= length()) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return csaReferenceList.get(i).index;
    }

    public static void main(String[] args)// unit testing of the methods (optional)
    {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        System.out.print(circularSuffixArray.index(0));
    }

}
