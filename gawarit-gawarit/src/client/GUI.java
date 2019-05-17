package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



public class GUI extends JPanel {

    GUI gui;
    MainFrame mainFrame;
    JPanel panelCenter, panelUp;
        
    JButton logoutB;//wylogowanie-przycisk
    JButton dodajB;
    JButton usunB;
    JLabel friendLabel;
    JComboBox<String> chooseFriend;//wybor do kogo piszemy
    JTextField dodajField, usunField; //do dodania/usuniecia znajomego

    public GUI(MainFrame mainFrame) throws IOException {
        gui = this;
        this.mainFrame = mainFrame;
        
       
        panelCenter = new JPanel();
        panelUp = new JPanel();
        logoutB = new JButton("Wyloguj");
        dodajB = new JButton("+ znajomego");
        usunB = new JButton("- znajomego");
        friendLabel = new JLabel("Do kogo chcesz napisać?");
        chooseFriend = new JComboBox<String>();
        dodajField = new JTextField("");
        usunField = new JTextField("");

        gui.setLayout(new BorderLayout());
        panelCenter.setLayout(new GridLayout(2,2));
        panelUp.setLayout(new GridLayout(2,1));       
        panelUp.add(friendLabel);
        panelUp.add(chooseFriend);
        
        panelCenter.add(dodajB);
        panelCenter.add(dodajField);
        panelCenter.add(usunB);
        panelCenter.add(usunField);

        gui.add(panelUp,BorderLayout.NORTH);
        gui.add(logoutB, BorderLayout.SOUTH);
        gui.add(panelCenter, BorderLayout.CENTER);        
    }
    
    public JComboBox<String> getChooseFriend(){return chooseFriend;}
    
    public static void updateFriendsList() {//odświeża combo box ze znajomymi
    	Client.mainFrame.getGui().getChooseFriend().removeAllItems();
    	for(String key : Client.friendsMap.keySet()) {
    		Client.mainFrame.getGui().getChooseFriend().addItem(key);
    	}
    }
}
