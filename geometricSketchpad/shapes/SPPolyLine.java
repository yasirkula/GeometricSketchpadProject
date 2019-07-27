package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public class SPPolyLine extends SPCompositeShape implements Decomposable { 
	
	// properties
	private static final long serialVersionUID = -2083547214422251772L;
	private SPGroup vertices;
	
	public static int polyLineCount;
	
	// constructors
	/*public SPPolyLine() {
		vertices = new SPGroup();
	}*/
	
	public SPPolyLine( SPPoint... points ) {
		vertices = new SPGroup();
		
		for( SPPoint p : points )
		{
			vertices.add( p );
		}
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public SPPolyLine( Number[] x, Number[] y ) {
		vertices = new SPGroup();
		
		for( int i = 0; i < x.length; i++ )
		{
			vertices.add( new SPPoint( x[i], y[i] ) );
		}
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public SPPolyLine( SPGroup points ) {
		vertices = points;
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public void adjustPosition( double dx, double dy )
	{
		for( int i = 0; i < vertices.size(); i++ )
		{
			SPPoint p = (SPPoint) vertices.get( i );
			
			vertices.set( i , new SPPoint( p.x() + dx, p.y() + dy ) );
		}
	}
	
	public SPPoint centroid() {
		double centX = 0;
		double lenX = 0;
		double centY = 0;
		double lenY = 0;
		SPPoint now, next;
		for (int itr = 0; itr < vertices.size() - 1; itr++) {
			next = (SPPoint) vertices.get(itr + 1);
			now = (SPPoint) vertices.get(itr);
			centX += (Math.pow(next.x(), 2)  - Math.pow(now.x(), 2)) / 2;
			lenX += next.x() - now.x(); 
			centY += (Math.pow(next.y(), 2)  - Math.pow(now.y(), 2)) / 2;
			lenY += next.y() - now.y(); 
		}
		return new SPPoint(centX / lenX, centY / lenY);
	}
	
	// methods
	// Shape - java.awt.Shape
	
	public boolean contains(double x, double y) {
		// TODO DON'T FORGET THIS
		return true;
	}
	
	public boolean contains(double x, double y, double w, double h) {
		// TODO DON'T FORGET THIS
		return true;
	}
	
	public boolean contains(Point2D p) {
		// TODO DON'T FORGET THIS
		return true;
	}
	
	public boolean contains(Rectangle2D r) {
		// TODO DON'T FORGET THIS
		return true;
	}
	
	public Rectangle getBounds() {
		// TODO DON'T FORGET THIS
		return null;
	}
	
	public Rectangle2D getBounds2D() {
		SPPoint centroid = centroid();
		Path2D path = new Path2D.Double();
		path.moveTo(vertices.get(0).x(), vertices.get(0).y());
		for ( SketchpadObject p : vertices) {
			path.lineTo(p.x(), p.y());
		}
		
		return AffineTransform.getRotateInstance(rotation(), centroid.x(), centroid.y()).createTransformedShape( path ).getBounds();
		
		// TODO DON'T FORGET THIS
		//return null;
	}
	
	public PathIterator getPathIterator(AffineTransform at) {
		SPPoint centroid = centroid();
		Path2D path = new Path2D.Double();
		path.moveTo(vertices.get(0).x(), vertices.get(0).y());
		for ( SketchpadObject p : vertices) {
			path.lineTo(p.x(), p.y());
		}
		
		if (at == null) {
			at = AffineTransform.getRotateInstance(rotation(), centroid.x(), centroid.y());
		} else {
			at.rotate(rotation(), centroid.x(), centroid.y());
		}
		
		return path.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		SPPoint centroid = centroid();
		Path2D path = new Path2D.Double();
		path.moveTo(vertices.get(0).x(), vertices.get(0).y());
		for ( SketchpadObject p : vertices) {
			path.lineTo(p.x(), p.y());
		}
		
		if (at == null) {
			at = AffineTransform.getRotateInstance(rotation(), centroid.x(), centroid.y());
		} else {
			at.rotate(rotation(), centroid.x(), centroid.y());
		}
		
		return path.getPathIterator(at);
	}
	
	public boolean intersects(double x, double y, double w, double h) {
		// TODO DON'T FORGET THIS
		return true;
	}
	
	public boolean intersects(Rectangle2D r) {
		// TODO DON'T FORGET THIS
		return true;
	}
	
	// Decomposable
	public SPGroup decompose() {
		SPGroup g = new SPGroup();
		
		for( int i = 1; i < vertices.size(); i++ )
		{
			g.add( new SPLine( ( SPPoint ) vertices.get( i - 1 ), ( SPPoint ) vertices.get( i ) ) );
		}
		
		return g;
	}
	
	// CompositeShape - Length
	public double length() {
		double result = 0;
		
		SPGroup g = decompose();
		
		for( SketchpadObject l : g )
		{
			result += ( (SPLine) l ).length();
		}
		
		return result;
	}
	
	// Shape - Intersectable
	public SPGroup intersections ( SPPoint other ) {
		SPGroup g = decompose();
		
		for( SketchpadObject l : g )
		{
			if( ( (SPLine) l ).contains( other ) )
			{
				SPPoint intersect = new SPPoint( other );
				intersect.setName("Intersection of " + this.name() + " and " + other.name());
				
				return new SPGroup( other );
			}
		}
		
		return null;
	}
	
	public SPGroup intersections ( SPLine other ) {
		SPGroup g = decompose();
		
		SPGroup result = new SPGroup();
		
		for( SketchpadObject l : g )
		{
			SPGroup intersections = ( (SPLine) l ).intersections( other );
			
			if( intersections != null )
			{
				SPPoint p = (SPPoint) intersections.get( 0 );
				p.setName("Intersection of " + this.name() + " and " + other.name());
				
				if( result.indexOf( p ) < 0 )
					result.add( p );
			}
		}
		
		if( result.size() == 0 )
			return null;
		
		return result;
	}
	
	public SPGroup intersections ( SPPolyLine other ) {
		SPGroup g1 = decompose();
		SPGroup g2 = other.decompose();
		
		SPGroup result = new SPGroup();
		
		for( SketchpadObject l1 : g1 )
		{
			for( SketchpadObject l2 : g2 )
			{
				SPGroup intersections = ( ( SPLine ) l1 ).intersections( ( ( SPLine ) l2 ) );
				
				if( intersections != null )
				{
					SPPoint p = ( SPPoint ) intersections.get( 0 );
					p.setName("Intersection of " + this.name() + " and " + other.name());
					
					if( result.indexOf( p ) < 0 )
						result.add( p );
				}
			}
		}
		
		if( result.size() == 0 )
			return null;
		
		return result;
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
		setName( "PolyLine " + polyLineCount );
		polyLineCount++;
	}
	
	// Returns SVG representation of the shape
	public String toString() {
		String points = "";
		
		for( int i = 0; i < vertices.size(); i++ )
		{
			SPPoint p = (SPPoint) vertices.get( i );
			points += "" + p.x() + "," + p.y() + " ";
		}
		
		return "<polyline id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + centroid().x() + " " + centroid().y() + ")\" fill=\"none\" stroke=\"black\" stroke-width=\"4\" points=\"" + points + "\" />";
	}
}