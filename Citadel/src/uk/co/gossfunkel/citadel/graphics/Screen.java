package uk.co.gossfunkel.citadel.graphics;

import uk.co.gossfunkel.citadel.level.tile.Tile;

public class Screen {
	
	// -------------------- variables -----------------------------------------
	
	private int width, height;
	public int[] pixels;
	
	public int xOffset, yOffset;

	// for renderPlayer
	// absolute position
	int xa, ya, xs, ys;
	int tileSize;
	int col;

	// -------------------- constructors --------------------------------------
	
	public Screen(int width, int height) {
		
		this.width = width;
		this.height = height;
		
		pixels = new int[width*height];
		
	}
	
	// -------------------- methods -------------------------------------------
	
	/* clear pixel array
	 * 
	 */
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	// -------------------- render methods ------------------------------------
	
	/* render individual tiles
	 * 
	 */
	public void renderTile(int xp, int yp, Tile tile) {
		// absolute position
		int ya, xa;
		xp -= xOffset;
		yp -= yOffset;
		
		int tileSize = tile.sprite.getSIZE();
		
		for (int y = 0; y < tileSize; y++) {
			ya = y + yp; 
			for (int x = 0; x < tileSize; x++) {
				xa = x + xp;
				if (xa < -tileSize || xa >= width ||
						    ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				pixels[xa + ya*width] = tile.sprite.pixels[x+y*tileSize];
			} // end x for
		} // end y for
	} // end renderTile
	
	/* render a player
	 * 
	 */
	public void renderPlayer(int xp, int yp, Sprite sprite, boolean reflect) {
		xp -= xOffset;
		yp -= yOffset;
				
		tileSize = sprite.getSIZE();
				
		for (int y = 0; y < tileSize; y++) {
			ya = y + yp; 
			ys = y;
			for (int x = 0; x < tileSize; x++) {
				xa = x + xp;
				if (reflect) {
					xs = (tileSize-1) - x;
				} else {xs = x;}
				if (xa < -tileSize || xa >= width ||
						    ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				col = sprite.pixels[xs+ys*tileSize];
				if (col != 0xffff00ff) pixels[xa + ya*width] = col;
			} // end x for
		} // end y for
	} // end renderPlayer
	
	/* render a player
	 * 
	 */
	public void renderSettlement(int xp, int yp, Sprite sprite) {
		// xp and yp are outside screen so not being rendered
		xp -= xOffset;
		yp -= yOffset;
				
		tileSize = sprite.getSIZE();
				
		for (int y = 0; y < tileSize; y++) {
			ya = y + yp; 
			ys = y;
			for (int x = 0; x < tileSize; x++) {
				xa = x + xp;
				if (xa < -tileSize || xa >= width ||
						    ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				col = sprite.pixels[x+y*tileSize];
				if (col != 0xffff00ff) pixels[xa + ya*width] = col;
			} // end x for
		} // end y for
	} // end renderSettlement
	
	public void setOffset (int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	// -------------------- getters -------------------------------------------
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}