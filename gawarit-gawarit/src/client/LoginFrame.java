package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
	
public class LoginFrame extends JFrame{

		private static final long serialVersionUID = 1L;
		GuiLoginFrame userInterface;
		LoginFrame mainFrame;
		
		JPanel panel;
		String text = "";
		
		static JFrame f = new JFrame();//do option pane
		
		public LoginFrame() throws LineUnavailableException, IOException{	
			this.mainFrame = this;
			
			this.setSize(300,300);
			this.setResizable(true);//zeby rozmiar okna byl staly
			this.setMinimumSize(new Dimension(300,300));//ustawia minimalny rozmiar okna
			this.setTitle("Gawarit-Gawarit");
			ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/logo_mini.png"));
			this.setIconImage(mainIcon.getImage());
			
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setLayout(new BorderLayout());
			
			userInterface = new GuiLoginFrame(this);
			this.add(userInterface, BorderLayout.CENTER);
			
			
			//USTAWIENIE LOOK AND FEEL
			try{          
	        	UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");            
	        }catch (Exception e1){
	            e1.printStackTrace();
	            System.err.println("Blad podczas ustawiania LookAndFeel");
	        }		
		}		
	}

