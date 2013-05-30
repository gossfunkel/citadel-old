package uk.co.gossfunkel.citadel;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.entity.mob.Player;
import uk.co.gossfunkel.citadel.entity.settlement.Settlement;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.graphics.WindowHandler;
import uk.co.gossfunkel.citadel.input.Keyboard;
import uk.co.gossfunkel.citadel.input.Mouse;
import uk.co.gossfunkel.citadel.launch.Launcher;
import uk.co.gossfunkel.citadel.level.Level;
import uk.co.gossfunkel.citadel.level.SpawnLevel;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.net.GameClient;
import uk.co.gossfunkel.citadel.net.GameServer;
import uk.co.gossfunkel.citadel.net.packets.Packet00Login;

/* The game's main running class
 * 
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	// -------------------- variables -----------------------------------------
	
	public static String title = "Citadel";
	private Timer timer;
	private Level level;
	private Player player;
	private GameClient client;
	private GameServer server;
	
	private int xScroll, yScroll, xOffset = 0, yOffset = 0;
	boolean wasplus = false;
	//Quadtree quadtree;
	
	private static List<Settlement> settlements;
	private static List<Integer> settx;
	private static List<Integer> setty;
	private static List<String> speech;
	
	// screen dimensions (16:9) etc
	private static int width = 1024;
	private static int height = (width / 16*9);
	private static int scale = 1;
	
	// multithreading stuff
	private Thread thread;
	
	// loop stuff
	private boolean running = false;
	
	// listeners
	private Keyboard key;
	private Mouse mouse;
	
	// day stuff
	private float globalLight = 0.6f;
	private int ticker = 0;
	private boolean day;
	private int hour;
	private int days;
	private int month = 0;
	@SuppressWarnings("unused")
	private int year = 0;
	
	// graphics stuff
	private boolean isFullscreen = false;
	private DisplayMode dm;
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
	
	public Game(String username) {
	    System.setProperty("sun.java2d.opengl", "True");
		
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);		
		
		dm = new DisplayMode(width,height,8,DisplayMode.REFRESH_RATE_UNKNOWN);
		screen = new Screen(width, height);
		transformer = new AffineTransform();
		frame = new JFrame();
		frame.setUndecorated(true);
		setKey(new Keyboard());
		setTimer(new Timer());
		new WindowHandler(this);
		
		setLevel(new SpawnLevel("/textures/garden.png"));
		client = new GameClient(this, "localhost");
		setPlayer(new OnlinePlayer(this, getKey(), getTimer(), username, null, -1, getLevel()));
		Packet00Login loginPack = new Packet00Login(username);
		if (server != null) server.addConnection((OnlinePlayer)getPlayer(), loginPack);
		loginPack.writeData(client);
		
		settlements = new ArrayList<Settlement>();
		settx = new ArrayList<Integer>();
		setty = new ArrayList<Integer>();
		speech = new ArrayList<String>();
		
		addKeyListener(getKey());
		
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	// -------------------- methods -------------------------------------------
	
	public static void main(String[] args) {
		if (args.length > 0) { 
			if (args[0].equals("-p")) new Launcher(args[1]);
			else if (args[0].equals("-e")) 
				System.out.println("Echo.");
			else System.out.println("Command line argument not recognised!");
		} else new Launcher(0);
	}
	
	public synchronized void start() {
		if (running) return;
		thread = new Thread(this, "Display");
		thread.start();
		running = true;
		
		if (JOptionPane.showConfirmDialog(this, "Server?") == 0) {
			server = new GameServer(this);
			server.start();
		} 
		client.start();

		//client.sendData("ping".getBytes());
	}
	
	public void run() {
		requestFocus();
		while (running) {
			getTimer().tick();
			if (System.currentTimeMillis() - getTimer().getSecond() > 1000) {
				// every second, add a second, print fps and mod title with fps
				getTimer().accumulateSecond();
				//System.out.println(getTimer().returnFPS());
				frame.setTitle(title + "  |  " + getTimer().returnFPS());
				getTimer().resetTick();
				ticker++;
			}
			while (getTimer().getDelta() >= 1) {
				// every time delta goes greater than one, update and supertick
				update();
				getTimer().superTick();
			}
			if (getTimer().getFPS() > 100) {
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					System.err.println("Sleeping failed: " + e);
				}
			} 
			render();
			if (ticker > 30) {
				ticker = 0;
				getTimer().hourTick();
			}
			
		}
		stop();
	}
	
	public synchronized void stop() {
		running = false;
		frame.dispose();
		try {
			if (server != null) {
				server.exit();
			}
			client.exit();
			thread.join();
			System.exit(0);
		} catch (InterruptedException e) {
			System.err.println("THREAD STOP ERROR - " + e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void update() {
		if (getTimer().getDay() != day) {
			day = getTimer().getDay();
			if (day) globalLight = 1f;
			else  globalLight = 0.6f;
		}
		if (getTimer().getDay()) {
			if (getTimer().getHour() != hour) {
				hour = getTimer().getHour();
				if (hour == 0) {
					// new day
					days++;
					for (Settlement s : settlements) {
						s.levelUp();
					}
				}
				globalLight -= 0.05f;
			} 
		} else {
			if (getTimer().getHour() != hour) {
				hour = getTimer().getHour();
				globalLight += 0.05f;
			}
		}
		if (days == 31) {
			// new month
			month++;
			days = 0;
		}
		if (month == 12) {
			// new year
			year++;
			month = 0;
		}
		if (Mouse.b() == 1 && Mouse.x() > width-100 && Mouse.x() < width-70 && 
				Mouse.y() > 0 && Mouse.y() < 17) {
			minimise();
		}
		if (Mouse.b() == 1 && Mouse.x() > width-70 && Mouse.x() < width-35 && 
				Mouse.y() > 0 && Mouse.y() < 17) {
			if (isFullscreen) unmaximise();
			else maximise();
		}
		if (Mouse.b() == 1 && Mouse.x() > width-35 && Mouse.x() < width && 
				Mouse.y() > 0 && Mouse.y() < 17) {
			stop();
		}
		if (Mouse.b() == 3) {
			// open right click menu
		} else if (Mouse.b() == 1) {
			// close right click menu
		}
		getKey().update();
		if (getKey().plus) {
			if (!wasplus) {
				wasplus = true;
				if (scale < 4) scale += 1;
			}
		}
		if (getKey().minus) {
			wasplus = false;
			if (scale > 1) scale -= 1;
		}
		getLevel().update();
		//quadtree.update();
		getPlayer().update();
		for (int i = 0; i < settlements.size(); i++) {
			settlements.get(i).update();
		}
	}

	public void render() {
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3); // woo, triple buffering!
			return;
		}
		
		//transformer.concatenate(at);
		//transformer.translate(-(r.width/2), -(r.height/2));
		
		screen.clear();		// remove previous data
		//pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		xScroll = getPlayer().x() - screen.getWidth()/2;
		yScroll = getPlayer().y() - screen.getHeight()/2;
		// ORDER LAYERS HERE
		getLevel().render(xScroll*scale, yScroll*scale, screen);	// generate current data
		for (int i = 0; i < settlements.size(); i++) {
			settlements.get(i).render(screen);
		}
		getPlayer().render(screen);
		getLevel().drawPanels(screen); // draw objects to go in front of the player
		
		// draw minimise, maximise, quit options
		screen.renderSysMenu();
		
		// draw screen.pixels to pixels
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x+y*width] = screen.pixels[x+y*screen.getWidth()];
			} 
		}
		
		//transformer.scale(scale/10, scale/10);
		//transformer.translate(screen.xOffset*scale,screen.yOffset*scale);

		//RescaleOp op = new RescaleOp(globalLight, 0, null);
		//image = op.filter(image, null);
		
		if (speech.size() == 1)	{
			image = box(image, height - 50);
			image = process(image, speech.get(0), height - 50);
		}
		if (speech.size() == 2)	{
			image = box(image, height - 80);
			image = box(image, height - 50);
			image = process(image, speech.get(0), height - 80);
			image = process(image, speech.get(1), height - 50);
		}
		if (speech.size() == 3) {
			image = box(image, height - 110);
			image = box(image, height - 80);
			image = box(image, height - 50);
			image = process(image, speech.get(0), height - 110);
			image = process(image, speech.get(1), height - 80);
			image = process(image, speech.get(2), height - 50);
		}
		
		switch (scale) {
		case 1: xOffset = yOffset = 0; break;
		case 2: xOffset = 250;yOffset = 250; break;
		//case 3: xOffset = 250;yOffset = 250; break;
		//case 4: xOffset = 250;yOffset = 250; break;
		default: xOffset = yOffset = 0; break;
		}
		
		BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = image2.createGraphics();
        g2d.setColor(new Color(.15f, .1f, .3f, 1f));
        g2d.fill(new Rectangle(0, 0, width, height));
        g2d.dispose();  
		
		// draw
		g2d = (Graphics2D)bs.getDrawGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                //RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                //RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		//g2d.setTransform(transformer);
		{
			transformer.setToIdentity();
			//transformer.translate(width / 2, height / 2);
			transformer.scale(scale, scale);
			g2d.setTransform(transformer);

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, globalLight));  
			g2d.drawImage(image, xOffset, yOffset, width, height, null);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - globalLight));  
			g2d.drawImage(image2, xOffset, yOffset, width, height, null);
			
		}
		g2d.dispose();
		
		bs.show(); // blit/show buffer

		Toolkit.getDefaultToolkit().sync();
	}
	
	private BufferedImage process(BufferedImage old, String s, int y) {
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.white);
        g2d.setFont(new Font("Veranda", Font.BOLD, 20));
        g2d.drawString(s, 25, y);
        g2d.dispose();
        return img;
    }
	
	private BufferedImage box(BufferedImage old, int y) {
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setColor(new Color(.1f, .1f, .3f, .4f));
        g2d.fill(new Rectangle(20, y-20, 500, 25));
        g2d.dispose();
        return img;
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
		return width;
	}
	
	public static int getWindowHeight() {
		return height;
	}

	public void build(int xm, int ym) {
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

	public void say(String str) {
		speech.add(str);
		//System.out.println(str);
		if (speech.size() > 3) {
			speech.remove(0);
		}
	}
	
	public String username() {
		return player.username();
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Keyboard getKey() {
		return key;
	}

	public void setKey(Keyboard key) {
		this.key = key;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public GameClient getClient() {
		return client;
	}
	
	private void minimise() {
		 frame.setExtendedState(JFrame.ICONIFIED);
	}
	
	public void maximise() {
	    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	public void unmaximise() {
	    frame.setExtendedState(JFrame.NORMAL);
	}

	public void fullScreen() {
		try {
			screen.goFullScreen(dm, frame);
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			screen.restoreScreen(frame);
			isFullscreen = true;
		}
	}

}
