package geometricSketchpad.tools;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Stroke;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.SPShape;
import geometricSketchpad.shapes.SPGroup;
import geometricSketchpad.shapes.SketchpadObject;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// SelectTool - Selects tools in a rectangular area
// Süleyman Yasir KULA

public class SelectTool extends JPanel implements SPTool, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 6349222280402988488L;

	Point2D start, end;
	
	Rectangle2D bounds;
	
	SPGroup shapes;
	
	public SelectTool( SPGroup shapes )
	{
		setPreferredSize(new Dimension (500,500));
		setSize(500,500);
		setOpaque(false);
		addMouseListener( this );
		addMouseMotionListener( this );
		
		this.shapes = shapes;
	}
	
	public void selectIntersections( SPGroup g )
	{
		bounds = new Rectangle2D.Double( Math.min( start.getX(), end.getX() ), Math.min( start.getY(), end.getY() ), Math.abs( start.getX() - end.getX() ), Math.abs( start.getY() - end.getY() ) );
		
		g.deselectAll();
		
		for( SketchpadObject s : g )
		{
			if( s instanceof SPGroup )
			{
				selectIntersections( (SPGroup) s );
			}
			else
			{
				if( ( (SPShape) s ).getBoundaries() != null )
				{
					if( ( (SPShape) s ).getBoundaries().intersects( bounds ) )
					{
						s.toggleSelect( true );
					}
				}
				else
				{
					System.out.println( "asdasfdas");
				}
			}
		}
		
		repaint();
	}
	
	public void paintComponent( Graphics g )
	{
		super.paintComponents( g );
		
		if( bounds != null )
		{
			Graphics2D g2D = (Graphics2D) g;
			
			Stroke currentS = g2D.getStroke();
			Color currentC = g2D.getColor();
	
		    g2D.setColor( Color.red );
		    g2D.setStroke( new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 7.0f }, 0.0f ) );
	        
		    g2D.draw ( bounds );
		    
		    g2D.setColor( new Color( 255, 0, 0, 75 ) );
		    
		    g2D.fill( bounds );
		    g2D.setStroke( currentS );
		    g2D.setColor( currentC );
		}
	}
	
	@Override
	public void mousePressed( MouseEvent e )
	{
		start = new Point2D.Double( e.getX(), e.getY() );
		end = new Point2D.Double( e.getX(), e.getY() );
	}
	
	@Override
	public void mouseDragged( MouseEvent e )
	{
		if( start != null && end != null )
		{
			end = new Point2D.Double( e.getX(), e.getY() );
		}
		
		selectIntersections( shapes );
	}
	
	@Override
	public void mouseReleased( MouseEvent e )
	{
		/*if( start != null && end != null )
		{
			end = new Point2D.Double( e.getX(), e.getY() );
		}*/
		
		bounds = null;
		start = null;
		end = null;
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public SPShape getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGrid(Grid g) {
		// TODO or not TODO
	}
	
	public static void main( String[] args )
	{
		JFrame frame = new JFrame ("RECTANGLE DRAWER");
		SelectTool tool = new SelectTool( new SPGroup() );
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
