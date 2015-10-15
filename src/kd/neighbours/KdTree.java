package kd.neighbours;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
* kd tree based on Robert Sedgewick "Algorithms for java IV edition"
* 
*  
*  
* @version 1.0
*/
public class KdTree {

    private static class Node {

        public Node (Point2D point, RectHV rect1) {
            p = point;
            rect = rect1;
        }

        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        public RectHV getRect(Point2D other, boolean xAxis) {
            if (xAxis) {
               boolean right = other.x() >= p.x();
               return new RectHV(right ? p.x(): rect.xmin(),
                                 rect.ymin(),
                                 right ? rect.xmax(): p.x(),
                                 rect.ymax()
                                 );
            }
            boolean top = other.y() >= p.y();
            return new RectHV(rect.xmin(),
                    top ? p.y(): rect.ymin(),
                    rect.xmax(),
                    top ? rect.ymax(): p.y()
                    );
        }
     }

    private int size;
    private Node root;
    
    /**
     * <p>Insert point into kdtree.</p>
     *
     * @param p point to insert in the kdtree
     */
    public void insert(Point2D p) {
      
        if (p == null)
            throw new NullPointerException("called insert() with a null point");
        
        root = (root == null) ? 
                put (root, p, true, new RectHV(0, 0, 1, 1)) :
                    put (root, p, true, root.rect);
    }

    private Node put(Node n, Point2D p, boolean xAxis, RectHV box) {
        if (n == null) {
            size++;
            return new Node(p, box);
        }

        if (p.equals(n.p)) {
            return n;
        }

        int cmp;
        if (xAxis) {
            cmp = Point2D.X_ORDER.compare(p, n.p);
        }
        else {
            cmp = Point2D.Y_ORDER.compare(p, n.p);
        }

        if (cmp < 0) {
            n.lb = put(n.lb, p, !xAxis, (n.lb == null) ? n.getRect(p, xAxis): null);
        } else if (0 <= cmp) {
            n.rt = put(n.rt, p, !xAxis, (n.rt == null) ? n.getRect(p, xAxis): null);
        }

        return n;
    }

    /**
     * <p>does the tree contain the point p?.</p>
     *
     * @param p point to search in the kdtree
     * @return True if kdtree contains point p, false otherwise
     */
    public boolean contains(Point2D p) {
        return get(root, p, true) != null;
    }

    private Point2D get(Node n, Point2D other, boolean xAxis) {

        if (n == null) {
            return null;
        }
        if (!n.rect.contains(other)) {
            return null;
        }
        
        if (n.p.equals(other)) {
            return n.p;
        }
        
        Point2D result = null;
        int cmp = xAxis ? Point2D.X_ORDER.compare(other, n.p) : Point2D.Y_ORDER.compare(other, n.p);
        if (cmp < 0) {
            result = get(n.lb, other, !xAxis);
        } else if (0 <= cmp) {
            result = get(n.rt, other, !xAxis);
        }
        return result;
    }

    /** Is the tree empty?   */
    public boolean isEmpty() {
        return root == null;
    }

    /** number of points in the tree */
    public int size() {
        return size;
    }

    /**
     * <p>Get the nearest neighbor point.</p>
     * if distance between points is less then epsilon consider points to be equal.
     *
     * @param geom bounding geometry to search points in
     * @return all points in the set that are inside the geometry
     */
    public Set<Point2D> range(Geometry geom) {
        Set<Point2D> result = new HashSet<Point2D>();
        range(root, result, geom);
        return result;
    }
    
    private void range(Node n, Set<Point2D> points, Geometry geom) {
        if (n == null) {
            return;
        }
        
        if (!n.rect.intersects(geom)) {
            return;
        }
        
        if (geom.contains(n.p)) {
            points.add(n.p);
        }

        range(n.lb, points, geom);
        range(n.rt, points, geom);
    }
    
    /**
     * <p>Get the nearest neighbor point.</p>
     * Neighbor is point is the point within double distance from this point to nearest one,
     * if distance between points is less then epsilon consider points to be equal.
     *
     * @param p Point to search nearest neighbor for
     * @param epsilon threshold for equality test in nearest neighbor search
     * @return a nearest neighbor to p in the tree; null if tree does not contain other points
     */
    public Point2D nearest(Point2D p, double epsilon) {
        if (root == null) {
            return null;
        }
        
        java.util.Queue<Node> queue = new LinkedList<Node>();
        Node nearest = root;
        double bestDist = root.p.distanceSquaredTo(p);
        queue.add(root);
        while (!queue.isEmpty()){
            Node current = queue.poll();
            if (bestDist < current.rect.distanceSquaredTo(p))
                continue;

            double curDist = current.p.distanceSquaredTo(p);
            //if distance equals zero - select another point
            if ((epsilon <= curDist && curDist < bestDist) || bestDist <= epsilon ) {
                bestDist = curDist;
                nearest = current;
            }
            
            if (current.lb != null) {
                queue.add(current.lb);
            }
            
            if (current.rt != null) {
                queue.add(current.rt);
            }

        }
        return nearest.p;
    }
    
    public Point2D[] getPoints(){
        if (root == null) {
            return null;
        }
        
        Point2D[] result = new Point2D[size()];
        
        java.util.Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        int i = 0;
        while (!queue.isEmpty()){
            Node current = queue.poll();
            result[i++] = current.p;
            if (current.lb != null) {
                queue.add(current.lb);
            }
            if (current.rt != null) {
                queue.add(current.rt);
            }

        }
        return result;
    }
    
    /**
     * <p>Get neighbor points.</p>
     * Neighbor is point is the point within double distance from this point to neareast one,
     * if distance between points is less then epsilon consider points to be equal.
     *
     * @param p Point to search neighgbours for
     * @param epsilon threshold for equality test in nearest neighbor search
     * @return Set of neighbour points
     */
    public Set<Point2D> getNeighours(Point2D p, double epsilon){
        Point2D nearest = nearest(p, epsilon);
        double dist = p.distanceTo(nearest) * 2;
        Circle circ = new Circle(p.x(), p.y(), dist);
        Set<Point2D> result = range(circ);
        result.remove(p);
        return result;
    }
}
