package uk.co.gossfunkel.citadel.item;

import uk.co.gossfunkel.citadel.graphics.Sprite;

public abstract class Item {
	
	protected String name;
	protected Sprite sprite;
	
	public String getName() {
		return name;
	}
	
	public Sprite getSprite() {
		return sprite;
	}

}
