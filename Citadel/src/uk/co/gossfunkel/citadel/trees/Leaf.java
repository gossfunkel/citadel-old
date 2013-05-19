package uk.co.gossfunkel.citadel.trees;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadel.entity.Entity;

public class Leaf extends BasicNode{

	private List<Entity> entity;
	private final int MAX_ENTS = 20;
	
	public Leaf(Rectangle pbounds, int depth, Quadtree quadtree) {
		this.quadtree = quadtree;
		entity = new ArrayList<Entity>();
		bounds = pbounds;
		DEPTH = depth;
	}
	
	public void insert(Entity ent) {
		entity.add(ent);
		 
		if (entity.size() > MAX_ENTS) {
			//TODO: deal with this
		}
	}
	
	public void removeEntity(int i) {
		entity.remove(i);
	}
	
	public void removeEntity(Entity ent) {
		entity.remove(ent);
	}
	
	@Override
	public List<Entity> retrieve(List<Entity> returnObjects, Entity ent) {
		return entity;
	}
	
	public void clear() {
		for (int i = 0; i < entity.size(); i++) {
			removeEntity(i);
		}
	}

}
