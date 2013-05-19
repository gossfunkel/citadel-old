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
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// update
		mousex = e.getX();
		mousey = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseb = e.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		mouseb = -1;
	}

}
