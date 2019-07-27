package geometricSketchpad.tools;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.*;

import java.awt.*;
import java.awt.event.*;
//import java.awt.geom.*;
import javax.swing.*;

/**
 * EquilateralTriangle 
 *
 * @author Görkem Bozbýyýk
 * @version 1.00 07/05/2013
 */

public class EquilateralTriangleTool extends JPanel implements MouseListener, MouseMotionListener, SPTool
{
	private static final long serialVersionUID = -3794938783732983401L;
	
	// PROPERTIES
	SPPolygon 	triangle;
	
	//SPLine 		line;
	SPPoint 	p1;
	SPPoint		p2;
	SPPoint 	p3;
	SPPoint 	pHeight;
	Grid grid;
	
	// CONSTRUCTOR
	public EquilateralTriangleTool()
	{
		setPreferredSize( new Dimension( 500, 500) );
		setSize( 500, 500 );
		addMouseListener( this );
		addMouseMotionListener( this );
		
		setOpaque( false );
	}
	
	// METHODS
	
	@Override
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g);

		if( p1 != null && p2 != null && p3 != null)
		{
			g.drawLine( (int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y() );
			g.drawLine( (int) p1.x(), (int) p1.y(), (int) p3.x(), (int) p3.y() );
			g.drawLine( (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y() );
		}
	}
	
	// To implement MouseListener & MouseMotionListener
	public void mousePressed( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		p1 = new SPPoint( e.getX(), e.getY() );
	}
	
	public void mouseReleased( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		pHeight = new SPPoint( e.getX(), e.getY() );
		
		p2 = new SPPoint( p1.x() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.cos( pHeight.angleWRT( p1) + (Math.PI / 6) ), 
				  p1.y() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.sin( pHeight.angleWRT( p1) + (Math.PI / 6) )  );

		p3 = new SPPoint( p1.x() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.cos( pHeight.angleWRT( p1) - (Math.PI / 6) ), 
						  p1.y() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.sin( pHeight.angleWRT( p1) - (Math.PI / 6) )  );
		
		triangle = new SPPolygon( p1, p2, p3, p1 );
		firePropertyChange("shape", false, true);
		SVGImporter.isSaved = false;
		repaint();
	}
	
	public void mouseDragged( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		pHeight = new SPPoint( e.getX(), e.getY() );
		
		p2 = new SPPoint( p1.x() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.cos( pHeight.angleWRT( p1) + (Math.PI / 6) ), 
				  p1.y() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.sin( pHeight.angleWRT( p1) + (Math.PI / 6) )  );

		p3 = new SPPoint( p1.x() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.cos( pHeight.angleWRT( p1) - (Math.PI / 6) ), 
						  p1.y() + 2/Math.sqrt( 3) * p1.distanceTo(pHeight) *Math.sin( pHeight.angleWRT( p1) - (Math.PI / 6) )  );
		
		repaint();
	}
	
	public void mouseMoved( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	public void mouseClicked( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	public void mouseEntered( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	public void mouseExited( MouseEvent e ) { if (grid != null) {e = grid.snap(e);} }
	
	@Override
	public SPShape getShape() {
		SPPolygon tmpTriangle = triangle;
		triangle = null;
		p1 = null;
		p2 = null;
		p3 = null;
		return tmpTriangle;
	}
	
	public void setGrid (Grid neue) {
		grid = neue;
	}
	
	//*******************************************************
	public static void main( String[] args ) 
	{
		JFrame f = new JFrame( "TestTriangleTool");
		
		f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE);
		f.setBounds( 100, 100, 400, 400);
		
		f.add( new EquilateralTriangleTool());
		f.pack();
		f.setVisible( true);
	}
	//*******************************************************
}
