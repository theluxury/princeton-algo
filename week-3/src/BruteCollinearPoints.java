import java.util.ArrayList;

/**
 * Created by theluxury on 9/25/15.
 */
public class BruteCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points) {
        // First check for nulls and equal points...
        if (points == null) {
            throw new java.lang.NullPointerException("Can't input null array.");
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i] == null || points[j] == null) {
                    throw new java.lang.NullPointerException("Can't input null Point.");
                }

                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new java.lang.IllegalArgumentException("Can't have duplicate Point");
                }
            }
        }


        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                double slope1 = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    double slope2 = points[i].slopeTo(points[k]);
                    if (slope1 != slope2) {
                        continue;
                    }

                    for (int l = k + 1; l < points.length; l++) {
                        double slope3 = points[i].slopeTo(points[l]);
                        if (slope1 != slope3)
                            continue;

                        Point[] linePoints = {points[i], points[j], points[k], points[l]};
                        findLineSegment(linePoints);
                    }

                }
            }
        }

    }   // finds all line segments containing 4 points

    public           int numberOfSegments() {
        return lineSegments.size();
    }       // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] lineArray = new LineSegment[lineSegments.size()];
        lineArray = lineSegments.toArray(lineArray);
        return lineArray;
    }               // the line segments

    private void findLineSegment(Point[] points) {
        Point lowest = points[0];
        Point highest = points[0];

        for (int i = 1; i < points.length; i++) {

            if (lowest == null || points[i].compareTo(lowest) < 0) {
                lowest = points[i];
            }

            if (highest == null || points[i].compareTo(highest) > 0) {
                highest = points[i];
            }
        }


        lineSegments.add(new LineSegment(lowest, highest));
    }
}
