package uk.co.gossfunkel.citadelserver;

public class Main {
	
	GameServer server = new GameServer(this);
	Level level = new Level();

	public Object getLevel() {
		return level;
	}

}
