package geometricSketchpad.tools;


import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.SPGroup;
import geometricSketchpad.shapes.SPPoint;
import geometricSketchpad.shapes.SPPolygon;
import geometricSketchpad.shapes.SPShape;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class NStarTool extends JPanel implements MouseListener, MouseMotionListener, SPTool 
{
	Grid grid;
	SPPoint center;
	SPPoint loc;
	int points;
	SPGroup point;
	
	public NStarTool(int points)
	{
		setPreferredSize(new Dimension (500,500));
		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(500,500);
		setOpaque(false);
		this.points = points;
		point = new SPGroup(2*points+1);
	}

	@Override
	public SPShape getShape() {
		// TODO Auto-generated method stub
		SPPolygon star = new SPPolygon(point);
		center = null;
		loc = null;
		point = new SPGroup();
		repaint();
		return star;
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		if (grid != null) {arg0 = grid.snap(arg0);}
		loc = new SPPoint(arg0.getPoint());
		repaint();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (grid != null) {e = grid.snap(e);}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (grid != null) {e = grid.snap(e);}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (grid != null) {e = grid.snap(e);}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (grid != null) {e = grid.snap(e);}
		center = new SPPoint(e.getPoint());
		repaint();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (grid != null) {e = grid.snap(e);}
		double base = center.angleWRT(loc);
		double step = Math.PI/points;
		boolean wide = false;
		double r = center.distanceTo(loc);
		double x2 = center.x() + (Math.cos(base + step) * r);
		double y2 = center.y() + (Math.sin(base + step) * r);
		point.add(new SPPoint (x2, y2));
		for (int i = 0; i <= 2*points; i++) {
			if (wide) {
				x2 = center.x() + (Math.cos(base + i*step) * r);
				y2 = center.y() + (Math.sin(base + i*step) * r);
			} else {
				x2 = center.x() + (Math.cos(base + i*step) * r/3);
				y2 = center.y() + (Math.sin(base + i*step) * r/3);
			}
			wide = !wide;
			point.add(new SPPoint (x2, y2));
		}
		
		point.add( point.get(0));
		firePropertyChange("shape", false, true);
		SVGImporter.isSaved = false;
		repaint();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGrid(Grid g) {
		grid = g;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (loc == null) {loc = center;}
		double base = center.angleWRT(loc);
		double step = Math.PI/points;
		boolean wide = false;
		double r = center.distanceTo(loc);
		double x1;
		double x2 = center.x() + (Math.cos(base + step) * r);
		double y1;
		double y2 = center.y() + (Math.sin(base + step) * r);
		for (int i = 0; i <= 2*points; i++) {
			x1 = x2;
			y1 = y2;
			if (wide) {
				x2 = center.x() + (Math.cos(base + i*step) * r);
				y2 = center.y() + (Math.sin(base + i*step) * r);
			} else {
				x2 = center.x() + (Math.cos(base + i*step) * r/3);
				y2 = center.y() + (Math.sin(base + i*step) * r/3);
			}
			wide = !wide;
			g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		}
	}
	
	public static void main( String[] args ) 
	{
    	JFrame f = new JFrame( "NStar Tool" );
    	f.setLocation( 100, 100 );
    	f.add( new NStarTool(18) );
    	f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    	f.pack();
    	f.setVisible( true );
	}

}
