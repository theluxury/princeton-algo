/**
 * Created by theluxury on 11/8/15.
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }         // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        int maxDistance = Integer.MAX_VALUE;
        String outcastCandidate = "";
        for (String noun : nouns) {
            int distance = getDistance(noun, nouns);
            if (distance < maxDistance) {
                outcastCandidate = noun;
            }
        }
        return outcastCandidate;
    }   // given an array of WordNet nouns, return an outcast

    private int getDistance(String noun, String[] nouns) {
        int distance = 0;
        for (String otherNoun : nouns) {
            if (noun != otherNoun) {
                distance += wordNet.distance(noun, otherNoun);
            }
        }
        return distance;
    }
    public static void main(String[] args) {

    }  // see test client below
}
