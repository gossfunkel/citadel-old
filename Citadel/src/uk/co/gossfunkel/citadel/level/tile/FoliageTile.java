package uk.co.gossfunkel.citadel.level.tile;

import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public class FoliageTile extends Tile {

	// -------------------- constructors --------------------------------------
	
	public FoliageTile(Sprite sprite) {
		super(sprite);
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 4, y << 4, this);
	}
	
	@Override
	public boolean solid() {
		return false;
	}

}