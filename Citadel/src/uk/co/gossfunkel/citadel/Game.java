package uk.co.gossfunkel.citadel;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import uk.co.gossfunkel.citadel.entity.mob.Player;
import uk.co.gossfunkel.citadel.entity.settlement.Settlement;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.input.Keyboard;
import uk.co.gossfunkel.citadel.input.Mouse;
import uk.co.gossfunkel.citadel.launch.Launcher;
import uk.co.gossfunkel.citadel.level.Level;
import uk.co.gossfunkel.citadel.level.SpawnLevel;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Tile;
//import uk.co.gossfunkel.citadel.trees.Quadtree;
/*
import java.awt.Color;
import java.awt.Font;*/

/* The game's main running class
 * 
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	// -------------------- variables -----------------------------------------
	
	public static String title = "Citadel";
	private Timer timer;
	private static Level level;
	private Player player;
	private int xScroll, yScroll;
	//Quadtree quadtree;
	
	private static List<Settlement> settlements;
	private static List<Integer> settx;
	private static List<Integer> setty;
	
	// screen dimensions (16:9) etc
	private static int width = 1000;
	private static int height = (width / 16*9);
	private static double scale = 1;
	
	// multithreading stuff
	private Thread thread;
	
	// loop stuff
	private boolean running = false;
	
	// input stuff
	private Keyboard key;
	private Mouse mouse;
	
	// graphics stuff
	public JFrame frame;
	private BufferStrategy bs; // declared here for heap-efficiency reasons
	private Graphics2D g2d;
	private AffineTransform transformer;
	private BufferedImage image 
		= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels 
		= ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private Screen screen;
	
	// -------------------- constructors --------------------------------------
	
	public Game() {
	    System.setProperty("sun.java2d.opengl", "True");
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);		
		
		screen = new Screen(width, height);
		transformer = new AffineTransform();
		frame = new JFrame();
		key = new Keyboard();
		timer = new Timer();
		
		level = new SpawnLevel("/textures/garden.png");		
		//quadtree = new Quadtree(level.getWidth(), level.getHeight());
		player = new Player(112, 50, key, timer, 16);
		player.init(level);
		//quadtree.add(player);
		
		settlements = new ArrayList<Settlement>();
		settx = new ArrayList<Integer>();
		setty = new ArrayList<Integer>();
		
		addKeyListener(key);
		
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	// -------------------- methods -------------------------------------------
	
	public static void main(String[] args) {
		new Launcher(0);
	}
	
	public void run() {
		requestFocus();
		while (running) {
			timer.tick();
			if (System.currentTimeMillis() - timer.getSecond() > 1000) {
				// every second, add a second, print fps and mod title with fps
				timer.accumulateSecond();
				System.out.println(timer.returnFPS());
				frame.setTitle(title + "  |  " + timer.returnFPS());
				timer.resetTick();
			}
			while (timer.getDelta() >= 1) {
				// every time delta goes greater than one, update and supertick
				update();
				timer.superTick();
			}
			if (timer.getFPS() > 100) {
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					System.err.println("Sleeping failed: " + e);
				}
			} 
			render();
			
		}
		stop();
	}
	
	public synchronized void start() {
		if (running) return;
		thread = new Thread(this, "Display");
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.println("THREAD STOP ERROR - " + e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void update() {
		if (Mouse.b() == 3) {
			// open right click menu
		} else if (Mouse.b() == 1) {
			// close right click menu
		}
		key.update();
		if (key.plus) {
			if (scale < 6.0) {
				scale += 0.1;
			}
		}
		if (key.minus) {
			if (scale > 0.0) {
				scale -= 0.1;
			}
		}
		level.update();
		//quadtree.update();
		player.update();
		for (int i = 0; i < settlements.size(); i++) {
			settlements.get(i).update();
		}
	}
	
	public void render() {
		
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); // woo, triple buffering!
			return;
		}
		
		transformer.setToScale(scale, scale);
		// translate to compensate for scaling
		
		screen.clear();		// remove previous data
		xScroll = player.x() - screen.getWidth()/2;
		yScroll = player.y() - screen.getHeight()/2;
		// ORDER LAYERS HERE
		level.render(xScroll, yScroll, screen);	// generate current data
		for (int i = 0; i < settlements.size(); i++) {
			settlements.get(i).render(screen);
		}
		player.render(screen);
		
		// draw screen.pixels to pixels
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		// draw
		g2d = (Graphics2D)bs.getDrawGraphics();
		g2d.setTransform(transformer);
		{
			g2d.drawImage(image, 0, 0, width, height, null);
			/* g.setColor(Color.WHITE);
			 * g.setFont(new Font("Veranda", 0, 50));
			 * g.drawString("B: " + Mouse.b(), 80, 80);
			*/
		}
		g2d.dispose();
		
		bs.show(); // blit/show buffer

		Toolkit.getDefaultToolkit().sync();
	}

	public static int width() {
		return width;
	}

	public static int height() {
		return height;
	}

	public static double scale() {
		return scale;
	}
	
	public static int getWindowWidth() {
		return width;
	}
	
	public static int getWindowHeight() {
		return height;
	}

	public static void build(int xm, int ym) {
		if (settx.contains(TileCoordinate.scale(xm)) 
				&& setty.contains(TileCoordinate.scale(ym))) {
			System.out.println("Square already filled");
		} else {
			if (level.getTile(TileCoordinate.scale(xm), 
					TileCoordinate.scale(ym)).equals(Tile.water) ||
					level.getTile(TileCoordinate.scale(xm), 
							TileCoordinate.scale(ym)).equals(Tile.rock)) {
				System.out.println("Illegal position");
			} else {
				Settlement genset = new Settlement(xm, ym);
				settlements.add(genset);
				settx.add(genset.x());
				setty.add(genset.y());
			} // end inner else
		} // end outer else
	} // end build

	public static void say(String str) {
		//TODO add str to speech ArrayList
		//TODO update speech ArrayList
	}

}
