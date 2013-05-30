package uk.co.gossfunkel.citadelserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadelserver.packets.Packet;
import uk.co.gossfunkel.citadelserver.packets.Packet.PacketTypes;
import uk.co.gossfunkel.citadelserver.packets.Packet00Login;

public class GameServer extends Thread {
	
	private DatagramSocket socket;
	private Main game;
	private List<OnlinePlayer> connectedPlayers;
	
	public GameServer(Main game) {
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
			System.out.println("CLIENT> " + new String(packet.getData()));
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
		case LOGIN: packet = new Packet00Login(data);
					System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((Packet00Login)packet).getUsername() + " has connected.");
					OnlinePlayer player = new OnlinePlayer(game, game.getTimer(), ((Packet00Login)packet).getUsername(), address, port, game.getLevel());
					addConnection(player, ((Packet00Login)packet));
					if (player != null) {
						this.connectedPlayers.add(player);
						game.getLevel().addEntity(player);
					}
					break;
		case DISCONNECT:
		case INVALID:
		default: break;
		}
	}

	private void addConnection(OnlinePlayer player, Packet00Login packet) {
		boolean alreadyConnected = false;
		for (OnlinePlayer p : connectedPlayers) {
			if (player.username().equalsIgnoreCase(p.username())){
				if (p.ip == null) p.ip = player.ip;
				if (p.port == -1) p.port = player.port;
				alreadyConnected = true;
			} 
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
			sendData(packet.getData(), player.ip, player.port);
		}
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
		for (OnlinePlayer p : connectedPlayers) {
			sendData(data, p.ip, p.port);
		}
	}

}
