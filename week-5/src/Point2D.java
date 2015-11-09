import edu.princeton.cs.algs4.StdDraw;

public class Point2D implements Comparable<Point2D> {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }              // construct the point (x, y)

    public  double x() {
        return x;
    }                              // x-coordinate

    public  double y() {
        return y;
    }                              // y-coordinate

    public  double distanceTo(Point2D that) {
        return Math.sqrt((x - that.x()) * (x - that.x()) + (y - that.y()) * (y - that.y()));
    }         // Euclidean distance between two points

    public  double distanceSquaredTo(Point2D that) {
        return (x - that.x()) * (x - that.x()) + (y - that.y()) * (y - that.y());
    } // square of Euclidean distance between two points

    public     int compareTo(Point2D that) {
        if (Double.compare(y, that.y()) != 0) return Double.compare(y, that.y());
        else return Double.compare(x, that.x);
    }         // for use in an ordered symbol table

    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Point2D that = (Point2D) y;
        return this.x == that.x() && this.y == that.y();
    }              // does this point equal that object?

    public    void draw() {
        StdDraw.point(x, y);
    }                           // draw to standard draw
    public  String toString() {
        return x + ", " + y;
    }                      // string representation
}