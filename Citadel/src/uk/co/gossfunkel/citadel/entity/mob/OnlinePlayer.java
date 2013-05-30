package uk.co.gossfunkel.citadel.entity.mob;

import java.net.InetAddress;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.Timer;
import uk.co.gossfunkel.citadel.input.Keyboard;
import uk.co.gossfunkel.citadel.level.Level;
import uk.co.gossfunkel.citadel.net.packets.Packet02Move;

public class OnlinePlayer extends Player {

	public InetAddress ip;
	public int port;

	public OnlinePlayer(Game game, Keyboard input, Timer timer, String username,
			InetAddress ip, int port, Level level) {
		super(game, input, timer, username, level);
		this.ip = ip;
		this.port = port;
	}

	public OnlinePlayer(Game game, Timer timer, String username,
			InetAddress ip, int port, Level level) {
		super(game, null, timer, username, level);
		this.ip = ip;
		this.port = port;
	}

	/*
	@Override
	public void update() {
		super.update();
	}*/
	
	@Override
	public void move(int xa, int ya) {
		super.move(xa, ya);
		Packet02Move packet = new Packet02Move(username, x, y);
		packet.writeData(game.getClient());
	}
	
}
