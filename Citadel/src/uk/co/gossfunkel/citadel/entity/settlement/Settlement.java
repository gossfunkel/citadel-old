package uk.co.gossfunkel.citadel.entity.settlement;

import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Tile;

public class Settlement extends Entity {
	
	protected Tile sprite;
	protected int level;
	protected int xp;

	public Settlement(int x, int y) {
		System.out.println("Making new Hamlet\nx:" + x + ", y:" + y);
		this.x = TileCoordinate.round(x);
		this.y = TileCoordinate.round(y);
		sprite = Tile.hamlet;
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void render(Screen screen) {
		screen.renderSettlement(x, y, sprite.sprite);
	}
	
	@Override
	public void update() {
		xp++;
		if (xp > 5000) {
			levelUp();
		}
	}
	
	public void levelUp() {
		level++;
		if (level%10==0) {
			if (level == 10) {
				sprite = Tile.village;
			} else if (level > 29) {
				sprite = Tile.city;
			}
		}
	}
}
