package uk.co.gossfunkel.citadel.entity.exporbs;

import java.awt.Rectangle;
import java.util.Random;

import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public abstract class ExpOrb extends Entity {
	
	protected int dx, dy;
	protected Sprite sprite;
	protected int value;
	protected Random random;
	
	public ExpOrb() {
		random = new Random();
	}
	
	public int getVal() {
		return value;
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, 10, 10);
	}

}
