package uk.co.gossfunkel.citadel.net.packets;

import uk.co.gossfunkel.citadel.net.GameClient;

public class Packet04Settlement extends Packet {
	private String username;
	private int x, y;
	
	public Packet04Settlement(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");
		username = dataArray[0].trim();
		x = Integer.parseInt(dataArray[1].trim());
		y = Integer.parseInt(dataArray[2].trim());
	}
	
	public Packet04Settlement(String username, int x, int y) {
		super(02);
		this.username = username.trim();
		this.x = x;
		this.y = y;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + username + "," + x + "," + y).getBytes();
	}
	
	public String username() {
		return username;
	} 
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	@Override
	public String toString() {
		return ("02" + username + "," + x + "," + y);
	}
}
