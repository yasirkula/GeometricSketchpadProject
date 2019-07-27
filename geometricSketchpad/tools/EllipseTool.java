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
import javax.swing.SwingUtilities;

public class EllipseTool extends JPanel implements MouseListener, MouseMotionListener, SPTool {
	private static final long serialVersionUID = 6514389076361087354L;
	Grid grid;
	SPEllipse ellipse;
	SPPoint center;
	SPPoint basedRotate;
	SPPoint nowRotate;
	boolean rotate;
	
	// Constructor
	public EllipseTool () {
		setPreferredSize(new Dimension (500,500));
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(500,500);
		// ellipse = new SPEllipse();
		rotate = false;
		setOpaque(false);
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		if (ellipse != null) {
			ellipse.draw(g);
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
		if (SwingUtilities.isLeftMouseButton(arg0)) {
			// System.out.println(arg0);
			center = new SPPoint(arg0.getPoint());
			ellipse = new SPEllipse (9, 10);
			ellipse.setRotation(0);
			ellipse.setLocation(center.x() - ellipse.getMinor()/2, center.y() - ellipse.getMajor()/2);
		}
		if (SwingUtilities.isRightMouseButton(arg0) && ellipse != null) {
			rotate = true;
			basedRotate = new SPPoint(arg0.getPoint());
			nowRotate = new SPPoint(arg0.getPoint());
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		if (rotate) {
			rotate = false;
			basedRotate = null;
			center = null;
		}
		if (ellipse != null) {
			ellipse.translate( 0, 0 ); // to calculate the bounds of the shape implicitly
			firePropertyChange("shape", false, true);
			SVGImporter.isSaved = false;
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		if (!rotate && ellipse != null) {
			if (center == null) {
				repaint();
				return;
			}
			ellipse.setMajor(2 * Math.abs(arg0.getX() - center.x()));
			ellipse.setMinor(2 * Math.abs(arg0.getY() - center.y()));
			ellipse.setLocation(center.x() - ellipse.getMajor()/2, center.y() - ellipse.getMinor()/2);
		} else if (basedRotate != null && ellipse != null) {
//			ellipse.rotate(center.angleWRT(basedRotate) - center.angleWRT(new SPPoint(arg0.getPoint())));
//			basedRotate.setLocation(arg0.getX(), arg0.getY());
			nowRotate.setLocation(arg0.getX(), arg0.getY());
			ellipse.setRotation(center.angleWRT(nowRotate) - center.angleWRT(basedRotate));
		}
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
		JFrame frame = new JFrame ("ELLIPSE DRAWER");
		EllipseTool tool = new EllipseTool();
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public SPShape getShape() {
		SPEllipse tmpEllipse = ellipse;
		ellipse = null;
		return tmpEllipse;
	}
	
	public void setGrid (Grid neue) {
		grid = neue;
	}
	
}
