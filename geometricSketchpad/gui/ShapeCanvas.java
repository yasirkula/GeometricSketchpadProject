package geometricSketchpad.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import geometricSketchpad.shapes.SPEllipse;
import geometricSketchpad.shapes.SPGroup;
import geometricSketchpad.shapes.SketchpadObject;
import geometricSketchpad.tools.SPToolTester;



public class ShapeCanvas extends JPanel {
	SPGroup shapes;
	
		
	
	public SPGroup getShapes()
	{
		return shapes;
	}
	
	public ShapeCanvas () {
		this(new SPGroup());
	}
	
	public ShapeCanvas (SPGroup shapes) {
		this.shapes = shapes;
		setPreferredSize(new Dimension(5000,5000));
		// setBackground(Color.WHITE);
		
		//bufferedImage for saving the canvas as jpg/png
//		BufferedImage img = new BufferedImage(this.getHeight(),this.getWidth(),  BufferedImage.TYPE_INT_RGB);
//		Graphics g = img.createGraphics();
		
		

		
	/*	SPEllipse ellipse;
		double major = 300;
		double minor = 400;
		double x = Math.random() * 250;
		double y = Math.random() * 250;
		for (int i = 0; i < 36; i++) {
			ellipse = new SPEllipse( major, minor, x, y);
			ellipse.setRotation(i*Math.PI/36);
			// System.out.println(ellipse);	
			shapes.add(ellipse);
		}
		// addShape (new SPEllipse(100,100,300,300));
		setPreferredSize(new Dimension(500,500));
		setOpaque(true);
		setBackground(Color.CYAN); */
		
	}
	
		
	
	public void addShape (SketchpadObject s) {
		shapes.add(s);
		repaint();
	}

	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		shapes.draw(g);
	}
	
	public static void main(String[] args) {	
		ShapeCanvas tester = new ShapeCanvas();
		JFrame frame = new JFrame("MAIN TESTER");
		frame.add(tester);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
