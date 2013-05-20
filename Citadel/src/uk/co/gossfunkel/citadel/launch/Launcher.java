package uk.co.gossfunkel.citadel.launch;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Launcher extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width = 240;
	private int height = 320;
	
	protected int button_width = 80;
	protected int button_height = 25;
	
	protected JPanel window = new JPanel();
	private JButton play, options, controls, quit, about;
	
	class ImagePanel extends JComponent {
		private static final long serialVersionUID = 1L;
		private Image image;
	    public ImagePanel(Image image) {
	        this.image = image;
	    }
	    @Override
	    protected void paintComponent(Graphics g) {
	    	super.paintComponent(g);
	        g.drawImage(image, 0, 0, null);
	    }
	}

	public Launcher(int id) throws IOException, URISyntaxException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Failed to get system buttons: " + e);
		}
		setUndecorated(true);
		setTitle("Citadel Launcher");
		BufferedImage myImage = ImageIO.read(new File(
				getClass().getResource("/launcher_img.png").toURI()));
		JInternalFrame myJFrame = new JInternalFrame("Image pane");
		myJFrame.setContentPane(new ImagePanel(myImage));
		setSize(new Dimension(width, height));
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(myJFrame);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);

		if (id == 0) drawButtons();
		
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
			public void actionPerformed(ActionEvent e) {
				new RunGame();
				dispose();
			}
		});
		options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new Options();
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		controls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("controls");
			}
		});
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("about");
			}
		});
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
