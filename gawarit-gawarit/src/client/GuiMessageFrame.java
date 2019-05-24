package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GuiMessageFrame extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
	GuiMessageFrame gui;
    MessageFrame frame;
    
    String nick = null;
    String history = "";
    String message = "здравствуйте!";
    JButton sendButton;
    JLabel  nickLabel;
    JEditorPane textPane,historyPane; //tu jest historia konwersacji i wiadomosc do wyslania
    JScrollPane historyScrollPane,scrollPane;
    JPanel panelDown;

    public GuiMessageFrame(MessageFrame frame) throws IOException {
        gui = this;
        this.frame = frame;
        this.nick = frame.getUsername();
        panelDown = new JPanel();
        historyScrollPane = new JScrollPane();
        nickLabel = new JLabel(nick, SwingConstants.CENTER);
        textPane = new JEditorPane();
        historyPane = new JEditorPane();
        scrollPane = new JScrollPane(textPane);
        historyScrollPane = new JScrollPane(historyPane);
        historyPane.setText(history);
        textPane.setText(message);//ustawia wiadomosc powitalna
        sendButton = new JButton("Wyślij");
        historyPane.setEditable(false); //zeby historii  nie edytowac
        
        panelDown.setLayout(new GridLayout(2,1));
        panelDown.add(scrollPane);
        panelDown.add(sendButton);
        
        gui.setLayout(new BorderLayout());
        gui.add(nickLabel, BorderLayout.NORTH);
        gui.add(historyScrollPane, BorderLayout.CENTER);
        gui.add(panelDown, BorderLayout.SOUTH);
        
        sendButton.addActionListener(this);
        sendButton.setActionCommand("send");      
    }

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("send")) {
			message = "~$message&\r\n" +
					nick + "\r\n" +
					textPane.getText() + "\r\n" +
					"~$end&";
			try {
				Client.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public JEditorPane getHistoryPane() {return historyPane;}
}
