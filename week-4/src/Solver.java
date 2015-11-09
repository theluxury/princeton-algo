import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

public class Solver {
    private MinPQ<Node> minPQ = new MinPQ<Node>();
    private MinPQ<Node> twinPQ = new MinPQ<Node>();
    private Node solution;

    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException("Cannot put in null board");
        minPQ.insert(new Node(initial, 0, initial.manhattan(), null));
        twinPQ.insert(new Node(initial.twin(), 0, initial.twin().manhattan(), null));

        tryToSolve();
    }           // find a solution to the initial board (using the A* algorithm)

    private void tryToSolve(){

        while (!minPQ.isEmpty() && !twinPQ.isEmpty()) {
            Node minNode = minPQ.delMin();
            if (minNode.board.isGoal()) {
                solution = minNode;
                break;
            } else {
                for (Board board : minNode.board.neighbors()) {
                    if (minNode.previous != null && board.equals(minNode.previous.board)) continue;
                    minPQ.insert(new Node(board, minNode.moves + 1, board.manhattan(), minNode));
                }
            }
            Node twinNode = twinPQ.delMin();
            if (twinNode.board.isGoal()) {
                break;
            } else {
                for (Board board : twinNode.board.neighbors()) {
                    if (twinNode.previous != null && board.equals(twinNode.previous.board)) continue;
                    twinPQ.insert(new Node(board, twinNode.moves + 1, board.manhattan(), twinNode));
                }
            }
        }
    }

    private class Node implements Comparable<Node> {
        public Board board;
        public int moves;
        public int manhatten;
        public int priority;
        public Node previous;

        public Node(Board board, int moves, int manhatten, Node previous) {
            this.board = board;
            this.moves = moves;
            this.manhatten = manhatten;
            this.previous = previous;
            this.priority = manhatten + moves;
        }

        public int compareTo(Node that) {
            if (this.priority - that.priority != 0) return this.priority - that.priority;
            else return this.manhatten - that.manhatten;
        }
    }

    public boolean isSolvable() {
        return solution != null;
    }            // is the initial board solvable?

    public int moves() {
        return solution == null ? -1 : solution.moves;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (solution == null) {
            return null;
        }

        Deque<Board> answer = new ArrayDeque<Board>();
        Node node = solution;

        do {
            answer.push(node.board);
            node = node.previous;
        } while (node != null);

        return answer;
    }      // sequence of boards in a shortest solution; null if unsolvable
    public static void main(String[] args) {

        java.util.Date date= new java.util.Date();
        System.out.println(new Timestamp(date.getTime()));
        // create initial board from file
        In in = new In(new File("/Users/theluxury/Documents/algs/week-4/8puzzle/puzzle34.txt"));      // input file
        // In in = new In(new File("/Users/theluxury/Documents/algs/week-4/8puzzle/puzzle3x3-unsolvable1.txt"));
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
        date= new java.util.Date();
        System.out.println(new Timestamp(date.getTime()));
    }// solve a slider puzzle (given below)
}