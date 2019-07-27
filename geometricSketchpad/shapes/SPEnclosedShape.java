package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public abstract class SPEnclosedShape extends SPCompositeShape { 
	private static final long serialVersionUID = 4931744332032215127L;
	// EnclosedShape - Area
	public abstract double area();
	
	// CompositeShape - Length
	public abstract double length();	
	
	// Shape - java.awt.Shape
	public abstract boolean contains(double x, double y);
	public abstract boolean contains(double x, double y, double w, double h);
	public abstract boolean contains(Point2D p);
	public abstract boolean contains(Rectangle2D r);
	public abstract Rectangle getBounds();
	public abstract Rectangle2D getBounds2D();
	public abstract PathIterator getPathIterator(AffineTransform at);
	public abstract PathIterator getPathIterator(AffineTransform at, double flatness);
	public abstract boolean intersects(double x, double y, double w, double h);
	public abstract boolean intersects(Rectangle2D r);
	
	// Shape - Intersectable
	public abstract SPGroup intersections (SPPoint other);
	public abstract SPGroup intersections (SPLine other);
	public abstract SPGroup intersections (SPPolyLine other);
	public abstract SPGroup intersections (SPPolygon other);
	public abstract SPGroup intersections (SPEllipse other);
}
