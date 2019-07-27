package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public class SPPolygon extends SPEnclosedShape implements Decomposable { 
	
	// properties
	private static final long serialVersionUID = 3792270312603449997L;
	private SPGroup vertices;
	
	public static int polygonCount;
	public static int rectangleCount;
	public static int squareCount;
	
	// constructors
	/*public SPPolygon() {
		vertices = new SPGroup();
	}*/
	
	public SPPolygon( SPPoint... points ) {
		vertices = new SPGroup();
		
		for( SPPoint p : points )
		{
			vertices.add( p );
		}
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public SPPolygon( Number[] x, Number[] y ) {
		vertices = new SPGroup();
		
		for( int i = 0; i < x.length; i++ )
		{
			vertices.add( new SPPoint( x[i], y[i] ) );
		}
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public SPPolygon( SPGroup points ) {
		vertices = points;
		
		nameShape();
		translate( 0, 0 ); // to calculate the bounds of the shape implicitly
	}
	
	public SPPolygon( SPPolyLine p ) {
		SPGroup g = p.decompose();
		
		for( int i = 0; i < g.size(); i++ )
		{
			vertices.add( new SPPoint( g.get( i ).x(), g.get( i ).y() ) );
			
			if( i == g.size() )
			{
				vertices.add( new SPPoint( ( ( SPLine ) g.get( i ) ).endPoint().x(), ( ( SPLine ) g.get( i ) ).endPoint().y() ) );
				vertices.add( new SPPoint( g.get( 0 ).x(), g.get( 0 ).y() ) );
			}
		}
		
		vertices = g;
	
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

	// methods
	public boolean regular() {
		for( int i = 2; i < vertices.size(); i++ )
		{
			if( Math.pow( vertices.get( i ).y() - vertices.get( i - 1 ).y(), 2 ) + Math.pow( vertices.get( i ).x() - vertices.get( i - 1 ).x(), 2 ) != Math.pow( vertices.get( i - 2 ).y() - vertices.get( i - 1 ).y(), 2 ) + Math.pow( vertices.get( i - 2 ).x() - vertices.get( i - 1 ).x(), 2 ) )
				return false;
		}
		
		return true;
	}
	
	public boolean convex() {
		// Used algorithm by Paul Baurke: http://debian.fmi.uni-sofia.bg/~sergei/cgsr/docs/clockwise.htm
		boolean sign = true;
		
		for( int i = 0; i < vertices.size(); i++ )
		{
			double dx1 = vertices.get( i + 1 ).x() - vertices.get( i ).x();
			double dy1 = vertices.get( i + 1 ).y() - vertices.get( i ).y();
			double dx2 = vertices.get( i + 2 ).x() - vertices.get( i + 1 ).x();
			double dy2 = vertices.get( i + 2 ).y() - vertices.get( i + 1 ).y();
			double crossProduct = dx1 * dy2 - dy1 * dx2;
			
			if( i == 0 )
				sign = ( crossProduct > 0 );
			else if( ( crossProduct > 0 ) != sign )
				return false;
		}
		
		return true;
	}
	
	public boolean concave() {
		return !convex();
	}
	
	public boolean selfIntersecting() {
		SPGroup g = decompose();
		
		for( int i = 0; i < g.size(); i++ )
		{
			for( int j = i + 1; j < g.size(); j++ )
			{
				if( ( (SPLine) g.get( i ) ).intersections( (SPLine) g.get( j ) ) != null )
					return true;
			}
		}
		
		return false;
	}
	
	public SPPoint centroid() {
		SPPoint now, next;
		double centX = 0;
		double centY = 0;
		double area = area();
		for (int itr = 0; itr < vertices.size() - 1; itr++ ) {
			now = (SPPoint) vertices.get(itr);
			next = (SPPoint) vertices.get(itr + 1);
			centX += (now.x() + next.x()) * (now.x() * next.y() - next.x() * now.y());
			centY += (now.y() + next.y()) * (now.x() * next.y() - next.x() * now.y());
		}
		centX = centX / (6 * area);
		centY = centY / (6 * area);
		return new SPPoint (centX, centY);
	}
	
	public boolean isRectangle() {
		if( vertices.size() != 5 ) // First vertex in the group also exists separately as the last vertex
		{
			return false;
		}

		// For rectangle, each angle should be 90 degrees and it implies that
		// if we take each edge as a vector, their vectoral multiplication should
		// be equal to 0.
		for( int i = 0; i <= 2; i++ )
		{
			// Using SPPoint class as a vector in 2D space
			SPPoint vector1 = new SPPoint( ( (SPPoint) vertices.get( i + 1 ) ).x() - ( (SPPoint) vertices.get( i ) ).x(), ( (SPPoint) vertices.get( i + 1 ) ).y() - ( (SPPoint) vertices.get( i ) ).y() );
			SPPoint vector2 = new SPPoint( ( (SPPoint) vertices.get( i + 2 ) ).x() - ( (SPPoint) vertices.get( i + 1 ) ).x(), ( (SPPoint) vertices.get( i + 2 ) ).y() - ( (SPPoint) vertices.get( i + 1 ) ).y() );
			
			if( vector1.x() * vector2.x() + vector1.y() * vector2.y() != 0 )
				return false;
		}
		
		return true;
	}
	
	public boolean isSquare() {
		if( !isRectangle() )
			return false;
		
		double edgeLength;
		
		SPGroup g = decompose();
		
		edgeLength = ( (SPLine) g.get( 0 ) ).length();
		
		for( SketchpadObject l : g )
		{
			if( ( (SPLine) l ).length() != edgeLength )
				return false;
		}
		
		return true;		
	}
	
	public boolean isTriangle() {
		if( vertices.size() == 4 ) // First vertex in the group also exists separately as the last vertex
			return true;
		
		return false;
	}
	
	public SPPoint rotateBase() {
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		for (SketchpadObject p : vertices) {
			maxX = Math.max(maxX, p.x());
			maxY = Math.max(maxY, p.y());
			minX = Math.min(minX, p.x());
			minY = Math.min(minY, p.y());
		}
		return new SPPoint((maxX + minX) / 2 , (maxY + minY) / 2);
	}
	
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
		SPPoint base = rotateBase();
		Path2D path = new Path2D.Double();
		path.moveTo(vertices.get(0).x(), vertices.get(0).y());
		for ( SketchpadObject p : vertices) {
			path.lineTo(p.x(), p.y());
		}
		
		return AffineTransform.getRotateInstance(rotation(), base.x(), base.y()).createTransformedShape( path ).getBounds();
		 
		
		// TODO DON'T FORGET THIS
		//return null;
	}
	
	public PathIterator getPathIterator(AffineTransform at) {
		SPPoint base = rotateBase();
		Path2D path = new Path2D.Double();
		path.moveTo(vertices.get(0).x(), vertices.get(0).y());
		for ( SketchpadObject p : vertices) {
			path.lineTo(p.x(), p.y());
		}
		
		if (at == null) {
			at = AffineTransform.getRotateInstance(rotation(), base.x(), base.y());
		} else {
			at.rotate(rotation(), base.x(), base.y());
		}
		
		return path.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		SPPoint base = rotateBase();
		Path2D path = new Path2D.Double();
		path.moveTo(vertices.get(0).x(), vertices.get(0).y());
		for ( SketchpadObject p : vertices) {
			path.lineTo(p.x(), p.y());
		}
		
		if (at == null) {
			at = AffineTransform.getRotateInstance(rotation(), base.x(), base.y());
		} else {
			at.rotate(rotation(), base.x(), base.y());
		}
		
		return path.getPathIterator(at, flatness);
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
			g.add( new SPLine( (SPPoint) vertices.get( i - 1 ), (SPPoint) vertices.get( i ) ) );
		}
		
		return g;
	}
	
	// EnclosedShape - Area
	public double area() {
		double area = 0;
		SPPoint now, next;
		if (selfIntersecting()) {
			// TODO DON'T FORGET THIS
		} else {
			for (int itr = 0; itr < vertices.size() - 1; itr++ ) {
				now = (SPPoint) vertices.get(itr);
				next = (SPPoint) vertices.get(itr + 1);
				area +=  now.x() * next.y() - next.x() * now.y(); 
			}
			area = area / 2;
		}
		return area;
	}

	// CompositeShape - Length
	public double length() {
		double result = 0;
		
		SPGroup g = decompose();
		
		for( SketchpadObject l : g )
		{
			result += ( ( SPLine ) l ).length();
		}
		
		return result;
	}
	
	// Shape - Intersectable
	public SPGroup intersections ( SPPoint other ) {
		SPGroup g = decompose();
		
		for( SketchpadObject l : g )
		{
			if( ( ( SPLine ) l ).contains( other ) )
			{
				SPPoint intersect = new SPPoint( other );
				intersect.setName("Intersection of " + this.name() + " and " + other.name());
				
				return new SPGroup( intersect );
			}
		}
		
		return null;
	}
	
	public SPGroup intersections ( SPLine other ) {
		SPGroup g = decompose();
		
		SPGroup result = new SPGroup();
		
		for( SketchpadObject l : g )
		{
			SPGroup intersections = l.intersections( other );
			
			if( intersections != null )
			{
				SPPoint p = ( SPPoint ) intersections.get( 0 );
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
	
	public SPGroup intersections ( SPPolygon other ) {
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
	
	public SPGroup intersections ( SPEllipse other ) {
		return other.intersections( this );
	}
	
	public SPGroup intersections ( SPGroup other ) {
		return other.intersections( this );
	}
	
	private void nameShape()
	{
		if( isRectangle() )
		{
			if( isSquare() )
			{
				setName( "Square " + squareCount );
				squareCount++;
			}
			else
			{
				setName( "Rectangle " + rectangleCount );
				rectangleCount++;
			}
		}
		else
		{
			setName( "Polygon " + polygonCount );
			polygonCount++;
		}
	}
	
	
	// Returns SVG representation of the shape
	public String toString() {
		if( isRectangle() )
		{
			System.out.println( "RECTANGLE!!");
			
			SPPoint startPoint, endPoint;
			
			double width, height;
			
			// Find the top left and bottom right points of the rectangle
			double minX, minY, maxX, maxY;
			
			SPPoint po = (SPPoint) vertices.get( 0 );
			startPoint = po;
			endPoint = po;
			
			minX = po.x();
			maxX = po.x();
			minY = po.y();
			maxY = po.y();
			
			for( int i = 1; i < vertices.size() - 1; i++ )
			{
				SPPoint p = (SPPoint) vertices.get( i );
				if( p.x() < minX )
					minX = p.x();
				if( p.x() > maxX )
					maxX = p.x();
				if( p.y() < minY )
					minY = p.y();
				if( p.y() > maxY )
					maxY = p.y();
			}
			
			for( int i = 0; i < vertices.size() - 1; i++ )
			{
				SPPoint p = (SPPoint) vertices.get( i );
				if( p.x() == minX && p.y() == minY )
					startPoint = p;
				if( p.x() == maxX && p.y() == maxY )
					endPoint = p;
			}
			
			width = endPoint.x() - startPoint.x();
			height = endPoint.y() - startPoint.y();
			
			
			/*return "<g transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + rotateBase().x() + " " + rotateBase().y() + ")\">" + "\n" +
				   "<rect x=\"" + startPoint.x() + "\" y=\"" + startPoint.y() + "\" width=\"" + width + "\" height=\"" + height + "\" fill=\"none\" stroke=\"black\" stroke-width=\"4\" />" + "\n" +
				   "</g>";*/
			
			return "<rect id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + rotateBase().x() + " " + rotateBase().y() + ")\" x=\"" + startPoint.x() + "\" y=\"" + startPoint.y() + "\" width=\"" + width + "\" height=\"" + height + "\" fill=\"none\" stroke=\"black\" stroke-width=\"4\" />";
		}
		// Assert: Shape is not in the format of rectangle
		
		System.out.println( "NOT RECTANGLE!!");
		
		String points = "";
		
		for( int i = 0; i < vertices.size() - 1; i++ )
		{
			SPPoint p = (SPPoint) vertices.get( i );
			points += "" + p.x() + "," + p.y() + " ";
		}
		
		return "<polygon id=\"" + name() + "\" transform=\"rotate(" + Math.toDegrees( rotation() ) + " " + rotateBase().x() + " " + rotateBase().y() + ")\" fill=\"none\" stroke=\"black\" stroke-width=\"4\" points=\"" + points + "\" />";
		
	}
}
