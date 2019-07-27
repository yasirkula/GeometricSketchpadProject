package geometricSketchpad.gui;

import geometricSketchpad.tools.CopyPasteTool;
import geometricSketchpad.tools.MoveRotateTool;
import geometricSketchpad.tools.SVGImporter;
import geometricSketchpad.tools.SelectTool;
import geometricSketchpad.tools.LineTool;
import geometricSketchpad.shapes.SPGroup;
import geometricSketchpad.shapes.SketchpadObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @(#)SketchpadMenuBar.java menuBar class for the sketchpad gui, incomplete
 *
 *
 * @author Kaan Mert Berkem & Suleyman Yasir Kula
 * @version 1.00 2013/4/30
 */


public class SketchpadMenuBar extends JMenuBar {
	MainCanvas mc;
	ToolboxPanel tp;

	//TO DO - menus
    public SketchpadMenuBar(MainCanvas mainc, ToolboxPanel toolb) {
    	mc = mainc;
    	tp = toolb;
    	
    	//Create the menus
    	JMenu fileMenu;
    	JMenu editMenu;
    	JMenu viewMenu;
    	JMenu toolsMenu;
    	JMenu helpMenu;
    	
    	fileMenu = new JMenu("File");
    	editMenu = new JMenu("Edit");
    	viewMenu = new JMenu("View");
    	toolsMenu = new JMenu("Tools");
    	helpMenu = new JMenu("Help");
		
		//Add the menus to the menu bar
    	add(fileMenu);
    	add(editMenu);
    	add(viewMenu);
    	add(toolsMenu);
    	add(helpMenu);

    	
    	//    	Create the menu items for each of the menus
    	
      
        //File menu actions
        JMenuItem newAction = new JMenuItem(new AbstractAction("New") {
            public void actionPerformed(ActionEvent e) {
            	//mc.getShapes() = new SPGroup();
            	if( !SVGImporter.isSaved )
            	{
            		int result = JOptionPane.showConfirmDialog((Component) null, "Save Changes?","Save Resource", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );
            		
            		if( result == JOptionPane.YES_OPTION )
            		{
            			if( saveAction() )
            			{
	            			mc.initCanvas();
	    	            	repaint();
	    	            	SVGImporter.isSaved = true;
            			}
            		}
            		else if( result == JOptionPane.NO_OPTION )
            		{
            			mc.initCanvas();
    	            	repaint();
    	            	SVGImporter.isSaved = true;
            		}
            	}
            	else
            	{
	            	mc.initCanvas();
	            	repaint();
            	}
            } } );
        
        // JMenuItem closeAction = new JMenuItem("Close");
        
        JMenuItem openAction = new JMenuItem(new AbstractAction("Open") {
            public void actionPerformed(ActionEvent e) {
            	if( !SVGImporter.isSaved )
            	{
            		int result = JOptionPane.showConfirmDialog((Component) null, "Save Changes?","Save Resource", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );
            		
            		if( result == JOptionPane.YES_OPTION )
            		{
            			if( saveAction() );
            				openAction();
            		}
            		else if( result == JOptionPane.NO_OPTION )
            		{
    	            	openAction();
            		}
            	}
            	else
            	{
            		openAction();
            	}
            } } );
        
        JMenuItem saveAction = new JMenuItem(new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
            	saveAction();
            } } );
        
        JMenuItem saveAsAction = new JMenuItem(new AbstractAction("Save As") {
            public void actionPerformed(ActionEvent e) {
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
            	
                int returnVal = fc.showSaveDialog( SketchpadMenuBar.this );

                if ( returnVal == JFileChooser.APPROVE_OPTION )
                {
                    File file = fc.getSelectedFile();
                    
                    String path = file.getPath().toLowerCase();
                    
                    if( !path.endsWith( ".svg" ) )
                    	path = path + ".svg";
                    
                    file = new File( path );
                    
                    SVGImporter.saveSVG( mc.getShapes(), path );
                    MainCanvas.currFile = file;
                    
                    //This is where a real application would open the file.
                    //log.append("Opening: " + file.getName() + "." + newline);
                }
                else
                {
                    //log.append("Open command cancelled by user." + newline);
                }
               
            } } );
        
        JMenuItem exportAction = new JMenuItem( new AbstractAction("Export") {
        	public void actionPerformed(ActionEvent e) {
        		if( exportAction() )
        			System.out.println( "Exported successfully" );
        		else
        			System.out.println( "Error while exporting" );
        	}
        } );
        
        JMenuItem exitAction = new JMenuItem(new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent e) {
            	if( !SVGImporter.isSaved )
            	{
            		int result = JOptionPane.showConfirmDialog((Component) null, "Save Changes?","Save Resource", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );
            		
            		if( result == JOptionPane.YES_OPTION )
            		{
            			if( saveAction() )
            				System.exit( 0 );
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
            } } );
        
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(saveAsAction);
        // fileMenu.add(closeAction);
        fileMenu.add(exportAction);
        fileMenu.add(exitAction);
        
        //Edit menu actions
        /*JMenuItem undoAction = new JMenuItem(new AbstractAction("Undo") {
            public void actionPerformed(ActionEvent e) {
            	mc.undo() ;
            	mc.repaint();
            	repaint();
            } } );
        
        JMenuItem redoAction = new JMenuItem(new AbstractAction("Redo") {
            public void actionPerformed(ActionEvent e) {
            	mc.redo() ;
            	mc.repaint();
            	repaint();
            } } );*/
        
        JMenuItem cutAction = new JMenuItem( new AbstractAction("Cut") {
            public void actionPerformed(ActionEvent e) {
            	CopyPasteTool.copySelected( mc.getShapes() );
            	mc.getShapes().deleteSelected();
            	mc.repaint();
            	repaint();
            } } );
        
        JMenuItem copyAction = new JMenuItem( new AbstractAction("Copy") {
            public void actionPerformed(ActionEvent e) {
            	CopyPasteTool.copySelected( mc.getShapes() );
            } } );
        
        JMenuItem pasteAction = new JMenuItem( new AbstractAction("Paste") {
            public void actionPerformed(ActionEvent e) {
            	try
            	{
            		mc.getShapes().deselectAll();
            		for( SketchpadObject s : CopyPasteTool.pasteData() )
            		{
            			//mc.getShapes().addAll( CopyPasteTool.pasteData() );
            			mc.getShapes().add( s  );
            		}
            		System.out.println(mc.getShapes().size());
            		mc.setActiveTool( new LineTool() );
            		mc.setActiveTool( new MoveRotateTool( mc.getShapes() ) );
            		repaint();
            		mc.repaint();
            		System.out.println( "Paste successful..." );
            	}
            	catch( Exception ex )
            	{
            		System.out.println( "Error while pasting the data..." );
            	}
            } } );
        
        JMenuItem deleteAction = new JMenuItem( new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
            	mc.getShapes().deleteSelected();
            	repaint();
            	mc.repaint();
            	
            } } );
        
        // editMenu.add(undoAction);
        // editMenu.add(redoAction);
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(deleteAction);
        
        //View menu actions
        JCheckBoxMenuItem gridlinesAction = new JCheckBoxMenuItem(new AbstractAction("Gridlines") {
            public void actionPerformed(ActionEvent e) {
            	mc.setGridVisible(!mc.gridVisible());
            	mc.repaint();
            	repaint();
            } } );
        
        gridlinesAction.setSelected(true);
        
        /*JMenuItem rulersAction = new JMenuItem(new AbstractAction("Rulers") {
            public void actionPerformed(ActionEvent e) {
            	mc.setRulersVisible(!mc.rulersVisible());
            	mc.repaint();
            	repaint();
            } } );*/
        
        JCheckBoxMenuItem toolboxAction = new JCheckBoxMenuItem(new AbstractAction("Toolbox") {
            public void actionPerformed(ActionEvent e) {
            	tp.setVisible( !tp.isVisible() );
            } } );
        
        toolboxAction.setSelected(true);
        
        //JMenuItem statusBarAction = new JMenuItem("Status Bar");
        
        
        viewMenu.add(gridlinesAction);
        // viewMenu.add(rulersAction);
        viewMenu.add(toolboxAction);
        //viewMenu.add(statusBarAction);
        
        //Tools menu actions
        
        JCheckBoxMenuItem snapToGridAction = new JCheckBoxMenuItem(new AbstractAction("Snap To Grid") {
            public void actionPerformed(ActionEvent e) {
            	mc.toggleSnapping(!mc.isSnapping());
            	mc.repaint();
            	repaint();
            } } );
        snapToGridAction.setSelected(true);
        toolsMenu.add(snapToGridAction);
        
        //Help menu actions
        JMenuItem aboutAction = new JMenuItem(new AbstractAction("About") {
            public void actionPerformed(ActionEvent e) {
            //	JOptionPane.showMessageDialog(null, "Geometric Sketchpad, 2013 Copyright\n\nby Warriors of JavaLand\n\nKnight: Abdullah Çaðlar ÖKSÜZ\nPaladin: Alper Nebi YASAK\nMage: Doðukan Oðuz SERT\nRogue: Görkem BOZBIYIK\nScout: Kaan Mert BERKEM\nArcher: Süleyman Yasir KULA\n ", "About", JOptionPane.INFORMATION_MESSAGE );
             	JOptionPane.showMessageDialog(null, "Geometric Sketchpad, 2013 Copyright\n\nAlper Nebi YASAK\nSüleyman Yasir KULA\nKaan Mert BERKEM\nAbdullah Çaðlar ÖKSÜZ\nGörkem BOZBIYIK\nDoðukan Oðuz SERT ", "About", JOptionPane.INFORMATION_MESSAGE );
            } } );
        
        helpMenu.add( aboutAction );
		//help menu has not been implemented yet	    	
    	//setHelpMenu(helpMenu);
    }
    
    public void setActiveCanvas (MainCanvas mainc) {
    	mc = mainc;
    	repaint();
    }
    
    private boolean saveAction()
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
    		int returnVal = fc.showSaveDialog( SketchpadMenuBar.this );
    		
    		if ( returnVal == JFileChooser.APPROVE_OPTION )
            {
                MainCanvas.currFile = fc.getSelectedFile();
                
                if ( !MainCanvas.currFile.getPath().toLowerCase().endsWith( ".svg" ) )
                	MainCanvas.currFile = new File( MainCanvas.currFile.getPath() + ".svg" );
                
                //This is where a real application would open the file.
                //log.append("Opening: " + file.getName() + "." + newline);
            }
    		else
    			return false;
    	}
    	
    	if( MainCanvas.currFile != null )
    	{
    		SVGImporter.saveSVG( mc.getShapes(), MainCanvas.currFile.getPath() );
    		SVGImporter.isSaved = true;
    		return true;
    	}
    	
    	return false;
    }
    
    private void openAction()
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
    	
        int returnVal = fc.showOpenDialog( SketchpadMenuBar.this );

        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            File file = fc.getSelectedFile();
            
            SVGImporter importer = new SVGImporter( file.getPath(), SVGImporter.PATH );
            mc.initCanvas();
            mc.getShapes().addAll(importer.getShapes());
            
            mc.setActiveTool( new SelectTool( mc.getShapes() ) );
            
            MainCanvas.currFile = file;

            repaint();
            mc.repaint();
            
            SVGImporter.isSaved = true;
        }
    }
    
    private boolean exportAction()
    {
    	final JFileChooser fc = new JFileChooser();
    	
    	fc.setFileFilter( new FileFilter() 
    	{
    		public boolean accept(File directory) 
    		{
    			return directory.isDirectory() || directory.getName().toLowerCase().endsWith(".png");
    		} 
    		
    		public String getDescription()
    		{
    			return "PNG File";
    		}
    	});
    	
    	fc.addChoosableFileFilter(new FileFilter() 
    	{
    		public boolean accept(File directory) 
    		{
    			return directory.isDirectory() || directory.getName().toLowerCase().endsWith(".jpeg");
    		} 
    		
    		public String getDescription()
    		{
    			return "JPEG File";
    		}
    	});
    	
    	fc.setAcceptAllFileFilterUsed( false );

		int returnVal = fc.showSaveDialog( SketchpadMenuBar.this );
		
		if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
			mc.getShapes().deselectAll();
			repaint();
			mc.repaint();
			
            File currFile = fc.getSelectedFile();
            
            if( fc.getFileFilter().getDescription().charAt( 0 ) == 'P' ) // If it is PNG file
            {
            	if( !currFile.getPath().endsWith( ".png" ) && !currFile.getPath().endsWith( ".PNG" ) )
            	{
            		currFile = new File( currFile.getPath() + ".png" );
            		
            		System.out.println( currFile.getPath() );
            	}
            	
            	mc.exportAsPNG( currFile );
            }
            else
            {
            	if( !currFile.getPath().endsWith( ".jpeg" ) && !currFile.getPath().endsWith( ".JPEG" ) && !currFile.getPath().endsWith( ".jpg" ) && !currFile.getPath().endsWith( ".JPG" ) )
            	{
            		currFile = new File( currFile.getPath() + ".jpeg" );
            		
            		System.out.println( currFile.getPath() );
            	}
            	
            	mc.exportAsJPEG( currFile );
            }
        }
		else
			return false;
    	
    	return true;
    }
//    public void exportAsPNG() {
//		BufferedImage img = new BufferedImage(this.getHeight(),this.getWidth(),  BufferedImage.TYPE_INT_RGB);
//		Graphics g = img.createGraphics();
//		paint(g);
//		g.dispose();  	
//		try {
//		ImageIO.write(img,"png",new File("testPNG.png"));
//		}catch (Exception exc) {}
//	}
//
//    public void exportAsJPEG() {
//		BufferedImage img = new BufferedImage(this.getHeight(),this.getWidth(),  BufferedImage.TYPE_INT_RGB);
//		Graphics g = img.createGraphics();
//			paint(g);
//			g.dispose();  	
//		try {
//			ImageIO.write(img,"jpg",new File("testJPG.jpg"));
//		}catch (Exception exc) {}
//   }


}