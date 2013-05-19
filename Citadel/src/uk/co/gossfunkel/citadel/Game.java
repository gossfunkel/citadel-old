package uk.co.gossfunkel.citadel;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
//import uk.co.gossfunkel.citadel.trees.Quadtree;
import uk.co.gossfunkel.citadel.input.*;
import uk.co.gossfunkel.citadel.level.*;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.entity.mob.Player;
import uk.co.gossfunkel.citadel.entity.settlement.Settlement;/*
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;*/

import javax.swing.JFrame;/*
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;*/

import uk.co.gossfunkel.citadel.graphics.Screen;

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
	private static int width = 300;
	private static int height = width / 16*9;
	private static int scale = 3;
	
	// multithreading stuff
	private Thread thread;
	
	// loop stuff
	private boolean running = false;
	
	// input stuff
	private Keyboard key;
	private Mouse mouse;
	
	// graphics stuff
	private JFrame frame;
/*	private static JMenuBar menuBar;
	private JMenu menu, submenu;
	private JMenuItem menuItem;*/
	private BufferStrategy bs; // declared here for heap-efficiency reasons
	private Graphics g;
	private BufferedImage image 
		= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels 
		= ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private Screen screen;
	
	// -------------------- constructors --------------------------------------
	
	public Game() {
		
		Dimension size = new Dimension(width*scale, height*scale);
		setPreferredSize(size);
		/*menuBar = new JMenuBar();
		menu = new JMenu();*/
		
		
		screen = new Screen(width, height);
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
	
	//@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Game game = new Game();
		
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		
		/*game.menu.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_M, ActionEvent.ALT_MASK));
		game.menu.getAccessibleContext().setAccessibleDescription("A Menu.");
		game.menuItem = new JMenuItem("Some text");
		game.menu.add(game.menuItem);
		game.menuItem = new JMenuItem("Some more");
		game.menu.add(game.menuItem);
		game.menu.addSeparator();
		game.submenu = new JMenu("A submenu");
		game.menuItem = new JMenuItem("Stuff");
		game.submenu.add(game.menuItem);
		game.menuItem = new JMenuItem("More");
		game.submenu.add(game.menuItem);
		game.menu.add(game.submenu);

		game.menuBar.add(game.menu);
		game.frame.setJMenuBar(menuBar);*/
		
		game.frame.getContentPane();
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
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
			render();
			
		}
		stop();
	}
	
	public synchronized void start() {
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
		}
	}
	
	@SuppressWarnings("static-access")
	public void update() {
		if (mouse.b() == 3) {
			// open right click menu
		} else if (mouse.b() == 1) {
			// close right click menu
		}
		key.update();
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
		g = bs.getDrawGraphics();
		{
			g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
			/* g.setColor(Color.WHITE);
			 * g.setFont(new Font("Veranda", 0, 50));
			 * g.drawString("B: " + Mouse.b(), 80, 80);
			*/
		}
		g.dispose();
		
		bs.show(); // blit/show buffer
		
	}
	
	public static int width() {
		return width;
	}

	public static int height() {
		return height;
	}

	public static int scale() {
		return scale;
	}
	
	public static int getWindowWidth() {
		return width * scale;
	}
	
	public static int getWindowHeight() {
		return height * scale;
	}

	@SuppressWarnings("unused")
	public static void build(int xm, int ym) {
		if (settx.contains(TileCoordinate.round(xm)) 
				&& setty.contains(TileCoordinate.round(ym))) {
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

}
