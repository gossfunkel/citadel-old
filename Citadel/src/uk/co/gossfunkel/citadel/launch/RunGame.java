package uk.co.gossfunkel.citadel.launch;

import javax.swing.JFrame;

import uk.co.gossfunkel.citadel.Game;

public class RunGame {
	
	public RunGame() {
		Game game = new Game();
		
		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		
		game.frame.getContentPane();
		game.frame.pack();
		game.frame.validate();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}

}
