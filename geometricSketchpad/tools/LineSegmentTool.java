package geometricSketchpad.tools;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.SPLine;
import geometricSketchpad.shapes.SPPoint;
import geometricSketchpad.shapes.SPShape;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.awt.Dimension;

/**
 * LineTool - Tool for drawing a line on sketchboard
 * 
 * @author Süleyman Yasir KULA
 * @version 1.00 04/30/13
 */

public class LineSegmentTool extends JPanel implements MouseListener, MouseMotionListener, SPTool
{
	private static final long serialVersionUID = 4287059133626557848L;
	// properties
	SPLine s;
	SPPoint p1;
	SPPoint p2;
	Grid grid;
	
	// constructors
	public LineSegmentTool()
	{
		this (500, 500);
	}
	
	public LineSegmentTool( int width, int height )
	{
		setPreferredSize( new Dimension( width, height ) );
		addMouseListener( this );
		addMouseMotionListener( this );
		s = null;
		setOpaque(false);
	}
	
	// methods
	public void mousePressed( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		p1 = new SPPoint( e.getX(), e.getY() );
		p2 = new SPPoint( e.getX(), e.getY() );
		
		// Create a restricted line
		// s = new SPLine( p1, p2 );
		
		repaint();
	}
	
	public void	mouseDragged( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		if( p1 != null )
		{
			p2 = new SPPoint( e.getX(), e.getY() );
		}
		
		// Create a restricted line
		// s = new SPLine( p1, p2 );
		
		repaint();
	}
	
	public void mouseReleased( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		p2 = new SPPoint( e.getX(), e.getY() );
		
		// Create a restricted line
		s = new SPLine( p1, p2 );
		firePropertyChange("shape", false, true);
		SVGImporter.isSaved = false;
		
		repaint();
	}
	
	
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		if( p1 != null && p2 != null )
		{
			// s.draw( g );

			g.drawLine( (int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y() );
		}
	}
	
	// methods required to be declared for MouseInputListener
	public void mouseClicked( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	public void mouseEntered( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	public void mouseExited( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	public void	mouseMoved( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	
	@Override
	public SPShape getShape() {
		SPLine tmpLine = s;
		s = null;
		p1 = null;
		p2 = null;
		return tmpLine;
	}
	
	// TESTING THE CLASS
	public static void main(String[] args) {
		JFrame frame = new JFrame ("LINE SEGMENT DRAWER");
		LineSegmentTool tool = new LineSegmentTool( 300, 500 );
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

	}
	
	public void setGrid (Grid neue) {
		grid = neue;
	}
}
