package uk.co.gossfunkel.citadel.entity.mob;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.entity.projectile.Fireball;
import uk.co.gossfunkel.citadel.entity.projectile.Projectile;
import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.level.tile.Tile;

public abstract class Mob extends Entity {
	
	// -------------------- variables -----------------------------------------
	
	protected Sprite sprite;
	protected int dir = 0;
	protected boolean moving = false;
	protected boolean swimming = false;
	protected Tile[] tile;
	
	protected List<Projectile> projectiles = new ArrayList<Projectile>();
	
	// -------------------- methods -------------------------------------------
	
	public void update() {}
	
	public void render() {}

	protected void move(int xa, int ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return;
		}
		if (xa > 0) dir = 1;
		if (xa < 0) dir = 3;
		if (ya > 0) dir = 0;
		if (ya < 0) dir = 2;
		
		if (!collision(xa, ya)) {
			x += xa;
			y += ya;
		}
	}

	private boolean collision(int xa, int ya) {
		boolean solid = false;
		for (int c = 0; c < 8; c++) {
			//                  num * scale + offset
			int xt = ((x+xa)+ c % 2 * 14 + 8) >> 4;
			int yt = ((y+ya)+ c / 2 * 6 + 4) >> 4;
			if (level.getTile(xt, yt).solid()) solid = true;
			for (int i = 0; i < level.treeLength(); i++) {
				Rectangle rect = level.getTree(i).getRect();
				if (rect.contains(xt, yt)) solid = true;
			}
		}
		return solid;
	}
	
	protected void shoot(int x, int y, double d) {
		Projectile p = new Fireball(x + 6, y + 6, d, level);
		projectiles.add(p);
		level.addEntity(p);
	}

	// -------------------- getters -------------------------------------------
	
	
}
