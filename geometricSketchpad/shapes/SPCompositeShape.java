package geometricSketchpad.shapes;

import java.awt.*;
import java.awt.geom.*;

public abstract class SPCompositeShape extends SPShape { 
	private static final long serialVersionUID = 5824714421974500492L;
	// CompositeShape - Length
	public abstract double length();
	
	// SketchpadShape - java.awt.Shape
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
	
	// SketchpadShape - Intersectable
	public abstract SPGroup intersections (SPPoint other);
	public abstract SPGroup intersections (SPLine other);
	public abstract SPGroup intersections (SPPolyLine other);
	public abstract SPGroup intersections (SPPolygon other);
	public abstract SPGroup intersections (SPEllipse other);
}
