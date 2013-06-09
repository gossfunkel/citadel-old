package uk.co.gossfunkel.citadel.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
	
	// -------------------- variables -----------------------------------------
	
	private boolean[] keys = new boolean[600];
	public boolean up, down, left, right, plus, minus, enter, esc;
	public boolean space, comma, fullStop, shift;
	public boolean a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, 
		u, v, w, x, y, z;
	public char lastEntered = 't';
	
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
		esc = keys[KeyEvent.VK_ESCAPE];
		up = keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_RIGHT];
		plus  = keys[KeyEvent.VK_PLUS] || keys[KeyEvent.VK_EQUALS];
		minus = keys[KeyEvent.VK_MINUS];
		enter = keys[KeyEvent.VK_ENTER];
		space = keys[KeyEvent.VK_SPACE];
		comma = keys[KeyEvent.VK_COMMA];
		fullStop = keys[KeyEvent.VK_PERIOD];
		shift = keys[KeyEvent.VK_SHIFT];
		a = keys[KeyEvent.VK_A];
		b = keys[KeyEvent.VK_B];
		c = keys[KeyEvent.VK_C];
		d = keys[KeyEvent.VK_D];
		e = keys[KeyEvent.VK_E];
		f = keys[KeyEvent.VK_F];
		g = keys[KeyEvent.VK_G];
		h = keys[KeyEvent.VK_H];
		i = keys[KeyEvent.VK_I];
		j = keys[KeyEvent.VK_J];
		k = keys[KeyEvent.VK_K];
		l = keys[KeyEvent.VK_L];
		m = keys[KeyEvent.VK_M];
		n = keys[KeyEvent.VK_N];
		o = keys[KeyEvent.VK_O];
		p = keys[KeyEvent.VK_P];
		q = keys[KeyEvent.VK_Q];
		r = keys[KeyEvent.VK_R];
		s = keys[KeyEvent.VK_S];
		t = keys[KeyEvent.VK_T];
		u = keys[KeyEvent.VK_U];
		v = keys[KeyEvent.VK_V];
		w = keys[KeyEvent.VK_W];
		x = keys[KeyEvent.VK_X];
		y = keys[KeyEvent.VK_Y];
		z = keys[KeyEvent.VK_Z];
	}
	
	public char entered() {
		char enteredString = (char)0;
		if (a) enteredString = 'a';
		if (b) enteredString = 'b';
		if (c) enteredString = 'c';
		if (d) enteredString = 'd';
		if (e) enteredString = 'e';
		if (f) enteredString = 'f';
		if (g) enteredString = 'g';
		if (h) enteredString = 'h';
		if (i) enteredString = 'i';
		if (j) enteredString = 'j';
		if (k) enteredString = 'k';
		if (l) enteredString = 'l';
		if (m) enteredString = 'm';
		if (n) enteredString = 'n';
		if (o) enteredString = 'o';
		if (p) enteredString = 'p';
		if (q) enteredString = 'q';
		if (r) enteredString = 'r';
		if (s) enteredString = 's';
		if (t) enteredString = 't';
		if (u) enteredString = 'u';
		if (v) enteredString = 'v';
		if (w) enteredString = 'w';
		if (x) enteredString = 'x';
		if (y) enteredString = 'y';
		if (z) enteredString = 'z';
		if (space) enteredString = ' ';
		if (comma) enteredString = ',';
		if (fullStop) enteredString = '.';
		
		if (shift) enteredString -= 32; //TODO stop this adding cents
		if (lastEntered == enteredString) {
			return (char)0;
		} else {
			lastEntered = enteredString;
			return enteredString;
		}
	}

}
