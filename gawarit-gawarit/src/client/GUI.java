package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class GUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	GUI gui;
    MainFrame mainFrame;
    JPanel panelCenter, panelUp, panelUpDown;
        
    JButton sendB;
    JButton logoutB;//wylogowanie-przycisk
    JButton dodajB;
    JButton usunB;
    JLabel imageLabel, friendLabel;
    JComboBox<String> chooseFriend;//wybor do kogo piszemy
    JTextField dodajField, usunField; //do dodania/usuniecia znajomego
    BufferedImage bfimg;
    ImageIcon ico;
    
    String message = "";
    String response = "";
	String newFriend = ""; //znajomy którego dodajemy
	String badGuy = ""; //którego usuwamy

    public GUI(MainFrame mainFrame) throws IOException {
        gui = this;
        this.mainFrame = mainFrame;
        
       
        panelCenter = new JPanel();
        panelUp = new JPanel();
        panelUpDown = new JPanel();
        sendB = new JButton("Napisz wiadomość");
        logoutB = new JButton("Wyloguj");
        dodajB = new JButton("+ znajomego");
        usunB = new JButton("- znajomego");
        imageLabel = new JLabel();
        friendLabel = new JLabel("Do kogo chcesz napisać?");
        chooseFriend = new JComboBox<String>();
        dodajField = new JTextField("");
        usunField = new JTextField("");
        
        bfimg = ImageIO.read(this.getClass().getResource("/logo_full2.png"));
        ico = new ImageIcon(bfimg.getScaledInstance(mainFrame.getWidth(), 130,Image.SCALE_SMOOTH));
        imageLabel.setIcon(ico);

        sendB.setBackground(Color.RED);
        
        gui.setLayout(new BorderLayout());
        panelCenter.setLayout(new GridLayout(2,2));
        panelUp.setLayout(new BorderLayout()); 
             
        panelUpDown.setLayout(new GridLayout(3,1));
        panelUpDown.add(friendLabel);
        panelUpDown.add(chooseFriend);
        panelUpDown.add(sendB);
        
        panelUp.add(imageLabel,BorderLayout.NORTH);
        panelUp.add(panelUpDown, BorderLayout.CENTER);
            
        panelCenter.add(dodajB);
        panelCenter.add(dodajField);
        panelCenter.add(usunB);
        panelCenter.add(usunField);

        gui.add(panelUp,BorderLayout.NORTH);
        gui.add(logoutB, BorderLayout.SOUTH);
        gui.add(panelCenter, BorderLayout.CENTER); 
        
        //action listenery dodaje
        sendB.addActionListener(this);
        sendB.setActionCommand("send");
        logoutB.addActionListener(this);
        logoutB.setActionCommand("logout");
        dodajB.addActionListener(this);
        dodajB.setActionCommand("add");
        usunB.addActionListener(this);
        usunB.setActionCommand("delete");
    }
    
    public JComboBox<String> getChooseFriend(){return chooseFriend;}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		
		//WYSYLANIE
		if(action.equals("send")) {			
			String [] words = chooseFriend.getSelectedItem().toString().split(" ");
			String username = words[0];
			if(Client.messageFrames.get(username) == null){
				try {
					Client.messageFrames.put(words[0], new MessageFrame(username, mainFrame.getMyName()));//username w konstruktorze przekazuje
				} catch (LineUnavailableException | IOException e) {
					e.printStackTrace();
				}
			}else {
				Client.messageFrames.get(username).setVisible(true);
			}
			
		//WYLOGOWYWANIE
		}else if(action.equals("logout")) {
			
			message = Client.createMessage("~$instr&","~$logout&");
			try {
				Client.send(message);//wysyła
			} catch (Exception e) {
				e.printStackTrace();
			}			
		//DODAWANIE ZNAJOMEGO
		}else if(action.equals("add")) {
			newFriend = dodajField.getText();
			message = "~$instr&\r\n" + 
					"~$addfriend&\r\n" + 
					newFriend + 
					"~$end&\r\n";
			try {
				response = Client.communicate(message);//wysyla i odbiera
			} catch (Exception e) {
				e.printStackTrace();
			}			
			String [] lines = response.split(System.getProperty("line.separator"));
			if(lines[0].equals("~$instr&") && lines[1].equals("~$rejectedinv&")) {
				JOptionPane.showMessageDialog(null, "Nie udało się dodać znajomego. "
						+ "Upewnij się, że istnieje i chce z tobą rozmawiać :)", null, JOptionPane.INFORMATION_MESSAGE);
			}else if(lines[0].equals("~$instr&") && lines[1].equals("~$acceptedinv&")){
				Client.friendsMap.put(lines[2], new Boolean("true")); //dodaje nowego znajomego do mapy
				Client.updateFriendsBox();
			}else {
				JOptionPane.showMessageDialog(null,"Coś poszło nie tak", null, JOptionPane.INFORMATION_MESSAGE);
				System.out.println("Coś poszło nie tak");
			}	
		//USUWANIE ZNAJOMEGO
		}else if(action.equals("delete")) {
			badGuy = usunField.getText();
			message = "~$instr&\r\n" + 
					"~$delfriend&\r\n" + 
					badGuy + 
					"~$end&\r\n";
			try {
				response = Client.communicate(message);//wysyla i odbiera
			} catch (Exception e) {
				e.printStackTrace();
			}			
			String [] lines = response.split(System.getProperty("line.separator"));
			if(lines[0].equals("~$instr&") && lines[1].equals("~$friends&")) {
				Client.updateFriendsMap(lines); //po usunieciu updateuje mape znajomych
			}else {
				JOptionPane.showMessageDialog(null,"Coś poszło nie tak", null, JOptionPane.INFORMATION_MESSAGE);
				System.out.println("Coś poszło nie tak");
			}	
		}
	}
}
