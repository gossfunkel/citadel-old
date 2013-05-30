package uk.co.gossfunkel.citadel.graphics;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javax.swing.JFrame;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.level.tile.Tile;

public class Screen {
	
	// -------------------- variables -----------------------------------------
	
	private int width, height;
	public int[] pixels;
	
	public int xOffset, yOffset;

	// for renderPlayer
	// absolute position
	int xa, ya, xs, ys, xp, yp;
	double zoom;
	int tileSize;
	int col;

	// -------------------- constructors --------------------------------------
	
	public Screen(int width, int height) {
		
		this.width = width;
		this.height = height;

		zoom = Game.scale(); 
		pixels = new int[(width*height/(int)zoom)];
		
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
		xp -= xOffset;
		yp -= yOffset;
		
		//zoom = Game.scale(); 
		
		//pixels = new int[(width*height/(int)zoom)];
		
		int tileSize = tile.sprite.getSIZE();
		
		for (int y = 0; y < tileSize; y++ /*= zoom*/) {
			ya = y + yp; 
			for (int x = 0; x < tileSize; x++/*= zoom*/) {
				xa = x + xp;
				if (xa < -tileSize || xa >= width ||
						    ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				if (tile.sprite.pixels[x+y*tileSize] != 0xffff00ff)
					pixels[xa + ya*width] = tile.sprite.pixels[x+y*tileSize];
			} // end x for
		} // end y for
	} // end renderTile
	
	/* render individual tiles
	 * 
	 */
	public void renderSprite(int xp, int yp, Sprite sprite) {
		// absolute position
		int ya, xa;
		xp -= xOffset;
		yp -= yOffset;
		
		int spriteSize = sprite.getSIZE();
		
		for (int y = 0; y < spriteSize; y++) {
			ya = y + yp; 
			for (int x = 0; x < spriteSize; x++) {
				xa = x + xp;
				if (xa < -spriteSize || xa >= width ||
						    ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				if (sprite.pixels[x+y*spriteSize] != 0xffff00ff)
					pixels[xa + ya*width] = sprite.pixels[x+y*spriteSize];
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
				
		for (float y = 0; y < tileSize; y += .5) {
			ya = (int) (Math.floor(y) + yp); 
			ys = (int) Math.floor(y);
			for (float x = 0; x < tileSize; x += .5) {
				xa = (int) (Math.floor(x) + xp);
				if (reflect) xs = (int) ((tileSize-1) - x);
				else xs = (int) Math.floor(x);
				if (xa < -tileSize/2 || xa >= width ||
						    ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				col = sprite.pixels[xs+ys*(tileSize)];
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
	
	
	public void renderSysMenu() {
		for (int x = (width-100), ix = 0; x < width; x++, ix++) {
			for (int y = 0; y < 20; y++) {
				col = Sprite.sysMenu.pixels[ix+y*100];
				if (col != 0xffff00ff) pixels[x+y*width] = col;
			}
		}
	}
	// -------------------- getters -------------------------------------------
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	// -------------------- setters -------------------------------------------
	
	public void goFullScreen(DisplayMode dm, JFrame window) {
		window.setUndecorated(true);
		GraphicsDevice vc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		vc.setFullScreenWindow(window);
		if (dm != null && vc.isDisplayChangeSupported()) {
			try {
				vc.setDisplayMode(dm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void restoreScreen(JFrame window) {
		window.setUndecorated(false);
		GraphicsDevice vc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Window w = vc.getFullScreenWindow();
		if (w != null) {
			w.dispose();
		}
		vc.setFullScreenWindow(null);
	}
	
}