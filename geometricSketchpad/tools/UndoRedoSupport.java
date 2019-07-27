package geometricSketchpad.tools;

import geometricSketchpad.gui.MainCanvas;
import geometricSketchpad.shapes.*;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Stack;

public class UndoRedoSupport implements PropertyChangeListener {
	MainCanvas mc;
	Stack<PropertyChangeEvent> undo;
	Stack<PropertyChangeEvent> redo;
	boolean listening;
	
	public UndoRedoSupport (MainCanvas mc) {
		this.mc = mc;
		undo = new Stack<PropertyChangeEvent>();
		redo = new Stack<PropertyChangeEvent>();
		listening = true;
	}
	
	public void undo() {
		if (undo.isEmpty()) { return; }
		listening = false;
		
		
		PropertyChangeEvent unEvt = undo.pop();
		Object source = unEvt.getSource();
		String propName = unEvt.getPropertyName();
		System.out.println(propName);
		System.out.println(undo);
		if (source instanceof SPGroup && unEvt instanceof IndexedPropertyChangeEvent) {
			if (propName.equals("element.add")) {
				((SPGroup)source).remove(((IndexedPropertyChangeEvent) unEvt).getIndex());
				if (undo.peek().getPropertyName().equals("parentGroup")) {
					undo();
				}
			} else if (propName.equals("element.remove")) {
				((SPGroup)source).add(((IndexedPropertyChangeEvent) unEvt).getIndex(), (SketchpadObject) unEvt.getOldValue());
			} else if (propName.equals("element.replace")) {
				((SPGroup)source).set(((IndexedPropertyChangeEvent) unEvt).getIndex(), (SketchpadObject) unEvt.getOldValue());
			}
		}
		// if (propName.equals("parentGroup")) { if(undo.peek().getPropertyName().equals("element.add")) undo();}
		if (propName.equals("parentGroup") && unEvt.getNewValue() != mc.getShapes() && unEvt.getOldValue() != mc.getShapes()) {
			if (unEvt.getOldValue() != null) {((SPGroup) unEvt.getOldValue()).remove(source);}
			((SPGroup) unEvt.getNewValue()).add((SketchpadObject) source);
		} else if (unEvt.getNewValue() == mc.getShapes() || unEvt.getOldValue() != mc.getShapes()) {
		 //	undo();
		} else if (propName.equals("location")) {
			((SketchpadObject) source).setLocation((SPPoint) unEvt.getOldValue());
		} else if (propName.equals("name")) {
			((SketchpadObject) source).setName((String) unEvt.getOldValue());
		}  else if (propName.equals("rotation")) {
			((SketchpadObject) source).setRotation((Number) unEvt.getOldValue());
		} else if (propName.equals("selected")) {
			((SketchpadObject) source).toggleSelect((boolean) unEvt.getOldValue());
		}
		
		redo.push(unEvt);
		listening = true;
		mc.repaint();
	}
	
	public void redo() {
		if (redo.isEmpty()) { return; }
		listening = false;
		
		PropertyChangeEvent reEvt = redo.pop();
		Object source = reEvt.getSource();
		String propName = reEvt.getPropertyName();
		System.out.println(propName);
		if (source instanceof SPGroup && reEvt instanceof IndexedPropertyChangeEvent) {
			if (propName.equals("element.add")) {
				((SPGroup)source).add(((IndexedPropertyChangeEvent) reEvt).getIndex(), (SketchpadObject) reEvt.getOldValue());
			} else if (propName.equals("element.remove")) {
				((SPGroup)source).remove(((IndexedPropertyChangeEvent) reEvt).getIndex());
			} else if (propName.equals("element.replace")) {
				((SPGroup)source).set(((IndexedPropertyChangeEvent) reEvt).getIndex(), (SketchpadObject) reEvt.getNewValue());
			}
		}
		// if (propName.equals("parentGroup")) {if(redo.peek().getPropertyName().equals("element.add")) redo();}
		if (propName.equals("parentGroup") && reEvt.getNewValue() != mc.getShapes() && reEvt.getOldValue() != mc.getShapes()) {
			((SPGroup) reEvt.getNewValue()).remove(source);
			((SPGroup) reEvt.getOldValue()).add((SketchpadObject) source);
		} else if (reEvt.getNewValue() == mc.getShapes() || reEvt.getOldValue() != mc.getShapes()) {
			// redo();
		} else if(propName.equals("location")) {
			((SketchpadObject) source).setLocation((SPPoint) reEvt.getNewValue());
		} else if (propName.equals("name")) {
			((SketchpadObject) source).setName((String) reEvt.getNewValue());
		}  else if (propName.equals("rotation")) {
			((SketchpadObject) source).setRotation((Number) reEvt.getNewValue());
		} else if (propName.equals("selected")) {
			((SketchpadObject) source).toggleSelect((boolean) reEvt.getNewValue());
		} 
		
		undo.push(reEvt);
		listening = true;
		mc.repaint();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		SVGImporter.isSaved = false;
		if (!listening) { return ; }
		listening = false;
		
		if (!redo.empty()) {
			redo.clear();
		}
		
		if (!undo.isEmpty()){
			PropertyChangeEvent tmp = undo.pop();
		
			if(tmp.getSource() != evt.getSource() || tmp.getPropertyName() == null || !(tmp.getPropertyName().equals(evt.getPropertyName()))) {
				undo.push(tmp);
			}
		}
		
		undo.push(evt);
		listening = true;
	}
	/*
	public static void main (String[] args) {
		//UndoRedoSupport urs = new UndoRedoSupport();
		// SPGroup shapes = new SPGroup();
		SPEllipse ellipse = new SPEllipse(300,500,30,50);
		SPLine line = new SPLine (new SPPoint (30,50), -2);
		ellipse.addPropertyChangeListener(urs);
		line.addPropertyChangeListener(urs);
		
		System.out.println("BEGINNING");
		System.out.println(ellipse);
		System.out.println(line);
		ellipse.setLocation(724,812);
		ellipse.setRotation(32);
		line.setLocation(86, 92);
		System.out.println("MODIFIED");
		System.out.println(ellipse);
		System.out.println(line);
		
		urs.undo();
		urs.undo();
		urs.undo();
		System.out.println("URS.UNDO x 3");
		System.out.println(ellipse);
		System.out.println(line);
		
		urs.redo();
		System.out.println("URS.REDO");
		System.out.println(ellipse);
		System.out.println(line);
		
		urs.redo();
		System.out.println("URS.REDO");
		System.out.println(ellipse);
		System.out.println(line);
		
		urs.redo();
		System.out.println("URS.REDO");
		System.out.println(ellipse);
		System.out.println(line);
	}*/
}
