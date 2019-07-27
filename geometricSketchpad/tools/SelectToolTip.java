package geometricSketchpad.tools;

import geometricSketchpad.shapes.*;
import geometricSketchpad.gui.*;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * SelectToolTip - 	While SelectToolTip is active, a tooltip will show the intersection points' locations.
 *
 * @author Görkem Bozbýyýk
 * @version 1.00 11/05/2013
 */

public class SelectToolTip extends JPanel implements MouseMotionListener {
	
	final double MaxRadiusForToolTip = 13;
	
	// PROPERTIES
	MainCanvas 	mainCanvas;
	boolean 	isActive;
	SPGroup 	intersectionPoints;
	
	// CONSTRUCTOR
	public SelectToolTip( MainCanvas mainCanvas)
	{
		this.mainCanvas = mainCanvas;
		isActive = false;
		intersectionPoints = mainCanvas.getShapes().selfIntersections();
		
		setPreferredSize(new Dimension (500,500));
		setOpaque(false);
		addMouseMotionListener( this );
	}
	
	// METHODS
	public void mouseMoved( MouseEvent e )
	{
		SPPoint mousePoint = new SPPoint( e.getX(), e.getY() );
		
		// For initializing variables.
		SPPoint closestPoint = (SPPoint) intersectionPoints.get( 0 );
		double minDistance = mousePoint.distanceTo( closestPoint );
		for ( SketchpadObject point : intersectionPoints )
		{
			double distance = mousePoint.distanceTo( (SPPoint) point );
			if( distance < minDistance )
			{
				minDistance = distance;
				closestPoint = (SPPoint) point;
			}
		}
		
		if( mousePoint.distanceTo( closestPoint ) < MaxRadiusForToolTip )
		{
			String pointLocation = String.format( "( %.2f, %.2f )", closestPoint.x(), closestPoint.y() );
			this.setToolTipText( closestPoint.name() + " at " + pointLocation );
		}
		else
		{
			this.setToolTipText( null);
		}
	}
	
	public void setActive( boolean b )
	{
		isActive = b;
	}
	
	// To implement MouseMotionListener
	public void mouseDragged( MouseEvent e ) {}
	
}
