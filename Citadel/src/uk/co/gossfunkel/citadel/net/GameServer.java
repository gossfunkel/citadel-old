package uk.co.gossfunkel.citadel.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.net.packets.Packet;
import uk.co.gossfunkel.citadel.net.packets.Packet.PacketTypes;
import uk.co.gossfunkel.citadel.net.packets.Packet00Login;
import uk.co.gossfunkel.citadel.net.packets.Packet01Disconnect;

public class GameServer extends Thread {
	
	private DatagramSocket socket;
	private Game game;
	private List<OnlinePlayer> connectedPlayers;
	
	public GameServer(Game game) {
		this.game = game;
		try {
			this.socket = new DatagramSocket(1042);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		connectedPlayers = new ArrayList<OnlinePlayer>();
	}
	
	public void run() {
		while (true) {
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
		case INVALID:
		default: break;
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
		this.connectedPlayers.remove(getOnlinePlayerIndex(p.username()));
		packet.writeData(this); //broadcast disconnect
	}
	
	private OnlinePlayer getOnlinePlayer(String usnm) {
		for (OnlinePlayer p : connectedPlayers) {
			if (usnm.equalsIgnoreCase(p.username())){
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

}
