package uk.co.gossfunkel.citadel.entity;

import java.awt.Rectangle;

import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Coord;

public class AppleSprite extends Entity{
	
	private Sprite sprite = Sprite.apple;
	
	public AppleSprite(Coord coord) {
		x = TileCoordinate.reverseScale(coord.x());
		y = TileCoordinate.reverseScale(coord.y());
	}
	
	public void render(Screen screen) {
		screen.renderSprite(x, y, sprite);
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, 16, 16);
	}

}
