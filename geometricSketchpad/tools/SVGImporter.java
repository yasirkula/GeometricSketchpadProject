package geometricSketchpad.tools;

// SVGImporter - Interprets the SVG code and returns a group of shapes
// Süleyman Yasir KULA

import geometricSketchpad.gui.ShapeCanvas; // For testing only

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;

import javax.swing.JFrame;

import geometricSketchpad.shapes.*;
 
public class SVGImporter
{
	// PROPERTIES
	public static final int PATH = 0;
	public static final int CODE = 1;
	
	public static boolean isSaved;
	
	private SPGroup result; // It is this SPGroup that should be returned as the imported objects
	private SPGroup shapes;
	
	private String code;
	
	boolean getLine, getRect, getPolyLine, getPolygon, getCircle, getEllipse, getPath, getGroup;
	
	// CONSTRUCTORS
	public SVGImporter( String s, int mode )
	{
		result = new SPGroup();
		shapes = result;
		
		this.getLine = true;
		this.getRect = true;
		this.getPath = true;
		this.getPolygon = true;
		this.getPolyLine = true;
		this.getCircle = true;
		this.getEllipse = true;
		this.getGroup = true;
		
		if( mode == CODE )
		{
			code = s;
		}
		else
		{
			code = "";
			
			try ( BufferedReader br = new BufferedReader( new FileReader( s ) ) )
			{
				String currentLine = br.readLine();
	 
				while ( currentLine != null )
				{
					code = code + "\n" + currentLine;
					currentLine = br.readLine();
				}
				
				System.out.println( code );
			} catch ( IOException e ) {
				System.out.println( "ERROR WHILE IMPORTING SVG FILE!" );
			}
		}
	}
	
	public SVGImporter( String s, int mode, boolean getLine, boolean getRect, boolean getPolyLine, boolean getPolygon, boolean getCircle, boolean getEllipse, boolean getPath, boolean getGroup )
	{
		result = new SPGroup();
		shapes = result;
		
		this.getLine = getLine;
		this.getRect = getRect;
		this.getPath = getPath;
		this.getPolygon = getPolygon;
		this.getPolyLine = getPolyLine;
		this.getCircle = getCircle;
		this.getEllipse = getEllipse;
		this.getGroup = getGroup;
		
		if( mode == CODE )
		{
			code = s;
		}
		else
		{
			code = "";
			
			try ( BufferedReader br = new BufferedReader( new FileReader( s ) ) )
			{
				String currentLine = br.readLine();
	 
				while ( currentLine != null )
				{
					code = code + "\n" + currentLine;
					currentLine = br.readLine();
				}
				
				System.out.println( code );
			} catch ( IOException e ) {
				System.out.println( "ERROR WHILE IMPORTING SVG FILE!" );
			}
		}
	}
	
	
	
	// METHODS
	public SPGroup getShapes()
	{
		//if( !getGroup )
		//{
		if( getLine )
			getLines( 0, code.length() - 1 );
		if( getRect )
			getRectangles( 0, code.length() - 1 );
		if( getPolyLine )
			getPolyLines( 0, code.length() - 1 );
		if( getPolygon )
			getPolygons( 0, code.length() - 1 );
		if( getCircle )
			getCircles( 0, code.length() - 1 );
		if( getEllipse )
			getEllipses( 0, code.length() - 1 );
		if( getPath )
			getPaths( 0, code.length() - 1 );
		/*}	
		else
		{
			int lastIndex = code.indexOf( "/svg" );
			
			if( lastIndex == -1 )
				lastIndex = code.length() - 1;
			
			getGroups( 0, lastIndex );
		}*/
		
		return result;
	}
	
