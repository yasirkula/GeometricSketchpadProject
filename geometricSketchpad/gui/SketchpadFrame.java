package geometricSketchpad.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import geometricSketchpad.tools.SVGImporter;
import geometricSketchpad.tools.SelectTool;

/**
 * @(#)SketchpadFrame.java
 *
 *
 * @author Kaan Mert Berkem & Suleyman Yasir Kula
 * @version 1.00 2013/4/30
 */


public class SketchpadFrame extends JFrame
{
	private static final long serialVersionUID = -7468014075989212811L;
	
	MainCanvas canvas;
	//StatusBar s;
	ToolboxPanel t;
	
	public SketchpadFrame() {
    	
    	//Set the default properties
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	setLayout ( new BorderLayout());
    	setPreferredSize( new Dimension ( 2*screen.width/3, 2*screen.height/3));
    	setBounds( 0 , 0 , 2*screen.width/3, 2*screen.height/3);
    	setTitle ( "Geometric Sketchpad");
    	addWindowListener( new MyWindowAdapter() );
    	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // Needed for WindowAdapter to work properly
    	
    	//Add the panels and the menu bar
    	
    	canvas = new MainCanvas();
    	t = new ToolboxPanel( canvas );
    	setJMenuBar( new SketchpadMenuBar(canvas, t) );
    	add( canvas);
    	
    //	s = new StatusBar();
    	
    	add( t, BorderLayout.WEST);
//    	add( s, BorderLayout.SOUTH);
    	
    	//s.setVisible( false);
    	setVisible( true );
	}
	
	class MyWindowAdapter extends WindowAdapter
	{
		public void windowClosing( WindowEvent e )
		{
			boolean exit = true;
			
			if( !SVGImporter.isSaved )
        	{
        		int result = JOptionPane.showConfirmDialog((Component) null, "Save Changes?","Save Resource", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );
        		
        		if( result == JOptionPane.YES_OPTION )
        		{
        			final JFileChooser fc = new JFileChooser();
    		    	
    		    	fc.setFileFilter( new FileFilter() 
    		    	{
    		    		public boolean accept(File directory) 
    		    		{
    		    			return directory.isDirectory() || directory.getName().toLowerCase().endsWith(".svg");
    		    		} 
    		    		
    		    		public String getDescription()
    		    		{
    		    			return "SVG (Scalable Vector Graphics) File";
    		    		}
    		    	});
    		    	
    		    	fc.setAcceptAllFileFilterUsed( false );
    		    	
    		    	if( MainCanvas.currFile == null )
    		    	{
    		    		int returnVal = fc.showSaveDialog( null );
    		    		
    		    		if ( returnVal == JFileChooser.APPROVE_OPTION )
    		            {
    		                MainCanvas.currFile = fc.getSelectedFile();
    		                
    		                if ( !MainCanvas.currFile.getPath().toLowerCase().endsWith( ".svg" ) )
    		                	MainCanvas.currFile = new File( MainCanvas.currFile.getPath() + ".svg" );
    		                
    		                //This is where a real application would open the file.
    		                //log.append("Opening: " + file.getName() + "." + newline);
    		            }
    		    		else
    		    		{
    		    			exit = false;
    		    		}
    		    	}
    		    	if( MainCanvas.currFile != null && exit )
    		    	{
    		    		SVGImporter.saveSVG( canvas.getShapes(), MainCanvas.currFile.getPath() );
    		    		SVGImporter.isSaved = true;
    		    		System.exit( 0 );
    		    	}
        		}
        		else if( result == JOptionPane.NO_OPTION )
        		{
        			System.exit( 0 );
        		}
        	}
        	else
        	{
        		System.exit( 0 );
        	}
		}
	}

	public void argsFile(String string) {
		File file = new File(string);
        
        SVGImporter importer = new SVGImporter( file.getPath(), SVGImporter.PATH );
        canvas.initCanvas();
        canvas.getShapes().addAll(importer.getShapes());
        
        canvas.setActiveTool( new SelectTool( canvas.getShapes() ) );
        
        MainCanvas.currFile = file;

        repaint();
        canvas.repaint();
        
        SVGImporter.isSaved = true;
	}
}