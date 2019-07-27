package geometricSketchpad.gui;

import geometricSketchpad.tools.EllipseTool;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Grid extends JPanel {
	boolean snapOn;
	boolean visible;
	double scale;
	
	public Grid() {
		this(32);
	}
	
	public Grid(double sc) {
		this (sc, false);
	}
	
	public Grid(double sc, boolean snap) {
		this (sc, snap, true);
	}
	
	public Grid(double sc, boolean snap, boolean visi) {
		setPreferredSize(new Dimension(5000,5000));
		setOpaque(true);
		scale = sc;
		snapOn = snap;
		visible = visi;
	}
	
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.LIGHT_GRAY);
		for (int itr1 = -1; itr1 < getHeight(); itr1 += scale) {
			g2.drawLine(-1, itr1, getWidth(), itr1);
		}
		
		for (int itr2 = -1; itr2 < getWidth(); itr2 += scale) {
			g2.drawLine(itr2, -1, itr2, getHeight());
		}
	}
	
	public MouseEvent snap(MouseEvent e) {
		if (!isSnapping()) {return e;}
		MouseEvent tmp = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), snap(e.getX()), snap(e.getY()), e.getClickCount(), e.isPopupTrigger(), e.getButton());
		e.consume();
		return tmp;
	}
	
	public int snap (int x) {
		if (!isSnapping()) {return x;}
		// return (int) (x - x % scale);
		return (int) (x + ((x % scale <= scale/2) ? -(x % scale) : (scale -(x % scale))));
	}
	
	public boolean isSnapping() {
		return snapOn;
	}
	
	public boolean toggleSnapping() {
		snapOn = !snapOn;
		return snapOn;
	}
	
	public void toggleSnapping(boolean snap) {
		snapOn = snap;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visi) {
		visible = visi;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame ("ERFING GRID");
		Grid tool = new Grid();
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
