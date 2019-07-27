package geometricSketchpad.shapes;

import geometricSketchpad.tools.SVGImporter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.geom.*;

public abstract class SPShape implements SketchpadObject, Shape { 
	private static final long serialVersionUID = 2296521279820010665L;
	private final SwingPropertyChangeSupport SPCS = new SwingPropertyChangeSupport(this);
	private double locX = 0;
	private double locY = 0;
	private double rotation = 0;
	private boolean selected = false;
	private SPGroup parentGroup = null;
	private String name = "Shape";
	private Rectangle2D bounds = null;
	
	public Rectangle2D getBoundaries()
	{
		if( bounds == null )
			return null;
		
		return new Rectangle2D.Double( bounds.getX() -1, bounds.getY() -1, bounds.getWidth() + 2, bounds.getHeight() + 2 );
	}

	public abstract void adjustPosition( double deltaX, double deltaY );
	
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
	public final SPGroup intersections(SketchpadObject other) {
		if (other instanceof SPPoint) {
			return this.intersections((SPPoint) other);
		} else if (other instanceof SPLine) {
			return this.intersections((SPLine) other);
		} else if (other instanceof SPPolyLine) {
			return this.intersections((SPPolyLine) other);
		} else if (other instanceof SPPolygon) {
			return this.intersections((SPPolygon) other);
		} else if (other instanceof SPEllipse) {
			return this.intersections((SPEllipse) other);
		} else if (other instanceof SPGroup) {
			return this.intersections((SPGroup) other);
		} else {
			return null;
		}
	}
	
	// SketchpadShape - Drawable
	public final void draw (Graphics g) {
		((Graphics2D) g).draw(this);
		
		// Draw boundaries of the shape as a rectangle if shape is selected
		//Rectangle2D boundaries = this.getBounds2D();
		
		if( bounds != null && selected )
		{
			Graphics2D g2D = ( Graphics2D ) g;
			
			Stroke currentS = g2D.getStroke();
			Color currentC = g2D.getColor();
	
		    g2D.setColor( Color.blue );
		    g2D.setStroke( new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 7.0f }, 0.0f ) );
	               
		    g2D.draw ( bounds );
		    
		    g2D.setStroke( currentS );
		    g2D.setColor( currentC );
		}
	}
	
	// SketchpadShape - Groupable
	public final boolean grouped() {
		return parentGroup != null;
	}
	
	public final SPGroup parentGroup() {
		return parentGroup;
	}
	
	public final void setParentGroup(SPGroup g) {
		SPGroup oldParentGroup = parentGroup;
		if (g == null || g.contains(this)) {
			parentGroup = g;
			firePropertyChange("parentGroup", oldParentGroup, parentGroup);
			SVGImporter.isSaved = false;
		}
	}
	
	// SketchpadShape - Locatable
	public final void setLocation (Number x, Number y) {
		double oldX = locX;
		double oldY = locY;
		this.locX = x.doubleValue();
		this.locY = y.doubleValue();
		if (getPropertyChangeListeners().length !=0) {
			firePropertyChange("location", new SPPoint (oldX, oldY), new SPPoint (locX, locY));
			SVGImporter.isSaved = false;
		}
	}
	
	public final void setLocation (SPPoint p) {
		double oldX = locX;
		double oldY = locY;
		this.locX = p.x();
		this.locY = p.y();
		if (getPropertyChangeListeners().length !=0) {
			firePropertyChange("location", new SPPoint (oldX, oldY), new SPPoint (locX, locY));
			SVGImporter.isSaved = false;
		}
		
		bounds = getBounds2D();
	}
	
	public final SPPoint getLocation() {
		return new SPPoint(x(),y());
	}
	
	public final void translate (double dx, double dy) {
		setLocation(x() + dx, y() + dy);
		
		adjustPosition( dx, dy );
		
		bounds = getBounds2D();
	}
	
	public final double x() {
		return locX;
	}
	
	public final double y() {
		return locY;
	}
	
	// SketchpadShape - Namable
	public final void setName (String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange("name", oldName, name);
		SVGImporter.isSaved = false;
	}
	
	public final String name() {
		return name;
	}
	
	// SketchpadShape - Selectable
	public final boolean toggleSelect() {
		toggleSelect(!selected);
		return selected;
	}
	
	public final void toggleSelect (boolean select) {
		boolean oldSelectedValue = this.selected;
		this.selected = select;
		if (parentGroup != null && parentGroup.selected() == oldSelectedValue) {
			parentGroup.toggleSelect(select);
		}
		firePropertyChange("selected", oldSelectedValue, selected);
		SVGImporter.isSaved = false;
	}
	
	public final boolean selected() {
		return selected;
	}
	
	// SketchpadShape - Rotatable
	public final double rotation() {
		return rotation;
	}
	
	public final void setRotation (Number radians) {
		double oldRotation = rotation;
		rotation = radians.doubleValue();
		firePropertyChange("rotation", oldRotation, rotation);
		SVGImporter.isSaved = false;
		
		bounds = getBounds2D();
	}
	
	public final void rotate (Number radians) {
		double oldRotation = rotation;
		rotation += radians.doubleValue();
		firePropertyChange("rotation", oldRotation, rotation);
		SVGImporter.isSaved = false;
		
		bounds = getBounds2D();
	}
	
	// SketchpadShape - SwingPropertyChangeSupporter
	public final void addPropertyChangeListener(PropertyChangeListener listener) {
		SPCS.addPropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		SPCS.addPropertyChangeListener(propertyName, listener);
	}

	final void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
		SPCS.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	final void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
		SPCS.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	final void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
		SPCS.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
	}

	final void firePropertyChange(PropertyChangeEvent event) {
		SPCS.firePropertyChange(event);
	}

	final void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		SPCS.firePropertyChange(propertyName, oldValue, newValue);
	}

	final void firePropertyChange(String propertyName, int oldValue, int newValue) {
		SPCS.firePropertyChange(propertyName, oldValue, newValue);
	}

	final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		SPCS.firePropertyChange(propertyName, oldValue, newValue);
	}

	public final boolean isNotifyOnEDT() {
		return SPCS.isNotifyOnEDT();
	}

	public final PropertyChangeListener[] getPropertyChangeListeners() {
		return SPCS.getPropertyChangeListeners();
	}

	public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		return SPCS.getPropertyChangeListeners(propertyName);
	}

	public final boolean hasListeners(String propertyName) {
		return SPCS.hasListeners(propertyName);
	}

	public final void removePropertyChangeListener(PropertyChangeListener listener) {
		SPCS.removePropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		SPCS.removePropertyChangeListener(propertyName, listener);
	}
}
