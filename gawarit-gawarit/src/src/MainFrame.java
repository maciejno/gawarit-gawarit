package src;

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


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	GUI userInterface;
	MainFrame mainFrame;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem end;
	JMenuItem about;
	JMenuItem tips;
	JMenuItem pro;
	
	JEditorPane textArea;
	JPanel panel;
	String text = "";
	
	static JFrame f = new JFrame();//do option pane
	
	public MainFrame() throws LineUnavailableException, IOException{	
		this.mainFrame = this;
		
		this.setSize(360,520);
		this.setResizable(true);//zeby rozmiar okna byl staly
		this.setMinimumSize(new Dimension(360,520));//ustawia minimalny rozmiar okna
		this.setTitle("Gawarit-Gawarit");
		//ImageIcon mainIcon = new ImageIcon(this.getClass().getResource("/MainIcon.png"));
		//this.setIconImage(mainIcon.getImage());
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		userInterface = new GUI(this);
		this.add(userInterface);

		menuBar = new JMenuBar();
		menu = new JMenu("Plik");
		end = new JMenuItem("Koniec programu");
		about = new JMenuItem("O programie");
		tips = new JMenuItem("Tips & tricks");
		pro = new JMenuItem("Wersja premium");		
		
		this.setJMenuBar(menuBar);
		menuBar.add(menu);
		menu.add(about);menu.add(tips);menu.add(pro);menu.add(end);		
		
		textArea = new JEditorPane();
		panel = new JPanel();panel.setLayout(new GridLayout(1,1));
		
		f.setSize(640,480);
		f.setLayout(new GridLayout(1,1));
		panel.add(textArea);
		f.add(panel);
		
		end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,"Au revoir!", null, JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);	
			}			
		});
		
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {	
				f.setVisible(true);
				text = readTextFile("OProgramie");
				textArea.setText(text);
			}			
		});
		tips.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				f.setVisible(true);
				text = readTextFile("TipsTricks");
				textArea.setText(text);
			}			
		});
		pro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				f.setVisible(true);
				text = readTextFile("ProPL");				
				textArea.setText(text);
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame mainFrame = null;
				
				try {
					mainFrame = new MainFrame();
				} catch (LineUnavailableException | IOException e) {					
					e.printStackTrace();
				}
				mainFrame.setVisible(true);
			}
		});				
	}
	protected String readTextFile(String fileName) {
		InputStreamReader streamReader = null;
		BufferedReader bufferedReader = null;
		String readText = "";
		try {							
			InputStream inputStream = getClass().getResourceAsStream("/" + fileName + ".txt");
			streamReader = new InputStreamReader(inputStream); // Otwieramy readera
			bufferedReader = new BufferedReader(streamReader); // Buforujemy readera
			String line;//na linie tekstu
			line = bufferedReader.readLine();//wczytanie linii tekstu do bufora

			while (line != null) { // readLine() zwraca null jesli plik sie skonczyl
				//System.out.println(line);	 			
				readText += line + "\n";
				line = bufferedReader.readLine();
			}
		}catch (Exception e) {	
			System.err.println("Blad przy otwarciu");
			readText = "";				
		}
		try {				
			streamReader.close();
			bufferedReader.close();
		} catch (IOException e) {
			System.err.println("BLAD PRZY ZAMYKANIU PLIKU!");
			System.exit(3);
			}
		return readText;
	}			
	
	public JMenu getMenu() {return menu;};
	public JMenuItem getEnd() {return end;};
	public JMenuItem getAbout() {return about;} 
	public JMenuItem getPro() {return pro;}
}
