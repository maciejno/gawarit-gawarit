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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class GUI extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	GUI gui;
    MainFrame mainFrame;
    JPanel panelCenter, panelUp, panelUpDown;
        
    JButton sendB;
    //JButton logoutB;//wylogowanie-przycisk
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
        //logoutB = new JButton("Wyloguj");
        dodajB = new JButton("Dodaj znajomego");
        usunB = new JButton("Usuń znajomego");
        imageLabel = new JLabel();
        friendLabel = new JLabel("Do kogo chcesz napisać?");
        chooseFriend = new JComboBox<String>();
        dodajField = new JTextField("");
        usunField = new JTextField("");
        
        bfimg = ImageIO.read(this.getClass().getResource("/logo_full2.png"));
        ico = new ImageIcon(bfimg.getScaledInstance(mainFrame.getWidth(), 150,Image.SCALE_SMOOTH));
        imageLabel.setIcon(ico);

        sendB.setBackground(Client.krasnyj);
        chooseFriend.setBackground(Client.galubyj);
        chooseFriend.setForeground(Color.WHITE);
        
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

        gui.add(panelUp,BorderLayout.CENTER);
       // gui.add(logoutB, BorderLayout.SOUTH);
        gui.add(panelCenter, BorderLayout.SOUTH); 
        
        //action listenery dodaje
        sendB.addActionListener(this);
        sendB.setActionCommand("send");
        //logoutB.addActionListener(this);
        //logoutB.setActionCommand("logout");
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
			
		//DODAWANIE ZNAJOMEGO
		}else if(action.equals("add")) {
			newFriend = dodajField.getText();
			boolean exists = false;
			for(String i : Client.friendsMap.keySet()) {
				if(i.equals(newFriend)) exists = true;
			}
			if(!exists) {
				message = Client.createMessage("~$instr&", "~$addfriend&", newFriend);
				try {
					Client.send(message);//wysyla
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else {
				JOptionPane.showMessageDialog(null, "Masz już znajomego: " + newFriend + ". Podaj poprawny nick."  ,
						 null, JOptionPane.INFORMATION_MESSAGE);
			}
			dodajField.setText("");
								
		//USUWANIE ZNAJOMEGO
		}else if(action.equals("delete")) {
			badGuy = usunField.getText();
			boolean exists = false;
			for(String i : Client.friendsMap.keySet()) {
				if(i.equals(badGuy)) exists = true;
			}
			if(exists) {
				message = Client.createMessage("~$instr&", "~$delfriend&", badGuy);
				try {
					Client.send(message);//wysyla
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}else {
				JOptionPane.showMessageDialog(null, "Nie masz znajomego: " + badGuy + ". Podaj poprawny nick."  ,
						 null, JOptionPane.INFORMATION_MESSAGE);
			}
			usunField.setText("");
		}
	}
}
