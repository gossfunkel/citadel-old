package uk.co.gossfunkel.citadel.entity.projectile;

import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public abstract class Projectile extends Entity {
	
	protected final int xOrigin, yOrigin;
	protected double angle;
	protected Sprite sprite;
	protected double nx, ny;
	protected double speed, damage, range;
	
	public Projectile(int x, int y, double a) {
		xOrigin = this.x = x;
		yOrigin = this.y = y;
		angle = a;
	}
	
	@Override
	public void update() {
		move();
	}
	
	protected void move() {
		x += nx;
		y += ny;
	}

}
