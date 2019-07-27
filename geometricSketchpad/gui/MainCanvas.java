package geometricSketchpad.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.io.File;

import geometricSketchpad.tools.*;
import geometricSketchpad.shapes.*;

public class MainCanvas extends JLayeredPane implements PropertyChangeListener {
	SPTool tool;
	ShapeCanvas canvas;
	Grid grid;
//	RulersTool ruler1, ruler2;
//	UndoRedoSupport urs;
	Dimension screen;
	
	public static File currFile;
	
	public MainCanvas () {
		SVGImporter.isSaved = true;
		currFile = null;
		
		screen = Toolkit.getDefaultToolkit().getScreenSize(); 
		setPreferredSize(new Dimension(500,500));
		// setLayout(new BorderLayout());
		
		 //JPanel middleScr = new JPanel();
		 //middleScr.setLayout( new BorderLayout() );
		 //ruler1 = new RulersTool( 500, 50 );
		 //ruler2 = new RulersTool( 50, 500 );
		canvas = new ShapeCanvas();
		canvas.setOpaque(false);
		canvas.setBounds(0, 0, screen.width, screen.height);
		// middleScr.add( canvas, BorderLayout.CENTER );
		 //middleScr.add( ruler1, BorderLayout.NORTH );
		 //middleScr.add( ruler2, BorderLayout.WEST );
		add(canvas, DEFAULT_LAYER, 0);		
		//add( middleScr, DEFAULT_LAYER, 0 );
		
		grid = new Grid(32, true);
		grid.setBounds(0, 0, screen.width, screen.height);
		add(grid, DEFAULT_LAYER - 1, 0);
		
//		urs = new UndoRedoSupport(this);
//		canvas.getShapes().addPropertyChangeListener(urs);
		// setPreferredSize( new Dimension(500, 500));
		// setLayout(new BorderLayout());
		// canvas.setBounds(0, 0, getWidth(), getHeight());
		// add(canvas, POPUP_LAYER);
		// setActiveTool(canvas);
		// setActiveTool(new SquareTool());
		
		// Initialize the number of shapes (for naming purposes)
		SPPolygon.rectangleCount = 0;
		SPPolygon.polygonCount = 0;
		SPPolygon.squareCount = 0;
		
		SPEllipse.circleCount = 0;
		SPEllipse.ellipseCount = 0;
		
		SPLine.lineCount = 0;
		SPLine.lineSegmentCount = 0;
		
		SPPolyLine.polyLineCount = 0;
		
		SPPoint.pointCount = 0;
	}
	
	public void setActiveTool(JPanel tool) {
		if (this.tool != null) {
			this.tool.setGrid(null);
			((JPanel) this.tool).removePropertyChangeListener(this);
			remove(getIndexOf((JPanel) this.tool));
			this.tool = null;
		}
		if (tool instanceof SPTool) {
			this.tool = (SPTool) tool;
			this.tool.setGrid(grid);
			tool.setBounds(0, 0, screen.width, screen.height);
			tool.addPropertyChangeListener("shape", this);
			tool.setBackground(Color.BLUE);
			add(tool, POPUP_LAYER, 0);
			repaint();
		}
	}
	
	public static void main(String[] args) {	
		Grid grid = new Grid(32,true);
		ArrayList<SPTool> tools = new ArrayList<SPTool>(10);
		tools.add(new CircleTool());
		tools.add(new EllipseTool());
		tools.add(new LineSegmentTool());
		tools.add(new LineTool());
		tools.add(new PolygonTool());
		tools.add(new PolyLineTool());
		tools.add(new RectangleTool());
		tools.add(new SquareTool());
		tools.add(new TriangleTool());
		
		for (SPTool tool: tools) {
			tool.setGrid(grid);
		}
		
		MainCanvas mcan = new MainCanvas();
		JFrame frame = new JFrame("MAIN CANVAS");
		frame.setLayout(new BorderLayout());
		frame.add(mcan, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Scanner scan = new Scanner(System.in);
		int choice = 9;
		do {
			choice = Math.abs(scan.nextInt() % 10);
			System.out.println(tools.get(choice).getClass());
			mcan.setActiveTool((JPanel) tools.get(choice));
		} while (choice != 9);
		scan.close();
	}
	
	public boolean isSnapping() {
		return grid.isSnapping();
	}
	
	public boolean toggleSnapping() {
		return grid.toggleSnapping();
	}
	
	public void toggleSnapping(boolean snap) {
		grid.toggleSnapping(snap);
	}
	
	public boolean gridVisible() {
		return grid.isVisible();
	}
	
	public void setGridVisible(boolean visi) {
		grid.setVisible(visi);
		canvas.setOpaque(!visi);
	}
	
	/*public boolean rulersVisible() {
		return ruler1.isVisible();
	}*/
	
	/*public void setRulersVisible( boolean visi )
	{
		ruler1.setVisible( visi );
		ruler2.setVisible( visi );
	}*/
	
/*	public void undo () {
		urs.undo();
		repaint();
	}
	
	public void redo () {
		urs.redo();
		repaint();
	} */
	
	public void initCanvas() {
		remove(canvas);
		canvas = new ShapeCanvas();
		canvas.setOpaque(false);
		canvas.setBounds(0, 0, screen.width, screen.height);
		add(canvas, DEFAULT_LAYER, 0);
		repaint();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (arg0.getSource() == tool) {
			SPShape shape = tool.getShape();
			// shape.addPropertyChangeListener(urs);
			canvas.addShape(shape);
			// ((JPanel) tool).removePropertyChangeListener(this);
			// ((JPanel) tool).setOpaque(true);
			// remove(getIndexOf((JPanel) tool));
			// tool = null;
			// setActiveTool(new EllipseTool());
			repaint();
		}
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		canvas.setBounds(0, 0, getWidth(), getHeight());
		if (tool != null) { ((JPanel) tool).setBounds(0, 0, screen.width, screen.height); }
		if (grid != null) { grid.setBounds(0, 0, screen.width, screen.height); }
	}
	
	public SPGroup getShapes()
	{
		return canvas.shapes;
	}
	
	// JPEG/PNG export methods
	public void exportAsPNG( File f ) {
		BufferedImage img = new BufferedImage(this.getWidth(),this.getHeight(),  BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		paint(g);
		g.dispose();  	
		try {
		ImageIO.write(img,"png", f );
		}catch (Exception exc) {}
	}
	
	public void exportAsJPEG( File f ) {
		BufferedImage img = new BufferedImage(this.getWidth(),this.getHeight(),  BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
  		paint(g);
  		g.dispose();  	
		try {
			ImageIO.write(img,"jpg", f );
		}catch (Exception exc) {}
	}
}
