package geometricSketchpad.tools;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.Font;

// RulersTool - Show rulers at each side of the maincanvas
// Süleyman Yasir KULA

public class RulersTool extends JPanel
{
	private static final long serialVersionUID = -4606701389723488834L;
	
	// constructors
	public RulersTool()
	{
		setOpaque(false);
	}

	public RulersTool( int width, int height )
	{
		setPreferredSize(new Dimension (width,height));
		setSize(width,height);
		setOpaque(false);
	}
	
	public RulersTool( Dimension dim )
	{
		this( dim.width, dim.height );
	}
	
	// methods
	public void paintComponent( Graphics g )
	{
		super.paintComponents( g );
		
		//Graphics2D g2D = (Graphics2D) g;
		
		int space = 10;
		
		int width = getSize().width;
		int height = getSize().height;
		//double length;
		int lineCount = 0;
		int currLoc = 0;
		
		Font f = g.getFont();
		
		g.setFont( new Font( "", Font.BOLD, 10 ) );
		
		if( width > height )
		{
			/*g2D.drawLine( -1, height, width + 1, height );
			g2D.draw( new Line2D.Double(  ) );*/
			
			while( currLoc < width )
			{
				if( lineCount % 10 == 0 )
				{
					g.drawLine( currLoc, height, currLoc, 0 );
					g.drawString( "" + currLoc, currLoc + 3, (int) height * 5 / 11 );
				}
				else if( lineCount % 2 == 0 )
				{
					g.drawLine( currLoc, height, currLoc, (int) height * 2 / 3 );
				}
				else
				{
					g.drawLine( currLoc, height, currLoc, (int) height * 4 / 5 );
				}
				
				currLoc += space;
				lineCount++;
			}
		}
		else
		{
			while( currLoc < height )
			{
				if( lineCount % 10 == 0 )
				{
					g.drawLine( width, currLoc, 0, currLoc );
					g.drawString( "" + currLoc, (int) width * 3 / 7, currLoc + 15 );
				}
				else if( lineCount % 2 == 0 )
				{
					g.drawLine( width, currLoc, (int) width * 2 / 3 , currLoc );
				}
				else
				{
					g.drawLine( width, currLoc, (int) width * 4 / 5, currLoc );
				}
				
				currLoc += space;
				lineCount++;
			}
		}
		
		g.setFont( f );
	}
	
	public static void main( String[] args )
	{
		JFrame frame = new JFrame ("RECTANGLE DRAWER");
		RulersTool tool = new RulersTool( 50, 500 );
		frame.add(tool);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
