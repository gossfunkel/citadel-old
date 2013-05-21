package uk.co.gossfunkel.citadel.launch;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Controls extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width = 550;
	private int height = 450;
	
	protected int button_width = 80;
	protected int button_height = 25;
	
	boolean running = true;
	protected int wx = 20, wy = 20;
	
	protected JPanel window = new JPanel();
	
	public Controls() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Failed to get system buttons: " + e);
		}
		setTitle("Controls - Citadel");
		setSize(new Dimension(width, height));
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		drawText();
		drawButtons();
		
		validate();
		repaint();
	}
	
	private void drawText() {
		JTextArea label = new JTextArea();
		label.setBounds(20, 20, width-100, height-100);
		label.setFont(new Font("Ariel", Font.PLAIN, 28));
		label.setLineWrap(true);
		label.setWrapStyleWord(true);
		label.setText("Control player movement with WASD or the arrow keys." +
				"To build a new settlement, press B. Jump with space. To open" +
				" the menu, right click anywhere. Click a settlement " +
				"to open the settlement options. Press F to build a farm and R" +
				" to build a road.");
		window.add(label);
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
