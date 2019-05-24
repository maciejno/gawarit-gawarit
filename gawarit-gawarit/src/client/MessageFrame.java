package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import server.ServerMain;

public class MessageFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	MessageFrame frame;
	JPanel panel;
	String text = "";
	GuiMessageFrame gui;
	String username = null;
	
	static JFrame f = new JFrame();//do option pane
	
	public MessageFrame(String username) throws LineUnavailableException, IOException {	
		this.frame = this;		
		this.username = username;
		this.gui = new GuiMessageFrame(this);		
		this.setSize(360,520);
		this.setVisible(true);
		this.setResizable(true);//zeby rozmiar okna byl staly
		this.setMinimumSize(new Dimension(360,520));//ustawia minimalny rozmiar okna
		this.setTitle("Gawarit-Gawarit");
		ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/logo_mini.png"));
		this.setIconImage(mainIcon.getImage());
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.add(gui);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		f.setSize(640,480);
		f.setLayout(new GridLayout(1,1));
		f.add(panel);
		
		/*end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,"до свидания!", null, JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);	
			}			
		});*/
		
		//USTAWIENIE LOOKAND FEEL
		try{          
        	UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");            
        }catch (Exception e1){
            e1.printStackTrace();
            System.err.println("Blad podczas ustawiania LookAndFeel");
        }		
	}

	public String getUsername() {return this.username;}
	public GuiMessageFrame getGui() {return this.gui;}
	
	
}


