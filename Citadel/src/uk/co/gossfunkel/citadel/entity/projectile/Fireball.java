package uk.co.gossfunkel.citadel.entity.projectile;

import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;

public class Fireball extends Projectile {

	public Fireball(int x, int y, double dir) {
		super(x, y, dir);
		range = 20;
		speed = 5;
		damage = 5;
		rate = 3;
		sprite = Sprite.fireball;
		
		nx = Math.cos(angle) * speed;
		ny = Math.sin(angle) * speed;
	}
	
	public void render(Screen screen) {
		screen.renderSprite((int)x, (int)y, sprite);
	}

}
