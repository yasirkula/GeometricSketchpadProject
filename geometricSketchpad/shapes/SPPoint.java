package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public class SPPoint extends SPShape implements Comparable<SPPoint> { 
	private static final long serialVersionUID = -1776955822912808316L;
	
	public static int pointCount;
	
	public SPPoint() {
		this(0,0);
	}
		
	public SPPoint(Number x, Number y) {
		setLocation(x,y);

		nameShape();
	}
	
	public SPPoint(Point p) {
		this(p.getX(),p.getY());
	}
	
	public SPPoint(SPPoint p) {
		this(p.x(),p.y());
	}
	
	public void adjustPosition( double dx, double dy )
	{
		// do nothing
	}
	
	public double polarAngle() {
		double atan2 = Math.atan2(x(), y());
		if (atan2 < 0) {
			atan2 = atan2 + 2*Math.PI;
		}
		return atan2;
	}
	
	public double angleWRT(SPPoint other) {
		double atan2 = Math.atan2(this.y() - other.y(), this.x() - other.x());
		if (atan2 < 0) {
			atan2 = atan2 + 2*Math.PI;
		}
		return atan2;
	}
	
	public double polarDistance() {
		return Math.sqrt(Math.hypot(x(), y()));
	}
	
	public double distanceTo(SPPoint p) {
		return Math.hypot(x() - p.x(), y() - p.y());
	}
	
	public Point2D get2D() {
		return new Point2D.Double(x(), y());
	}
	
	/*public String toString() {
		return name() + " (" + x() + ", " + y() + ")";
	}*/
	
	// Shape - java.awt.Shape
	public boolean contains(double x, double y) {
		return (x == x()) && (y == y());
	}
	
	public boolean contains(double x, double y, double w, double h) {
		if ( w == 0 && y == 0) {
			return contains (x, y);
		} else {
			return false;
		}
	}
	
	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}
	
	public boolean contains(Rectangle2D r) {
		return contains (r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	
	public Rectangle getBounds() {
		int bX = 0;
		int bY = 0;
		int wX = 1;
		int wY = 1;
		if (x() == Math.rint(x())) {
			bX = ((int) x());
			wX = 2;
		} else {
			bX = (int) Math.floor(x());
		}
		
		if (y() == Math.rint(y())) {
			bY = ((int) y());
			wY = 2;
		} else {
			bY = (int) Math.floor(y());
		}
		
		return new Rectangle(bX, bY, wX, wY);
	}
	
	public Rectangle2D getBounds2D() {
		double bX = x() - Double.MIN_NORMAL;
		double bY = y() - Double.MIN_NORMAL;
		double w = 2 * Double.MIN_NORMAL;
		return new Rectangle2D.Double(bX, bY, w, w);
	}
	
	public PathIterator getPathIterator(AffineTransform at) {
		Ellipse2D wrap = new Ellipse2D.Double(x() - 1.6, y() - 1.6, 3.2, 3.2);
		return wrap.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		Ellipse2D wrap = new Ellipse2D.Double(x() - 1.6, y() - 1.6, 3.2, 3.2);
		return wrap.getPathIterator(at, flatness);
	}
	
	public boolean intersects(double x, double y, double w, double h) {
		return (x <= x()) && (x() < x + w) && (y <= y()) && (y() < y+w);
	}
	
	public boolean intersects(Rectangle2D r) {
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	
	// Shape - Intersectable
	public SPGroup intersections (SPPoint other) {
		if((x() == other.x()) && (y() == other.y())) {
			this.setName("Intersection of " + this.name() + " and " + other.name());
			return new SPGroup(this);
		} else {
			return null;
		}
	}
	
	public SPGroup intersections (SPLine other) {
		return other.intersections(this);
	}
	
	public SPGroup intersections (SPPolyLine other) {
		return other.intersections(this);
	}
	
	public SPGroup intersections (SPPolygon other) {
		return other.intersections(this);
	}
	
	public SPGroup intersections (SPEllipse other) {
		return other.intersections(this);
	}
	
	public SPGroup intersections (SPGroup other) {
		return other.intersections(this);
	}

	@Override
	public int compareTo(SPPoint other) {
		if (this.polarAngle() < other.polarAngle()) {
			return -1;
		} else if (this.polarAngle() > other.polarAngle()) {
			return 1;
		} else {
			if (this.polarDistance() < other.polarDistance()) {
				return 1;
			} else if (this.polarDistance() > other.polarDistance()) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	private void nameShape()
	{
		setName( "Point " + pointCount );
		pointCount++;
	}
	
	// Returns SVG representation of the shape
	public String toString() {
		return "";
	}
}
