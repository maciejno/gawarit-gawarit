package client;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class GuiLoginFrame extends JPanel implements KeyListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	GuiLoginFrame gui;
    LoginFrame loginFrame;
    Font labelFont;
    GridLayout guiLayout;
    
    JTextField loginField;
	JPasswordField passField;
	JTextField newLoginField;
	JPasswordField newPassField;
	JPasswordField newPass2Field;
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
    
    String login = "";
    String password = "";
    String newLogin = "";
    String newPassword = "";
    String newPassword2 = "";
    String message = "";
    String response = "";

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
		passField = new JPasswordField("");
		newLoginField = new JTextField("");
		newPassField = new JPasswordField("");
		newPass2Field = new JPasswordField("");
		loginLabel = new JLabel("Login:");
		passLabel = new JLabel("Hasło:");
		newLoginLabel = new JLabel("Login:");
		newPassLabel = new JLabel("Hasło:");
		pass2Label = new JLabel("Powtórz hasło:");
		logLabel = new JLabel("LOGOWANIE", SwingConstants.CENTER);
		signLabel = new JLabel("\n REJESTRACJA", SwingConstants.CENTER);
		
		loginButton.setBackground(Client.galubyj);
		loginButton.setForeground(Color.WHITE);
		signButton.setBackground(Client.krasnyj);
		
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
		signPanel.add(newPass2Field);
		 
		downPanel.add(signLabel, BorderLayout.NORTH);
		downPanel.add(signPanel, BorderLayout.CENTER);
		downPanel.add(signButton, BorderLayout.SOUTH);
		
	    this.add(upPanel);
	    this.add(downPanel);
	    
	    //dodanie listenerów
	    loginField.addKeyListener(this);
	    passField.addKeyListener(this);
	    newLoginField.addKeyListener(this);
	    newPassField.addKeyListener(this);
	    newPass2Field.addKeyListener(this);
	    
	    loginButton.addActionListener(this);
		loginButton.setActionCommand("login");
		signButton.addActionListener(this);
		signButton.setActionCommand("sign");
	           
    } //koniec konstruktora
    
   /* 
       JButton loginButton;
       JButton signButton;*/
    
    static boolean isTextFieldEmpty(JTextField triedTextField) throws EmptyTextFieldException{
		if (triedTextField.getText()==null) 
			throw new EmptyTextFieldException(triedTextField);
		return true;
	}
    
  //listenery do pol tekstowych
  	public void keyReleased(KeyEvent arg0) {
  		try { 			  			
			if(! loginField.getText().equals(login)) {
				login = loginField.getText();
			}
			else if(! (new String(passField.getPassword())).equals(password)) {
				password = new String(passField.getPassword());
			}	
			else if(! newLoginField.getText().equals(newLogin)) {
				newLogin = newLoginField.getText();
			}	
			else if(! (new String(newPassField.getPassword())).equals(newPassword)) {
				newPassword = new String(newPassField.getPassword());
			}
			else if(! (new String(newPass2Field.getPassword())).equals(newPassword2)) {
				newPassword2 = new String(newPass2Field.getPassword());
			}
  		}
  		catch(Exception e) {
  			System.err.println("Blad key listener - moze puste pole! (?)");
  		}		
  	}
	public void keyPressed(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

	
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		
		//LOGOWANIE
		if(action.equals("login")) {
			login = loginField.getText();
			message = Client.createMessage("~$instr&","~$login&", login + "\r\n" + password);
			try {
				Client.send(message);//wysyla
			} catch (Exception e) {
				e.printStackTrace();
			}
			Client.scheduler = Executors.newScheduledThreadPool(1);
			Client.scheduler.scheduleAtFixedRate( (new Runnable() {// co pewien czas wysyła żądanie listy znajomych
				@Override
				public void run() {
					message = Client.createMessage("~$instr&", "~$friends&");
					try {
						Client.send(message);//wysyla
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}), 5, 5, SECONDS);
			
		}
		
		//REJESTRACJA
		if(action.equals("sign")) {
			if(newPassword.equals(newPassword2)) {
				login = newLoginField.getText();
				message = Client.createMessage("~$instr&", "~$register&", newLogin + "\r\n" + newPassword);
				try {
					Client.send(message);//wysyla
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}else {
				JOptionPane.showMessageDialog(null, "Hasła nie są zgodne!!!", null, JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public String getLogin() {return login;}
}
