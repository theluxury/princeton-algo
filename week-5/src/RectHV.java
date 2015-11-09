import edu.princeton.cs.algs4.StdDraw;

public class RectHV {
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;

    public    RectHV(double xmin, double ymin,      // construct the rectangle [xmin, xmax] x [ymin, ymax]
                     double xmax, double ymax) {

        if (ymin > ymax || xmin > xmax) throw new java.lang.IllegalArgumentException("Illegal rectangle");
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }     // construct the rectangle [xmin, xmax] x [ymin, ymax]
    // throw a java.lang.IllegalArgumentException if (xmin > xmax) or (ymin > ymax)

    public  double xmin() {
        return xmin;
    }                          // minimum x-coordinate of rectangle
    public  double ymin() {
        return ymin;
    }                          // minimum y-coordinate of rectangle
    public  double xmax() {
        return xmax;
    }                          // maximum x-coordinate of rectangle
    public  double ymax() {
        return ymax;
    }                          // maximum y-coordinate of rectangle
    public boolean contains(Point2D p) {
        return p.x() >= xmin && p.x() <= xmax && p.y() >= ymin && p.y() <= ymax;
    }             // does this rectangle contain the point p (either inside or on boundary)?
    public boolean intersects(RectHV that) {
        return !(xmin > that.xmax() || xmax < that.xmin() || ymin > that.ymax() || ymax < that.ymin());
    }         // does this rectangle intersect that rectangle (at one or more points)?

    public  double distanceTo(Point2D p) {
        if (contains(p)) return 0;
        double midX = (xmax + xmin) / 2;
        double midY = (ymax + ymin) / 2;
        double dx = Math.abs(midX - p.x()) - (xmax - xmin) / 2;
        double dy = Math.abs(midY - p.y()) - (ymax - ymin) / 2;
        return Math.sqrt(midX * midX + midY * midY);
    }           // Euclidean distance from point p to closest point in rectangle

    public  double distanceSquaredTo(Point2D p) {
        if (contains(p)) return 0;
        double midX = (xmax + xmin) / 2;
        double midY = (ymax + ymin) / 2;
        double dx = Math.abs(midX - p.x()) - (xmax - xmin) / 2;
        double dy = Math.abs(midY - p.y()) - (ymax - ymin) / 2;
        return midX * midX + midY * midY;
    }    // square of Euclidean distance from point p to closest point in rectangle

    public boolean equals(Object y) {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        RectHV that = (RectHV) y;
        return this.xmin == that.xmin() && this.xmax == that.xmax()
                && this.ymin == that.ymin() && this.ymax == that.ymax();
    }             // does this rectangle equal that object?

    public    void draw() {
        StdDraw.rectangle(xmin, ymin, (xmax - xmin) / 2, (ymax - ymin) / 2);
    }                          // draw to standard draw
    public  String toString() {
        return xmin + ", " + ymin + " to " + xmax + ", " + ymax;
    }                       // string representation
}