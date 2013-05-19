package uk.co.gossfunkel.citadel.graphics;

public class Sprite {
	
	// -------------------- variables -----------------------------------------
	
	private final String NAME;
	private final int SIZE;
	private int x, y;	// offsets
	private SpriteSheet sheet;
	public int[] pixels;
	
	// -------------------- sprites -------------------------------------------
	
	// tiles ---
	public static Sprite grass  = new Sprite(16, 0, 0, SpriteSheet.tiles, "GrassTile");
	public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles, "FlowerTile");
	public static Sprite rock   = new Sprite(16, 2, 0, SpriteSheet.tiles, "RockTile");
	public static Sprite voidSprite = new Sprite(16, 0x6495ed, "Void");
	
	// settlements ---
	public static Sprite hamlet = new Sprite(16, 0, 4, SpriteSheet.tiles, "HamletTile");
	public static Sprite village = new Sprite(16, 1, 4, SpriteSheet.tiles, "VillageTile");
	public static Sprite city = new Sprite(32, 2, 4, SpriteSheet.tiles, "CityTile");
	
	// mobs ---
	
	// player face south
	public static Sprite playerS = new Sprite(16, 0, 0, SpriteSheet.mobs, "PlayerSouth");
	// player face east
	public static Sprite playerE = new Sprite(16, 1, 0, SpriteSheet.mobs, "PlayerSide");
	// player face north
	public static Sprite playerN = new Sprite(16, 2, 0, SpriteSheet.mobs, "PlayerNorth");

	// player face south
	public static Sprite playerS_1 = new Sprite(16, 0, 1, SpriteSheet.mobs, "PlayerSouth-1");
	// player face east
	public static Sprite playerE_1 = new Sprite(16, 1, 1, SpriteSheet.mobs, "PlayerSide-1");
	// player face north
	public static Sprite playerN_1 = new Sprite(16, 2, 1, SpriteSheet.mobs, "PlayerNorth-1");
	
	// -------------------- constructors --------------------------------------
	
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this(size, x, y, sheet, "DEFAULT");
	}
	
	public Sprite(int size, int x, int y, SpriteSheet sheet, String name) {
		
		SIZE 	   = size;
		this.x 	   = x * SIZE;
		this.y 	   = y * SIZE;
		this.sheet = sheet;
		pixels = new int[SIZE*SIZE];
		NAME = name;
		
		load();
		
	}
	
	public Sprite(int size, int colour) {
		this(size, colour, "DEFAULT-COLOURED: " + colour);
	}
	
	public Sprite(int size, int colour, String name) {
		SIZE = size;
		pixels = new int[SIZE*SIZE];
		NAME = (name);
		setColour(colour);
	}
	
	// -------------------- methods -------------------------------------------
	
	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				pixels[x+y*SIZE] = sheet.pixels[(this.x + x) 
				                              + (this.y + y) * sheet.SIZE];
			}
		}
	}
	
	private void setColour(int colour) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = colour;
		}
	}
	
	// -------------------- getters -------------------------------------------
	
	@Override
	public String toString() {
		return ("SPRITE ---\n" + NAME + "\nSize: " + SIZE + ", xOffset: " 
					+ x + ", yOffset: " + y + "\n---");
	}
	
	public int getSIZE() {
		return SIZE;
	}

}
