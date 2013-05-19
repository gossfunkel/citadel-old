package uk.co.gossfunkel.citadel.trees;

import java.awt.Rectangle;
import java.util.List;

import uk.co.gossfunkel.citadel.entity.Entity;

public abstract class BasicNode {
	
	protected Quadtree quadtree;
	protected int DEPTH;
	protected Rectangle bounds;
	
	public abstract void clear();
	public abstract void insert(Entity ent);
	public abstract List<Entity> retrieve(List<Entity> rO, Entity e);

}
