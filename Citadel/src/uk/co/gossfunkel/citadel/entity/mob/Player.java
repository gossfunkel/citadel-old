package uk.co.gossfunkel.citadel.entity.mob;

import java.util.ArrayList;
import uk.co.gossfunkel.citadel.input.Keyboard;
import uk.co.gossfunkel.citadel.input.Mouse;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.Timer;

public class Player extends Mob {
	
	// -------------------- variables -----------------------------------------
	
	private Keyboard input;
	private Timer timer;
	int xa, ya;
	Sprite sprite;
	boolean flip;
	
	private boolean bPress = false;

	// -------------------- constructors --------------------------------------
	
	public Player(Keyboard input, Timer timer, int size) {
		this.input = input;
		this.timer = timer;
		sprite = Sprite.playerS;
		SIZE = size;
		nearEntities = new ArrayList<Entity>();
	}
	
	// spawn locale constructor
	public Player(int x, int y, Keyboard input, Timer timer, int size) {
		this(input, timer, size);
		teleport(x, y); 
	}

	// -------------------- methods -------------------------------------------
	
	@Override
	public void update() {
		xa = 0;
		ya = 0;
		
		if (input.up) ya--;
		if (input.down) ya++;
		if (input.left) xa--;
		if (input.right) xa++;
		
		if (level.findTileAt(x+SIZE/2, y+SIZE/2).equals(Tile.water))
			 swimming = true;
		else swimming = false;
		
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			moving = true;
		} else {
			moving = false;
		}
		
		if (input.b){
			if (!bPress) {
				Game.build(x, y);
				System.out.println("b");
				bPress = true;
			}
		} else {
			bPress = false;
		}
		clear();
		updateShooting();
	} // end update
	
	private void updateShooting() {
		if (Mouse.b() == 1) {
			shoot(x, y, Math.atan2((Mouse.y() - (Game.getWindowHeight())/2), 
									(Mouse.x() - (Game.getWindowWidth())/2)));
		}
	}
	
	@Override
	public void render(Screen screen) {
		flip = false;
		if (dir == 0) {
			if (swimming) {
				sprite = Sprite.playerwetN;
				if ((System.currentTimeMillis() - timer.getSecond() > 500)) {
					sprite = Sprite.playerwetN_1;
				} // end moving animation if
			} else {
				sprite = Sprite.playerN;
				if ((System.currentTimeMillis() - timer.getSecond() > 500) 
					&& moving) {
					sprite = Sprite.playerN_1;
				} // end moving animation if
			} // end swimming if
		} // end north if
		if (dir == 1) {
			if (swimming) {
				sprite = Sprite.playerwetE;
				if ((System.currentTimeMillis() - timer.getSecond() > 500)) {
					sprite = Sprite.playerwetE_1;
				}
			} else {
				sprite = Sprite.playerE;
				if ((System.currentTimeMillis() - timer.getSecond() > 500) 
						&& moving) {
					sprite = Sprite.playerE_1;
				}
			}
		} // end east if
		if (dir == 2) {
			if (swimming) {
				sprite = Sprite.playerwetS;
				if ((System.currentTimeMillis() - timer.getSecond() > 500) ) {
					sprite = Sprite.playerwetS_1;
				}
			} else {
				sprite = Sprite.playerS;
				if ((System.currentTimeMillis() - timer.getSecond() > 500) 
					&& moving) {
					sprite = Sprite.playerS_1;
				}
			}
		} // end south if
		if (dir == 3) {
			if (swimming) {
				sprite = Sprite.playerwetE; 
				if ((System.currentTimeMillis() - timer.getSecond() > 500)) {
					sprite = Sprite.playerwetE_1;
				}
				flip = true;
			} else {
				sprite = Sprite.playerE; 
				if ((System.currentTimeMillis() - timer.getSecond() > 500) 
					&& moving) {
					sprite = Sprite.playerE_1;
				}
			}
			flip = true;
		} // end west if
		screen.renderPlayer(x, y, sprite, flip);
	} // end render
	
	// move the player to a location
	private void teleport(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private void clear() {
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).isRemoved()) projectiles.remove(i);
		} // end for
	} // end clear
	
}
