import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Queue;

import java.io.File;
import java.util.*;

/**
 * Created by theluxury on 11/8/15.
 */
public class SAP {

    private class Node {
        final int value;
        final int distance;

        public Node(int value, int distance) {
            this.value = value;
            this.distance = distance;
        }
    }

    private Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        this.graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        List<Integer> vArray = convertElementToList(v);
        List<Integer> wArray = convertElementToList(w);
        return length(vArray, wArray);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        List<Integer> vArray = convertElementToList(v);
        List<Integer> wArray = convertElementToList(w);
        return ancestor(vArray, wArray);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.NullPointerException();
        }
        Set<Integer> vSet = convertIterableToSet(v);
        Set<Integer> wSet = convertIterableToSet(w);
        Node minDistanceNode = getSAPNode(vSet, wSet);
        if (minDistanceNode.distance == Integer.MAX_VALUE) {
            return -1;
        } else {
            return minDistanceNode.distance;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.NullPointerException();
        }
        Set<Integer> vSet = convertIterableToSet(v);
        Set<Integer> wSet = convertIterableToSet(w);
        Node minDistanceNode = getSAPNode(vSet, wSet);
        return minDistanceNode.value;
    }

    private List<Integer> convertElementToList(int v) {
        List<Integer> vArray = new ArrayList<>();
        vArray.add(v);
        return vArray;
    }

    private Set<Integer> convertIterableToSet(Iterable<Integer> v) {
        Set<Integer> vSet = new HashSet<>();
        for (int i : v) {
            vSet.add(i);
        }
        return vSet;
    }

    private Node getSAPNode(Iterable<Integer> v, Iterable<Integer> w) {
        for (int i : v) {
            if (i < 0 || i >= graph.V()) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        for (int j : w) {
            if (j < 0 || j >= graph.V()) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        // First, get hashmap that maps distance frm v to everything it can reach.
        Map<Integer, Integer> distanceToVMap = new HashMap<>();
        Queue<Node> bfsQueue = new Queue<>();
        for (int i : v) {
            addNodeToQueueAndHashmap(i, 0, bfsQueue, distanceToVMap);
        }
        while (!bfsQueue.isEmpty()) {
            Node currentNode = bfsQueue.dequeue();
            addNeighbors(currentNode, bfsQueue, distanceToVMap);
        }

        // Then, get a distance you can stop at. If w is an ancestor of v, then don't have to try
        // any longer than that.
        Node minDistanceNode = new Node(-1, Integer.MAX_VALUE);
        for (int j : w) {
            if (distanceToVMap.containsKey(j)) {
                minDistanceNode = checkForNewMinNode(minDistanceNode, new Node(j, 0), distanceToVMap);
            }
        }

        boolean[] marked = new boolean[graph.V()];
        bfsQueue = new Queue<>();
        for (int j : w) {
            bfsQueue.enqueue(new Node(j, 0));
            marked[j] = true;
        }
        // additional requirement here is that distance is shorter than min distance, since otherwise can just
        // return that.
        while (!bfsQueue.isEmpty() && bfsQueue.peek().distance < minDistanceNode.distance) {
            Node currentNode = bfsQueue.dequeue();
            minDistanceNode = checkForNewMinNode(minDistanceNode, currentNode, distanceToVMap);
            for (int neighbor : graph.adj(currentNode.value)) {
                if (!marked[neighbor]) {
                    marked[neighbor] = true;
                    bfsQueue.enqueue(new Node(neighbor, currentNode.distance + 1));
                }
            }
        }
        return minDistanceNode;
    }

    private Node checkForNewMinNode(Node prevNode, Node distanceToWNode, Map<Integer, Integer> map) {
        if (!map.containsKey(distanceToWNode.value)) {
            return prevNode;
        } else {
            int distanceToV = map.get(distanceToWNode.value);
            // If previous distance less than or equal, that one's still fine.
            if (prevNode.distance <= distanceToV + distanceToWNode.distance) {
                return prevNode;
            } else {
                // If new one is closer, than the new W node is the closest ancestor.
                return new Node(distanceToWNode.value, distanceToV + distanceToWNode.distance);
            }
        }
    }

    private void addNeighbors(Node node, Queue<Node> queue, Map<Integer, Integer> map) {
        for (int neighbor : graph.adj(node.value)) {
            if (!map.containsKey(neighbor)) {
                addNodeToQueueAndHashmap(neighbor, node.distance + 1, queue, map);
            }
        }
    }

    private void addNodeToQueueAndHashmap(int value, int distance, Queue<Node> queue, Map<Integer, Integer> map) {
        map.put(value, distance);
        queue.enqueue(new Node(value, distance));
    }

    // do unit testing of this class
    public static void main(String[] args) {

        In in = new In(new File("/Users/theluxury/Documents/algs/week-6/wordnet/digraph2.txt"));
        Digraph graph = new Digraph(in);
        SAP sap = new SAP(graph);
        int v = -1;
        int w = 5;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
