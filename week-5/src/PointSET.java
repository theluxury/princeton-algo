import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

import java.util.ArrayList;

public class PointSET {

    private SET<Point2D> pointsSet;

    public         PointSET() {
        pointsSet = new SET<Point2D>();
    }                              // construct an empty set of points

    public           boolean isEmpty() {
        return pointsSet.isEmpty();
    }                     // is the set empty?

    public               int size() {
        return pointsSet.size();
    }                        // number of points in the set

    public              void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        pointsSet.add(p);
    }             // add the point to the set (if it is not already in the set)

    public           boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        return pointsSet.contains(p);
    }            // does the set contain point p?
    public              void draw() {
        for (Point2D point2D : pointsSet) {
            point2D.draw();
        }
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException();
        ArrayList<Point2D> pointsArray = new ArrayList<Point2D>();
        for (Point2D point : pointsSet) {
            if (rect.contains(point)) pointsArray.add(point);
        }
        return pointsArray;
    }             // all points that are inside the rectangle
    public           Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        if (pointsSet.isEmpty()) return null;

        double maxDistanceSquared = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;

        for (Point2D point : pointsSet) {
            if (point.distanceSquaredTo(p) < maxDistanceSquared) {
                nearestPoint = point;
                maxDistanceSquared = point.distanceSquaredTo(p);
            }
        }

        return nearestPoint;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }                 // unit testing of the methods (optional)
}