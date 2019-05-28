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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	GUI userInterface;
	MainFrame mainFrame;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem end;
	
	JEditorPane textArea;
	JPanel panel;
	String text = "";
	String myName = null;
	
	static JFrame f = new JFrame();//do option pane
	
	public MainFrame(String myName) throws LineUnavailableException, IOException{	
		this.mainFrame = this;
		this.myName = myName;
		this.setSize(300,250);
		this.setResizable(true);//zeby rozmiar okna byl staly
		this.setMinimumSize(new Dimension(300,250));//ustawia minimalny rozmiar okna
		this.setTitle(myName + "-zalogowano");
		ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/logo_mini.png"));
		this.setIconImage(mainIcon.getImage());
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		userInterface = new GUI(this);
		this.add(userInterface);

		menuBar = new JMenuBar();
		menu = new JMenu("Zakończ");
		end = new JMenuItem("Koniec programu");		
		
		this.setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(end);		
		
		textArea = new JEditorPane();
		panel = new JPanel();panel.setLayout(new GridLayout(1,1));
		
		f.setSize(640,480);
		f.setLayout(new GridLayout(1,1));
		panel.add(textArea);
		f.add(panel);
		
		end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,"до свидания!", null, JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);	
			}			
		});
		
		//USTAWIENIE LOOKAND FEEL
		try{          
        	UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");            
        }catch (Exception e1){
            e1.printStackTrace();
            System.err.println("Blad podczas ustawiania LookAndFeel");
        }		
	}
		
	public String getMyName() {return myName;}
	public JMenu getMenu() {return menu;};
	public JMenuItem getEnd() {return end;};
	public GUI getGui() {return userInterface;}
}
