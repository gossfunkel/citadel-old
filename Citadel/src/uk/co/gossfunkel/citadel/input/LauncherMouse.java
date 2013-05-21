package uk.co.gossfunkel.citadel.input;

import java.awt.event.MouseEvent;

public class LauncherMouse extends Mouse {

	protected static int mousepx = -1;
	protected static int mousepy = -1;
	private static boolean pressed = false;
	
	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		mousepx = e.getX();
		mousepy = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
		mousepx = -1;
		mousepy = -1;
	}

	public static boolean isPressed() {
		return pressed;
	}
	
	public static int px() {
		return mousepx;
	}
	
	public static int py() {
		return mousepy;
	}

}
