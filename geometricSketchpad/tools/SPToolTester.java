package geometricSketchpad.tools;

import geometricSketchpad.shapes.SPGroup;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SPToolTester extends JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = -6799558983709246142L;
	SPGroup shapes;
	ArrayList<JFrame> frames;
	ArrayList<SPTool> tools;
	SPTool ellipseD;
	SPTool ecircleD;
	SPTool lineSegD;
	SPTool infLineD;
	SPTool polygonD;
	SPTool polylinD;
	SPTool trianglD;
	SPTool rectangD;
	SPTool rsquareD;
	
	public SPToolTester () {
		setPreferredSize(new Dimension (500,500));
		shapes = new SPGroup(15);
		frames = new ArrayList<JFrame>(5);
		ellipseD = new EllipseTool();
		ecircleD = new CircleTool();
		lineSegD = new LineSegmentTool();
		infLineD = new LineTool();
		polygonD = new PolygonTool();
		polylinD = new PolyLineTool();
		trianglD = new TriangleTool();
		rectangD = new RectangleTool();
		rsquareD = new SquareTool();

		tools = new ArrayList<SPTool>();

		tools.add(ellipseD);
		tools.add(ecircleD);
		tools.add(lineSegD);
		tools.add(infLineD);
		tools.add(polygonD);
		tools.add(polylinD);
		tools.add(trianglD);
		tools.add(rectangD);
		tools.add(rsquareD);

		JFrame frame;
		for (SPTool tool : tools) {
			((JPanel) tool).addPropertyChangeListener("shape", this);
			frame = new JFrame (tool.getClass().getName());
			frame.add((JPanel) tool);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			frames.add(frame);
		}
		frame = null;
	}
	
	public static void main(String[] args) {	
		SPToolTester tester = new SPToolTester();
		JFrame frame = new JFrame("MAIN TESTER");
		frame.add(tester);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		shapes.add(((SPTool)evt.getSource()).getShape());
		repaint();
	}

	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		shapes.draw(g);
	}
}
