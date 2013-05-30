package uk.co.gossfunkel.citadel.entity;

import java.awt.Rectangle;

import uk.co.gossfunkel.citadel.level.tile.Coord;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.level.Level;

public class Tree {
	
	private Coord coord;
	private Tile tile = Tile.tree;
	private Rectangle boundingBox;
	private Level level;

	private int brokenness = 0;
	
	public Tree(Coord coord, Rectangle bbox) {
		this.coord = coord;
		boundingBox = bbox;
	}
	
	public void render(Coord coord, Screen screen, Level level) {
		tile.render(coord, screen);
		this.level = level;
	}
	
	public Coord getCoord() {
		return coord;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public Rectangle getRect() {
		return boundingBox;
	}

	public void onClick() {
		System.out.println("brokenness: " + brokenness);
		brokenness++;
		if (brokenness > 100) {
			collapse();
		}
	}
	
	public void collapse() {
		// drop apple
		level.removePanel(coord);
	}

}
