package uk.co.gossfunkel.citadel.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.net.packets.*;
import uk.co.gossfunkel.citadel.net.packets.Packet.PacketTypes;

public class GameServer extends Thread {
	
	private DatagramSocket socket;
	private Game game;
	private List<OnlinePlayer> connectedPlayers;
	private boolean running = false;
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1042);
		} catch (Exception e) {
			try {
				this.socket = new DatagramSocket(1043);
			} catch (Exception e1) {
				try {
					this.socket = new DatagramSocket(1044);
				} catch (Exception e2) {
					try {
						this.socket = new DatagramSocket(1045);
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
			}
		}
		connectedPlayers = new ArrayList<OnlinePlayer>();
	}
	
	@Override
	public void start() {
		running = true;
	}
	
	public void run() {
		while (running) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			String message = new String(packet.getData());
			//System.out.println("CLIENT> " + new String(packet.getData()));
			if (message.trim().equalsIgnoreCase("ping"))
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
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
		case LOGIN: // log new player in from host to new OnlinePlayer
			packet = new Packet00Login(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login)packet).getUsername() + " has connected.");
			OnlinePlayer player = new OnlinePlayer(game, game.getTimer(), ((Packet00Login)packet).getUsername(), address, port, game.getLevel());
			addConnection(player, ((Packet00Login)packet));
			if (player != null) {
				this.connectedPlayers.add(player);
				game.getLevel().addEntity(player);
			}
			break;
		case DISCONNECT: // remove player from connectedPlayers and game
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect)packet).getUsername() + " has left.");
			removeConnection((Packet01Disconnect)packet);
			break;
		case MOVE: // move the player
			//TODO this isn't working somewhere
			packet = new Packet02Move(data);
			//System.out.println(((Packet02Move)packet).getUsername() + " has " 
					//+ "moved to " + ((Packet02Move)packet).x() + ", " +
					//((Packet02Move)packet).y() + ".");
			this.handleMovement((Packet02Move)packet);
			break;
		case INVALID:
		default: //TODO complain about unrecognised format
			break;
		}
	}

	private void handleMovement(Packet02Move packet) {
		String usnm = packet.getUsername();
		if (getOnlinePlayer(usnm) != null) {
			// player exists
			int index = getOnlinePlayerIndex(usnm);
			connectedPlayers.get(index).setX(packet.x());
			connectedPlayers.get(index).setY(packet.y());
			packet.writeData(this);
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
			sendData(packet.getData(), player.ip, player.port);
		}
	}

	private void removeConnection(Packet01Disconnect packet) {
		OnlinePlayer p = getOnlinePlayer(packet.getUsername());
		if (p != null) this.connectedPlayers.remove(getOnlinePlayerIndex(p.username()));
		packet.writeData(this); //broadcast disconnect
	}
	
	private OnlinePlayer getOnlinePlayer(String usnm) {
		for (OnlinePlayer p : connectedPlayers) {
			if (usnm.equalsIgnoreCase(p.username())) {
				System.out.println(usnm + " is " + p.username());
				return p;
			}
			else System.out.println(usnm + " is not " + p.username());
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

}
