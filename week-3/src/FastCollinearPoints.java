import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class FastCollinearPoints {
    private ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
    private HashMap<Double, ArrayList<Points>> lineSegmentHashMap = new HashMap<Double, ArrayList<Points>>();

    private class Points {

        public Point p1;
        public Point p2;

        public Points(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    public FastCollinearPoints(Point[] points)  {
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
            HashMap<Double, ArrayList<Point>> pointsMap = new HashMap<Double, ArrayList<Point>>();

            for (int j = i + 1; j < points.length; j++) {
                double slope = points[i].slopeTo(points[j]);

                if (!pointsMap.containsKey(slope)) {
                    ArrayList<Point> point = new ArrayList<>();
                    point.add(points[j]);
                    pointsMap.put(slope, point);
                } else {
                    ArrayList<Point> point = pointsMap.get(slope);
                    point.add(points[j]);
                }
            }

            // Then iterate through it to see if any of them contain more than 3, if so you're good
            for (Double key : pointsMap.keySet()) {
                if (pointsMap.get(key).size() >= 3) {
                    pointsMap.get(key).add(points[i]);
                    findLineSegment(pointsMap.get(key));
                }
            }
        }
    }    // finds all line segments containing 4 or more points

    private void findLineSegment(ArrayList<Point> points) {
        Point lowest = points.get(0);
        Point highest = points.get(0);

        for (int i = 1; i < points.size(); i++) {

            if (points.get(i).compareTo(lowest) < 0) {
                lowest = points.get(i);
            }

            if (points.get(i).compareTo(highest) > 0) {
                highest = points.get(i);
            }
        }

        double slope = lowest.slopeTo(highest);
        if (lineSegmentHashMap.containsKey(slope)) {
            // Gotta check to see if it contains.
            ArrayList<Points> implPoints = lineSegmentHashMap.get(slope);
            for (Points badHack : implPoints) {
                if (badHack.p1.slopeTo(highest) == slope) {
                    // See if same points.
                    if (badHack.p1.slopeTo(lowest) == Double.NEGATIVE_INFINITY ||
                            badHack.p2.slopeTo(highest) == Double.NEGATIVE_INFINITY) {
                        return;
                    }

                    // Have to see if in between?
                    if (lowest.compareTo(badHack.p1) > 0 && lowest.compareTo(badHack.p2) < 0)
                        return;
                }
            }

            implPoints.add(new Points(lowest, highest));
        } else {
            ArrayList<Points> newPoints = new ArrayList<Points>();
            newPoints.add(new Points(lowest, highest));
            lineSegmentHashMap.put(lowest.slopeTo(highest), newPoints);
        }

        lineSegments.add(new LineSegment(lowest, highest));
    }

    public           int numberOfSegments() {
        return lineSegments.size();

    }       // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] lineArray = new LineSegment[lineSegments.size()];
        lineArray = lineSegments.toArray(lineArray);
        return lineArray;
    }               // the line segments
}