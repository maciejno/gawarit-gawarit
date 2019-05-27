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


public class MainFrame extends JFrame implements Runnable{

	private static final long serialVersionUID = 1L;
	GUI userInterface;
	MainFrame mainFrame;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem end;
	
	JEditorPane textArea;
	JPanel panel;
	String text = "";
	String [] envelope = new String [2];
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
	
	@Override
	public void run() {
		while(true) {
			System.out.println("zaczyna słuchać");//DEBUGGING
			envelope = Client.listen();
			System.out.println("dostałem wiadomość");//DEBUGGING
			String username = envelope[0];
			String message = envelope[1];
			if(Client.messageFrames.get(username)!=null) {//jesli juz jest to okienko
				//to sprawdza czy jest widoczne, a jak nie to uwidacznia
				if(!Client.messageFrames.get(username).isVisible()) Client.messageFrames.get(username).setVisible(true);

				//System.out.println("wypiszę:" + Client.messageFrames.get(username).getGui().getHistoryPane().getText());
				String oldHistory = Client.messageFrames.get(username).getGui().getHistoryPane().getText();				
				String newHistory = oldHistory + username + ":\r\n" + message;//dopisuje do obecnego tekstu nową wiadomość
				Client.messageFrames.get(username).getGui().getHistoryPane().setText(newHistory);// ustawia na nowo tekst w oknie historii wiadomości
			}else {//jesli jeszcze nie ma okienka to dodaje a dalej to samo
				try {
					Client.messageFrames.put(username, new MessageFrame(username,myName));
					String newHistory = username + ":\r\n" + message;//dopisuje do obecnego tekstu nową wiadomość
					Client.messageFrames.get(username).getGui().getHistoryPane().setText(newHistory);// ustawia na nowo tekst w oknie historii wiadomości
				} catch (LineUnavailableException | IOException e) {
					e.printStackTrace();
				}
			}
		}				
	}
		
	public String getMyName() {return myName;}
	public JMenu getMenu() {return menu;};
	public JMenuItem getEnd() {return end;};
	public GUI getGui() {return userInterface;}
}
