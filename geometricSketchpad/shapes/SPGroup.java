package geometricSketchpad.shapes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.SwingPropertyChangeSupport;
import java.util.*;
import java.awt.*;
import geometricSketchpad.tools.SVGImporter;

public class SPGroup extends ArrayList<SketchpadObject> implements Decomposable, SketchpadObject {
	private static final long serialVersionUID = 3406399871302267001L;
	private final SwingPropertyChangeSupport SPCS = new SwingPropertyChangeSupport(this);
	private double locX;
	private double locY;
	private boolean selected;
	private SPGroup parentGroup;
	private String name;
	private double rotation;
	
	public SPGroup () {
		this(10);
	}
	
	public SPGroup (int size) {
		super(size);
		locX = 0;
		locY = 0;
		selected = false;
		parentGroup = null;
		name = "Group";
		rotation = 0;
	}
	
	public SPGroup (SketchpadObject... items) {
		this(items.length);
		for (SketchpadObject item : items) {
			add(item);
			item.setParentGroup(this);
		}
	}
	
	public SPGroup (Collection<SketchpadObject> collection) {
		this(collection.size());
		for (SketchpadObject item : collection) {
			add(item);
			item.setParentGroup(this);
		}
	}
	
	public String toString() {
		String others = "";
		for (SketchpadObject shape : this) {
			others += shape.toString() + "\n"; 
		}
		return String.format("<g transform=\"rotate(%f %f %f)\">\n%s</g>", rotation(), x(), y(), others);  
	}
	
	public void deleteSelected()
	{
		for( int i = this.size() - 1; i >= 0; i-- )
		{
			SketchpadObject s = get( i );
			
			if( s instanceof SPGroup )
			{
				( (SPGroup) s ).deleteSelected();
			}
			else
			{
				if( s.selected() )
				{
					this.remove( indexOf( s ) );
				}
			}
		}
	}
	
	public void moveSelected( double deltaX, double deltaY )
	{
		for( SketchpadObject s : this )
		{
			if( s instanceof SPGroup )
			{
				( (SPGroup) s ).moveSelected( deltaX, deltaY );
			}
			else
			{
				if( s.selected() )
					( ( SPShape ) s ).translate( deltaX, deltaY );
			}
		}
	}
	
	public void rotateSelected( double radians )
	{
		for( SketchpadObject s : this )
		{
			if( s instanceof SPGroup )
			{
				( (SPGroup) s ).rotateSelected( radians );
			}
			else
			{
				if( s.selected() )
					( ( SPShape ) s ).rotate( radians );
			}
		}
	}
	
	public void deselectAll()
	{
		for( SketchpadObject s : this )
		{
			if( s instanceof SPGroup )
			{
				( (SPGroup) s ).deselectAll();
			}
			else
			{
				if( s != null )
					s.toggleSelect( false );
			}
		}
	}
	
	// Overrides - ArrayList
	@Override
	public void add (int index, SketchpadObject element) {
		super.add(index, element);
		element.setParentGroup(this);
		fireIndexedPropertyChange("element.add", index, get(index+1), element);
	}
	
	@Override
	public boolean add (SketchpadObject element) {
		boolean retBool = super.add(element);
		element.setParentGroup(this);
		fireIndexedPropertyChange("element.add", size() - 1, null, get(size() - 1) );
		return retBool;
	}
	
	@Override
	public SketchpadObject remove (int index) {
		SketchpadObject removed = super.remove(index);
		removed.setParentGroup(null);
		// fireIndexedPropertyChange("element.remove", index, removed, get(index));
		return removed;
	}
	
	@Override
	public boolean remove (Object o) {
		int index = indexOf(o);
		boolean removed = super.remove(o);
		if (index != -1 && removed) {
			((SketchpadObject) o).setParentGroup(null);
			fireIndexedPropertyChange("element.remove", index, o, get(index));
		}
		return removed;
	}
	
	@Override
	public boolean addAll(Collection<? extends SketchpadObject> c) {
		int oldSize = size();
		boolean changed = super.addAll(c);
		for (int itr = oldSize; itr < size(); itr++) {
			get(itr).setParentGroup(this);
			fireIndexedPropertyChange("element.add", itr, null, get(itr));
		}
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends SketchpadObject> c) {
		int oldSize = size();
		boolean changed = super.addAll(index, c);
		for (int itr = oldSize; itr < size(); itr++) {
			get(itr).setParentGroup(this);
			fireIndexedPropertyChange("element.add", itr, null, get(itr));
		}
		return changed;
	}

