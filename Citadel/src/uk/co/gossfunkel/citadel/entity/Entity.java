package uk.co.gossfunkel.citadel.entity;

import java.util.List;
import java.util.Random;
import uk.co.gossfunkel.citadel.level.Level;
import uk.co.gossfunkel.citadel.graphics.Screen;

public abstract class Entity {
	
	// -------------------- variables -----------------------------------------
	
	protected int x, y;
	private boolean removed = false;
	protected Level level;
	protected final Random random = new Random();
	protected int SIZE;
	protected List<Entity> nearEntities;
	
	// -------------------- methods -------------------------------------------
	
	public void update() {}
	
	public void render(Screen screen) {}

	// -------------------- getters -------------------------------------------
	
	public boolean isRemoved() {
		return removed;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int getSIZE() {
		return SIZE;
	}

	// -------------------- setters -------------------------------------------

	public void remove() {
		// destruction animation
		removed = true;
	}
	
	public void addNear(Entity e) {
		nearEntities.add(e);
	}
	
	public void teleport(int x, int y) {
		this.x = x;
		this.y = y;
		System.out.println("> " + x + ", " + y);
	}

}
