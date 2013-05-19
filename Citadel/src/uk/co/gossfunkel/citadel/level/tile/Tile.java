package uk.co.gossfunkel.citadel.level.tile;

import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.graphics.Screen;

public class Tile {

	// -------------------- variables -----------------------------------------
	
	public int x, y;
	public Sprite sprite;
	
	// -------------------- tiles ---------------------------------------------

	public static Tile voidTile = new VoidTile(Sprite.voidSprite);
	public static Tile grass = new GrassTile(Sprite.grass);
	public static Tile flower = new FlowerTile(Sprite.flower);
	public static Tile rock = new RockTile(Sprite.rock);
	public static Tile water1 = new WaterTile(Sprite.water1);
	public static Tile water2 = new WaterTile(Sprite.water2);
	public static Tile water3 = new WaterTile(Sprite.water3);
	public static Tile water4 = new WaterTile(Sprite.water4);
	
	//settlements ---
	public static Tile hamlet = new RockTile(Sprite.hamlet);
	public static Tile village = new RockTile(Sprite.village);
	public static Tile city = new RockTile(Sprite.city);
	
	// -------------------- constructors --------------------------------------
	
	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	
	// -------------------- methods -------------------------------------------
	
	public void render(int x, int y, Screen screen) {}
	
	@Override
	public String toString() {
		return ("TILE ---\n" + sprite + "\nx: " + x + ", y: " + y + "\n---");
	}
	
	public boolean solid() {
		return false;
	}
	
}
