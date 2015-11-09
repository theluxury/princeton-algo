import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.*;

/**
 * Created by theluxury on 11/8/15.
 */
public class WordNet {
    // So actually need two maps. Okay.
    private Map<Integer, String> idToNounMap = new HashMap<>();
    private Map<String, Set<Integer>> nounToIdMap = new HashMap<>();
    private SAP nounSap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.NullPointerException();
        }
        // check to see if rooted dag

        Digraph graph;
        graph = createAndPopulateGraph(synsets, hypernyms);
        checkForOneRoot(graph);
        checkForAcyclic(graph);

        // Dont think we actually need the gloss. That's convenient. Can have a map from noun -> number.
        // That should be well defined.
        In synsetIn = new In(new File(synsets));
        while (synsetIn.hasNextLine()) {
            // systen has form id, names seperated by comma, definition
            String line = synsetIn.readLine();
            String[] csv = line.split(",");
            int synsetId = Integer.valueOf(csv[0]);
            String synsetNouns = csv[1];
            // String[] synsetNouns = csv[1].split("\\s+");
            // So have to do two things. Add all the synset values to id, and then add id to that noun's map.
            updateIdToNounsMap(synsetId, synsetNouns, idToNounMap);
            // Then update nouns to id
            String[] sysnetNounsArray = synsetNouns.split("\\s+");
            updateNounToIdMap(synsetId, sysnetNounsArray, nounToIdMap);
        }

        nounSap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIdMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.NullPointerException();
        }
        return nounToIdMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        return nounSap.length(nounToIdMap.get(nounA), nounToIdMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        return idToNounMap.get(nounSap.ancestor(nounToIdMap.get(nounA), nounToIdMap.get(nounB)));
    }

    private void checkForOneRoot(Digraph graph) {
        int roots = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) {
                roots++;
            }
        }

        if (roots != 1) {
            throw new java.lang.IllegalArgumentException("Graph does not have one root");
        }
    }

    private void checkForAcyclic(Digraph graph) {
        boolean[] marked = new boolean[graph.V()];
        for (int i = 0; i < graph.V(); i++) {
            checkIfRecurse(i, graph, marked, new HashSet<>());
        }
    }

    private void checkIfRecurse(int id, Digraph graph, boolean[] marked, Set<Integer> backEdges) {
        if (!marked[id]) {
            Set<Integer> newBackEdges = new HashSet<>(backEdges);
            newBackEdges.add(id);
            DFS(id, graph, marked, newBackEdges);
        }
    }

    private void DFS(int id, Digraph graph, boolean[] marked, Set<Integer> backEdges) {
        marked[id] = true;
        for (int neighbor : graph.adj(id)) {
            if (backEdges.contains(neighbor)) {
                throw new java.lang.IllegalArgumentException("Not acyclic");
            }
            checkIfRecurse(neighbor, graph, marked, backEdges);
        }
    }

    private Digraph createAndPopulateGraph(String synset, String hypernyms) {
        In synsets = new In(new File(synset));
        int numberOfIds = 0;
        while (synsets.hasNextLine()) {
            synsets.readLine();
            numberOfIds++;
        }
        Digraph digraph = new Digraph(numberOfIds);

        In hypernumIn = new In(new File(hypernyms));
        while (hypernumIn.hasNextLine()) {
            String line = hypernumIn.readLine();
            String[] csv = line.split(",");
            int synsetId = Integer.valueOf(csv[0]);
            for (int i = 1; i < csv.length; i++) {
                digraph.addEdge(synsetId, Integer.valueOf(csv[i]));
            }
        }
        return digraph;
    }

    private void updateIdToNounsMap(int id, String nouns, Map<Integer, String> map) {
        map.put(id, nouns);
    }

    private void updateNounToIdMap(int id, String[] nouns, Map<String, Set<Integer>> map) {
        for (String noun : nouns) {
            if (map.containsKey(noun)) {
                map.get(noun).add(id);
            } else {
                Set<Integer> idSet = new HashSet<>();
                idSet.add(id);
                map.put(noun, idSet);
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("/Users/theluxury/Documents/algs/week-6/wordnet/synsets6.txt",
                "/Users/theluxury/Documents/algs/week-6/wordnet/hypernyms6InvalidCycle.txt");
        System.out.println(wordNet.nouns());
        // System.out.println(wordNet.distance("a", "o"));
    }
}
