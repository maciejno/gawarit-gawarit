package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class IpFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	IpFrame frame;
	
	public IpFrame() {
		this.frame = this;
		JPanel ipPanel = new JPanel();
		this.setVisible(true);
		this.setSize(300, 100);
		ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/logo_mini.png"));
		this.setIconImage(mainIcon.getImage());
		this.setTitle("Gawarit-Gawarit");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ipPanel.setLayout(new BorderLayout());
		
		JTextField ipField = new JTextField(Client.ip);
		JButton ipButton = new JButton("OK");	
		ipButton.setBackground(Client.galubyj);
		ipButton.setForeground(Color.white);
		JLabel ipLabel = new JLabel("Podaj adres IP serwera:");
		ipPanel.add(ipLabel, BorderLayout.NORTH);
		ipPanel.add(ipField, BorderLayout.CENTER);
		ipPanel.add(ipButton, BorderLayout.EAST);
		this.add(ipPanel);

		try{          
        	UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");            
        }catch (Exception e1){
            e1.printStackTrace();
            System.err.println("Blad podczas ustawiania LookAndFeel");
        }
		
		ipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Client.ip = new String(ipField.getText());				
				try {
					Client.loginFrame = new LoginFrame();
				} catch (LineUnavailableException | IOException e) {
					e.printStackTrace();
				}			
				Client.initialize();
				frame.dispose();
			}	
		});	
	}
}