	public static boolean saveSVG( SPGroup shapes )
	{
		try
		{
			// Create file 
			FileWriter fstream = new FileWriter("out_" + System.currentTimeMillis() + ".svg");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("<?xml version=\"1.0\" standalone=\"no\"?>" + "\n" +
			    "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"" + "\n" +
			    "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">" + "\n" +
			    "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">" + "\n");
			out.write(shapes.toString());
			out.write("\n</svg>");
			
			//Close the output stream
			out.close();
			
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	
	public static boolean saveSVG( SPGroup shapes, String location )
	{
		try
		{
			// Create file 
			FileWriter fstream = new FileWriter( location );
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("<?xml version=\"1.0\" standalone=\"no\"?>" + "\n" +
			    "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"" + "\n" +
			    "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">" + "\n" +
			    "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">" + "\n");
			out.write(shapes.toString());
			out.write("\n</svg>");
			
			//Close the output stream
			out.close();
			
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	
	/*private int getGroups( int startIndex, int stopIndex )
	{
		int currChar, endChar, nextGroup;
		
		SPGroup previousGroup;
		
		currChar = code.indexOf( "<g" );
		
		while( currChar != -1 )
		{
			endChar = Math.min( code.indexOf( "</g", currChar +2 ), code.indexOf( "<g", currChar + 2 ) );
			
			if( endChar > stopIndex )
				break;
			
			nextGroup = code.indexOf( "<g", currChar + 2 );
			
			if( nextGroup == -1 )
				nextGroup = code.length() - 1;
			
			try
			{
				previousGroup = shapes;
				shapes = new SPGroup();
				
				if( getLine )
					getLines( currChar, nextGroup );
				if( getRect )
					getRectangles( currChar, nextGroup );
				if( getPolyLine )
					getPolyLines( currChar, nextGroup );
				if( getPolygon )
					getPolygons( currChar, nextGroup );
				if( getCircle )
					getCircles( currChar, nextGroup );
				if( getEllipse )
					getEllipses( currChar, nextGroup );
				if( getPath )
					getPaths( currChar, nextGroup );
				
				// Get the name of the group if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					shapes.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					shapes.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
				
				tester.addShape( (SketchpadObject) o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes this value from "123" to " 123"
				// or there is no value for this tag), then forget about that particular
				// line and try importing other lines!!!
				
				currChar = code.indexOf( "<g", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<g", currChar + 5 );
		}
	}*/
	
	
	private void getLines( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		double x1, x2, y1, y2;
		
		currChar = code.indexOf( "<line", startIndex );
		
		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			try
			{
				x1 = Double.parseDouble( getTagValue( "x1", currChar ) );
				x2 = Double.parseDouble( getTagValue( "x2", currChar ) );
				y1 = Double.parseDouble( getTagValue( "y1", currChar ) );
				y2 = Double.parseDouble( getTagValue( "y2", currChar ) );
				
				SPLine o = new SPLine( new SPPoint( x1, y1), new SPPoint( x2, y2 ) );
				
				// Get the name of the shape if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					o.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					o.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes a tag's value from "123" to " 123"
				// or there is no value for a required tag), then forget about that particular
				// shape and try importing other shapes!!!
				
				currChar = code.indexOf( "<line", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<line", currChar + 5 );
		}
	}
	
	private void getRectangles( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		double x, y, width, height;
		
		currChar = code.indexOf( "<rect", startIndex );
		
		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			try
			{
				x = Double.parseDouble( getTagValue( "x", currChar ) );
				y = Double.parseDouble( getTagValue( "y", currChar ) );
				width = Double.parseDouble( getTagValue( "width", currChar ) );
				height = Double.parseDouble( getTagValue( "height", currChar ) );
				
				System.out.println( width + " "+ height);
				SPPolygon o = new SPPolygon( new SPPoint( x, y), new SPPoint( x + width, y ), new SPPoint( x + width, y + height ), new SPPoint( x, y + height ), new SPPoint( x, y ) );
				
				// Get the name of the shape if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					o.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					o.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes a tag's value from "123" to " 123"
				// or there is no value for a required tag), then forget about that particular
				// shape and try importing other shapes!!!
				
				currChar = code.indexOf( "<rect", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<rect", currChar + 5 );
		}
	}
	
	private void getPolyLines( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		String points;
		
		ArrayList<Double> x, y;
		
		currChar = code.indexOf( "<polyline", startIndex );
		
		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			x = new ArrayList<Double>();
			y = new ArrayList<Double>();
			
			try
			{
				int currCommaIndex, prevCommaIndex;
				
				points = getTagValue( "points", currChar );
				
				prevCommaIndex = 0;
				currCommaIndex = points.indexOf( "," );
				
				while( currCommaIndex != -1 )
				{
					int startPoint, endPoint;
					
					startPoint = points.indexOf( " ", prevCommaIndex );
					startPoint++;
					
					// if it is the first point in the list
					if( startPoint > currCommaIndex )
						startPoint = 0;
					
					endPoint = points.indexOf( " ", currCommaIndex );
					
					// if it is the last point in the list
					if( endPoint == -1 )
						endPoint = points.length();
					
					try
					{
						x.add( Double.parseDouble( points.substring( startPoint, currCommaIndex ) ) );
						y.add( Double.parseDouble( points.substring( currCommaIndex + 1, endPoint ) ) );
					}
					catch( Exception e )
					{
						prevCommaIndex = currCommaIndex;
						currCommaIndex = points.indexOf( ",", currCommaIndex + 1 );
						
						continue;
					}
					
					prevCommaIndex = currCommaIndex;
					currCommaIndex = points.indexOf( ",", currCommaIndex + 1 );
				}
				
				if( x.size() == 0 || y.size() == 0 || x.size() != y.size() )
				{
					currChar = code.indexOf( "<polyline", currChar + 5 );
					
					continue;
				}
				
				SPGroup vertices = new SPGroup();
				
				for( int i = 0; i < x.size(); i++ )
				{
					vertices.add( new SPPoint( x.get( i ), y.get( i ) ) );
				}
				
				SPPolyLine o = new SPPolyLine( vertices );
				
				// Get the name of the shape if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					o.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					o.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes a tag's value from "123" to " 123"
				// or there is no value for a required tag), then forget about that particular
				// shape and try importing other shapes!!!
				
				currChar = code.indexOf( "<polyline", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<polyline", currChar + 5 );
		}
	}
	
	private void getPolygons( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		String points;
		
		ArrayList<Double> x, y;
		
		currChar = code.indexOf( "<polygon", startIndex );
		
		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			x = new ArrayList<Double>();
			y = new ArrayList<Double>();
			
			try
			{
				int currCommaIndex, prevCommaIndex;
				
				points = getTagValue( "points", currChar );
				
				prevCommaIndex = 0;
				currCommaIndex = points.indexOf( "," );
				
				while( currCommaIndex != -1 )
				{
					int startPoint, endPoint;
					
					startPoint = points.indexOf( " ", prevCommaIndex );
					startPoint++;
					
					// if it is the first point in the list
					if( startPoint > currCommaIndex )
						startPoint = 0;
					
					endPoint = points.indexOf( " ", currCommaIndex );
					
					// if it is the last point in the list
					if( endPoint == -1 )
						endPoint = points.length();
					
					try
					{
						x.add( Double.parseDouble( points.substring( startPoint, currCommaIndex ) ) );
						y.add( Double.parseDouble( points.substring( currCommaIndex + 1, endPoint ) ) );
					}
					catch( Exception e )
					{
						prevCommaIndex = currCommaIndex;
						currCommaIndex = points.indexOf( ",", currCommaIndex + 1 );
						
						continue;
					}
					
					prevCommaIndex = currCommaIndex;
					currCommaIndex = points.indexOf( ",", currCommaIndex + 1 );
				}
				
				if( x.size() == 0 || y.size() == 0 || x.size() != y.size() )
				{
					currChar = code.indexOf( "<polygon", currChar + 5 );
					
					continue;
				}
				
				SPGroup vertices = new SPGroup();
				
				for( int i = 0; i < x.size(); i++ )
				{
					vertices.add( new SPPoint( x.get( i ), y.get( i ) ) );
				}
				
				// Connect last point with the first point
				vertices.add( vertices.get( 0 ) );
				
				SPPolygon o = new SPPolygon( vertices );
				
				// Get the name of the shape if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					o.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					o.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes a tag's value from "123" to " 123"
				// or there is no value for a required tag), then forget about that particular
				// shape and try importing other shapes!!!
				
				currChar = code.indexOf( "<polygon", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<polygon", currChar + 5 );
		}
	}
	
	private void getCircles( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		double cx, cy, r;
		
		currChar = code.indexOf( "<circle", startIndex );
		
		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			try
			{
				cx = Double.parseDouble( getTagValue( "cx", currChar ) );
				cy = Double.parseDouble( getTagValue( "cy", currChar ) );
				r = Double.parseDouble( getTagValue( "r", currChar ) );
				
				SPEllipse o = new SPEllipse( 2 * r, 2 * r, cx - r, cy - r );
				
				// Get the name of the shape if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					o.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					o.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes a tag's value from "123" to " 123"
				// or there is no value for a required tag), then forget about that particular
				// shape and try importing other shapes!!!
				
				currChar = code.indexOf( "<circle", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<circle", currChar + 5 );
		}
	}
	
	private void getEllipses( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		double cx, cy, rx, ry;
		
		currChar = code.indexOf( "<ellipse", startIndex );
		
		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			try
			{
				cx = Double.parseDouble( getTagValue( "cx", currChar ) );
				cy = Double.parseDouble( getTagValue( "cy", currChar ) );
				rx = Double.parseDouble( getTagValue( "rx", currChar ) );
				ry = Double.parseDouble( getTagValue( "ry", currChar ) );
				
				SPEllipse o = new SPEllipse( 2 * rx, 2 * ry, cx - rx, cy - ry );
				
				// Get the name of the shape if possible
				try
				{
					String name = getTagValue( "id", currChar, endChar );
					
					o.setName( name );
				}
				catch( Exception e ) {}
				
				// Set the rotation of the shape if possible
				try
				{
					double rotation = getRotation( currChar, endChar );
					
					o.setRotation( Math.toRadians( rotation ) );
				}
				catch( Exception e ) {}
				
				shapes.add( o );
			}
			catch( Exception e )
			{
				// if an error exists (like user changes a tag's value from "123" to " 123"
				// or there is no value for a required tag), then forget about that particular
				// shape and try importing other shapes!!!
				
				currChar = code.indexOf( "<ellipse", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<ellipse", currChar + 5 );
		}
	}
	
	private void getPaths( int startIndex, int stopIndex )
	{
		int currChar, endChar;

		String points;
		
		ArrayList<Double> x, y;
		
		x = new ArrayList<Double>();
		y = new ArrayList<Double>();
		
		currChar = code.indexOf( "<path", startIndex );

		while( currChar != -1 && currChar <= stopIndex )
		{
			endChar = code.indexOf( "/>", currChar );
			
			points = getTagValue( "d", currChar );
			
			// Check if the path is convertable to this program's shapes
			boolean isLegit = true;
			
			for( int i = 0; i < points.length(); i++ )
			{
				char c = points.charAt( i );
				
				if( c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9' && c != ' ' && c != ',' && c != '.' && c != 'm' && c != 'l' && c != 'M' && c != 'L' && c != 'z' && c != 'Z' && c != '-' )
				{
					isLegit = false;
					break;
				}
			}
	
			if( !isLegit )
			{
				System.out.println( "not legit!");
				currChar = code.indexOf( "<path", currChar + 5 );
				
				continue;
			}
			
			points.replace( 'M', 'm' );
			points.replace( 'Z', 'z' );
			
			points = points + "m";
			
			int temp = points.indexOf( 'm' );
			while( temp != -1 )
			{
				points = points.substring( 0, temp ) + " " + points.substring( temp );
				temp = points.indexOf( 'm', temp + 2 );
			}
			
			temp = points.indexOf( 'l' );
			while( temp != -1 )
			{
				points = points.substring( 0, temp ) + " " + points.substring( temp );
				temp = points.indexOf( 'l', temp + 2 );
			}
			
			temp = points.indexOf( 'L' );
			while( temp != -1 )
			{
				points = points.substring( 0, temp ) + " " + points.substring( temp );
				temp = points.indexOf( 'L', temp + 2 );
			}
			
			temp = points.indexOf( 'z' );
			while( temp != -1 )
			{
				points = points.substring( 0, temp ) + " " + points.substring( temp );
				temp = points.indexOf( 'z', temp + 2 );
			}
			
			///*****
			System.out.println( points );
			///*****
			
			try
			{
				int currIndex, currCommaIndex, nextSpace;

				currIndex = 0;
				
				while( currIndex != -1 )
				{		
					while( points.charAt( currIndex ) != 'm' && points.charAt( currIndex ) != 'l' && points.charAt( currIndex ) != 'L' && points.charAt( currIndex ) != 'z' )
						currIndex++;
					
					if( currIndex == points.length() )
						break;
					
					// System.out.print( currIndex + "-" );
					
					if( points.charAt( currIndex ) == 'z' )
					{
						x.add( x.get( 0 ) );
						y.add( y.get( 0 ) );
						
						/*if( x.size() > 1 && y.size() > 1 )
						{
							if( x.get( 0 ) != x.get( x.size() - 1 ) || y.get( 0 ) != y.get( y.size() - 1 ) )
							{
								x.add( x.get( 0 ) );
								y.add( y.get( 0 ) );
							}
								
							SPGroup vertices = new SPGroup();
							
							for( int i = 0; i < x.size(); i++ )
							{
								vertices.add( new SPPoint( x.get( i ), y.get( i ) ) );
							}
							
							SPPolygon o = new SPPolygon( vertices );
							
							// Get the name of the shape if possible
							try
							{
								String name = getTagValue( "id", currChar, endChar );
								
								o.setName( name );
							}
							catch( Exception e ) {}
							
							// Set the rotation of the shape if possible
							try
							{
								double rotation = getRotation( currChar, endChar );
								
								o.setRotation( Math.toRadians( rotation ) );
							}
							catch( Exception e ) {}
							
							shapes.add( o );
							
							tester.addShape( (SketchpadObject) o );
						}
						
						x = new ArrayList<Double>();
						y = new ArrayList<Double>();*/
						
					}
					else if( points.charAt( currIndex ) == 'm' )
					{
						// If we had a shape so far
						if( x.size() > 1 && y.size() > 1 && x.size() == y.size() )
						{								
							SPGroup vertices = new SPGroup();
							
							for( int i = 0; i < x.size(); i++ )
							{
								vertices.add( new SPPoint( x.get( i ), y.get( i ) ) );
							}
							
							if( x.get( 0 ) == x.get( x.size() - 1 ) || y.get( 0 ) == y.get( y.size() - 1 ) )
							{
								SPPolygon o = new SPPolygon( vertices );
								
								// Get the name of the shape if possible
								try
								{
									String name = getTagValue( "id", currChar, endChar );
									
									o.setName( name );
								}
								catch( Exception e ) {}
								
								// Set the rotation of the shape if possible
								try
								{
									double rotation = getRotation( currChar, endChar );
									
									o.setRotation( Math.toRadians( rotation ) );
								}
								catch( Exception e ) {}
								
								shapes.add( o );
							}
							else
							{
								SPPolyLine o = new SPPolyLine( vertices );
								
								// Get the name of the shape if possible
								try
								{
									String name = getTagValue( "id", currChar, endChar );
									
									o.setName( name );
								}
								catch( Exception e ) {}
								
								// Set the rotation of the shape if possible
								try
								{
									double rotation = getRotation( currChar, endChar );
									
									o.setRotation( Math.toRadians( rotation ) );
								}
								catch( Exception e ) {}
								
								shapes.add( o );
							}
						}

						x = new ArrayList<Double>();
						y = new ArrayList<Double>();
						
						currCommaIndex = Math.min( points.indexOf( " ", currIndex ), points.indexOf( ",", currIndex ) );
						nextSpace = points.indexOf( " ", currCommaIndex + 1 );
						
						x.add( Double.parseDouble( points.substring( currIndex + 1, currCommaIndex ) ) );
						y.add( Double.parseDouble( points.substring( currCommaIndex + 1, nextSpace ) ) );
						
					}
					else if( points.charAt( currIndex ) == 'l' )
					{
						currCommaIndex = Math.min( points.indexOf( " ", currIndex ), points.indexOf( ",", currIndex ) );
						nextSpace = points.indexOf( " ", currCommaIndex + 1 );
						
						// add new point relative to the last one's coordinates
						x.add( x.get( x.size() - 1 ) + Double.parseDouble( points.substring( currIndex + 1, currCommaIndex ) ) );
						y.add( y.get( y.size() - 1 ) + Double.parseDouble( points.substring( currCommaIndex + 1, nextSpace ) ) );
					}
					else
					{
						currCommaIndex = Math.min( points.indexOf( " ", currIndex ), points.indexOf( ",", currIndex ) );
						nextSpace = points.indexOf( " ", currCommaIndex + 1 );
						
						// add new point's coordinates directly
						x.add( Double.parseDouble( points.substring( currIndex + 1, currCommaIndex ) ) );
						y.add( Double.parseDouble( points.substring( currCommaIndex + 1, nextSpace ) ) );
					}
					
					currIndex++;
				}
			}
			catch( Exception e )
			{
				currChar = code.indexOf( "<path", currChar + 5 );
				
				continue;
			}
			
			currChar = code.indexOf( "<path", currChar + 5 );
		}
	}
	
	
	// Returns the value of the first tag starting after fromIndex as String
	private String getTagValue( String tag, int fromIndex )
	{
		int loc1, loc2;
		
		// find x1 coordinate
		loc1 = code.indexOf( " " + tag + "=", fromIndex );
		
		// find the opening quotation marks
		loc1 = code.indexOf( "\"", loc1 );
		
		// find the closing quotation marks
		loc2 = code.indexOf( "\"", loc1 + 1 );
		
		try
		{
			return code.substring( loc1 + 1, loc2 );
		}
		catch( Exception e )
		{
			throw e;
		}
	}
	
	// Returns the value of the first tag starting after fromIndex as String
	private String getTagValue( String tag, int fromIndex, int toIndex )
	{
		int loc1, loc2;
		
		// find x1 coordinate
		loc1 = code.indexOf( " " + tag + "=", fromIndex );
		
		// find the opening quotation marks
		loc1 = code.indexOf( "\"", loc1 );
		
		// find the closing quotation marks
		loc2 = code.indexOf( "\"", loc1 + 1 );
		
		try
		{
			if( toIndex < fromIndex || loc1 < fromIndex || loc1 > toIndex || loc2 < fromIndex || loc2 > toIndex )
				System.out.println( ( 1 / 0 ) ); // Go to catch statement xD
				
			return code.substring( loc1 + 1, loc2 );
		}
		catch( Exception e )
		{
			throw e;
		}
	}
	
	
	private double getRotation( int fromIndex, int toIndex )
	{
		int loc1, loc2, loc3;
		
		loc1 = code.indexOf( "rotate(", fromIndex );
		
		// find the closing quotation marks
		loc2 = code.indexOf( " ", loc1 + 1 );
		loc3 = code.indexOf( ")", loc1 + 1 );
		
		if( loc3 < loc2 )
			loc2 = loc3;
		
		try
		{
			if( toIndex < fromIndex || loc1 < fromIndex || loc1 > toIndex || loc2 < fromIndex || loc2 > toIndex )
				System.out.println( ( 1 / 0 ) ); // Go to catch statement xD
			
			return Double.parseDouble( code.substring( loc1 + 7, loc2 ) );
		}
		catch( Exception e )
		{
			throw e;
		}
	}
	
	
	
	public static void main( String[] args )
	{
		SVGImporter s = new SVGImporter( "out_1367926259676.svg", SVGImporter.PATH );
		//SVGImporter s = new SVGImporter( "googlesvg.svg", SVGImporter.PATH );
		
		ShapeCanvas tester = new ShapeCanvas();
		JFrame frame = new JFrame("MAIN TESTER");
		frame.add(tester);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		tester.addShape( s.getShapes() );
	}
}
