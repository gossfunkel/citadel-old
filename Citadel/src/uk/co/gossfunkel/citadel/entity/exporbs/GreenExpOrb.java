package uk.co.gossfunkel.citadel.entity.exporbs;

import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.level.Level;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Coord;

public class GreenExpOrb extends ExpOrb {
	
	public GreenExpOrb(Coord coord, Level level, int i) {
		super();
		x = TileCoordinate.reverseScale(coord.x());
		y = TileCoordinate.reverseScale(coord.y());
		dx = x+random.nextInt(20);
		dy = y+random.nextInt(20);
		this.level = level;
		sprite = Sprite.greenExpOrb;
		value = i;
	}
	
	public GreenExpOrb(Coord coord, Level level) {
		this(coord, level, 10);
	}
	
	public GreenExpOrb(int x, int y, Level level, int i) {
		super();
		this.x = x;
		this.y = y;
		dx = x+random.nextInt(20);
		dy = y+random.nextInt(20);
		this.level = level;
		sprite = Sprite.greenExpOrb;
		value = i;
	}
	
	public GreenExpOrb(int x, int y, Level level) {
		this(x, y, level, 10);
	}
	
	@Override
	public void update() {
		if (x > dx) {
			x--;
		} else if (x < dx) {
			x++;
		}
		if (y > dy) {
			y--;
		} else if (y < dy) {
			y++;
		}
	}

	public void render(Screen screen) {
		screen.renderSprite(x, y, sprite);
	}

}
