package client;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;



public class GUI extends JPanel {

    GUI gui;
    MainFrame mainFrame;
    
    String invitation;
    
    JButton login;
    JButton send;
    JEditorPane textArea;
    JScrollPane scrollPane;

    public GUI(MainFrame mainFrame) throws IOException {
        gui = this;
        this.mainFrame = mainFrame;
        
        invitation = new String("Zaloguj siê by s³aæ wiadomoœci na ca³y œwiat!");
        
        login = new JButton("Zaloguj");
        send = new JButton("Wyœlij");
        textArea = new JEditorPane();
        scrollPane = new JScrollPane(textArea);
        gui.setLayout(new BorderLayout());
        
        textArea.setText(invitation);//ustawia wiadomosc powitalna
        
        gui.add(login, BorderLayout.NORTH);
        gui.add(send, BorderLayout.SOUTH);
        gui.add(scrollPane, BorderLayout.CENTER);
        
    }
}
