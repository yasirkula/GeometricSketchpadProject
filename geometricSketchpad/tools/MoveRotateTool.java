package geometricSketchpad.tools;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;

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
import javax.swing.SwingUtilities;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// MoveRotateTool - Move - Rotate selected shapes
// Süleyman Yasir KULA

public class MoveRotateTool extends JPanel implements SPTool, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 6349222280402988488L;

	Point2D start, end, prevEnd;
	
	SPGroup shapes;
	Grid grid;
	
	public MoveRotateTool( SPGroup shapes )
	{
		setPreferredSize(new Dimension (500,500));
		setSize(500,500);
		setOpaque(false);
		addMouseListener( this );
		addMouseMotionListener( this );
		
		this.shapes = shapes;
	}
	
	public void paintComponent( Graphics g )
	{
		super.paintComponents( g );

			Graphics2D g2D = (Graphics2D) g;
			
			Stroke currentS = g2D.getStroke();
			Color currentC = g2D.getColor();
			
			if( start != null && end != null && prevEnd != null )
			{
				if( end.getX() - start.getX() - end.getY() + start.getY() > 0 )
					g2D.setColor( Color.red );
				else
					g2D.setColor( Color.MAGENTA );
				
			    g2D.setStroke( new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 7.0f }, 0.0f ) );
		        
			    g2D.draw ( new Line2D.Double( start, end ) );
			    
			    g2D.setStroke( currentS );
			    g2D.setColor( currentC );
			}
	}
	
	@Override
	public void mousePressed( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		start = new Point2D.Double( e.getX(), e.getY() );
		end = new Point2D.Double( e.getX(), e.getY() );
		prevEnd = end;
	}
	
	@Override
	public void mouseDragged( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		if( start != null && end != null )
		{
			end = new Point2D.Double( e.getX(), e.getY() );
			
			double deltaX = end.getX() - prevEnd.getX();
			double deltaY = end.getY() - prevEnd.getY();
			
			if( SwingUtilities.isLeftMouseButton( e ) )
			{
				shapes.moveSelected( deltaX, deltaY );
			}
			else
			{
				shapes.rotateSelected( ( deltaX - deltaY ) / 100 );
			}
			
			prevEnd = end;
			
			repaint();
			
			/*public void moveSelected( double deltaX, double deltaY )
			{
				for( SketchpadObject s : this )
				{
					if( s instanceof SPGroup )
					{
						( (SPGroup) s ).moveSelected();
					}
					else
					{
						( ( SPShape ) s ).translate( deltaX, deltaY );
					}
				}
			}
			
			public void rotateSelected( double radians )
			{
				for( SketchpadObject s : this )
				{
					if( s instanceof SPGroup )
					{
						( (SPGroup) s ).rotateSelected();
					}
					else
					{
						( ( SPShape ) s ).rotate( radians );
					}
				}
			}
			*/
		}
	}
	
	@Override
	public void mouseReleased( MouseEvent e )
	{
		if (grid != null) {e = grid.snap(e);}
		/*if( start != null && end != null )
		{
			end = new Point2D.Double( e.getX(), e.getY() );
		}*/
		start = null;
		end = null;
		
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {if (grid != null) {arg0 = grid.snap(arg0);}}

	@Override
	public void mouseClicked(MouseEvent arg0) {if (grid != null) {arg0 = grid.snap(arg0);}}

	@Override
	public void mouseEntered(MouseEvent arg0) {if (grid != null) {arg0 = grid.snap(arg0);}}

	@Override
	public void mouseExited(MouseEvent arg0) { if (grid != null) {arg0 = grid.snap(arg0);}}
	
	@Override
	public SPShape getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGrid (Grid neue) {
		grid = neue;
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
