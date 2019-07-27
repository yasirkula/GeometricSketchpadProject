package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public class SPEllipse extends SPEnclosedShape { 
	private static final long serialVersionUID = -8804630626830267174L;
	private double major;
	private double minor;
	
	public static int circleCount;
	public static int ellipseCount;
	
	public static void main (String[] args) {
		SPEllipse ellipse;
		double major = 300;
		double minor = 400;
		double x = 200 + Math.random() * 500;
		double y = 200 + Math.random() * 500;
//		SPGroup ellipses = new SPGroup();
		for (int i = 0; i < 18; i++) {
			ellipse = new SPEllipse( major, minor, x, y);
			ellipse.setRotation(i*Math.PI/36);
			System.out.println(ellipse);
			
//			ellipses.add(ellipse);
		}
/*		
		for (int i = 0; i < 18; i++) {
			ellipse = new SPEllipse( major, minor, x, y);
			ellipse.setRotation(i*Math.PI/36);
			System.out.println(ellipse.toStringS());
		}
//		System.out.println(ellipses);
*/

	}
	
	public SPEllipse () {
		this(0,0,0,0);
	}
	
	public SPEllipse (Number major, Number minor) {
		this(major,minor,0,0);
	}
	public SPEllipse (Number major, Number minor, Number x, Number y) {
		this.major = major.doubleValue();
		this.minor = minor.doubleValue();
		setLocation(x,y);
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public void adjustPosition( double dx, double dy )
	{
		// do nothing
	}
	
	public double getMinor() {
		return minor;
	}
	public double getMajor() {
		return major;
	}
	public void setMajor(double major) {
		this.major = major;
	}
	public void setMinor(double minor) {
		this.minor = minor;
	}
	public boolean isCircle() {
		return getMajor() == getMinor();
	}

	// Shape - java.awt.Shape
	public boolean contains(double x, double y) {
		// TODO DONT FORGET TO IMPLEMENT THIS
				return true;	
	}
	public boolean contains(double x, double y, double w, double h) {
		// TODO DONT FORGET TO IMPLEMENT THIS
				return true;	
	}
	public boolean contains(Point2D p) {
		// TODO DONT FORGET TO IMPLEMENT THIS
				return true;	
	}
	public boolean contains(Rectangle2D r) {
		// TODO DONT FORGET TO IMPLEMENT THIS
				return true;	
	}
	public Rectangle getBounds() {
		// TODO DONT FORGET TO IMPLEMENT THIS
				return null;	
	}
	public Rectangle2D getBounds2D() {
		return AffineTransform.getRotateInstance( rotation(), x() + getMajor()/2, y() + getMinor()/2 ).createTransformedShape(new Ellipse2D.Double(x(), y(), getMajor(), getMinor() )).getBounds();
	}
	
	public PathIterator getPathIterator(AffineTransform at) {
		Ellipse2D wrap = new Ellipse2D.Double(x(), y(), getMajor(), getMinor());
		if (at != null) {
			at.rotate(rotation(), x(), y());
		} else {
			at = AffineTransform.getRotateInstance(rotation(), x() + getMajor()/2, y() + getMinor()/2);
		}
		return wrap.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		Ellipse2D wrap = new Ellipse2D.Double(x(), y(), getMajor(), getMinor());
		if (at != null) {
			at.rotate(rotation(), x(), y());
		} else {
			at = AffineTransform.getRotateInstance(rotation(), x() + getMajor()/2, y() + getMinor()/2);
		}
		return wrap.getPathIterator(at, flatness);
	}
	
	public boolean intersects(double x, double y, double w, double h) {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return true;	
	}

	public boolean intersects(Rectangle2D r) {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return true;
	}
	
	// EnclosedShape - Area
	public double area() {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return 0;	
		
	}
	
	// CompositeShape - Length
	public double length() {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return 0;	
		
	}
	
	// Shape - Intersectable
	public SPGroup intersections (SPPoint other) {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return null;	
	}
	
	public SPGroup intersections (SPLine other) {
		SPGroup result = new SPGroup();
		
		double x1, x2, y1, y2;
		double h, b, k, a;
		double m;
		
		m = other.slope();
		h = x();
		k = y();
		a = getMajor();
		b = getMinor();
		
		double epsilon = other.y() - other.slope() * other.x() - y();
		double omega = other.y() - other.slope() * other.x() + other.slope() * x();
		
		double inSqrt = Math.sqrt( a * a * m * m + b * b - omega * omega - k * k + 2 * omega * k );
		double denominator = a * a * m * m + b * b;
		
		x1 = ( int )( ( h * b * b - m * a * a * epsilon + a * b * inSqrt ) / denominator );
		x2 = ( int )( ( h * b * b - m * a * a * epsilon - a * b * inSqrt ) / denominator );
		
		y1 = ( int )( ( b * b * omega + k * a * a * m * m + a * b * m * inSqrt ) / denominator );
		y2 = ( int )( ( b * b * omega + k * a * a * m * m - a * b * m * inSqrt ) / denominator );
		
		SPPoint p1, p2;
		p1 = new SPPoint( x1, y1 );
		p1.setName("Intersection of " + this.name() + " and " + other.name());
		
		if( other.contains( p1 ) )
			result.add( p1 );
		
		if( x1 != x2 || y1 != y2 )
		{
			p2 = new SPPoint( x2, y2 );
			p2.setName("Intersection of " + this.name() + " and " + other.name());
			
			if( other.contains( p2 ) )
			{
				result.add( p2 );
			}
		}
		
		if( result.size() == 0 )
			return null;
		
		return result;
	}
	
	public SPGroup intersections (SPPolyLine other) {
		SPGroup g = other.decompose();
		
		SPGroup result = new SPGroup();

		for( SketchpadObject l : g )
		{
			SPGroup intersections = intersections( ( ( SPLine ) l ) );
			
			if( intersections != null )
			{
				for( SketchpadObject p : intersections )
				{					
					if( result.indexOf( ( SPPoint ) p ) < 0 )
						result.add( p );
				}
			}
		}
		
		if( result.size() == 0 )
			return null;
		
		return result;	
	}
	
	public SPGroup intersections (SPPolygon other) {
		SPGroup g = other.decompose();
		
		SPGroup result = new SPGroup();

		for( SketchpadObject l : g )
		{
			SPGroup intersections = intersections( ( ( SPLine ) l ) );
			
			if( intersections != null )
			{
				for( SketchpadObject p : intersections )
				{					
					if( result.indexOf( ( SPPoint ) p ) < 0 )
						result.add( p );
				}
			}
		}
		
		if( result.size() == 0 )
			return null;
		
		return result;		
	}
	
	public SPGroup intersections (SPEllipse other) {
		// TODO DONT FORGET TO IMPLEMENT THIS
		return null;	
	}
	
	public SPGroup intersections (SPGroup other) {
		return other.intersections(this);
	}
	
	private void nameShape()
	{
		if( isCircle() )
		{
			setName( "Circle " + circleCount );
			circleCount++;
		}
		else
		{
			setName( "Ellipse " + ellipseCount );
			ellipseCount++;
		}
	}
	
	// Returns SVG representation of the shape
	public String toString() {
		if (isCircle()) {
			return "<circle id=\"" + name() + "\" cx=\"" + (x() + getMajor()/2) + "\" cy=\"" + (y() + getMajor()/2) + "\" r=\"" + getMajor()/2 + "\""
				+ " fill=\"none\" stroke=\"black\" stroke-width=\"4\"/>";
		} else {
			return "<ellipse id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees(rotation()) + " " + (x() + getMajor()/2) + " " + (y() + getMinor()/2) + ")\" " 
				+ "cx=\"" + (x() + getMajor()/2) + "\" cy=\""+ (y() + getMinor()/2) + "\" rx=\"" + getMajor()/2 + "\" ry=\""+ getMinor()/2 + "\" "
				+ "fill=\"none\" stroke=\"black\" stroke-width=\"4\"/>";
		}
	}
}
