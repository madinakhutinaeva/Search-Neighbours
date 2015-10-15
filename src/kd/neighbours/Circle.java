package kd.neighbours;

public class Circle implements Geometry{
	    private final double x, y, radius;   // minimum x- and y-coordinates, radius

	    // construct the axis-aligned rectangle [xmin, xmax] x [ymin, ymax]
	    public Circle(double x, double y, double radius) {
	        if (radius <= 0) {
	            throw new IllegalArgumentException("Invalid radius");
	        }
	        this.x = x;
	        this.y = y;
	        this.radius = radius;
	    }

	    // accessor methods for 4 coordinates
	    public double x() { return x; }
	    public double y() { return y; }
	    public double radius() { return radius; }

	    // distance from p to closest point on this axis-aligned rectangle
	    public double distanceTo(Point2D p) {
	        return Math.sqrt(this.distanceSquaredTo(p)) - radius;
	    }

	    // distance squared from p to closest point on this axis-aligned rectangle
	    public double distanceSquaredTo(Point2D p) {
	        double dx = 0.0, dy = 0.0;
	        dx = p.x() - x();
	        dy = p.y() - y();
	        return dx*dx + dy*dy;
	    }

	    // does this axis-aligned rectangle contain p?
	    public boolean contains(Point2D p) {
	        return distanceTo(p) <= 0;
	    }

	    // are the two axis-aligned rectangles equal?
	    public boolean equals(Object obj) {
	        if (obj == this) return true;
	        if (obj == null) return false;
	        if (obj.getClass() != this.getClass()) return false;
	        Circle that = (Circle) obj;
	        if (this.x() != that.x()) return false;
	        if (this.y() != that.y()) return false;
	        if (this.radius() != that.radius()) return false;
	        return true;
	    }

	    //* return a string representation of this circle */
	    public String toString() {
	        return "[" + x() + ", " + y() + "] x [" + radius() + "]";
	    }

		@Override
		public boolean intersects(Geometry g) {
			return g.intersects(this);
		}

		@Override
		public boolean intersects(Circle g) {
			// distance between centers is less or equal to sum of radiuses  
			return distanceTo(new Point2D(g.x(), g.y())) <= g.radius() + this.radius();
		}
		
	    // does this axis-aligned rectangle intersect that one?
	    public boolean intersects(RectHV that) {
	        return that.intersects(this);
	    }

}
