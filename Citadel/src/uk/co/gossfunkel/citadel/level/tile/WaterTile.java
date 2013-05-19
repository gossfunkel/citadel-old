package uk.co.gossfunkel.citadel.level.tile;

import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public class WaterTile extends Tile {
	
	private int i = 0;

	// -------------------- constructors --------------------------------------
	
	public WaterTile(Sprite sprite) {
		super(sprite);
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void update() {
		i++;
		if (i == 10000) {
			sprite = Sprite.water2;
		} else if (i == 20000) {
			sprite = Sprite.water4;
		} else if (i == 30000) {
			sprite = Sprite.water3;
		} else if (i > 40000) {
			sprite = Sprite.water1;
			i = 0;
		}
	}
	
	@Override
	public void render(int x, int y, Screen screen) { 
		screen.renderTile(x << 4, y << 4, this);
	}

}
