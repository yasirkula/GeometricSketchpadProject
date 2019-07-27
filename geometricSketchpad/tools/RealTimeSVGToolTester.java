package geometricSketchpad.tools;

import geometricSketchpad.shapes.SPShape;

import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;

public class RealTimeSVGToolTester extends SPToolTester {
	private static final long serialVersionUID = -3150969156743419888L;

	public static void main(String[] args) {
		SPToolTester tester = new RealTimeSVGToolTester();
		JFrame frame = new JFrame("MAIN TESTER");
		frame.add(tester);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		SPShape shape = ((SPTool)evt.getSource()).getShape();
		System.out.println(shape);
		shapes.add(shape);
		repaint();
	}
}
