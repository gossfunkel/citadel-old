package uk.co.gossfunkel.citadel.level;

import java.util.Random;

//import uk.co.gossfunkel.citadel.graphics.Screen;

public class RandomLevel extends Level {

	// -------------------- variables -----------------------------------------
	
	private static final Random random = new Random();

	// -------------------- constructors --------------------------------------

	public RandomLevel(int width, int height) {
		super(width, height);
	}

	// -------------------- methods -------------------------------------------
	
	/* randomly generate a level
	 * 
	 */
	protected void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x+y*width] = makeRandom();
			} // end x for
		} // end y for
	} // end generateLevel
	
	private int makeRandom() {
		int num = random.nextInt(6);
		if (num == 1 || num == 2 || num == 3) {
			num = random.nextInt(4);
			if (num == 1 || num == 2 || num == 3) {
				num = random.nextInt(4);
				if (num == 1 || num == 2) {
					num = random.nextInt(4);
					if (num == 2) {
						num = random.nextInt(4);
					}
				}
			}
		}
		return num;
	}
	

	/*@Override
	public void render(int xScroll, int yScroll, Screen screen) {
		screen.setOffset(xScroll, yScroll);
		// cornerpins
		int x0 = xScroll >> 4;
		int x1 = (xScroll + screen.getWidth() + 16) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + screen.getHeight() + 16) >> 4;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);
			} // end x for
		} // end y for
	}*/
	
}
