package kd.neighbours;


/**
* Interface for bounding geometry
* 
* <P>Contains methods for checking for intersection between implementing classes using double dispatch
*  
* @see <a href="http://c2.com/cgi/wiki?DoubleDispatchExample">http://c2.com/cgi/wiki?DoubleDispatchExample</a>
*  
* @author Les Paul
* @version 2.0
*/
public interface Geometry {
	public boolean intersects(Geometry g);

	public boolean intersects(Circle g);

	public boolean intersects(RectHV g);

	 // distance from p to closest point on this axis-aligned rectangle
    public double distanceTo(Point2D p);

    // distance squared from p to closest point on this axis-aligned rectangle
    public double distanceSquaredTo(Point2D p);

    // does this axis-aligned rectangle contain p?
    public boolean contains(Point2D p);

    // are the two axis-aligned rectangles equal?
    public boolean equals(Object obj);
}
