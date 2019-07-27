package geometricSketchpad.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * @(#)StatusBar.java
 *
 *
 * @author Kaan Mert Berkem
 * @version 1.00 2013/4/30
 */


public class StatusBar extends JPanel {
    //properties
    JLabel statusLabel;

	//constructor
    public StatusBar() {
    	//The statusLabel
    	statusLabel = new JLabel("Ready...");    	  
    	setSize( 20,768);
    	setBackground( Color.WHITE);
    	Font font = new Font ( Font.SERIF, Font.PLAIN, 16);
    	statusLabel.setFont( font);
    	statusLabel.setHorizontalAlignment( SwingConstants.LEFT);

    	add(statusLabel);
    	
		//TO DO
    }
    
}