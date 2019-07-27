package geometricSketchpad.tools;

import geometricSketchpad.gui.Grid;
import geometricSketchpad.shapes.*;

import java.awt.geom.Line2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RulerTool extends JPanel implements MouseListener, MouseMotionListener, SPTool {
	private static final long serialVersionUID = 6514389076361087354L;
	SPPolygon ruler;
	SPGroup points;
	SPPoint p1, p2, start, end;
	SPPoint rulerRBase;
	SPPoint basedRotate;
	SPPoint nowRotate;
	boolean rotate;
	Grid grid;
	
	// Constructor
	public RulerTool () {
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
		
		if( ruler != null )
		{
			ruler.draw( g );
		}
		else
		{
			Graphics2D g2D = (Graphics2D) g;
			calculatePoints();
			
			if( points != null )
			{
				for( int i = 0; i < points.size() - 1; i++ )
				{
					g2D.draw( new Line2D.Double( points.get( i ).x(), points.get( i ).y(), points.get( i + 1 ).x(), points.get( i + 1 ).y() ) );
				}
			}
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
			start = new SPPoint(arg0.getPoint());
			end = null;
			ruler = null;
		}
		if (SwingUtilities.isRightMouseButton(arg0) && start != null && end != null) {
			calculatePoints();
			ruler = new SPPolygon ( points );
			rulerRBase = ruler.rotateBase();
			start = null;
			end = null;
			points = null;
			ruler.setLocation(rulerRBase);
			rotate = true;
			basedRotate = new SPPoint(arg0.getPoint());
			nowRotate = new SPPoint(arg0.getPoint());
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (rotate) {
			if (grid != null) {arg0 = grid.snap(arg0);}
			rotate = false;
			//basedRotate = null;
		}
		
		if (ruler == null) {
			calculatePoints();
			
			ruler = new SPPolygon ( points );
			rulerRBase = ruler.rotateBase();
			start = null;
			end = null;
			ruler.setLocation(ruler.rotateBase());
		} 

		firePropertyChange("shape", false, true);
		SVGImporter.isSaved = false;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
		if (!rotate) {
			if (start == null) {
				repaint();
				return;
			}
			
			end = new SPPoint(arg0.getPoint());
		} else if (basedRotate != null && ruler != null) {
			nowRotate.setLocation(arg0.getX(), arg0.getY());
			ruler.setRotation(rulerRBase.angleWRT(nowRotate) - rulerRBase.angleWRT(basedRotate));
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if (grid != null) {arg0 = grid.snap(arg0);}
	}
	
	private void calculatePoints()
	{
		if( start != null && end != null )
		{
			if( start.x() < end.x() )
			{
				p1 = start;
				p2 = end;
			}
			else
			{
				p1 = new SPPoint( end.x(), start.y() );
				p2 = new SPPoint( start.x(), end.y() );
			}
	
			boolean dirDown;
			
			
			if( p1.y() > p2.y() )
				dirDown = false;
			else
				dirDown = true;
			
			System.out.println( p1.y() + " " + p2.y() + " " + dirDown );
			
			// double centimeters = p2.x() - p1.x() * 2.54 / 96;
	
			// VERY IMPORTANT!!! oneCentimetersLength should be equal to the amount
			// of pixels that correspond to 1 centimeter in real life
			// If the space between two long vertical lines is not equal to 1 centimeter
			// then consider fixing that formula
			double oneCentimetersLength = Toolkit.getDefaultToolkit().getScreenResolution() / 2.54;
			
			double height = ( p2.y() - p1.y() ) *2 / 3;
	
			int centCount = 1;
			double currX = p1.x() + oneCentimetersLength / 10;
			double targetX = p2.x();
			
			points = new SPGroup();
	
			points.add( p1 );
	
			while( currX < targetX )
			{
				points.add( new SPPoint( currX, p1.y() ) );
				
				if( centCount % 10 == 0 )
				{
					points.add( new SPPoint( currX, p1.y() + height ) );
					points.add( new SPPoint( currX, p1.y() ) );
				}
				else if( centCount % 5 == 0 )
				{
					points.add( new SPPoint( currX, p1.y() + height * 3 / 4 ) );
					points.add( new SPPoint( currX, p1.y() ) );
				}
				else
				{
					points.add( new SPPoint( currX, p1.y() + height / 2 ) );
					points.add( new SPPoint( currX, p1.y() ) );
				}
				
				centCount++;
				currX += oneCentimetersLength / 10;
			}
	
			points.add( new SPPoint( targetX, p1.y() ) );
			points.add( new SPPoint( targetX, p2.y() ) );
			points.add( new SPPoint( p1.x(), p2.y() ) );
			points.add( p1 );
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame ("RECTANGLE DRAWER");
		RulerTool tool = new RulerTool();
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public SPShape getShape() {
		SPPolygon tmpRuler = ruler;
		ruler = null;
		points = null;
		return tmpRuler;
	}

	@Override
	public void setGrid(Grid g) {
		grid = g;
		
	}
}
