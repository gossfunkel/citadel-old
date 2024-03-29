package uk.co.gossfunkel.citadel.launch;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Options extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width = 550;
	private int height = 450;
	
	protected int button_width = 80;
	protected int button_height = 25;
	
	boolean running = true;
	protected int wx = 20, wy = 20;
	
	protected JPanel window = new JPanel();

	public Options() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Failed to get system buttons: " + e);
		}
		setTitle("Options - Citadel");
		setSize(new Dimension(width, height));
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		drawButtons();
		
		validate();
		repaint();
	}
	
	private void drawButtons() {
		JButton ok = new JButton("OK");
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
