package uk.co.gossfunkel.citadel.level;

public class TileCoordinate {

	private int x, y;
	
	private final static int SCALE = 16;
	
	public TileCoordinate(int x, int y) {
		this.x = x*SCALE;
		this.y = y*SCALE;
	}
	
	/*
	 * round x to the scaling factor
	 * basically makes x tile-formatted
	 */
	public static int round(int x) {
		float xf = (float)x;
		xf /= SCALE;
		x = (int) Math.floor(xf);
		x *= SCALE;
		return x;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
