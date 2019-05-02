package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GuiLoginFrame extends JPanel{
	GuiLoginFrame gui;
    LoginFrame loginFrame;
    Font labelFont;
    GridLayout guiLayout;
    
    JTextField loginField;
	JTextField passField;
	JTextField newLoginField;
	JTextField newPassField;
	JTextField pass2Field;
    JButton loginButton;
    JButton signButton;
    
    JLabel loginLabel;
    JLabel passLabel;
    JLabel pass2Label;
    JLabel logLabel;
    JLabel signLabel;
    JLabel newLoginLabel;
    JLabel newPassLabel;
    
    JPanel upPanel;
    JPanel downPanel;
    JPanel loginPanel;
    JPanel signPanel;

    public GuiLoginFrame(LoginFrame loginFrame) throws IOException {
	    gui = this;
	    this.loginFrame = loginFrame;	
	    labelFont = new Font("Calibri", Font.BOLD, 18);
	    
	    guiLayout = new GridLayout(2,1,10,10);
	    this.setLayout(guiLayout);
	    
	    upPanel = new JPanel();
	    downPanel = new JPanel();
	    loginPanel = new JPanel();
	    signPanel = new JPanel();
	    
	    loginButton = new JButton("Zaloguj");
	    signButton = new JButton("Zarejestruj");
	    loginField = new JTextField("");
		passField = new JTextField("");
		newLoginField = new JTextField("");
		newPassField = new JTextField("");
		pass2Field = new JTextField("");
		loginLabel = new JLabel("Login:");
		passLabel = new JLabel("Hasło:");
		newLoginLabel = new JLabel("Login:");
		newPassLabel = new JLabel("Hasło:");
		pass2Label = new JLabel("Powtórz hasło:");
		logLabel = new JLabel("LOGOWANIE", SwingConstants.CENTER);
		signLabel = new JLabel("\n REJESTRACJA", SwingConstants.CENTER);
		
		logLabel.setFont(labelFont);
		signLabel.setFont(labelFont);
	    
		upPanel.setLayout(new BorderLayout());
		downPanel.setLayout(new BorderLayout());
		loginPanel.setLayout(new GridLayout(2,2));
		signPanel.setLayout(new GridLayout(3,2));
		
		loginPanel.add(loginLabel);
		loginPanel.add(loginField);
		loginPanel.add(passLabel);
		loginPanel.add(passField);
		
		upPanel.add(logLabel, BorderLayout.NORTH);
		upPanel.add(loginPanel, BorderLayout.CENTER);
		upPanel.add(loginButton, BorderLayout.SOUTH);
		
		signPanel.add(newLoginLabel);
		signPanel.add(newLoginField);
		signPanel.add(newPassLabel);
		signPanel.add(newPassField);
		signPanel.add(pass2Label);
		signPanel.add(pass2Field);
		 
		downPanel.add(signLabel, BorderLayout.NORTH);
		downPanel.add(signPanel, BorderLayout.CENTER);
		downPanel.add(signButton, BorderLayout.SOUTH);
		
	    this.add(upPanel);
	    this.add(downPanel);
	    
       
        
    }
}
