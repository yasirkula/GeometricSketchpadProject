package geometricSketchpad.tools;

import geometricSketchpad.shapes.SPGroup;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;

public class SVGTester extends JPanel implements PropertyChangeListener, WindowListener
{
	private static final long serialVersionUID = 1963794662505224385L;
	
	SPGroup shapes;
	ArrayList<JFrame> frames;
	ArrayList<SPTool> tools;
	SPTool ellipseD;
	SPTool ecircleD;
	SPTool lineSegD;
	SPTool infLineD;
	SPTool polygonD;
	SPTool polylinD;
	SPTool trianglD;
	SPTool rectangD;
	SPTool rsquareD;
	SPTool rulerD;
	
	public SVGTester () {
		setPreferredSize(new Dimension (500,500));
		shapes = new SPGroup(15);
		frames = new ArrayList<JFrame>(5);
		ellipseD = new EllipseTool();
		ecircleD = new CircleTool();
		lineSegD = new LineSegmentTool();
		infLineD = new LineTool();
		polygonD = new PolygonTool();
		polylinD = new PolyLineTool();
		trianglD = new TriangleTool();
		rectangD = new RectangleTool();
		rsquareD = new SquareTool();
		rulerD = new RulerTool();

		tools = new ArrayList<SPTool>();

		tools.add(ellipseD);
		tools.add(ecircleD);
		tools.add(lineSegD);
		tools.add(infLineD);
		tools.add(polygonD);
		tools.add(polylinD);
		tools.add(trianglD);
		tools.add(rectangD);
		tools.add(rsquareD);
		tools.add(rulerD);

		JFrame frame;
		for (SPTool tool : tools) {
			((JPanel) tool).addPropertyChangeListener("shape", this);
			frame = new JFrame (tool.getClass().getName());
			frame.add((JPanel) tool);
			frame.pack();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			frames.add(frame);
		}
		frame = null;
		
		JFrame framee = new JFrame("MAIN TESTER");
		framee.addWindowListener( this );
		framee.add(this);
		framee.pack();
		framee.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framee.setVisible(true);
	}
	
	public static void main(String[] args) {	
		new SVGTester();
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		shapes.add(((SPTool)evt.getSource()).getShape());
		repaint();
	}

	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		shapes.draw(g);
	}
	
	
	// SVG'YE ÇEVÝRMEK ÝÇÝN MAIN TESTER FRAMEÝNÝ KÜÇÜLTÜP BÜYÜLT. SVG DOSYASI DROPBOX KLASÖRÜNDE
	// GeometricSketchpad adýndaki ilk klasörde yer alacak.
	public void windowIconified( WindowEvent event ) {
		System.out.println("Clicked");
		
		if( !SVGImporter.saveSVG(shapes) )
			System.out.println( "HATA VERDÝ LAN!! SVGTester windowIconified");
	}
	
	public void windowOpened( WindowEvent event ) {}
	public void windowActivated( WindowEvent event ) {}
	public void windowDeactivated( WindowEvent event ) {}
	public void windowClosed( WindowEvent event ) {}
	public void windowDeiconified( WindowEvent event ) {}
	public void windowClosing( WindowEvent event ) {}
	
}
