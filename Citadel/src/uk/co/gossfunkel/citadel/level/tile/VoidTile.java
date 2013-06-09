package uk.co.gossfunkel.citadel.level.tile;

import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public class VoidTile extends Tile {
	
	// -------------------- constructors --------------------------------------
	
	public VoidTile(Sprite sprite) {
		super(sprite);
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void render(int x, int y, Screen screen) {
		screen.renderTile(x << 5, y << 5, this);
	}

}
