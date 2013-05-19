package uk.co.gossfunkel.citadel.trees;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import uk.co.gossfunkel.citadel.entity.Entity;

public class Quadtree {
	
	public final int XSIZE;
	public final int YSIZE;
	public final int MAX_DEPTH = 10;
	public final int MAX_OBJS = 5;
	private Node node;
	public List<Entity> entities;
	
	public Quadtree(int xsize, int ysize) {
		XSIZE = xsize;
		YSIZE = ysize;
		entities = new ArrayList<Entity>();
	}
	
	public void update() {
		if (entities.size() > 0 && node == null) {
			node = new Node(new Rectangle(0, 0, XSIZE, YSIZE), 0, this);
		} else {
			// check if entities are dead
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i).isRemoved()) {
					// take out of tree and restructure
				}
			}
		}
	}
	
	public void insert(Entity ent) {
		if (node != null) {
			node.insert(ent);
		} else {
			node = new Node(new Rectangle(0, 0, XSIZE, YSIZE), 0, this);
			node.insert(ent);
		}
	}
	
	public void add(Entity ent) {
		entities.add(ent);
		// allocate ent to a leaf
	}
	
	public void clear() {
		node.clear();
		node = null;
	}

}
