package geometricSketchpad.tools;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * TriangleTool 
 *
 * @author Görkem Bozbýyýk
 * @version 1.00 2013/4/30
 */

public class TriangleTool extends JPanel implements MouseListener, MouseMotionListener, SPTool
{
	private static final long serialVersionUID = -8760502765749214830L;
	// PROPERTIES
	SPPolygon 	triangle;
	SPLine 		line;
	SPPoint 	p1;
	SPPoint		p2;
	SPPoint 	p3;
	Grid grid;
	
	// CONSTRUCTOR
	public TriangleTool()
	{
		setPreferredSize(new Dimension (500,500));
		
		addMouseListener( this );
		addMouseMotionListener( this );
		setOpaque(false);
	}
	
	// METHODS
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g);
		
		// Draw first line Step
		if( p1 != null && p2 != null )
		{
			g.drawLine( (int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y() );
		}
		// Draw triangle Step
		if( p1 != null && p2 != null && p3 != null)
		{
			g.drawLine( (int) p1.x(), (int) p1.y(), (int) p3.x(), (int) p3.y() );
			g.drawLine( (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y() );
		}
	}
	
	// To implement MouseListener & MouseMotionListener
	public void mousePressed( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		if( p1 == null )
		{
			p1 = new SPPoint( e.getX(), e.getY() );
		}
		else
		{
			p3 = new SPPoint( e.getX(), e.getY() );
		}
	}
	
	public void mouseReleased( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		if( line == null )
		{
			p2 = new SPPoint( e.getX(), e.getY() );
			if (p1.x() == p2.x() && p1.y() == p2.y()) {
				p1 = null;
				p2 = null;
			} else {
				line = new SPLine( p1, p2 );
			}
		}
		else
		{
			p3 = new SPPoint( e.getX(), e.getY() );
			triangle = new SPPolygon( p1, p2, p3, p1 );
			firePropertyChange("shape", false, true);
			SVGImporter.isSaved = false;
			// removeMouseListener( this );
			// removeMouseMotionListener( this );
		}
		repaint();
	}
	
	public void mouseDragged( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		if( line == null )
		{
			p2 = new SPPoint( e.getX(), e.getY() );
		}
		else
		{
			p3 = new SPPoint( e.getX(), e.getY() );
		}
		repaint();
	}
	
	public void mouseMoved( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		if( line != null && triangle == null)
		{
			p3 = new SPPoint( e.getX(), e.getY() );
		}
		repaint();
	}
	
	public void mouseClicked( MouseEvent e ) {if (grid != null) {e = grid.snap(e);}}
	public void mouseEntered( MouseEvent e ) {if (grid != null) {e = grid.snap(e);}}
	public void mouseExited( MouseEvent e ) {if (grid != null) {e = grid.snap(e);}}

	@Override
	public SPShape getShape() {
		SPPolygon tmpTriangle = triangle;
		triangle = null;
		p1 = null;
		p2 = null;
		p3 = null;
		line = null;
		return tmpTriangle;
	}
	
	public static void main( String[] args ) 
	{
		JFrame f = new JFrame( "TestTriangleTool");
		
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE);
		f.setBounds( 100, 100, 400, 300);
		
		f.add( new TriangleTool());

		f.setVisible( true);
	}

	@Override
	public void setGrid(Grid g) {
		grid = g;
		
	}
}