package uk.co.gossfunkel.citadel.entity.projectile;

import java.awt.Rectangle;
import java.util.Random;
import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.level.Level;

public abstract class Projectile extends Entity {
	
	protected double x, y;
	protected final double xOrigin, yOrigin;
	protected double angle;
	protected Sprite sprite;
	protected double nx, ny;
	protected double speed, damage, range;
	protected static double rate;
	protected int dir;
	
	protected final Random random = new Random();
	
	public Projectile(double x, double y, double a, Level level) {
		xOrigin = this.x = x;
		yOrigin = this.y = y;
		angle = a;
		this.level = level;
	}
	
	@Override
	public void update() {
		move((int)nx, (int)ny);
		if (calculateDistance() > range) {
			this.remove();
			// projectiles.remove();
		}
	}
	
	protected void move(int xa, int ya) {
		if (xa > 0) dir = 1;
		if (xa < 0) dir = 3;
		if (ya > 0) dir = 0;
		if (ya < 0) dir = 2;
		
		if (collision(xa, ya)) {
			this.remove();
		} else {
			x += xa;
			y += ya;
		}
	}

	private boolean collision(int xa, int ya) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			//                  num * scale + offset
			int xt = ((((int)x)+xa)+ c % 2 * 6 + 4) >> 5;
			int yt = ((((int)y)+ya)+ c / 2 * 7 + 4) >> 5;
			if (level.getTile(xt, yt).solid()) solid = true;
			for (int i = 0; i < level.treeLength(); i++) {
				Rectangle rect = level.getTree(i).getRect();
				if (rect.contains(xt, yt)) solid = true;
			}
		}
		return solid;
	}
	
	private double calculateDistance() {
		return Math.sqrt(Math.abs((xOrigin - x)*(xOrigin - x) + 
									(yOrigin - y)*(yOrigin - y)));
	}

	public static double getRate() {
		return rate;
	}

}
