import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by theluxury on 9/10/15.
 */
public class PercolationStats {

    private double[] counts;

    public PercolationStats(int N, int T) {

        if (N <= 0 || T <= 0) throw new java.lang.IllegalArgumentException("Negative arguments not allowed");

        counts = new double[T];
        for (int i = 0; i < T; i++) {
            double count = 0;
            Percolation perc = new Percolation(N);
            while (!perc.percolates()) {
                openRandom(perc, N);
                count++;
            }
            counts[i] = count / (N * N);
        }
    } // perform T independent experiments on an N-by-N grid

    private void openRandom(Percolation perc, int width) {
        int randomInt;
        do {
            randomInt = StdRandom.uniform((int) (width * width));
        } while (perc.isOpen((randomInt / width) + 1, (randomInt % width) + 1));

        perc.open((randomInt / width) + 1, (randomInt % width) + 1);
    }

    public double mean()          {
        return StdStats.mean(counts);
    }             // sample mean of percolation threshold

    public double stddev()      {
        return StdStats.stddev(counts);
    }               // sample standard deviation of percolation threshold

    public double confidenceLo()            {
        return StdStats.mean(counts) - 1.96 * StdStats.stddev(counts) / Math.sqrt(counts.length);
    }   // low  endpoint of 95% confidence interval

    public double confidenceHi()          {
        return StdStats.mean(counts) + 1.96 * StdStats.stddev(counts) / Math.sqrt(counts.length);
    }     // high endpoint of 95% confidence interval

    public static void main(String[] args)    {

    } // test client (described below)

}
