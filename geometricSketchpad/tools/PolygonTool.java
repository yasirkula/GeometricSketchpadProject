package geometricSketchpad.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.*;

public class PolygonTool extends JPanel implements SPTool, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 7900131350095034680L;
	SPPolygon		 poly;
	SPGroup  		 points;
	SPPoint 		 currentPoint;
	SPPoint  		 currentEnd;
	Grid grid;
	boolean		 	 finalised;
	
    public PolygonTool()
	{	
		points = new SPGroup(15);
		currentPoint = null;
		currentEnd = null;
		finalised = false;
		
		setPreferredSize( new Dimension( 500, 500 ) );
		addMouseListener( this);
		addMouseMotionListener( this);
		setOpaque(false);
	}
	
	public void mousePressed( MouseEvent e )
	{			
		if (grid != null) {e = grid.snap(e);}
		if (SwingUtilities.isLeftMouseButton(e)) {
			currentPoint = new SPPoint(e.getPoint());
			points.add( currentPoint );
			currentEnd = new SPPoint(e.getPoint());
		}
		
		if (SwingUtilities.isRightMouseButton(e))
		{
			points.add( points.get(0));
			currentPoint = null;
			finalised = true;
		}
		
		repaint();
	}
	
	public void mouseDragged( MouseEvent e) 
	{
		if (grid != null) {e = grid.snap(e);}
		if( points.size() != 0)
			{
				currentEnd.setLocation( e.getX(), e.getY());
				repaint();
			}
	}
	public void mouseReleased( MouseEvent e) {
		if (finalised)
		{
			poly = new SPPolygon(points);
			firePropertyChange("shape", false, true);
			SVGImporter.isSaved = false;
			points = new SPGroup();
			finalised = false;
			repaint();
		}
	}
	public void mouseClicked( MouseEvent e) {if (grid != null) {e = grid.snap(e);}}
	public void mouseEntered( MouseEvent e) {if (grid != null) {e = grid.snap(e);}}
	public void mouseExited( MouseEvent e) {if (grid != null) {e = grid.snap(e);}}
	public void	mouseMoved( MouseEvent e) 
	{
		if (grid != null) {e = grid.snap(e);}
		if( points.size() != 0)
		{
			currentEnd.setLocation( e.getX(), e.getY());
			repaint();
		}
	}
		
	public void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		
		if( points.size() != 0)
		{
			for( int i = 0; i < points.size() - 1; i++)
			{
				g.drawLine( (int)points.get(i).x(), (int)points.get(i).y(),
							(int)points.get(i + 1).x(), (int)points.get(i + 1).y() );
			}
			
			if( currentPoint != null)
			{
				g.drawLine( (int)points.get(points.size() - 1).x(), 
					    	(int)points.get(points.size() - 1).y(),
							(int)currentEnd.x(),
							(int)currentEnd.y() );
			}
		}
	}
	
    public static void main( String[] args ) 
	{
    	JFrame f = new JFrame( "Polygon Tool" );
    	f.setLocation( 100, 100 );
    	f.add( new PolygonTool() );
    	f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    	f.pack();
    	f.setVisible( true );
	}
    
    @Override
    public SPShape getShape() {
    	SPPolygon tmpPolygon = poly;
    	poly = null;
    	currentPoint = null;
    	currentEnd = null;
    	finalised = false;
    	return tmpPolygon;
    }

	@Override
	public void setGrid(Grid g) {
		grid = g;
		
	}
}
    