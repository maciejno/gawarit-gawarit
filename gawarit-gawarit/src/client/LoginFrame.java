package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
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

		public static void main(String[] args) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JFrame loginFrame = null;
					
					try {
						loginFrame = new LoginFrame();
					} catch (LineUnavailableException | IOException e) {					
						e.printStackTrace();
					}
					loginFrame.setVisible(true);
				}
			});				
		}
		
	}

