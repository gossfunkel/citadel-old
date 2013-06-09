package uk.co.gossfunkel.citadel.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.net.packets.*;
import uk.co.gossfunkel.citadel.net.packets.Packet.PacketTypes;

public class GameServer implements Runnable {
	
	private DatagramSocket socket;
	private Game game;
	private OnlinePlayer lPlayer;
	private List<OnlinePlayer> connectedPlayers;
	private boolean running = false;
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1042);
		} catch (Exception e) {
			e.printStackTrace();
		}
		connectedPlayers = new ArrayList<OnlinePlayer>();
		running = true;
	}
	
	public void run() {
		System.out.println("server running");
		while (running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			} catch (UnknownHostException e) {
				System.err.println("butts, it doesn't like localhost.");
			}
			String message = new String(packet.getData());
			//System.out.println("CLIENT> " + new String(packet.getData()));
			if (message.trim().equalsIgnoreCase("ping"))
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
		}
	}
	
	public void exit() {
		running = false;
		socket.close();
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) throws UnknownHostException {
		String message = new String(data).trim();
		PacketTypes type;
		try {
			type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		} catch (NumberFormatException e) {
			type = PacketTypes.INVALID;
		}
		switch (type) {
		case LOGIN: // log new player in from host to new OnlinePlayer
			Packet00Login packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.username() + " has connected.");
			OnlinePlayer player;
			if (game.getPlayer() == null) {
				player = new OnlinePlayer(game.getLevel().getSpawnX(),
					game.getLevel().getSpawnY(), game, game.getInput(), 
					game.getTimer(), packet.username(), InetAddress.getByName("localhost"), 1042, game.getLevel());
				lPlayer = player;
			} else {
				player = new OnlinePlayer(packet.x(), packet.y(), game, 
						game.getTimer(), packet.username(), address, port, 
						game.getLevel());
			}
			addConnection(player, packet);
			if (player != null) {
				this.connectedPlayers.add(player);
				game.getLevel().addEntity(player);
			}
			//TODO add a move packet here
			break;
		case DISCONNECT: // remove player from connectedPlayers and game
			Packet01Disconnect packet1 = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet1.username() + " has left.");
			removeConnection(packet1);
			break;
		case MOVE: // move the player
			Packet02Move packet2 = new Packet02Move(data);
			//System.out.println(((Packet02Move)packet).getUsername() + " has " 
					//+ "moved to " + ((Packet02Move)packet).x() + ", " +
					//((Packet02Move)packet).y() + ".");
			this.handleMovement(packet2);
			break;
		case INVALID:
		default: //TODO complain about unrecognised format
			break;
		}
	}

	private void handleMovement(Packet02Move packet) {
		String usnm = packet.username();
		if (getOnlinePlayer(usnm) != null) {
			// player exists
			int index = getOnlinePlayerIndex(usnm);
			connectedPlayers.get(index).teleport(packet.x(), packet.y());
		} else {
			//TODO deal with it.
		}
	}

	public void addConnection(OnlinePlayer player, Packet00Login packet) {
		OnlinePlayer p = getOnlinePlayer(player.username());
		if (p != null) {
			// if the player has already connected, update
			if (p.ip == null) p.ip = player.ip;
			if (p.port == -1) p.port = player.port;
			//TODO: tell client they're already connected
			// deny connection?
		} else {
			// if the player has not connected before, add 'em
			this.connectedPlayers.add(player);
			game.getLevel().addEntity(player);
			if (player.port == -1) {
				if (player.ip == null) {
					System.out.println("New player has given no ip");
				} else {
					sendData(packet.getData(), player.ip, 1042);
				}
			} else {
				sendData(packet.getData(), player.ip, player.port);
			}	
		}
	}

	private void removeConnection(Packet01Disconnect packet) {
		OnlinePlayer p = getOnlinePlayer(packet.username());
		if (p != null) this.connectedPlayers.remove(getOnlinePlayerIndex(p.username()));
	}
	
	private OnlinePlayer getOnlinePlayer(String usnm) {
		for (OnlinePlayer p : connectedPlayers) {
			if (usnm.equalsIgnoreCase(p.username())) {
				return p;
			}
		}
		return null;
	}
	
	private int getOnlinePlayerIndex(String usnm) {
		int i = 0;
		for (OnlinePlayer p : connectedPlayers) {
			if (usnm.equalsIgnoreCase(p.username())){
				break;
			}
			i++;
		}
		return i;
	}

	public void sendData(byte[] data, InetAddress ip, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		// broadcast
		for (OnlinePlayer p : connectedPlayers) {
			sendData(data, p.ip, p.port);
		}
	}
	
	public OnlinePlayer getPlayer() {
		return lPlayer;
	}

}
