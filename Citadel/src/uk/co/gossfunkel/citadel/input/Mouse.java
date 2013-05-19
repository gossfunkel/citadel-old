package uk.co.gossfunkel.citadel.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
	
	// custom
	
	private static int mousex = -1;
	private static int mousey = -1;
	private static int mouseb = -1;
	
	public static int x() {
		return mousex;
	}
	
	public static int y() {
		return mousey;
	}
	
	public static int b() {
		return mouseb;
	}
	
	// predefined

	@Override
	public void mouseDragged(MouseEvent e) {
		mousex = e.getX();
		mousex = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// update
		mousex = e.getX();
		mousey = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseb = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseb = -1;
	}

}
