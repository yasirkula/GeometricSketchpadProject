package geometricSketchpad.tools;

import java.beans.PropertyChangeListener;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.SPShape;

public interface SPTool {
	public SPShape getShape();
	public void setGrid(Grid g);
}