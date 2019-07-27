package geometricSketchpad.tools;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RectangleTool extends JPanel implements MouseListener, MouseMotionListener, SPTool {
	private static final long serialVersionUID = 6514389076361087354L;
	SPPolygon rect;
	SPPoint rectBase1;
	SPPoint rectBase2;
	SPPoint rectRBase;
	SPPoint basedRotate;
	SPPoint nowRotate;
	boolean rotate;
	Grid grid;
	
	// Constructor
	public RectangleTool () {
		setPreferredSize(new Dimension (500,500));
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(500,500);
		rotate = false;
		setOpaque(false);
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		if (rect != null) {
			rect.draw(g);
		}  else if (rectBase1 != null && rectBase2 != null) {
			Point rectInvBase1 = new Point ((int) rectBase1.x(), (int) rectBase2.y());
			Point rectInvBase2 = new Point ((int) rectBase2.x(), (int) rectBase1.y());
			g.drawLine((int)rectBase1.x(), (int)rectBase1.y(), rectInvBase1.x, rectInvBase1.y);
			g.drawLine(rectInvBase1.x, rectInvBase1.y, (int)rectBase2.x(), (int)rectBase2.y());
			g.drawLine((int)rectBase2.x(), (int)rectBase2.y(), rectInvBase2.x, rectInvBase2.y);
			g.drawLine(rectInvBase2.x, rectInvBase2.y, (int)rectBase1.x(), (int)rectBase1.y());
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
			rectBase1 = new SPPoint(arg0.getPoint());
			rectBase2 = null;
			rect = null;
		}
		if (SwingUtilities.isRightMouseButton(arg0) && rectBase1 != null && rectBase2 != null) {
			SPPoint rectInvBase1 = new SPPoint (rectBase1.x(), rectBase2.y());
			SPPoint rectInvBase2 = new SPPoint (rectBase2.x(), rectBase1.y());
			rect = new SPPolygon (rectBase1, rectInvBase1, rectBase2, rectInvBase2, rectBase1);
			rectRBase = rect.rotateBase();
			rectBase1 = null;
			rectBase2 = null;
			rect.setLocation(rect.rotateBase());
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
		}
		
		if (rect == null) {
			SPPoint rectInvBase1 = new SPPoint (rectBase1.x(), rectBase2.y());
			SPPoint rectInvBase2 = new SPPoint (rectBase2.x(), rectBase1.y());
			rect = new SPPolygon (rectBase1, rectInvBase1, rectBase2, rectInvBase2, rectBase1);
			rectRBase = rect.rotateBase();
			rectBase1 = null;
			rectBase2 = null;
			rect.setLocation(rect.rotateBase());
		} 

		firePropertyChange("shape", false, true);
		SVGImporter.isSaved = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		if (!rotate) {
			if (rectBase1 == null) {
				repaint();
				return;
			}
			rectBase2 = new SPPoint(arg0.getPoint());
		} else if (basedRotate != null && rect != null) {
			nowRotate.setLocation(arg0.getX(), arg0.getY());
			rect.setRotation(rectRBase.angleWRT(nowRotate) - rectRBase.angleWRT(basedRotate));
//			System.out.println(rectRBase + " " + basedRotate + " " + nowRotate + " " + rect.rotation());
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}

	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame ("RECTANGLE DRAWER");
		RectangleTool tool = new RectangleTool();
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public SPShape getShape() {
		SPPolygon tmpRect = rect;
		rect = null;
		return tmpRect;
	}

	@Override
	public void setGrid(Grid g) {
		grid = g;
		
	}
}
