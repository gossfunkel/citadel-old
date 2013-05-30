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
	public static Sprite voidSprite = new Sprite(16, 0x6495ed, "Void");
	public static Sprite grass  = new Sprite(16, 0, 0, SpriteSheet.tiles, "GrassTile");
	public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles, "FlowerTile");
	public static Sprite rock   = new Sprite(16, 2, 0, SpriteSheet.tiles, "RockTile");
	public static Sprite water1   = new Sprite(16, 3, 0, SpriteSheet.tiles, "WaterTile-1");
	public static Sprite water2   = new Sprite(16, 3, 1, SpriteSheet.tiles, "WaterTile-2");
	public static Sprite water3   = new Sprite(16, 4, 0, SpriteSheet.tiles, "WaterTile-3");
	public static Sprite water4   = new Sprite(16, 4, 1, SpriteSheet.tiles, "WaterTile-4");
	public static Sprite tree   = new Sprite(32, 3, 0, SpriteSheet.tiles, "TreeTile");
	
	// settlements ---
	public static Sprite hamlet = new Sprite(16, 0, 4, SpriteSheet.tiles, "HamletTile");
	public static Sprite village = new Sprite(16, 1, 4, SpriteSheet.tiles, "VillageTile");
	public static Sprite city = new Sprite(32, 2, 4, SpriteSheet.tiles, "CityTile");
	
	// mobs ---
	
	// player face south
	public static Sprite playerS = new Sprite(32, 0, 0, SpriteSheet.mobs32, "PlayerSouth");
	// player face east
	public static Sprite playerE = new Sprite(32, 1, 0, SpriteSheet.mobs32, "PlayerSide");
	// player face north
	public static Sprite playerN = new Sprite(32, 2, 0, SpriteSheet.mobs32, "PlayerNorth");

	// player face south
	public static Sprite playerS_1 = new Sprite(32, 0, 1, SpriteSheet.mobs32, "PlayerSouth-1");
	// player face east
	public static Sprite playerE_1 = new Sprite(32, 1, 1, SpriteSheet.mobs32, "PlayerSide-1");
	// player face north
	public static Sprite playerN_1 = new Sprite(32, 2, 1, SpriteSheet.mobs32, "PlayerNorth-1");
	
	// player water face south
	public static Sprite playerwetS = new Sprite(16, 0, 2, SpriteSheet.mobs, "PlayerSouthW");
	// player water face east
	public static Sprite playerwetE = new Sprite(16, 1, 2, SpriteSheet.mobs, "PlayerSideW");
	// player water face north
	public static Sprite playerwetN = new Sprite(16, 2, 2, SpriteSheet.mobs, "PlayerNorthW");

	// player water face south
	public static Sprite playerwetS_1 = new Sprite(16, 0, 3, SpriteSheet.mobs, "PlayerSouthW-1");
	// player water face east
	public static Sprite playerwetE_1 = new Sprite(16, 1, 3, SpriteSheet.mobs, "PlayerSideW-1");
	// player face water north
	public static Sprite playerwetN_1 = new Sprite(16, 2, 3, SpriteSheet.mobs, "PlayerNorthW-1");
	
	//TODO: add mining sprites

	// items ---
	
	public static Sprite fireball = new Sprite(16, 0, 0, SpriteSheet.items, "Fireball");
	
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
