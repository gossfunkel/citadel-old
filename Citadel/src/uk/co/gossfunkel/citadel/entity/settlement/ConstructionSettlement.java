package uk.co.gossfunkel.citadel.entity.settlement;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.SoundEffect;
import uk.co.gossfunkel.citadel.entity.Entity;
import uk.co.gossfunkel.citadel.graphics.Screen;
import uk.co.gossfunkel.citadel.level.TileCoordinate;
import uk.co.gossfunkel.citadel.level.tile.Tile;
import uk.co.gossfunkel.citadel.net.packets.Packet02Move;
import uk.co.gossfunkel.citadel.net.packets.Packet04Settlement;

public class ConstructionSettlement extends Entity {
	
	private Game game;
	protected Tile sprite;
	protected int level;
	private int progress = 0;

	public ConstructionSettlement(Game game, int x, int y) {
		this.game = game;
		this.x = TileCoordinate.round(x);
		this.y = TileCoordinate.round(y);
		Packet04Settlement packet = new Packet04Settlement(game.username(),
															this.x, this.y);
		packet.writeData(game.getClient());
		sprite = Tile.construction1;
		SoundEffect.CONSTRUCT.play();
	}
	
	// -------------------- methods -------------------------------------------
	
	@Override
	public void render(Screen screen) {
		screen.renderSettlement(x, y, sprite.sprite);
	}
	
	@Override
	public void update() {
		progress++;
		if (progress % 20 == 0) sprite = Tile.construction1;
		else if (progress+10 % 20 == 0) sprite = Tile.construction2;
		if (progress > 1000) complete();
	}
	
	private void complete() {
		Settlement s = new Settlement(x, y);
		game.addSett(s);
		game.removeConSett(this);
	}

}
