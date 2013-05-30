package uk.co.gossfunkel.citadel.net.packets;

import uk.co.gossfunkel.citadel.net.GameClient;
import uk.co.gossfunkel.citadel.net.GameServer;

public class Packet02Move extends Packet {
	
	private String username;
	private int x, y;
	
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		username = dataArray[0];
		x = Integer.parseInt(dataArray[1]);
		y = Integer.parseInt(dataArray[2]);
	}
	
	public Packet02Move(String username, int x, int y) {
		super(02);
		this.username = username;
		this.x = x;
		this.y = y;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + username + "," + x + "," + y).getBytes();
	}
	
	public String getUsername() {
		return username;
	} 
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}

}
