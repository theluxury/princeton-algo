import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.ArrayList;

/**
 * Created by theluxury on 10/6/15.
 */
public class KdTree {

    private Node root;
    private int size;

    private static class Node {

        private Point2D point;
        private Node left;
        private Node right;
        private RectHV rect;

        public Node(Point2D point2D, RectHV rect) {
            this.point = point2D;
            this.rect = rect;
        }

        public Point2D getPoint() {
            return point;
        }

        public void setPoint(Point2D point) {
            this.point = point;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public RectHV getRect() { return this.rect; }

        public static Node insert(Node node, Node parentNode, Point2D p, boolean even) {
            if (node == null && parentNode == null) return new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            else if (node != null && node.getPoint().equals(p)) return node;
            else if (node == null) {
                if (even && p.y() >= parentNode.point.y()) {
                    RectHV newRect = new RectHV(parentNode.getRect().xmin(), parentNode.point.y(),
                            parentNode.getRect().xmax(), parentNode.getRect().ymax());
                    return new Node(p, newRect);
                } else if (even && p.y() < parentNode.point.y()) {
                    RectHV newRect = new RectHV(parentNode.getRect().xmin(), parentNode.getRect().ymin(),
                            parentNode.getRect().xmax(), parentNode.point.y());
                    return new Node(p, newRect);
                } else if (!even && p.x() >= parentNode.point.x()) {
                    RectHV newRect = new RectHV(parentNode.point.x(), parentNode.getRect().ymin(),
                            parentNode.getRect().xmax(), parentNode.getRect().ymax());
                    return new Node(p, newRect);
                } else {
                    RectHV newRect = new RectHV(parentNode.getRect().xmin(), parentNode.getRect().ymin(),
                            parentNode.point.x(), parentNode.getRect().ymax());
                    return new Node(p, newRect);
                }
            } else {
                if (even && p.x() >= node.point.x()) {
                    node.right = insert(node.right, node, p, !even);
                } else if (even && p.x() < node.point.x()) {
                    node.left = insert(node.left, node, p, !even);
                } else if (!even && p.y() >= node.point.y()) {
                    node.right = insert(node.right, node, p, !even);
                } else {
                    node.left = insert(node.left, node, p, !even);
                }
            }
            return node;
        }

        public static Node get(Node node, Point2D point, boolean even) {
            if (node == null) return null;

            if (node.getPoint().equals(point)) return node;
            else if (even && point.x() >= node.point.x()) return get(node.right, point, !even);
            else if (even && point.x() < node.point.x()) return get(node.left, point, !even);
            else if (!even && point.y() >= node.point.y()) return get(node.right, point, !even);
            else if (!even && point.y() < node.point.y()) return get(node.left, point, !even);
            else return node;
        }

        public static void range(Node node, RectHV rect, ArrayList<Point2D> point2DArrayList) {
            if (node == null || !node.getRect().intersects(rect)) return;
            if (rect.contains(node.point)) point2DArrayList.add(node.point);

            range(node.left, rect, point2DArrayList);
            range(node.right, rect, point2DArrayList);
        }

        public static Point2D nearest(Node node, Point2D point2D, double minDistance) {
            if (node == null || node.getRect().distanceSquaredTo(point2D) > minDistance)
                return null;

            minDistance = Math.min(minDistance, node.getPoint().distanceSquaredTo(point2D));
            Point2D left;
            Point2D right;

            if (node.getLeft() != null && node.getLeft().getRect().contains(point2D)) {
                left = nearest(node.getLeft(), point2D, minDistance);
                minDistance = Math.min(minDistance, point2D.distanceSquaredTo(left));
                right = nearest(node.getRight(), point2D, minDistance);
            } else {
                // default to get right.
                right = nearest(node.getRight(), point2D, minDistance);
                // this might crash if null?
                if (right != null) minDistance = Math.min(minDistance, point2D.distanceSquaredTo(right));
                left = nearest(node.getLeft(), point2D, minDistance);
            }

            // left or right could be null
            Point2D closestPoint = node.getPoint();
            if (left != null && left.distanceSquaredTo(point2D) < closestPoint.distanceSquaredTo(point2D))
                closestPoint = left;

            if (right != null && right.distanceSquaredTo(point2D) < closestPoint.distanceSquaredTo(point2D))
                closestPoint = right;

            return closestPoint;
        }

        public static void draw(Node node, boolean even) {
            if (node == null) return;

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            StdDraw.point(node.getPoint().x(), node.getPoint().y());

            if (even) {
                // If even then up down
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.getPoint().x(), node.getRect().ymin(), node.getPoint().x(), node.getRect().ymax());
            } else {
                // If even then up down
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.getRect().xmin(), node.getPoint().y(), node.getRect().xmax(), node.getPoint().y());
            }

            draw(node.getRight(), !even);
            draw(node.getLeft(), !even);
        }

        public static int size(Node node) {
            if (node == null) return 0;
            return 1 + size(node.getLeft()) + size(node.getRight());
        }
    }
    public         KdTree() { size = 0; }                               // construct an empty set of points
    public           boolean isEmpty() {
        return root == null;
    }                      // is the set empty?
    public               int size() {
        return size;
    }                        // number of points in the set
    public              void insert(Point2D p) {
        if (contains(p)) return;
        size++;
        root = Node.insert(root, null, p, true);
    }             // add the point to the set (if it is not already in the set)

    public           boolean contains(Point2D p) {
        return Node.get(root, p, true) != null;
    }           // does the set contain point p?

    public              void draw() {
        Node.draw(root, true);
    }                         // draw all points to standard draw
    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> point2DArrayList = new ArrayList<Point2D>();
        Node.range(root, rect, point2DArrayList);
        return point2DArrayList;
    }             // all points that are inside the rectangle

    public           Point2D nearest(Point2D p) {
        return Node.nearest(root, p, Double.POSITIVE_INFINITY);
    }            // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }                 // unit testing of the methods (optional)
}
