package uk.co.gossfunkel.citadel.net.packets;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import uk.co.gossfunkel.citadel.net.GameClient;
import uk.co.gossfunkel.citadel.entity.mob.OnlinePlayer;
import uk.co.gossfunkel.citadel.entity.settlement.Settlement;

public class Packet10LoginResponse extends Packet {
	
	private List<OnlinePlayer> players;
	private List<Settlement> settlements;
	
	public Packet10LoginResponse(byte[] data, GameClient client, InetAddress ip, int port) {
		super(10);
		players = new ArrayList<OnlinePlayer>();
		settlements = new ArrayList<Settlement>();
		String[] dataArray = readData(data).split(",");
		String[] players = dataArray[2].split("+");
		String[] settlements = dataArray[4].split("+");
		for(int i = 0; i < Integer.parseInt(dataArray[1]); i++) {
			String[] playerInfo = players[i].split("_");
			this.players.add(client.makePlayer(Integer.parseInt(playerInfo[0]), 
									  	  Integer.parseInt(playerInfo[1]),
									  	 				   playerInfo[2],
									  	 				   ip, port));
		}
		for(int i = 0; i < Integer.parseInt(dataArray[3]); i++) {
			String[] settInfo = settlements[i].split("_");
			this.settlements.add(new Settlement(Integer.parseInt(settInfo[0]), 
									  	   Integer.parseInt(settInfo[1]),
									   						settInfo[2]));
		}
		System.out.println(players + ", " + settlements);
	}
	
	public Packet10LoginResponse() {
		super(10);
	}

	@Override
	public void writeData(GameClient c) {
		c.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("00" + "," + players.size() + "," + players() + "," +
				settlements.size() + "," + settlements()).getBytes();
	}
	
	private String players() {
		String str = " ";
		for (int i = 0; i < players.size(); i++) {
			str += players.get(i);
		}
		return str;
	}
	
	public ArrayList<OnlinePlayer> getPlayers() {
		return (ArrayList<OnlinePlayer>) players;
	}
	
	private String settlements() {
		String str = " ";
		for (int i = 0; i < settlements.size(); i++) {
			str += settlements.get(i);
		}
		return str;
	}
	
	public ArrayList<Settlement> getSettlements() {
		return (ArrayList<Settlement>) settlements;
	}
	
	@Override
	public String toString() {
		return ("00" + "," + players.size() + "," + settlements.size());
	}


}
