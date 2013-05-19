package uk.co.gossfunkel.citadel.entity.mob;

import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.entity.Entity;

public abstract class Mob extends Entity {
	
	// -------------------- variables -----------------------------------------
	
	protected Sprite sprite;
	protected int dir = 0;
	protected boolean moving = false;
	protected Tile[] tile;
	
	// -------------------- methods -------------------------------------------
	
	public void update() {}
	
	public void render() {}

	public void move(int xa, int ya) {
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
		for (int c = 0; c < 4; c++) {
			//                  num * scale + offset
			int xt = ((x+xa)+ c % 2 * 6 + 4) >> 4;
			int yt = ((y+ya)+ c / 2 * 7 + 4) >> 4;
			if (level.getTile(xt, yt).solid()) solid = true;
		}
		return solid;
	}

	// -------------------- getters -------------------------------------------
	
	
}
