package uk.co.gossfunkel.citadel.level;

import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.level.tile.*;

public class Level {
	
	// -------------------- variables -----------------------------------------

	protected static int width;
	protected static int height;
	protected static int[] tiles;
	protected static Tile[] ttiles;
	protected static List<Entity> entities = new ArrayList<Entity>();
	
	// -------------------- constructors --------------------------------------
	
	public Level(int width, int height) {
		
		Level.width = width;
		Level.height = height;
		tiles = new int[width*height];
		
		generateLevel();
		populate();
		
	}
	
	public Level(String path) {
		loadLevel(path);
		generateLevel();
		populate();
	}
	
	// -------------------- methods -------------------------------------------
	
	// randomly generate a level
	protected void generateLevel() {}
	
	// load a level from file
	protected void loadLevel(String path) {}
	
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		for (int i = 0; i < ttiles.length; i++) {
			ttiles[i].update();
		}
	}
	
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		// cornerpins
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.getWidth() + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.getHeight() + 16) >> 4;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				if (x < 0 || y < 0 || x >= width || y >= height) 
					Tile.voidTile.render(x, y, screen);
				else
					//getTile(x, y).render(x, y, screen);
					ttiles[x+y*width].render(x, y, screen);
			} // end x for
		} // end y for} 
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
	}
	
	/* translate colours into tile values
	 * grass  = 00FF00
	 * flower = FFFF00
	 * rock   = 7F7F00
	 */
	public Tile getTile(int x, int y) {
		switch (tiles[x+y*width]) {
			case 0xFF00FF00: return Tile.grass;
			case 0xFFFFFF00: return Tile.flower;
			case 0xFF7F7F00: return Tile.rock;
			case 0xFF0000FF: return Tile.water;
			default: return Tile.voidTile;
		}
	}
	
	protected void populate() {
		ttiles = new Tile[tiles.length];
		for(int i = 0; i < tiles.length; i++) {
			switch (tiles[i]) {
			case 0xFF00FF00: ttiles[i] = Tile.grass; break;
			case 0xFFFFFF00: ttiles[i] = Tile.flower; break;
			case 0xFF7F7F00: ttiles[i] = Tile.rock; break;
			case 0xFF0000FF: ttiles[i] = Tile.water; break;
			default: ttiles[i] = Tile.voidTile;
			}
		}
	}
	
	public static Tile[] findTouching(int x, int y, int size) {
		int x1 = TileCoordinate.round(x) >> 4;
		int y1 = TileCoordinate.round(y) >> 4;
		return new Tile[]{ttiles[x1+y1*width], ttiles[(x1+1)+y1*width], 
				ttiles[x1+(y1+1)*width], ttiles[(x1+1)+(y1+1)*width]};
	}
	
	public static boolean collides(Entity a, Entity b) {
		if (a.x() < b.x()) {
			if (a.y() < b.y()) {
				if (((a.x() + a.getSIZE()) >= b.x()) 
						&& ((a.y() + a.getSIZE()) >= b.y())) return true;
				else return false;
			} else if (a.y() == b.y()){
				if (a.x() + a.getSIZE() > b.x()) return true;
				else return false;
			} else {
				if ((b.y() + b.getSIZE() > a.y()) 
						&& (a.x() + a.getSIZE() > b.x())) return true;
				else return false;
			}
		} else if (a.x() == b.x()) {
			if (a.y() == b.y()) return true;
			else if (a.y() > b.y()) {
				if ((b.y() + b.getSIZE()) >= a.y()) return true;
				else return false;
			} else {
				if ((a.y() + a.getSIZE()) >= b.y()) return true;
				else return false;
			}
		} else {
			if (a.y() < b.y()) {
				if (((b.x() + b.getSIZE()) >= a.x()) 
						&& ((a.y() + a.getSIZE()) >= b.y())) return true;
				else return false;
			} else if (a.y() == b.y()){
				if (b.x() + b.getSIZE() > a.x()) return true;
				else return false;
			} else {
				if ((b.y() + b.getSIZE() > a.y()) 
						&& (b.x() + b.getSIZE() > a.x())) return true;
				else return false;
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public Tile findTileAt(int a, int b) {
		a = TileCoordinate.scale(a);
		b = TileCoordinate.scale(b);
		return getTile(a, b);
	}

}