	@Override
	public void clear() {
		while (size() > 0) {
			remove(0).setParentGroup(null);
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Iterator<?> items = c.iterator();
		boolean retBool = false;
		while (items.hasNext()) {
			retBool = retBool || remove(items.next());
		}
		return retBool;
	}

	@Override
	public Iterator<SketchpadObject> iterator() {
		return new GroupIterator();
	}
	
	@Override 
	public ListIterator<SketchpadObject> listIterator() {
		return new GroupListIterator();
	}
	
	@Override 
	public ListIterator<SketchpadObject> listIterator(int index) {
		return new GroupListIterator(index);
	}

	private class GroupIterator implements Iterator<SketchpadObject> {
		int cursor = 0;
		int lastCalled = -1;
		
		public boolean hasNext() { 
			return cursor < size();
		}
		
		public SketchpadObject next() {
			if (hasNext()) {
				SketchpadObject next = get(cursor);
				lastCalled = cursor;
				cursor++;
				return next;
			} else {
				throw new NoSuchElementException();
			}
		}
		
		public void remove () {
			if (lastCalled != -1) {
				SPGroup.this.remove(lastCalled);
				lastCalled = -1;
			} else {
				throw new IllegalStateException();
			}
		}
    }
    
    private class GroupListIterator extends GroupIterator implements ListIterator<SketchpadObject> {
		public GroupListIterator () {
			this (0);
		}
		
		public GroupListIterator (int index) {
			super();
			cursor = index;
		}
		
		public boolean hasPrevious() {
			return cursor > 0;
		}
		
		public int nextIndex() {
			return cursor;
		}
		
		public int previousIndex() {
			return cursor - 1;
		}
		
		public SketchpadObject previous() {
			if (hasPrevious()) {
				SketchpadObject previous = get(cursor - 1);
				lastCalled = cursor - 1;
				cursor--;
				return previous;
			} else {
				throw new NoSuchElementException();
			}
		}
		
		public void set (SketchpadObject item) {
			SPGroup.this.set(cursor, item);
		}
		
		public void add(SketchpadObject item) {
			SPGroup.this.add(cursor, item);
		}
    }

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		for (int itr = 0; itr < size(); itr++) {
			if (!c.contains(get(itr))) {
				modified = true;
				remove(itr);
				itr--;
			}
		}
		return modified;
	}

	@Override
	public SketchpadObject set(int index, SketchpadObject element) {
		SketchpadObject oldElement = super.set(index, element);
		oldElement.setParentGroup(null);
		element.setParentGroup(this);
		fireIndexedPropertyChange("element.replace", index, oldElement, element);
		return oldElement;
	}
	
	// Group - Intersectable 
	
	public SPGroup intersections (SPPoint other) {
		SPGroup items = new SPGroup();
		for (SketchpadObject item : this) {
			items.addAll(item.intersections(other));
		}
		return items;
	}
	
	public SPGroup intersections (SPLine other) {
		SPGroup items = new SPGroup();
		for (SketchpadObject item : this) {
			items.addAll(item.intersections(other));
		}
		return items;
	}
	
	public SPGroup intersections (SPPolyLine other) {
		SPGroup items = new SPGroup();
		for (SketchpadObject item : this) {
			items.addAll(item.intersections(other));
		}
		return items;
	}
	
	public SPGroup intersections (SPPolygon other) {
		SPGroup items = new SPGroup();
		for (SketchpadObject item : this) {
			items.addAll(item.intersections(other));
		}
		return items;
	}
	
	public SPGroup intersections (SPEllipse other) {
		SPGroup items = new SPGroup();
		for (SketchpadObject item : this) {
			items.addAll(item.intersections(other));
		}
		return items;
	}
	
	public SPGroup intersections (SPGroup other) {
		SPGroup items = new SPGroup();
		for (SketchpadObject item : this) {
			items.addAll(item.intersections(other));
		}
		return items;
	}
	
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
	
	public SPGroup selfIntersections () {
		SPGroup items = new SPGroup();
		for (SketchpadObject item1 : this) {
			for (SketchpadObject item2 : this) {
				if (item1 != item2) {
					items.addAll(item1.intersections(item2));
				}
			}
		}
		return items;
	}
	
	
	// Group - Decomposable
	// This is just ridiculous.
	public final SPGroup decompose() {
		/*
		SPGroup newGroup = new SPGroup();
		for (SketchpadObject item : this) {
			this.remove(item);
			newGroup.add(item);
		}
		firePropertyChange("decomposed", false, true);
		return newGroup;
		*/
		return this;
	}
	
	// Group - Drawable
	public final void draw (Graphics g) {
		for (SketchpadObject item : this) {
			if (item != null) item.draw(g);
		}
	}
	
	// Group - Groupable
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
	
	// Group - Locatable
	public final void setLocation (Number x, Number y) {
		double oldX = locX;
		double oldY = locY;
		this.locX = x.doubleValue();
		this.locY = y.doubleValue();
		if(getPropertyChangeListeners().length != 0) {
			firePropertyChange("location", new SPPoint(oldX,oldY), new SPPoint(locX,locY));
		}
		SVGImporter.isSaved = false;
	}
	
	public final void setLocation (SPPoint p) {
		double oldX = locX;
		double oldY = locY;
		this.locX = p.x();
		this.locY = p.y();
		if(getPropertyChangeListeners().length != 0) {
			firePropertyChange("location", new SPPoint(oldX,oldY), new SPPoint(locX,locY));
		}
		SVGImporter.isSaved = false;
	}
	
	public final SPPoint getLocation() {
		return new SPPoint(x(),y());
	}
	
	public final void translate (double dx, double dy) {
		setLocation(x() + dx, y() + dy);
	}
	
	public final double x() {
		return locX;
	}
	
	public final double y() {
		return locY;
	}
	
	// Group - Namable
	public final void setName (String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange("name", oldName, name);
		SVGImporter.isSaved = false;
	}
	
	public final String name() {
		return name;
	}
	
	// Group - Rotatable
	public final double rotation() {
		return rotation;
	}
	
	public final void setRotation (Number radians) {
		double oldRotation = rotation;
		rotation = radians.doubleValue();
		firePropertyChange("rotation", oldRotation, rotation);
		SVGImporter.isSaved = false;
	}
	
	public final void rotate (Number radians) {
		double oldRotation = rotation;
		rotation += radians.doubleValue();
		firePropertyChange("rotation", oldRotation, rotation);
		SVGImporter.isSaved = false;
	}
	
	// Group - Selectable
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
	
	// Group - SwingPropertyChangeSupporter
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
