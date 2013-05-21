package uk.co.gossfunkel.citadel.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	
	// -------------------- variables -----------------------------------------
	
	private boolean[] keys = new boolean[600];
	public boolean up, down, left, right, b, t, plus, minus;
	
	// -------------------- override methods ----------------------------------

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	// -------------------- methods -------------------------------------------
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		plus  = keys[KeyEvent.VK_PLUS] || keys[KeyEvent.VK_EQUALS];
		minus = keys[KeyEvent.VK_MINUS];
		b = keys[KeyEvent.VK_B];
		t = keys[KeyEvent.VK_T];
	}

}
