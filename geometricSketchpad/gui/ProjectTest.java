package geometricSketchpad.gui;

/**
 * ProjectTest 
 *
 * @author G1F
 * @version 1.00 2013/5/12
 */
 
public class ProjectTest 
{
    public static void main( String[] args) 
	{
    	// Create an instance of the program
		SketchpadFrame frame = new SketchpadFrame();
		if (args.length == 1) {
			frame.argsFile(args[0]);
		}
		
	}
	
} // end of class ProjectTest
