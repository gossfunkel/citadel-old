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

public class GameClient implements Runnable {
	
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
			//TODO tell the user that they put an unusable address in.
			//		in future, add a checker to ip before this is constructed
			e.printStackTrace();
		}
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
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type;
		try {
			type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		} catch (NumberFormatException e) {
			type = PacketTypes.INVALID;
		}
		switch (type) {
		case LOGIN: 
			Packet00Login packet = new Packet00Login(data);
			System.out.println(packet.username() + " has joined the game.");
			OnlinePlayer player = new OnlinePlayer(packet.x(), packet.y(), game,
					game.getTimer(), ((Packet00Login)packet).username(), 
						address, port, game.getLevel());
			if (player != null) game.getLevel().addEntity(player);
			break;
		case DISCONNECT:
			Packet01Disconnect packet1 = new Packet01Disconnect(data);
			System.out.println(packet1.username() + " has left.");
			game.getLevel().removeOnlinePlayer(packet1.username());
			break;
		case MOVE:
			Packet02Move packet2 = new Packet02Move(data);
			handleMovement(packet2);
			break;
		case INVALID:
		default: break;
		}
	}

	private void handleMovement(Packet02Move packet) {
		game.getLevel().movePlayer(packet.username(), packet.x(), packet.y());
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
