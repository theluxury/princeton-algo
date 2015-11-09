/**
 * Created by theluxury on 11/8/15.
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }         // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        int maxDistance = Integer.MIN_VALUE;
        String outcastCandidate = "";
        for (String noun : nouns) {
            int distance = getDistance(noun, nouns);
            if (maxDistance < distance) {
                outcastCandidate = noun;
                maxDistance = distance;
            }
        }
        return outcastCandidate;
    }   // given an array of WordNet nouns, return an outcast

    private int getDistance(String noun, String[] nouns) {
        int distance = 0;
        for (String otherNoun : nouns) {
            if (!noun.equals(otherNoun)) {
                distance += wordNet.distance(noun, otherNoun);
            }
        }
        return distance;
    }
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/Users/theluxury/Documents/algs/week-6/wordnet/synsets50000-subgraph.txt",
                "/Users/theluxury/Documents/algs/week-6/wordnet/hypernyms50000-subgraph.txt");
        Outcast outcast = new Outcast(wordNet);
        String[] nouns = new String[] {"Asia", "Australia", "North_America", "India",
                "Europe", "Antarctica", "South_America" };
        System.out.print(outcast.outcast(nouns));


    }  // see test client below
}
