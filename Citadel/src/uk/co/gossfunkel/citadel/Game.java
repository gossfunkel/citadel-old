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
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.entity.settlement.ConstructionSettlement;
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
import uk.co.gossfunkel.citadel.net.packets.Packet00Login;
//import java.net.InetAddress;
//import java.net.UnknownHostException;

/* The game's main running class
 * 
 */
public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	// -------------------- variables -----------------------------------------
	
	public static String title = "Citadel";
	private Timer timer;
	private Level level;
	private OnlinePlayer player;
	private String username;
	
	private int xScroll, yScroll, xOffset = 0, yOffset = 0;
	boolean wasplus = false;
	boolean wasesc  = false;
	
	private static List<Settlement> settlements;
	private static List<ConstructionSettlement> consettlements;
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
	BufferedImage image2;
	private AffineTransform transformer;
	private BufferedImage image 
		= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels 
		= ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	private Screen screen;
	int type = 0;
	
	// net stuff
	private GameClient client;
	private Thread clientThread;
	private String ip;
	
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
		
		settlements = new ArrayList<Settlement>();
		consettlements = new ArrayList<ConstructionSettlement>();
		settx = new ArrayList<Integer>();
		setty = new ArrayList<Integer>();
		speech = new ArrayList<String>();
		
		addKeyListener(getKey());
		
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	    
	    this.username = username;
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
		
		ip = JOptionPane.showInputDialog("Connect to what server?");

		client = new GameClient(this, ip);
		clientThread = new Thread(client, "Client");
		clientThread.start();

		player = new OnlinePlayer(level.getSpawnX(), level.getSpawnY(), this,
				getKey(), getTimer(), username(), null, -1, getLevel());
		
		/*
		try {
			address = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/
		
		client = new GameClient(this, "localhost");
		
		thread = new Thread(this, "Display");
		thread.start();
		running = true;

		Packet00Login loginPack = new Packet00Login(username(), level.getSpawnX(), level.getSpawnY());
		//if (server != null) server.addConnection(new OnlinePlayer(level.getSpawnX(),
			//	level.getSpawnY(), this, timer, username(), address, 
				//1042, level), loginPack);
		loginPack.writeData(client);

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
			render(null);
			if (ticker > 30) {
				ticker = 0;
				getTimer().hourTick();
			}
			
		}
	}
	
	public synchronized void stop() {
		running = false;
		frame.dispose();
        WindowEvent wev = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		try {
			client.exit();
			new Thread(){
	            public void run() {
	                System.exit(0);
	            }
			}.start();
			thread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
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
				}
				globalLight -= 0.05f;
				globalLight *= 100;
				globalLight = Math.round(globalLight);
				globalLight /= 100;
				if (globalLight < 0.6) globalLight = .6f;
			} 
		} else {
			if (getTimer().getHour() != hour) {
				hour = getTimer().getHour();
				globalLight += 0.05f;
				globalLight *= 100;
				globalLight = Math.round(globalLight);
				globalLight /= 100;
				if (globalLight > 1) globalLight = 1;
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
		//MOUSE STUFF----------------------------------------------------------
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
			type = 2;
			for (Settlement s : settlements) {
				//TODO mouse pos is relative to screen:
				//		adjust with player pos
				if (s.x() == TileCoordinate.round(Mouse.x()) 
						&& s.y() == TileCoordinate.round(Mouse.y())) {
					System.out.println("right-clicked on settlement");
				}
			}
		} 
		
		getKey().update();
		if (type == 0 || type == 2) { // if the game is running
			if (getKey().plus) {
				if (!wasplus) {
					wasplus = true;
					if (scale < 4) scale += 1;
				}
			} else {
				wasplus = false;
			}
			if (getKey().minus) {
				wasplus = false;
				if (scale > 1) scale -= 1;
			}
			if (getKey().esc) {
				if (!wasplus) {
					wasesc = true;
					type = 1;
				}
			} else wasesc = false;
			
			getLevel().update();
			getPlayer().update();
			for (int i = 0; i < consettlements.size(); i++) {
				consettlements.get(i).update();
			}
			for (int i = 0; i < settlements.size(); i++) {
				settlements.get(i).update();
			}
		} else if (type == 1) {
			if (getKey().esc) {
				if (!wasplus) {
					wasesc = true;
					type = 0;
				}
			} else wasesc = false;
		}
		
		//TODO menu listeners etc (use type to check for menus)
	}

	public void render(Settlement settle) {
		
		// pixels to size of image and reset image
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		bs = getBufferStrategy();
		if (bs == null) { 
			// create triple buffer
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();

		if (type == 0) { //gameplay
			xScroll = getPlayer().x() - screen.getWidth()/2;
			yScroll = getPlayer().y() - screen.getHeight()/2;
			// 	ORDER LAYERS HERE
			getLevel().render(xScroll*scale, yScroll*scale, screen);	// generate current data
			for (int i = 0; i < consettlements.size(); i++) {
				consettlements.get(i).render(screen);
			}
			for (int i = 0; i < settlements.size(); i++) {
				settlements.get(i).render(screen);
			}
			getPlayer().render(screen);
			getLevel().drawPanels(screen); // draw objects to go in front of the player
		} 
		
		screen.renderSysMenu();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x+y*width] = screen.pixels[x+y*screen.getWidth()];
			} 
		}
		
		switch (speech.size()) {
		case 3:
			image = box(image, height - 110);
			image = box(image, height - 80);
			image = box(image, height - 50);
			image = drawConsoleText(image, speech.get(0), height - 110);
			image = drawConsoleText(image, speech.get(1), height - 80);
			image = drawConsoleText(image, speech.get(2), height - 50);
			break;
		case 2: 
			image = box(image, height - 80);
			image = box(image, height - 50);
			image = drawConsoleText(image, speech.get(0), height - 80);
			image = drawConsoleText(image, speech.get(1), height - 50);
			break;
		case 1: 
			image = box(image, height - 50);
			image = drawConsoleText(image, speech.get(0), height - 50);
			break;
		}
		
		image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		g2d = image2.createGraphics();
        g2d.setColor(new Color(.15f, .1f, .3f, 1f));
        g2d.fill(new Rectangle(0, 0, width, height));
        g2d.dispose(); 
		
		switch (scale) {
		case 1: xOffset = yOffset = 0; break;
		case 2: xOffset = 250;yOffset = 250; break;
		//case 3: xOffset = 250;yOffset = 250; break;
		//case 4: xOffset = 250;yOffset = 250; break;
		default: xOffset = yOffset = 0; break;
		} 
		
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
			if (type == 0) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - globalLight));  
				g2d.drawImage(image2, xOffset, yOffset, width, height, null);
			} else if (type == 1) {
				try {
					g2d.drawImage(ImageIO.read(Launcher.class.getResource
							("/guis/menu.png")), 0, 0, width, height, null);
					g2d.drawImage(ImageIO.read(Launcher.class.getResource
							("/guis/sysgui.png")), width-100, 0, 100, 20, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		g2d.dispose();
		
		bs.show(); // blit/show buffer

		Toolkit.getDefaultToolkit().sync();
	}
	
	private BufferedImage drawConsoleText(BufferedImage old, String s, int y) {
        return drawText(old, s, "Veranda", 20, 25, y);
    }
	
	private BufferedImage drawText(BufferedImage old, String s, String font, 
			int size, int x, int y) {
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.white);
        g2d.setFont(new Font(font, Font.BOLD, size));
        g2d.drawString(s, x, y);
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
				consettlements.add(new ConstructionSettlement(this, xm, ym));
			} // end inner else
		} // end outer else
	} // end build
	
	public void removeConSett(ConstructionSettlement cs) {
		consettlements.remove(cs);
	}
	
	public void addSett(Settlement s) {
		settlements.add(s);
		settx.add(s.x());
		setty.add(s.y());
	}

	public void say(String str) {
		speech.add(str);
		//System.out.println(str);
		if (speech.size() > 3) {
			speech.remove(0);
		}
	}
	
	public String username() {
		try {
			return player.username();
		} catch (NullPointerException e) {
			return username;
		}
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

	public OnlinePlayer getPlayer() {
		return player;
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

	public Keyboard getInput() {
		return key;
	}

}
