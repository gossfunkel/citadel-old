package uk.co.gossfunkel.citadel.level;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.level.tile.Coord;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.entity.Tree;
import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;

public class Level {
	
	// -------------------- variables -----------------------------------------

	protected static int width;
	protected static int height;
	protected static int[] tiles;
	protected static Tile[] ttiles;
	protected static List<Entity> entities = new ArrayList<Entity>();
	protected static List<Tree> trees = new ArrayList<Tree>();
	
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
			if (entities.get(i).isRemoved()) entities.remove(i);
			else entities.get(i).update();
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
	
	public void drawPanels(Screen screen) {
		for (int i = 0; i < trees.size(); i++) {
			trees.get(i).render(trees.get(i).getCoord(), screen, this);
		}
	}
	
	public Integer[] getCoordXs() {
		List<Integer> coord = new ArrayList<Integer>(trees.size());
		for (int i = 0; i < coord.size(); i++) {
			coord.add(trees.get(i).getCoord().x());
		}
		return coord.toArray(new Integer[coord.size()]);
	}
	
	public Integer[] getCoordYs() {
		List<Integer> coord = new ArrayList<Integer>(trees.size());
		for (int i = 0; i < coord.size(); i++) {
			coord.add(trees.get(i).getCoord().y());
		}
		return coord.toArray(new Integer[coord.size()]);
	}
	
	public Coord[] getCoords() {
		List<Coord> coord = new ArrayList<Coord>(trees.size());
		for (int i = 0; i < coord.size(); i++) {
			coord.add(trees.get(i).getCoord());
		}
		return coord.toArray(new Coord[coord.size()]);
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
			case 0xFF700000: return Tile.tree;
			default: return Tile.voidTile;
		}
	}
	
	protected void populate() {
		ttiles = new Tile[tiles.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				switch (tiles[x+y*width]) {
				case 0xFFFFFF00: ttiles[x+y*width] = Tile.flower; break;
				case 0xFF7F7F00: ttiles[x+y*width] = Tile.rock; break;
				case 0xFF0000FF: ttiles[x+y*width] = Tile.water; break;
				case 0xFF700000: trees.add(new Tree(new Coord(x, y), 
											new Rectangle(x, y+1, 2, 1)));
				case 0xFF00FF00: ttiles[x+y*width] = Tile.grass; break;
				default: ttiles[x+y*width] = Tile.voidTile;
				}
			}
		}
	}
	
	public static Tile[] findTouching(int x, int y, int size) {
		int x1 = TileCoordinate.scale(x);
		int y1 = TileCoordinate.scale(y);
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
	
	public Tree getTree(int i) {
		return trees.get(i);
	}
	
	public Tree getTree(Coord key) {
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getCoord().equals(key))
				return trees.get(i);
		}
		return null;
	}
	
	public Tree getTree(Rectangle rect) {
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getRect().equals(rect))
				return trees.get(i);
		}
		return null;
	}
	
	public Tree[] getTrees() {
		Tree[] treeArray = new Tree[trees.size()];
		for (int i = 0; i < trees.size(); i++) {
			treeArray[i] = trees.get(i);
		}
		return treeArray;
	}
	
	public Rectangle[] getTreeRects() {
		Rectangle[] treeArray = new Rectangle[trees.size()];
		for (int i = 0; i < trees.size(); i++) {
			treeArray[i] = trees.get(i).getRect();
		}
		return treeArray;
	}
	
	public int treeLength() {
		return trees.size();
	}
	
	public Tile findTileAt(int a, int b) {
		a = TileCoordinate.scale(a);
		b = TileCoordinate.scale(b);
		return getTile(a, b);
	}
	
	public void removePanel(Coord key) {
		trees.remove(getTree(key));
	}

	public void removeOnlinePlayer(String username) {
		int i = 0;
		for (Entity e : entities) {
			if (e instanceof OnlinePlayer && ((OnlinePlayer)e).username().equals(username)) {
				break;
			}
			i++;
		}
		entities.remove(i);
	}

}
