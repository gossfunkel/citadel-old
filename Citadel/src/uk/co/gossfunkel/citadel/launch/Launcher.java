package uk.co.gossfunkel.citadel.launch;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Launcher extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width = 240;
	private int height = 320;
	
	private int button_width = 80;
	private int button_height = 25;
	
	private JPanel window = new JPanel();
	private JButton play, options, controls, quit, about;

	public Launcher() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Failed to get system buttons: " + e);
		}
		setUndecorated(true);
		setTitle("Citadel Launcher");
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		drawButtons();
		
		// to prevent buttons glitching out- 
		//   effectively a refresh
		validate();
		repaint();
	}
	
	private void drawButtons() {
		play = new JButton("Play");
		options = new JButton("Options");
		controls = new JButton("Controls");
		about = new JButton("About");
		quit = new JButton("Quit");
		
		play.setBounds((width/2)-(button_width/2),90,button_width,button_height);
		options.setBounds((width/2)-(button_width/2),120,button_width,button_height);
		controls.setBounds((width/2)-(button_width/2),150,button_width,button_height);
		about.setBounds((width/2)-(button_width/2),180,button_width,button_height);
		quit.setBounds((width/2)-(button_width/2),210,button_width,button_height);
		
		window.add(play);
		window.add(options);
		window.add(controls);
		window.add(about);
		window.add(quit);
		
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new RunGame();
				dispose();
			}
		});
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Options();
			}
		});
		controls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("controls");
			}
		});
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("about");
			}
		});
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

}
