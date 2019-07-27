package geometricSketchpad.shapes;

import java.beans.PropertyChangeListener;
import java.io.Serializable;
//import java.util.Collection;
import java.awt.*;

public interface SketchpadObject extends Serializable{
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	public boolean isNotifyOnEDT();
	public PropertyChangeListener[] getPropertyChangeListeners();
	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);
	public boolean hasListeners(String propertyName);
	public void removePropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
	
	public boolean toggleSelect();
	public void toggleSelect (boolean selected);
	public boolean selected();
	
	public double rotation();
	public void setRotation (Number radians);
	public void rotate (Number radians);
	
	public void setLocation (Number x, Number y);
	public void setLocation (SPPoint p);
	public SPPoint getLocation();
	public double x();
	public double y();
	
	public void setName (String name);
	public String name();
	
	public boolean grouped();
	public SPGroup parentGroup();
	public void setParentGroup(SPGroup g);
	
	public void draw (Graphics g);
	
	public SPGroup intersections (SPPoint other);
	public SPGroup intersections (SPLine other);
	public SPGroup intersections (SPPolyLine other);
	public SPGroup intersections (SPPolygon other);
	public SPGroup intersections (SPEllipse other);
	public SPGroup intersections (SPGroup other);
	public SPGroup intersections (SketchpadObject other);
	
	public String toString();

}
