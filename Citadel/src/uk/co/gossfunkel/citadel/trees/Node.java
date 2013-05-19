package uk.co.gossfunkel.citadel.trees;

import java.awt.Rectangle;
import java.util.List;

import uk.co.gossfunkel.citadel.entity.Entity;

public class Node extends BasicNode {

	private BasicNode[] nodes;
	
	public Node(Rectangle pbounds, int depth, Quadtree quadtree) {
		this.quadtree = quadtree;
		bounds = pbounds;
		DEPTH = depth;
		nodes = new BasicNode[3];
	}
	
	public void spawnNode() {
		for (int i = 0; i < 3; i++) {
			if (nodes[i].equals(null))
				nodes[i] = nodeGen(i);
		}
	}
	
	private BasicNode nodeGen(int num) {
		int xn, yn;
		if (num == 0) {
			xn = (int)bounds.getX();
			yn = (int)bounds.getY();
		} else if (num == 1) {
			xn = (int)bounds.getX() + bounds.width/2;
			yn = (int)bounds.getY();
		} else if (num == 2) {
			xn = (int)bounds.getX();
			yn = (int)bounds.getY() + bounds.height/2;
		} else {
			xn = (int)bounds.getX() + bounds.width/2;
			yn = (int)bounds.getY() + bounds.height/2;
		}
		if (quadtree.MAX_DEPTH == DEPTH+1)
			return new Leaf(new Rectangle
				(xn, yn, bounds.width/2, bounds.height/2), DEPTH+1, quadtree);
		else return new Node(new Rectangle
				(xn, yn, bounds.width/2, bounds.height/2), DEPTH+1, quadtree);
	}

	public int getIndex(Entity ent) {
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);
		 
		// Object can completely fit within the top quadrants
		boolean topQuadrant = (ent.y() < horizontalMidpoint && ent.y() + ent.getSIZE() < horizontalMidpoint);
		// Object can completely fit within the bottom quadrants
		boolean bottomQuadrant = (ent.y() > horizontalMidpoint);
		
		// Object can completely fit within the left quadrants
		if (ent.x() < verticalMidpoint && ent.x() + ent.getSIZE() < verticalMidpoint) {
		   if (topQuadrant) index = 1;
		   else if (bottomQuadrant) index = 2;
		}
		// Object can completely fit within the right quadrants
		else if (ent.x() > verticalMidpoint) {
		    if (topQuadrant) index = 0;
		    else if (bottomQuadrant) index = 3;
		}
		return index;
	}
	
	public void insert(Entity ent) {
		if (nodes[0] != null) {
			int i = getIndex(ent);
			if (i != -1) {
				nodes[i].insert(ent);
				return;
		    }
		} else {
			spawnNode();
		}
	}
	
	public List<Entity> retrieve(List<Entity> returnObjects, Entity ent) {
		 int i = getIndex(ent);
		 if (nodes[i] instanceof Leaf) return nodes[i].retrieve(null, null);
		 if (i != -1 && nodes[0] != null) 
			 return nodes[i].retrieve(returnObjects, ent);
		 if (nodes[0] == null) {
			 spawnNode();
		 }
		 return null;
	}
	
	public void clear() {
		for (int i = 0; i < nodes.length; i++) nodes[i].clear();
		nodes = new Node[3];
	}
	
	public BasicNode getNode(int i) {
		return nodes[i];
	}

}
