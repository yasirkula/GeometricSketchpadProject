package geometricSketchpad.tools;

import geometricSketchpad.gui.ShapeCanvas;
import geometricSketchpad.shapes.*;

import java.awt.datatransfer.*;
import java.awt.Toolkit;

import javax.swing.JFrame;

// CopyPasteTool - Copies SVG code of the shapes to clipboard and when pasted, gets the code and convertes it to shapes
// Süleyman Yasir KULA

public class CopyPasteTool
{
	public static void copySelected( SPGroup shapes )
	{
		String code = "<?xml version=\"1.0\" standalone=\"no\"?>" + "\n" +
			    "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"" + "\n" +
			    "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">" + "\n" +
			    "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">" + "\n";
		
		for( SketchpadObject s : shapes )
		{
			if( s.selected() )
				code += s.toString() + "\n";
		}
		code += "\n</svg>";
		
		StringSelection selection = new StringSelection( code );
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, selection);
	}
	
	public static SPGroup pasteData() throws Exception
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		String code = (String) clipboard.getData(DataFlavor.stringFlavor);
		
		SVGImporter importer = new SVGImporter( code, SVGImporter.CODE );
		SVGImporter.isSaved = false;
		SPGroup result = importer.getShapes();
		
		for( SketchpadObject s : result )
			s.toggleSelect( true );
		
		return result;
	}
	
	public static void main( String[] args )
	{
		SPGroup g = new SPGroup();
		
		// Line below will be copied as it is selected
		SPLine l =  new SPLine( new SPPoint( 500,50 ), new SPPoint( 200,75 ) );
		l.toggleSelect( true );
		g.add( l );
		
		// Line below won't be copied as it is unselected
		l = new SPLine( new SPPoint( 50,50 ), new SPPoint( 100,75 ) );
		g.add( l );
		
		ShapeCanvas tester = new ShapeCanvas();
		JFrame frame = new JFrame("MAIN TESTER");
		frame.add(tester);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		copySelected( g );
		
		try
		{
			tester.addShape( pasteData() );
		}
		catch( Exception e ) {}
	}
}
