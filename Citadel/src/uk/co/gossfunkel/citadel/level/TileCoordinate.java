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
	 * 
	 * DO NOT USE FOR GETTING TILE UNDER PLAYER
	 * not accurate enough
	 * use scale(int n)
	 */
	public static int round(int n) {
		float xf = (float)n;
		xf /= SCALE;
		n = (int) Math.floor(xf);
		n *= SCALE;
		return n;
	}
	
	public static int scale(int n) {
		return round(n)>>5;
	}
	
	public static int reverseScale(int n) {
		return n<<5;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
