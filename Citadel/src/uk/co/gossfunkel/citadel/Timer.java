package uk.co.gossfunkel.citadel;

public class Timer {
	
	// -------------------- variables -----------------------------------------
	
	private long lastTime;
	private long now;
	private final double fps = 60.0;
	private final double ns= 1000000000.0 / fps;
	private double delta;
	
	private int frames;
	private int updates;
	
	private long second;
	
	// -------------------- constructors --------------------------------------
	
	public Timer() {
		
		lastTime = System.nanoTime();
		now = lastTime;
		delta = 0.0;
		frames = 0;
		updates = 0;
		second = System.currentTimeMillis();
		
	}
	
	// -------------------- methods -------------------------------------------
	
	public void tick() {
		
		lastTime = now;
		now = System.nanoTime();
		delta += (now - lastTime) / ns;
		frames++;
		
	}
	
	public void superTick() {
		updates++;
		delta--;
		lastTime = now;
	}
	
	public void resetTick() {
		updates = 0;
		frames = 0;
	}
	
	public String returnFPS() {
		return (updates + " ups, " + frames + " fps, ");
	}
	
	public void accumulateSecond() {
		// add 1000 to second
		second += 1000;
	}
	
	// -------------------- getters -------------------------------------------
	
	public double getDelta() {
		// return nanosecond difference between now and lastTime
		return delta;
	}
	
	public long getSecond() {
		return second;
	}

}
