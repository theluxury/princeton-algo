import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by theluxury on 10/12/15.
 */


public class Test {

    private static ArrayList<String> maxAnswersList = new ArrayList<String>();

    public static class IJCoordinate {
        public int i;
        public int j;

        public IJCoordinate(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }





    public static void main(String[] args) {Set<String> dictionary = new HashSet<String>();

        IJCoordinate[] meh = new IJCoordinate[4];

        dictionary.add("EGO");
        dictionary.add("ON");
        dictionary.add("ONE");
        dictionary.add("GONE");
        dictionary.add("EN");
        dictionary.add("N");
        char[][] matrix = new char[][] {{'E', 'N', 'P'}, {'G', 'S', 'C'}, {'O', 'N', 'E'}};

        ArrayList<String> answerList = new ArrayList<String>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                ArrayList<IJCoordinate> coordinateArrayList = new ArrayList<IJCoordinate>();
                startFind(i,j, "", matrix, dictionary, 0, coordinateArrayList, answerList);
            }
        }

        System.out.print(maxAnswersList);
    }

    public static void startFind(int row, int column, String string, char[][] matrix,
                                 Set<String> dictionary, int wordsFound, ArrayList<IJCoordinate> coordinateArrayList,
                                 ArrayList<String> answerList) {

        if (row < 0 || column < 0 || row >= matrix.length || column >= matrix.length) {
            if (wordsFound > maxAnswersList.size()) {
                maxAnswersList = answerList;
            }
            return;
        }

        for (IJCoordinate coordinate : coordinateArrayList) {
            if (coordinate.i == row && coordinate.j == column) {
                if (wordsFound > maxAnswersList.size()) {
                    maxAnswersList = answerList;
                }
                return;
            }
        }

        coordinateArrayList.add(new IJCoordinate(row, column));
        ArrayList<IJCoordinate> newCords1 = new ArrayList<IJCoordinate>(coordinateArrayList);
        ArrayList<IJCoordinate> newCords2 = new ArrayList<IJCoordinate>(coordinateArrayList);
        ArrayList<IJCoordinate> newCords3 = new ArrayList<IJCoordinate>(coordinateArrayList);
        ArrayList<IJCoordinate> newCords4 = new ArrayList<IJCoordinate>(coordinateArrayList);

        String newString;
        if (dictionary.contains(string + matrix[row][column])) {
            wordsFound++;
            answerList.add(string + matrix[row][column]);
            newString = "";
        } else {
            newString = string + matrix[row][column];
        }

        ArrayList<String> newStrings1 = new ArrayList<String>(answerList);
        ArrayList<String> newStrings2 = new ArrayList<String>(answerList);
        ArrayList<String> newStrings3 = new ArrayList<String>(answerList);
        ArrayList<String> newStrings4 = new ArrayList<String>(answerList);


        if (newString == "") {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    ArrayList<IJCoordinate> newCords = new ArrayList<IJCoordinate>(coordinateArrayList);
                    ArrayList<String> newStrings = new ArrayList<String>(answerList);
                    startFind(i, j, newString, matrix, dictionary, wordsFound, newCords, newStrings);
                }
            }
        } else {
            startFind(row + 1, column, newString, matrix, dictionary, wordsFound, newCords1, newStrings1);
            startFind(row - 1, column, newString, matrix, dictionary, wordsFound, newCords2, newStrings2);
            startFind(row, column + 1, newString, matrix, dictionary, wordsFound, newCords3, newStrings3);
            startFind(row, column - 1, newString, matrix, dictionary, wordsFound, newCords4, newStrings4);
        }

    }
}
