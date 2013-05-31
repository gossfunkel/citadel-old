package uk.co.gossfunkel.citadel.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.net.packets.Packet;
import uk.co.gossfunkel.citadel.net.packets.Packet.PacketTypes;
import uk.co.gossfunkel.citadel.net.packets.Packet00Login;
import uk.co.gossfunkel.citadel.net.packets.Packet01Disconnect;
import uk.co.gossfunkel.citadel.net.packets.Packet02Move;

public class GameClient extends Thread {
	
	private InetAddress ip;
	private DatagramSocket socket;
	private Game game;
	private boolean running = false;
	
	public GameClient(Game game, String ip) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ip = InetAddress.getByName(ip);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		running = true;
	}
	
	@Override
	public void run() {
		while (running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println("SERVER> " + new String(packet.getData()));
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}
	
	public void exit() {
		running = false;
		Packet01Disconnect p = new Packet01Disconnect(game.username());
		p.writeData(this);
		try {
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type;
		try {
			type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		} catch (NumberFormatException e) {
			type = PacketTypes.INVALID;
		}
		Packet packet;
		switch (type) {
		case LOGIN: 
			packet = new Packet00Login(data);
			System.out.println(((Packet00Login)packet).getUsername() + " has joined the game.");
			OnlinePlayer player = new OnlinePlayer(game, game.getTimer(), ((Packet00Login)packet).getUsername(), address, port, game.getLevel());
			if (player != null) game.getLevel().addEntity(player);
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println(((Packet01Disconnect)packet).getUsername() + " has left.");
			game.getLevel().removeOnlinePlayer(((Packet01Disconnect)packet).getUsername());
			break;
		case MOVE:
			packet = new Packet02Move(data);
			handleMovement((Packet02Move)packet);
			break;
		case INVALID:
		default: break;
		}
	}

	private void handleMovement(Packet02Move packet) {
		game.getLevel().movePlayer(packet.getUsername(), packet.x(), packet.y());
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, 1042);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
