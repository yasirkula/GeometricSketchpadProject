package geometricSketchpad.tools;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CircleTool extends JPanel implements MouseListener, MouseMotionListener, SPTool {
	private static final long serialVersionUID = 6514389076361087354L;
	Grid grid;
	SPEllipse circle;
	SPPoint center;
	
	// Constructor
	public CircleTool () {
		setPreferredSize(new Dimension (500,500));
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(500,500);
		// circle = new SPEllipse();
		setOpaque(false);
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		if (circle != null) {
			circle.draw(g);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		// System.out.println(arg0);
		center = new SPPoint(arg0.getPoint());
		circle = new SPEllipse (10, 10);
		circle.setRotation(0);
		circle.setLocation(center.x() - circle.getMinor()/2, center.y() - circle.getMajor()/2);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		if (circle != null) {
			circle.translate( 0, 0 ); // to calculate the bounds of the shape implicitly
			firePropertyChange("shape", false, true);
			SVGImporter.isSaved = false;
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		if (center == null) {
			repaint();
			return;
		}
		double radius = 2*center.distanceTo(new SPPoint(arg0.getPoint()));
		circle.setMajor(radius);
		circle.setMinor(radius);
		circle.setLocation(center.x() - circle.getMajor()/2, center.y() - circle.getMinor()/2);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		/*
		System.out.println(e);
		if (starter == null) {
			repaint();
			return;
		}
		ellipse.width = e.getX() - starter.getX();
		ellipse.height = e.getY() - starter.getY();
		repaint();
		*/

	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame ("CIRCLE DRAWER");
		CircleTool tool = new CircleTool();
		Grid sg = new Grid();
		sg.toggleSnapping(true);
		tool.setGrid(sg);
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public SPShape getShape() {
		SPEllipse tmpEllipse = circle;
		circle = null;
		return tmpEllipse;
	}
	
	public void setGrid (Grid neue) {
		grid = neue;
	}
}
