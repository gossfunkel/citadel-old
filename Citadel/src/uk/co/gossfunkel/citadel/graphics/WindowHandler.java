package uk.co.gossfunkel.citadel.graphics;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import uk.co.gossfunkel.citadel.Game;
import uk.co.gossfunkel.citadel.net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener, WindowStateListener {
	
	private final Game game;
	
	public WindowHandler(Game game) {
		this.game = game;
		this.game.frame.addWindowListener(this);
		this.game.frame.addWindowStateListener(this);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Packet01Disconnect packet = new Packet01Disconnect(this.game.getPlayer().username());
		packet.writeData(this.game.getClient());
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		//TODO -AFK
	}

	@Override
	public void windowIconified(WindowEvent e) {
		//TODO AFK
	}

	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if (e.equals("MAXIMIZED_BOTH")) {
			game.fullScreen();
		}
	}

}
