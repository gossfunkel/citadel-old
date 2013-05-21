package uk.co.gossfunkel.citadel.launch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import uk.co.gossfunkel.citadel.input.LauncherMouse;

public class Launcher extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private int width = 240;
	private int height = 320;
	
	protected int button_width = 80;
	protected int button_height = 25;
	
	boolean running = true;
	protected int wx = 20, wy = 20;
	
	LauncherMouse mouse = new LauncherMouse();
	protected JPanel window = new JPanel();
	
	private String playpath = "/launcher/play.png";
	private String optionspath = "/launcher/options.png";
	private String controlspath = "/launcher/controls.png";
	private String aboutpath = "/launcher/about.png";
	private String exitpath = "/launcher/quit.png"; //jmtb02

	public Launcher(int id) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Failed to get system buttons: " + e);
		}
		setUndecorated(true);
		setTitle("Citadel Launcher");
		setSize(new Dimension(width, height));
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);

		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		new Thread(this, "launcher").start();
	}
	
	@Override
	public void run() {
		while (running) {
			render();
			update();
		}
	}
	
	private void update() {
		if (LauncherMouse.x() > width/2 - 50 && LauncherMouse.x() < width/2 + 50) {
			if (LauncherMouse.y() > 120 && LauncherMouse.y() < 120+35) {
				playpath = "/launcher/play_down.png";
				if (LauncherMouse.isPressed()) {
					new RunGame();
					dispose();
				}
			} else playpath = "/launcher/play.png";
			if (LauncherMouse.y() > 155 && LauncherMouse.y() < 155+35) {
				optionspath = "/launcher/options_down.png";
				if (LauncherMouse.isPressed()) {
					new Options();
				}
			} else optionspath = "/launcher/options.png";
			if (LauncherMouse.y() > 190 && LauncherMouse.y() < 190+35) {
				controlspath = "/launcher/controls_down.png";
				if (LauncherMouse.isPressed()) {
					new Controls();
				}
			} else controlspath = "/launcher/controls.png";
			if (LauncherMouse.y() > 225 && LauncherMouse.y() < 225+35) {
				aboutpath = "/launcher/about_down.png";
				if (LauncherMouse.isPressed()) {
					new About();
				}
			} else aboutpath = "/launcher/about.png";
			if (LauncherMouse.y() > 260 && LauncherMouse.y() < 260+35) {
				exitpath = "/launcher/quit_down.png";
				if (LauncherMouse.isPressed()) {
					stop();
				}
			} else exitpath = "/launcher/quit.png";
		}
		if (LauncherMouse.isPressed()) {
			Point p = window.getLocation();
			setLocation(p.x + LauncherMouse.x() - LauncherMouse.px(), 
					p.y + LauncherMouse.y() - LauncherMouse.py());
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		try {
			g.drawImage(ImageIO.read(Launcher.class.getResource("/launcher/background.png")), 0, 0, width, height, null);
			g.drawImage(ImageIO.read(Launcher.class.getResource(playpath)), width/2 - 70, 120, 140, 35, null);
			g.drawImage(ImageIO.read(Launcher.class.getResource(optionspath)), width/2 - 70, 155, 140, 35, null);
			g.drawImage(ImageIO.read(Launcher.class.getResource(controlspath)), width/2 - 70, 190, 140, 35, null);
			g.drawImage(ImageIO.read(Launcher.class.getResource(aboutpath)), width/2 - 70, 225, 140, 35, null);
			g.drawImage(ImageIO.read(Launcher.class.getResource(exitpath)), width/2 - 70, 260, 140, 35, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g.dispose();
		bs.show();
	}
	
	public void stop() {
		running = false;
		System.exit(0);
	}
	

}
