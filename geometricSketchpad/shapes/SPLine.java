package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public class SPLine extends SPCompositeShape {
	private static final long serialVersionUID = -5095517855755758016L;
	private SPPoint endPoint;
	private boolean restricted;
	
	public static int lineCount;
	public static int lineSegmentCount;
	
	// public SPLine () {}
	
	public SPLine( SPPoint a, SPPoint b)
	{
		this (a,b,true);
	}
	
	public SPLine( SPPoint a, SPPoint b, boolean restricted )
	{
		setLocation( a );
		endPoint = b;
		this.restricted = restricted;
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public SPLine( SPPoint p, double angle )
	{
		setLocation( p );
		endPoint = new SPPoint( p.x() + Math.cos( angle ) * 10, p.y() + Math.sin( angle ) * 10);
		restricted = false;
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public void adjustPosition( double dx, double dy )
	{
		endPoint = new SPPoint( endPoint.x() + dx, endPoint.y() + dy );
	}
	
	public double slope() {
		return slope (getLocation(), endPoint);
	}
	
	public static double slope (SPPoint p1, SPPoint p2) {
		return (p1.y() - p2.y()) / (p1.x() - p2.x());
	}
	
	public static double slope (double x1, double y1, double x2, double y2) {
		double slope = (y1 - y2) / (x1 - x2);
		
		if (slope == 0) {
			return Double.MIN_NORMAL;
		}
		
		return slope;
	}
	
	public boolean contains (SPPoint p) {
		return contains(p.x(), p.y());
	}
	
	public SPPoint endPoint() {
		return new SPPoint(endPoint);
	}
	
	// Restrictable
	public void restrict (SPPoint a, SPPoint b) {}
	public void restrict (int l1, int l2, char xORy) {}
	public void restrict (int l1, int l2, String xORy) {}
	
	public boolean restricted() {
		return restricted;
	}
	
	// Shape - java.awt.Shape
	public boolean contains(double x, double y) {
		return slope() == slope(x(), y(), x, y);
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
		if (restricted) {
			return new Rectangle(Integer.MIN_VALUE/2, Integer.MIN_VALUE/2, Integer.MAX_VALUE, Integer.MAX_VALUE);
		} else {
			int lx = (int) Math.min(x(), endPoint.x());
			int hx = (int) Math.max(x(), endPoint.x());
			int ly = (int) Math.min(y(), endPoint.y());
			int hy = (int) Math.max(y(), endPoint.y());
			return new Rectangle(lx, hy, hx-lx, hy-ly); 
		}
	}
	
	public Rectangle2D getBounds2D() {
		if ( !restricted ) {
			return new Rectangle2D.Double(Double.MIN_VALUE/2, Double.MIN_VALUE/2, Double.MAX_VALUE, Double.MAX_VALUE);
		} else {
			Path2D wrap = new Path2D.Double();
			wrap.moveTo(x(), y());
			wrap.lineTo(endPoint.x(), endPoint.y());
			
			return AffineTransform.getRotateInstance(rotation(), ( x() + endPoint.x() ) / 2, ( y() + endPoint.y() ) / 2 ).createTransformedShape( wrap ).getBounds();
			
			/*double lx = Math.min(x(), endPoint.x());
			double hx = Math.max(x(), endPoint.x());
			double ly = Math.min(y(), endPoint.y());
			double hy = Math.max(y(), endPoint.y());
			return new Rectangle2D.Double(lx, ly, hx - lx, hy - ly);*/
		}
	}
	
	public PathIterator getPathIterator(AffineTransform at) {
		Path2D wrap = new Path2D.Double();
		if (restricted) {
			wrap.moveTo(x(), y());
			wrap.lineTo(endPoint.x(), endPoint.y());
		} else {
			if (x() == endPoint.x()) {
				wrap.moveTo(x(), y() - 5000);
				wrap.lineTo(x(), y() + 5000);
			}
			wrap.moveTo(x() - 5000, y() - 5000 * slope());
			wrap.lineTo(endPoint.x() + 5000, endPoint.y() + 5000 * slope());
		}
		if (at != null) {
			at.rotate(rotation(), x(), y());
		} else {
			at = AffineTransform.getRotateInstance(rotation(), ( x() + endPoint.x() ) / 2, ( y() + endPoint.y() ) / 2 );
		}
		return wrap.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		Path2D wrap = new Path2D.Double();
		if (restricted) {
			wrap.moveTo(x(), y());
			wrap.lineTo(endPoint.x(), endPoint.y());
		} else {
			wrap.moveTo(x() - 5000, y() - 5000 * slope());
			wrap.lineTo(endPoint.x() + 5000, endPoint.y() + 5000 * slope());
		}
		if (at != null) {
			at.rotate(rotation(), ( x() + endPoint.x() ) / 2, ( y() + endPoint.y() ) / 2 );
		} else {
			at = AffineTransform.getRotateInstance(rotation(), x(), y());
		}
		return wrap.getPathIterator(at, flatness);
	}
	
	public boolean intersects(double x, double y, double w, double h) {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return true;
	}
	
	public boolean intersects(Rectangle2D r) {
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}
	
	// CompositeShape - Length
	public double length() {
		if (restricted) {
			return getLocation().distanceTo(endPoint);
		} else {
			return Double.POSITIVE_INFINITY;
		}
	}
	
	// Shape - Intersectable
	public SPGroup intersections (SPPoint other) {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return null;
	}
	
	public SPGroup intersections (SPLine other) {
		SPGroup result = new SPGroup();
		
		double x1, x2, x3, x4;
		double y1, y2, y3, y4;
		double denominator;
		
		x1 = x();
		x2 = endPoint.x();
		x3 = other.x();
		x4 = other.endPoint.x();
		
		y1 = y();
		y2 = endPoint.y();
		y3 = other.y();
		y4 = other.endPoint.y();
		
		denominator = ( x1 - x2 ) * ( y3 - y4 ) - ( y1 - y2 ) * ( x3 - x4 );
		
		// If slopes are not same, then find the collision point
		if( denominator != 0 )
		{
			SPPoint intersect = new SPPoint( ( ( x1 * y2 - y1 * x2 ) * ( x3 - x4 ) - ( x1 - x2 ) * ( x3 * y4 - y3 * x4 ) ) / denominator, ( ( x1 * y2 - y1 * x2 ) * ( y3 - y4 ) - ( y1 - y2 ) * ( x3 * y4 - y3 * x4 ) ) / denominator );
			intersect.setName("Intersection of " + this.name() + " and " + other.name());
			result.add( intersect );
			
			// If at least one of the lines does not contain the point, then return nothing
			if( ( !contains( (SPPoint) result.get( 0 ) ) && restricted ) || ( !other.contains( (SPPoint) result.get( 0 ) ) && other.restricted ) )
			{
				return null;
			}
		}

		return result;
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
	
	private void nameShape()
	{
		if( restricted )
		{
			setName( "LineSegment " + lineSegmentCount );
			lineSegmentCount++;
		}
		else
		{
			setName( "Line " + lineCount );
			lineCount++;
		}
	}
	
	// Returns SVG representation of the shape
	public String toString() {
		if( restricted )
			return "<line id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + ( ( x() + endPoint.x() ) / 2 ) + " " + ( ( y() + endPoint.y() ) / 2 ) + ")\" x1=\"" + x() + "\" y1=\"" + y() + "\" x2=\"" + endPoint.x() + "\" y2=\"" + endPoint.y() + "\" stroke=\"black\" stroke-width=\"4\" />";
		else if (x() == endPoint.x()) {
			return "<line id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + ( ( x() + endPoint.x() ) / 2 ) + " " + ( ( y() + endPoint.y() ) / 2 ) + ")\" x1=\"" + ( x() ) + "\" y1=\"" + ( y() - 5000 ) + "\" x2=\"" + ( endPoint.x()) + "\" y2=\"" + ( endPoint.y() + 5000 ) + "\" stroke=\"black\" stroke-width=\"4\" />";
		} else {
			return "<line id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + ( ( x() + endPoint.x() ) / 2 ) + " " + ( ( y() + endPoint.y() ) / 2 ) + ")\" x1=\"" + ( x() - 5000 ) + "\" y1=\"" + ( y() - 5000 * slope() ) + "\" x2=\"" + ( endPoint.x() + 5000 ) + "\" y2=\"" + ( endPoint.y() + 5000 * slope() ) + "\" stroke=\"black\" stroke-width=\"4\" />";
		}
	}

}