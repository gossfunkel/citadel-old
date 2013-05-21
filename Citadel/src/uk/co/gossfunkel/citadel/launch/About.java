package uk.co.gossfunkel.citadel.launch;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class About extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width = 550;
	private int height = 450;
	
	protected int button_width = 80;
	protected int button_height = 25;
	
	boolean running = true;
	protected int wx = 20, wy = 20;
	
	protected JPanel window = new JPanel();
	
	public About() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Failed to get system buttons: " + e);
		}
		setTitle("About - Citadel");
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
		JLabel label = new JLabel();
		label.setBounds(20, 20, width-100, height-100);
		label.setFont(new Font("Ariel", Font.PLAIN, 28));
		label.setText("Andrew Goss (Gossfunkel) 2013");
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
