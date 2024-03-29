package uk.co.gossfunkel.citadel.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	// -------------------- variables -----------------------------------------
	
	private String path;
	public final int SIZE;
	public int[] pixels;
	
	// -------------------- sheets --------------------------------------------
	
	public static SpriteSheet tiles 
		= new SpriteSheet("/textures/spritesheet.png", 256);
	public static SpriteSheet mobs
		= new SpriteSheet("/mobs.png", 256);
	public static SpriteSheet mobs32
		= new SpriteSheet("/32/mobs.png", 256);
	public static SpriteSheet items
		= new SpriteSheet("/items.png", 256);
	public static SpriteSheet gui 
		= new SpriteSheet("/guis/sysgui.png", 100);
	public static SpriteSheet exporbs
		= new SpriteSheet("/exporbs.png", 30);
	
	// -------------------- constructors --------------------------------------
	
	public SpriteSheet(String path, int size) {
		
		this.path = path;
		SIZE 	  = size;
		pixels 	  = new int[SIZE*SIZE];
		
		load();
		
	}
	
	// -------------------- methods -------------------------------------------
	
	private void load() {
		try {
			BufferedImage image 
			  = ImageIO.read(SpriteSheet.class.getResource(path));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0,  0, w, h, pixels, 0, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
