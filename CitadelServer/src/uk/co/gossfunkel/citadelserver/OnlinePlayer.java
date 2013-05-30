package uk.co.gossfunkel.citadelserver;

import java.net.InetAddress;

public class OnlinePlayer {
	
	//TODO merge with Player

	public InetAddress ip;
	public int port;

	public OnlinePlayer(Main game, Keyboard input, Timer timer, String username,
			InetAddress ip, int port, Level level) {
		super(game, input, timer, username, level);
		this.ip = ip;
		this.port = port;
	}

	public OnlinePlayer(Main game, Timer timer, String username,
			InetAddress ip, int port, Level level) {
		super(game, null, timer, username, level);
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void update() {
		super.update();
	}
	
}
