package uk.co.gossfunkel.citadel.launch;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;

public class Options extends Launcher {
	private static final long serialVersionUID = 1L;
	
	private int width = 550;
	private int height = 450;
	
	private JButton ok;

	public Options() throws IOException, URISyntaxException {
		super(1);
		setTitle("Options - Citadel");
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		drawButtons();
		validate();
		repaint();
	}
	
	private void drawButtons() {
		ok = new JButton("OK");
		ok.setBounds(width-100,height-70,button_width,button_height);
		window.add(ok);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

}
