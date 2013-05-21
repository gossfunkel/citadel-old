package uk.co.gossfunkel.citadel.entity.projectile;

import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public abstract class Projectile extends Entity {
	
	protected double x, y;
	protected final double xOrigin, yOrigin;
	protected double angle;
	protected Sprite sprite;
	protected double nx, ny;
	protected double speed, damage, range;
	protected static double rate;
	
	public Projectile(double x, double y, double a) {
		xOrigin = this.x = x;
		yOrigin = this.y = y;
		angle = a;
	}
	
	@Override
	public void update() {
		move();
		if (calculateDistance() > range) {
			this.remove();
			// projectiles.remove();
		}
	}
	
	protected void move() {
		x += nx;
		y += ny;
	}
	
	private double calculateDistance() {
		return Math.sqrt(Math.abs((xOrigin - x)*(xOrigin - x) + 
									(yOrigin - y)*(yOrigin - y)));
	}

	public static double getRate() {
		return rate;
	}

}
