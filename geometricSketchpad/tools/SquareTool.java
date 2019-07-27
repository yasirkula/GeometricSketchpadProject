package geometricSketchpad.tools;

import geometricSketchpad.shapes.*;

import java.awt.event.MouseEvent;
import javax.swing.JFrame;

public class SquareTool extends RectangleTool {
	private static final long serialVersionUID = 8147597333945534010L;

	@Override
	public void mouseDragged(MouseEvent arg0) {
		super.mouseDragged(arg0);
		squareify();
	}
	/*
	public void mouseReleased(MouseEvent arg0) {
		super.mouseReleased(arg0);
		squareify();
		repaint();
	}
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame ("SQUARE DRAWER");
		SquareTool tool = new SquareTool();
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void squareify () {
		if (rectBase1 != null && rectBase2 != null && Math.abs(rectBase1.x() - rectBase2.x()) != Math.abs(rectBase1.y()- rectBase2.y())) {
			double offset = Math.min(Math.abs(rectBase2.x() - rectBase1.x()), Math.abs(rectBase2.y()- rectBase1.y()));
			if (rectBase1.x() <= rectBase2.x() && rectBase1.y() <= rectBase2.y()) {
				rectBase2 = new SPPoint(rectBase1.x() + offset, rectBase1.y() + offset);
			} else if (rectBase1.x() >= rectBase2.x() && rectBase1.y() <= rectBase2.y()) {
				rectBase2 = new SPPoint(rectBase1.x() - offset, rectBase1.y() + offset);
			} else if (rectBase1.x() <= rectBase2.x() && rectBase1.y() >= rectBase2.y()) {
				rectBase2 = new SPPoint(rectBase1.x() + offset, rectBase1.y() - offset);
			} else if (rectBase1.x() >= rectBase2.x() && rectBase1.y() >= rectBase2.y()) {
				rectBase2 = new SPPoint(rectBase1.x() - offset, rectBase1.y() - offset);
			}
		}
	}
}