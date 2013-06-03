package uk.co.gossfunkel.citadel.entity.mob;

import java.awt.Rectangle;
import uk.co.gossfunkel.citadel.level.Level;
import java.util.ArrayList;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.Timer;
import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.Sprite;
import uk.co.gossfunkel.citadel.input.Keyboard;
import uk.co.gossfunkel.citadel.input.Mouse;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Coord;
import uk.co.gossfunkel.citadel.level.tile.Tile;
//import uk.co.gossfunkel.citadel.entity.projectile.Fireball;

public class Player extends Mob {
	
	// -------------------- variables -----------------------------------------
	
	protected Game game;
	private Keyboard input;
	private Timer timer;
	private Screen screen;
	protected final String username;
	int xa, ya;
	Sprite sprite;
	boolean flip;
	boolean speaking = false;
	boolean mining = false;
	String saying;
	int fire = 10;
	
	private boolean bPress = false, tPress = false;

	// -------------------- constructors --------------------------------------
	
	public Player(Game game, Keyboard input, Timer timer, String username, Level level) {
		this.game = game;
		this.input = input;
		this.timer = timer;
		this.level = level;
		this.username = username;
		sprite = Sprite.playerS;
		SIZE = sprite.getSIZE();
		nearEntities = new ArrayList<Entity>();
		
		init();
	}
	
	// spawn locale constructor
	public Player(int x, int y, Game game, Keyboard input, Timer timer, String username, Level level) {
		this(game, input, timer, username, level);
		teleport(x, y); 
	}

	// -------------------- methods -------------------------------------------
	
	public void init() {
		level.addEntity(this);
		//if (game.server != null) game.server.addConnection((OnlinePlayer)player);
	}
	
	@Override
	public void update() {
		xa = 0;
		ya = 0;
		if (input != null) {
			if (speaking) {
				updateSpeaking();
				if (input.enter) {
					game.say(saying);
					input.lastEntered = 't';
					speaking = false;
				}//end say if
			} else {
				if (input.up) ya--;
				if (input.down) ya++;
				if (input.left) xa--;
				if (input.right) xa++;
		
				if (level.findTileAt(x+SIZE/2, y+SIZE/2).equals(Tile.water))
					swimming = true;
				else swimming = false;
		
				if (xa != 0 || ya != 0) { //-----
					move(xa, ya);
					moving = true;
				} else moving = false; //--------
		
				if (input.b){ //------------------
					if (!bPress) {
						game.build(x, y);
						bPress = true;
					}
				} else bPress = false; //---------
				if (input.t && !speaking){ //---
					if (!tPress) {
						speaking = true;
						saying = "";
						tPress = true;
					}
				} else tPress = false; //-------
				if (Mouse.b() == 1) {
					Coord coord = new Coord((TileCoordinate.scale((Mouse.x()-screen.getWidth())+x))+32,
						(TileCoordinate.scale((Mouse.y()-screen.getHeight())+y))+17);
					boolean shooting = true;
					for (Rectangle rect : level.getTreeRects()) {
						if (rect != null) {
							if (rect.contains(coord.x(), coord.y())) {
								int scaleX = TileCoordinate.scale(x);
								int scaleY = TileCoordinate.scale(y);
								if (rect.x - 1 < scaleX && rect.y - 1 > scaleY
									&& rect.x+rect.width+1 > scaleX 
									&& rect.y+rect.height+1 > scaleY ){
									shooting = false;
									mining = true;
								} else {
									shooting = true;
									mining = false;
								}
								level.getTree(rect).onClick();
							} //end contains if
						} //end null if
					} //end rect for
				
					if (shooting) updateShoot();
				} //end mouse if
			} //end not speaking else
		} //end null input if
		clear();
	} // end update

	private void updateShoot() {
		fire++;
		if (fire > 10) {
			fire = 0;
			shoot(x, y, Math.atan2((Mouse.y() - (Game.getWindowHeight())/2), 
					(Mouse.x() - (Game.getWindowWidth())/2)));
		}
	}
	
	@Override
	public void render(Screen screen) {
		this.screen = screen;
		flip = false;
		if (dir == 0) {
			if (swimming) {
				sprite = Sprite.playerwetN;
				if ((System.currentTimeMillis() - timer.getSecond() > 500)) {
					sprite = Sprite.playerwetN_1;
				} // end moving animation if
			} else {
				if (mining) {
					//sprite = Sprite.playerminingN;
				} else {
					sprite = Sprite.playerN;
					flip = false;
					if ((System.currentTimeMillis() - timer.getSecond() > 330) 
							&& moving) {
						sprite = Sprite.playerN_1;
					} 
					if ((System.currentTimeMillis() - timer.getSecond() > 660)) {
						flip = true;
					}// end moving animation if
				}
			} // end swimming if
		} // end north if
		if (dir == 1) {
			if (swimming) {
				sprite = Sprite.playerwetE;
				if ((System.currentTimeMillis() - timer.getSecond() > 500)) {
					sprite = Sprite.playerwetE_1;
				}
			} else {
				if (mining) {
					//sprite = Sprite.playerminingE;
				} else {
					sprite = Sprite.playerE;
					if ((System.currentTimeMillis() - timer.getSecond() > 500) 
							&& moving) {
						sprite = Sprite.playerE_1;
					} 
				}
			}
		} // end east if
		if (dir == 2) {
			if (swimming) {
				sprite = Sprite.playerwetS;
				if (System.currentTimeMillis() - timer.getSecond() > 500) {
					sprite = Sprite.playerwetS_1;
				}
			} else {
				if (mining) {
					//sprite = Sprite.playerminingS;
				} else {
					sprite = Sprite.playerS;
					flip = false;
					if ((System.currentTimeMillis() - timer.getSecond() > 330) 
							&& moving) {
						sprite = Sprite.playerS_1;
					}
					if (System.currentTimeMillis() - timer.getSecond() > 660) {
						flip = true;
					}
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
				if (mining) {
					//sprite = Sprite.playerminingE;
				} else {
					sprite = Sprite.playerE; 
					if ((System.currentTimeMillis() - timer.getSecond() > 500) 
							&& moving) {
						sprite = Sprite.playerE_1;
					}
				}
			}
			flip = true;
		} // end west if
		screen.renderPlayer(x, y, sprite, flip);
	} // end render
	
	private void updateSpeaking() {
		char tmpChar = input.entered();
		if (tmpChar != (char)0) saying += tmpChar;
		// render currently written text
	}
	
	private void clear() {
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).isRemoved()) projectiles.remove(i);
		} // end for
	} // end clear
	
	public String username() {
		return username;
	}
	
}
